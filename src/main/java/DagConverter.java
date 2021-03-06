import astree.*;
import codegen.WhereGenerator;
import common.UnionFindSet;
import common.Utils;
import common.schema.Schema;
import common.schema.SchemaSet;
import dag.*;

import java.util.*;

/**
 * Created by honghaijie on 11/20/16.
 */
public class DagConverter {
    public DagConverter() {

    }
    public List<SimpleFilterNode> GetEquiFilters(SelectNode selectNode) {
        TreeNode filterNode = selectNode.where;
        List<SimpleFilterNode> ans = new ArrayList<>();
        if (filterNode instanceof FilterNode) {
            if (!((FilterNode) filterNode).IsAnd()) {
                return ans;
            }
            FilterNode left = new FilterNode();
            left.op = "&&";
            for (TreeNode filter : ((FilterNode) filterNode).filters) {
                if (filter instanceof SimpleFilterNode) {
                    if (((SimpleFilterNode) filter).IsEqui() &&
                            ((SimpleFilterNode) filter).left.IsColumn() &&
                            ((SimpleFilterNode) filter).right.IsColumn()) {
                        ans.add((SimpleFilterNode) filter);
                    } else {
                        left.filters.add(filter);
                    }
                }
            }
            selectNode.where = left;
        } else if (filterNode instanceof SimpleFilterNode) {
            if (((SimpleFilterNode) filterNode).IsEqui() &&
                    ((SimpleFilterNode) filterNode).left.IsColumn() &&
                    ((SimpleFilterNode) filterNode).right.IsColumn()) {
                selectNode.where = null;
                ans.add((SimpleFilterNode) filterNode);
            }
            return ans;
        }
        return ans;
    }

    public String GenerateJoinTree(List<SimpleFilterNode> eqs, List<String> tables, TreeNode noeqs) {
        Set<String> tableSet = new HashSet<>(tables);
        UnionFindSet<String> ufs = new UnionFindSet();
        Map<String, JoinGraphNode> msj = new HashMap<>();
        JoinGraphNode ans = null;
        SchemaSet curSet = SchemaSet.Instance().SubSet(tables);
        for (SimpleFilterNode eq : eqs) {
            Column c1 = new Column(eq.left.columnName, curSet);
            Column c2 = new Column(eq.right.columnName, curSet);

            if (!tableSet.contains(c1.GetTable()) || !tableSet.contains(c2.GetTable())) {
                continue;
            }
            if (ufs.HasSameRoot(c1.GetTable(), c2.GetTable())) {
                continue;
            }
            String r1 = ufs.FindRoot(c1.GetTable());
            String r2 = ufs.FindRoot(c2.GetTable());

            String newTableName = Utils.GetTaskName("JOIN");
            Schema newSchema = Schema.Combine(newTableName, SchemaSet.Instance().Get(r1), SchemaSet.Instance().Get(r2));
            SchemaSet.Instance().Add(newSchema);

            JoinGraphNode jgn = new JoinGraphNode(newTableName, c1.GetKey(), c2.GetKey(), Arrays.asList(r1, r2), newTableName);
            ufs.Add(c1.GetTable(), c2.GetTable());
            ufs.Add(newTableName, c1.GetTable());
            nodes.add(jgn);
            msj.put(newTableName, jgn);
            ans = jgn;
        }
        ans.SetReducerFilter(noeqs);
        return ans.GetOutput();
    }
    private GraphNode GetNodeByOutput(String output) {
        for (GraphNode node : nodes) {
            if (node.GetOutput().equals(output)) {
                return node;
            }
        }
        return null;
    }
    public void PushDownPredicates(GraphNode node) {
        List<String> inputs = node.GetInputs();
        TreeNode filter = node.GetReducerFilter();
        filter = Utils.SimplifyFilterNode(filter);
        List<TreeNode> filters = new ArrayList<>();
        if (filter instanceof SimpleFilterNode) {
            filters.add(filter);
        } else {
            FilterNode fn = (FilterNode)filter;
            if (fn != null && fn.IsAnd()) {
                for (TreeNode f : fn.filters) {
                    filters.add(f);
                }
            }
        }
        List<Schema> schemas = new ArrayList<>();
        for (String input : inputs) {
            schemas.add(SchemaSet.Instance().Get(input));
        }
        List<TreeNode> mapperFilter = new ArrayList<>();
        for (int i = 0; i < node.GetInputs().size(); ++i) {
            mapperFilter.add(null);
        }
        List<TreeNode> reducerFilter = new ArrayList<>();
        for (TreeNode f : filters) {
            Set<String> filterTables = new WhereGenerator(f, schemas).GetTables();
            if (filterTables.size() == 1) {
                String tableName = filterTables.iterator().next();
                GraphNode intermediate = GetNodeByOutput(tableName);
                if (intermediate == null) {
                    int idx = node.GetInputs().indexOf(tableName);
                    mapperFilter.set(idx, Utils.And(mapperFilter.get(idx), f));
                } else {
                    TreeNode nf = Utils.And(intermediate.GetReducerFilter(), f);
                    nf = Utils.SimplifyFilterNode(nf);
                    intermediate.SetReducerFilter(nf);
                }
            } else {
                reducerFilter.add(f);
            }
        }
        node.SetMapperFilter(mapperFilter);
        node.SetReducerFilter(Utils.And(reducerFilter));
        for (String input : node.GetInputs()) {
            GraphNode ch = GetNodeByOutput(input);
            if (ch == null) continue;
            PushDownPredicates(ch);
        }
    }
    public String Convert(SelectNode node) {
        List<TableNode> tables = node.from;
        for (TableNode t : tables) {
            if (t.subQuery != null) {
                //TODO handle subQuery
                Convert(t.subQuery);
            }
        }
        String inputTable = null;
        boolean join = node.from.size() > 1;
        if (join) {
            List<SimpleFilterNode> eqs = GetEquiFilters(node);
            List<String> tablenames = new ArrayList<>();
            for (TableNode tnode : node.from) {
                tablenames.add(tnode.GetName());
            }
            inputTable = GenerateJoinTree(eqs, tablenames, node.where);
        } else {
            inputTable = node.from.get(0).GetName();
        }
        if (node.groupby != null) {
            List<Column> groupByKeys = new ArrayList<>();
            for (String c : node.groupby.cols) {
                groupByKeys.add(new Column(c, inputTable));
            }
            String newTable = Utils.GetTaskName("AGG");
            AggrGraphNode aggrNode = new AggrGraphNode(newTable, groupByKeys, node.columns, null, inputTable, newTable);
            inputTable = newTable;
            SchemaSet.Instance().Add(aggrNode.GetOutputSchema());
            nodes.add(aggrNode);
        }
        if (node.orderby != null) {
            List<Column> orderByKeys = new ArrayList<>();
            for (String s : node.orderby.cols) {
                orderByKeys.add(new Column(s, inputTable));
            }
            String newTable = Utils.GetTaskName("SORT");
            OrderByGraphNode sortNode = new OrderByGraphNode(newTable, orderByKeys, inputTable, newTable);
            SchemaSet.Instance().Add(sortNode.GetOutputSchema());
            nodes.add(sortNode);
        }
        PushDownPredicates(nodes.get(2));
        return inputTable;
    }
    public List<GraphNode> GetNodes() {
        return nodes;
    }
    private List<GraphNode> nodes = new ArrayList<>();
}
