package dag;

import astree.ArithNode;
import codegen.AggrGenerator;
import common.schema.Schema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by honghaijie on 11/19/16.
 */
public class AggrGraphNode extends GraphNode {
    public AggrGraphNode(List<Column> groupByKeys, List<ArithNode> outputColumns, List<String> outputColumnNames, String input, String output) {
        this.groupByKeys = groupByKeys;
        this.input = input;
        this.output = output;
        this.outputColumns = outputColumns;
        this.outputColumnNames = outputColumnNames;
        GetOutputSchemas();
    }

    @Override
    public Schema GetOutputSchemas() {
        Schema inputSchema = GetInputSchemas().get(0);
        Schema ans = new Schema(output);
        int i = 0;
        for (ArithNode node : outputColumns) {
            String cname = null;
            if (outputColumnNames != null) {
                cname = outputColumnNames.get(i);
            }
            if (cname == null) {
                cname = node.GetDefaultName();
            }
            ans.Add(cname, node.GuessReturnType());
            ++i;
        }
        return ans;
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
        return new AggrGenerator(this).Generate();
    }

    public List<String> GetGroupByKeys() {
        List<String> ans = new ArrayList<>();
        for (Column c : groupByKeys) {
            ans.add(c.GetKey());
        }
        return ans;
    }

    public List<ArithNode> GetOutputColumns() {
        return outputColumns;
    }

    private String input;
    private String output;
    private List<Column> groupByKeys;
    private List<ArithNode> outputColumns;
    private List<String> outputColumnNames;
}
