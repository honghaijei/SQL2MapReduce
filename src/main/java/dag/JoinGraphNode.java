package dag;

import astree.ArithNode;
import common.schema.Schema;

import java.util.List;

/**
 * Created by honghaijie on 11/19/16.
 */
public class JoinGraphNode extends GraphNode {
    public JoinGraphNode(String key1, String key2, List<String> input, String output) {
        this.key1 = key1;
        this.key2 = key2;
        this.inputs = input;
        this.output = output;
    }

    @Override
    public Schema GetOutputSchemas() {
        return null;
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
        return null;
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
}
