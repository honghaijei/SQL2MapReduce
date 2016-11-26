package common;

import com.sun.corba.se.impl.io.TypeMismatchException;
import common.schema.DataType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

/**
 * Created by honghaijie on 11/13/16.
 */
public class AggrFunction {
    public enum FunctionType {
        SUM,
        AVG,
        MIN,
        MAX
    }
    public AggrFunction(String funcName, List<DataType> inputTypes) {
        this.funcName = funcName;
        this.inputTypes = inputTypes;
        this.funcType = ParseAggr(funcName);
        if (AggrFunctionInputCheck(funcType, inputTypes) == false) {
            throw new TypeMismatchException();
        }
    }
    String funcName;
    List<DataType> inputTypes;
    FunctionType funcType;

    public String GenerateInternal(String retVarName, List<String> argsNames) {
        if (funcType == FunctionType.SUM) {
            return retVarName + " += " + String.join(", ", argsNames) + ";";
        } else if (funcType == FunctionType.MAX) {
            return retVarName +
                    " = " +
                    "Math.max(" +
                    retVarName +
                    String.join(", ", argsNames) +
                    ");\n";
        } else if (funcType == FunctionType.MIN) {
            return retVarName +
                    " = " +
                    "Math.min(" +
                    retVarName +
                    String.join(", ", argsNames) +
                    ");\n";
        } else if (funcType == FunctionType.AVG) {
            return retVarName + " += " + String.join(", ", argsNames) + ";";
        } else {
            throw new NotImplementedException();
        }
    }
    public String GenerateExternal(String retVarName, String count) {
        if (funcType == FunctionType.SUM) {
            return "";
        } else if (funcType == FunctionType.MAX) {
            return "";
        } else if (funcType == FunctionType.MIN) {
            return "";
        } else if (funcType == FunctionType.AVG) {
            return retVarName + " /= " + count + ";\n";
        } else {
            throw new NotImplementedException();
        }
    }

    private FunctionType ParseAggr(String s) {
        if (s.equals("sum")) {
            return FunctionType.SUM;
        } else if (s.equals("avg")) {
            return FunctionType.AVG;
        } else if (s.equals("min")) {
            return FunctionType.MIN;
        } else if (s.equals("max")) {
            return FunctionType.MAX;
        } else {
            throw new NotImplementedException();
        }
    }
    private boolean AggrFunctionInputCheck(FunctionType func, List<DataType> input) {
        if (func == FunctionType.SUM) {
            return input.size() == 1 && (input.get(0) == DataType.DECIMAL || input.get(0) == DataType.INTEGER);
        } else if (func == FunctionType.AVG) {
            return input.size() == 1 && (input.get(0) == DataType.DECIMAL || input.get(0) == DataType.INTEGER);
        } else if (func == FunctionType.MIN) {
            return input.size() == 1 && (input.get(0) == DataType.DECIMAL || input.get(0) == DataType.INTEGER);
        } else if (func == FunctionType.MAX) {
            return input.size() == 1 && (input.get(0) == DataType.DECIMAL || input.get(0) == DataType.INTEGER);
        } else {
            throw new NotImplementedException();
        }
    }
    public DataType GetOutputType() {
        if (funcType == FunctionType.SUM) {
            return inputTypes.get(0);
        } else if (funcType == FunctionType.AVG) {
            return inputTypes.get(0);
        } else if (funcType == FunctionType.MIN) {
            return inputTypes.get(0);
        } else if (funcType == FunctionType.MAX) {
            return inputTypes.get(0);
        } else {
            throw new NotImplementedException();
        }
    }
}
