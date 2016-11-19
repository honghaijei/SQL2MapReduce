package astree;

import common.AggrFunction;
import common.schema.DataType;
import common.schema.Schema;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

import static common.Utils.GetSchemaIdxByColumnName;

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
    public boolean IsColumn() {
        return columnName != null;
    }

    public DataType GuessReturnType(List<Schema> schemas) {
        if (this.constant != null) {
            return type;
        }
        if (this.columnName != null) {
            int schemaIdx = GetSchemaIdxByColumnName(schemas, columnName);
            Schema schema = schemas.get(schemaIdx);
            this.type = schema.Get(schema.GetPosByName(columnName)).type;
            return this.type;
        }
        List<DataType> inputTypes = new ArrayList<>();
        for (ArithNode ch : children) {
            inputTypes.add(ch.GuessReturnType(schemas));
        }
        if (this.function != null) {
            AggrFunction f = new AggrFunction(this.function, inputTypes);
            this.type = f.GetOutputType();
            return f.GetOutputType();
        }
        DataType retType = inputTypes.get(0);
        for (int i = 1; i < inputTypes.size(); ++i) {
            if (retType == DataType.INT32) {
                if (inputTypes.get(i) == DataType.DOUBLE) {
                    retType = DataType.DOUBLE;
                } else if (inputTypes.get(i) == DataType.INT32) {
                    retType = DataType.INT32;
                } else {
                    throw new NotImplementedException();
                }
            } else if (retType == DataType.DOUBLE) {
                if (inputTypes.get(i) == DataType.INT32 || inputTypes.get(i) == DataType.DOUBLE) {
                    retType = DataType.DOUBLE;
                } else {
                    throw new NotImplementedException();
                }
            } else if (retType == DataType.STRING) {
                if (inputTypes.get(i) == DataType.STRING) {
                    retType = DataType.STRING;
                } else {
                    throw new NotImplementedException();
                }
            }
        }
        this.type = retType;
        return retType;
    }
    public String GetDefaultName() {
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
