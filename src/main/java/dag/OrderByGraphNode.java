package dag;

import astree.FilterNode;
import astree.TreeNode;
import codegen.OrderByGenerator;
import common.schema.Schema;
import common.schema.SchemaSet;

import java.util.Arrays;
import java.util.List;

/**
 * Created by honghaijie on 11/19/16.
 */
public class OrderByGraphNode extends GraphNode {
    public OrderByGraphNode(String name, List<Column> orderByKeys, String input, String output) {
        this.input = input;
        this.output = output;
        this.orderByKeys = orderByKeys;
        this.name = name;
    }

    @Override
    public String GetName() {
        return this.name;
    }

    @Override
    public List<Schema> GetMapperOutputSchema() {
        return this.GetInputSchemas();
    }

    @Override
    public Schema GetOutputSchema() {
        return SchemaSet.Instance().Get(input);
    }

    @Override
    public List<String> GetInputs() {
        return Arrays.asList(input);
    }

    @Override
    public String GetOutput() {
        return output;
    }

    @Override
    public String Generate() {
        return new OrderByGenerator(this).Generate();
    }

    @Override
    public void SetMapperFilter(List<TreeNode> filters) {
        this.mapperFilters = filters;
    }

    @Override
    public void SetReducerFilter(TreeNode filters) {
        this.reducerFilters = filters;
    }

    @Override
    public List<TreeNode> GetMapperFilter() {
        return mapperFilters;
    }

    @Override
    public TreeNode GetReducerFilter() {
        return reducerFilters;
    }

    public List<Column> GetOrderByKeys() {
        return orderByKeys;
    }

    private String input;
    private String output;
    private List<Column> orderByKeys;

    private List<TreeNode> mapperFilters;
    private TreeNode reducerFilters;

    private String name;
}
