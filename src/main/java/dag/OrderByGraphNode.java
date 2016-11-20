package dag;

import common.schema.Schema;
import common.schema.SchemaSet;

import java.util.Arrays;
import java.util.List;

/**
 * Created by honghaijie on 11/19/16.
 */
public class OrderByGraphNode extends GraphNode {
    public OrderByGraphNode(List<Column> orderByKeys, String input, String output) {
        this.input = input;
        this.output = output;
        this.orderByKeys = orderByKeys;
    }

    @Override
    public Schema GetOutputSchemas() {
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
        return null;
    }

    public List<Column> GetOrderByKeys() {
        return orderByKeys;
    }

    private String input;
    private String output;
    private List<Column> orderByKeys;
}
