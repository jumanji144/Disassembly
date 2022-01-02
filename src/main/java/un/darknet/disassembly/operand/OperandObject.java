package un.darknet.disassembly.operand;

import un.darknet.disassembly.labels.Label;

import java.util.*;

public class OperandObject {

    // object pool
    public static final List<OperandObject> pool = new ArrayList<>();
    // objects can only have 1 type
    public int type;
    public Object value;

    public Label label; // used for label references

    public OperandObject(int type, Object value) {
        this.type = type;
        this.value = value;
    }

    public static OperandObject forRegister(String register) {
        return forObj(register, Operand.TYPE_REGISTER);
    }

    public static OperandObject forImmediate(long value) {
        return forObj(value, Operand.TYPE_CONSTANT);
    }

    public static OperandObject forSegment(String segment) { return forObj(segment, Operand.TYPE_SEGMENT); }

    public static OperandObject forObj(Object value, int type) {

        for (OperandObject obj : pool) {
            if (obj.type == type && obj.value.equals(value)) {
                return obj;
            }
        }

        OperandObject obj = new OperandObject(type, value);
        pool.add(obj);
        return obj;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperandObject that = (OperandObject) o;
        return type == that.type && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }
}
