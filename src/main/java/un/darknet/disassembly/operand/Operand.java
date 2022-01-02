package un.darknet.disassembly.operand;

import un.darknet.disassembly.X86.Constants;
import un.darknet.disassembly.util.Flags;

import java.util.Arrays;
import java.util.Objects;

/**
 * An object representing an operand in an instruction.
 */
public class Operand {

    // types reused in operand objects
    public static final int TYPE_REGISTER = 0b000;
    public static final int TYPE_CONSTANT = 0b001;
    public static final int TYPE_MEMORY =   0b010; // only operand only type
    public static final int TYPE_SEGMENT =  0b011; // object only

    public OperandObject[] getObjects() {
        return objects;
    }

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

    public OperandObject find(int type) {
        for (OperandObject object : objects) {
            if (object.type == type)
                return object;
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        OperandObject segment = find(TYPE_SEGMENT);
        if(segment != null)
            sb.append(segment.value).append(":");
        if (types.has(TYPE_MEMORY))
            sb.append("[");
        for (OperandObject object : objects) {
            if(object.type == TYPE_SEGMENT)
                continue;
            if (object.type == TYPE_REGISTER) {
                sb.append(object.value);
            } else if (object.type == TYPE_CONSTANT) {
                long value = (long) object.value;
                if(object.label != null)
                    sb.append(object.label);
                else
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operand operand = (Operand) o;
        return Arrays.equals(objects, operand.objects) && Objects.equals(types, operand.types);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(types);
        result = 31 * result + Arrays.hashCode(objects);
        return result;
    }

    /**
     * Will append an object to the operand.
     * @param obj the object to append
     */
    public void append(OperandObject obj) {
        // we need to resize array to add object
        // we will assume that the array always needs to be resized
        if (obj.type == TYPE_REGISTER)
            types.set(TYPE_REGISTER);
        objects = Arrays.copyOf(objects, objects.length + 1);
        objects[objects.length - 1] = obj;
    }
}
