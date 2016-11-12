package codegen;

import common.schema.DataType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static common.Helper.DataType2JavaType;
import static common.Helper.Spaces;

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
                + "    }";
    }
    public String Definition() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < types.length; ++i) {
            sb.append("        public " + DataType2JavaType(types[i]) + " k" + i + ";\n");
        }
        return sb.toString();
    }
    public String ConstructorArgs() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < types.length; ++i) {
            sb.append(DataType2JavaType(types[i]) + " k" + i);
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
            return Spaces((s + 3) * 4) + "return 0;\n";
        } else {
            return Spaces((s + 3) * 4) + "if (k" + s + ".equals(y.k" + s + ")) {\n"
                    + CompareFunction(s + 1, n)
                    + Spaces((s + 3) * 4) + "}\n"
                    + Spaces((s + 3) * 4) + "else {\n"
                    + Spaces((s + 4) * 4) + "return k" + s + ".compareTo(y.k" + s + ");\n"
                    + Spaces((s + 3) * 4) + "}\n";
        }
    }
    public static void main(String[] args) {
        System.out.println(new CompositeKeyGenerator(new DataType[]{DataType.STRING, DataType.INT32, DataType.DOUBLE}).Generate());
    }


    DataType[] types;
}
