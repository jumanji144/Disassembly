package un.darknet.disassembly.operand;

import un.darknet.disassembly.X86.Constants;
import un.darknet.disassembly.util.Flags;

/**
 * An object representing an operand in an instruction.
 */
public class Operand {

    // types reused in operand objects
    public static final int TYPE_REGISTER = 0b000;
    public static final int TYPE_CONSTANT = 0b001;
    public static final int TYPE_MEMORY = 0b010; // only operand only type

    public int segment = -1; // used for memory operands
    public OperandObject[] objects;
    public Flags types;

    public Operand(OperandObject... objects) {

        this.objects = objects;
        types = new Flags();

        for (OperandObject object : objects) {
            if (object.type == TYPE_REGISTER) // if operand objects contains a register
                types.set(TYPE_REGISTER);
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (types.has(TYPE_MEMORY)) {
            if (segment != -1)
                sb.append(Constants.SEGMENTS[segment]).append(":");
            sb.append("[");
        }
        for (OperandObject object : objects) {
            if (object.type == TYPE_REGISTER) {
                sb.append(object.value);
            } else if (object.type == TYPE_CONSTANT) {
                long value = (long) object.value;
                sb.append(String.format("0x%X", value));
            }
            sb.append(" + ");
        }
        // remove last " + "
        sb.delete(sb.length() - 3, sb.length());
        if (types.has(TYPE_MEMORY))
            sb.append("]");
        return sb.toString();
    }
}
