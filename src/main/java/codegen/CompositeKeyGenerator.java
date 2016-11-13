package codegen;

import common.schema.DataType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import codegen.Helper;

/**
 * Created by honghaijie on 11/12/16.
 */
public class CompositeKeyGenerator {
    public CompositeKeyGenerator(DataType[] types) {
        this.types = types;
    }
    public String Generate() {
        return "    public static class CompositeKey implements WritableComparable<CompositeKey> {\n" +
                "        public CompositeKey() {}\n" +
                "        public CompositeKey("
                + ConstructorArgs()
                + ") {\n"
                + ConstructorAssign()
                + "        }\n"
                + "        public int compareTo(CompositeKey y) {\n"
                + CompareFunction(0, types.length)
                + "        }\n"
                + "        public void write(DataOutput dataOutput) throws IOException {\n"
                + WriteToDataOutput()
                + "        }\n"
                + "        public void readFields(DataInput dataInput) throws IOException {\n"
                + ReadFromDataInput()
                + "        }\n"
                + Definition()
                + "    }\n";
    }
    public String Definition() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < types.length; ++i) {
            sb.append("        public " + Helper.DataType2JavaType(types[i]) + " k" + i + ";\n");
        }
        return sb.toString();
    }
    public String ConstructorArgs() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < types.length; ++i) {
            sb.append(Helper.DataType2JavaType(types[i]) + " k" + i);
            if (i != types.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
    public String ConstructorAssign() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < types.length; ++i) {
            sb.append("            this.k" + i + "= k" + i + ";\n");
        }
        return sb.toString();
    }
    public String WriteToDataOutput() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < types.length; ++i) {
            sb.append("            dataOutput.write");
            if (types[i] == DataType.INT32) {
                sb.append("Int(");
            } else if (types[i] == DataType.STRING) {
                sb.append("UTF(");
            } else if (types[i] == DataType.DOUBLE) {
                sb.append("Double(");
            } else {
                throw new NotImplementedException();
            }
            sb.append("k" + i + ");\n");
        }
        return sb.toString();
    }
    public String ReadFromDataInput() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < types.length; ++i) {
            sb.append("            k" + i + "=dataInput.read");
            if (types[i] == DataType.INT32) {
                sb.append("Int(");
            } else if (types[i] == DataType.STRING) {
                sb.append("UTF(");
            } else if (types[i] == DataType.DOUBLE) {
                sb.append("Double(");
            } else {
                throw new NotImplementedException();
            }
            sb.append(");\n");
        }
        return sb.toString();
    }
    public String CompareFunction(int s, int n) {
        if (s == n) {
            return Helper.Spaces((s + 3) * 4) + "return 0;\n";
        } else {
            return Helper.Spaces((s + 3) * 4) + "if (k" + s + ".equals(y.k" + s + ")) {\n"
                    + CompareFunction(s + 1, n)
                    + Helper.Spaces((s + 3) * 4) + "} else {\n"
                    + Helper.Spaces((s + 4) * 4) + "return k" + s + ".compareTo(y.k" + s + ");\n"
                    + Helper.Spaces((s + 3) * 4) + "}\n";
        }
    }
    DataType[] types;
}
