package un.darknet.disassembly.operand;

import un.darknet.disassembly.Label;

import java.util.HashMap;
import java.util.Map;

public class OperandObject {

    // cache for operand objects of type TYPE_REGISTER
    public static final Map<String, OperandObject> registerObjectCache = new HashMap<>();
    // objects can only have 1 type
    public int type;
    public Object value;

    public OperandObject(int type, Object value) {
        this.type = type;
        this.value = value;
    }

    public static OperandObject forRegister(String register) {
        OperandObject obj = registerObjectCache.get(register);
        if (obj == null) {
            obj = new OperandObject(Operand.TYPE_REGISTER, register);
            registerObjectCache.put(register, obj);
        }
        return obj;
    }

    public static OperandObject forImmediate(long value) {
        return new OperandObject(Operand.TYPE_CONSTANT, value);
    }


}
