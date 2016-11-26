package astree;

import common.AggrFunction;
import common.schema.DataType;
import common.schema.Schema;
import common.schema.SchemaSet;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by honghaijie on 11/8/16.
 */
public class ArithNode extends TreeNode {
    public ArithNode() {
    }
    public String constant;
    public String columnName;
    public List<ArithNode> children = new ArrayList<>();
    public List<String> op = new ArrayList<>();
    public DataType type = DataType.UNKNOWN;
    public String function;
    public String alias;
    public boolean IsColumn() {
        return columnName != null;
    }

    public DataType GuessReturnType() {
        if (this.constant != null) {
            return type;
        }
        if (this.columnName != null) {
            String schemaName = SchemaSet.Instance().GetTableNameByColumnName(columnName);
            Schema schema = SchemaSet.Instance().Get(schemaName);

            this.type = schema.Get(schema.GetPosByName(columnName)).type;
            return this.type;
        }
        List<DataType> inputTypes = new ArrayList<>();
        for (ArithNode ch : children) {
            inputTypes.add(ch.GuessReturnType());
        }
        if (this.function != null) {
            AggrFunction f = new AggrFunction(this.function, inputTypes);
            this.type = f.GetOutputType();
            return f.GetOutputType();
        }
        DataType retType = inputTypes.get(0);
        for (int i = 1; i < inputTypes.size(); ++i) {
            if (retType == DataType.INTEGER) {
                if (inputTypes.get(i) == DataType.DECIMAL) {
                    retType = DataType.DECIMAL;
                } else if (inputTypes.get(i) == DataType.INTEGER) {
                    retType = DataType.INTEGER;
                } else {
                    throw new NotImplementedException();
                }
            } else if (retType == DataType.DECIMAL) {
                if (inputTypes.get(i) == DataType.INTEGER || inputTypes.get(i) == DataType.DECIMAL) {
                    retType = DataType.DECIMAL;
                } else {
                    throw new NotImplementedException();
                }
            } else if (retType == DataType.TEXT) {
                if (inputTypes.get(i) == DataType.TEXT) {
                    retType = DataType.TEXT;
                } else {
                    throw new NotImplementedException();
                }
            }
        }
        this.type = retType;
        return retType;
    }
    public String GetDefaultName() {
        if (this.alias != null) {
            return this.alias;
        }
        if (this.columnName != null) {
            return this.columnName;
        }
        for (ArithNode node : children) {
            String t = node.GetDefaultName();
            if (t != null) {
                return t;
            }
        }
        return null;
    }
}
