package dag;

import astree.ArithNode;
import astree.FilterNode;
import astree.TreeNode;
import codegen.JoinGenerator;
import common.schema.Schema;

import java.util.List;

/**
 * Created by honghaijie on 11/19/16.
 */
public class JoinGraphNode extends GraphNode {
    public JoinGraphNode(String name, String key1, String key2, List<String> input, String output) {
        this.key1 = key1;
        this.key2 = key2;
        this.inputs = input;
        this.output = output;
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
        Schema s = new Schema(output);
        if (outputColumns == null) {
            List<Schema> schemas = this.GetInputSchemas();
            return Schema.Combine(output, schemas.get(0), schemas.get(1));
        } else {
            for (int i = 0; i < outputColumns.size(); ++i) {
                String cname = outputColumnNames.get(i);
                if (cname == null) {
                    cname = outputColumns.get(i).GetDefaultName();
                }
                s.Add(cname, outputColumns.get(i).GuessReturnType(), output);
            }
        }
        return s;
    }

    @Override
    public List<String> GetInputs() {
        return inputs;
    }

    @Override
    public String GetOutput() {
        return output;
    }

    @Override
    public String Generate() {
        return new JoinGenerator(this).Generate();
    }

    @Override
    public void AddMapperFilter(List<TreeNode> filters) {
        this.mapperFilters = filters;
    }

    @Override
    public void AddReducerFilter(TreeNode filters) {
        this.reducerFilters = filters;
    }

    @Override
    public List<TreeNode> GetMapperFilter() {
        return this.mapperFilters;
    }

    @Override
    public TreeNode GetReducerFilter() {
        return this.reducerFilters;
    }

    public void SetProjection(List<ArithNode> outputColumns, List<String> outputColumnNames) {
        this.outputColumns = outputColumns;
        this.outputColumnNames = outputColumnNames;
    }

    public List<ArithNode> GetOutputColumns() {
        return outputColumns;
    }

    public String GetLeftKey() {
        return key1;
    }

    public String GetRightKey() {
        return key2;
    }

    private List<String> inputs;
    private String output;
    private String key1;
    private String key2;

    private List<ArithNode> outputColumns;
    private List<String> outputColumnNames;

    private List<TreeNode> mapperFilters;
    private TreeNode reducerFilters;

    private String name;
}
