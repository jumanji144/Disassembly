package un.darknet.disassembly.operand;

import un.darknet.disassembly.labels.Label;

import java.util.HashMap;
import java.util.Map;

public class OperandObject {

    // object pool
    public static final Map<Object, OperandObject> pool = new HashMap<>();
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

    public static OperandObject forObj(Object value, int type) {

        OperandObject obj = pool.get(value);
        if (obj == null) {
            obj = new OperandObject(type, value);
            pool.put(value, obj);
        }
        return obj;

    }


}
