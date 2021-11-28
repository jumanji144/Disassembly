package un.darknet.disassembly;

import un.darknet.disassembly.data.Opcode;
import un.darknet.disassembly.operand.Operand;

public class GenericOpcode implements Opcode {

    private final String mnemonic;
    private final long size;
    private final Operand[] operands;

    public GenericOpcode(String mnemonic, long size, Operand... operands) {

        this.mnemonic = mnemonic;
        this.operands = operands;
        this.size = size;

    }


    /**
     * @return The opcode mnemonic.
     */
    @Override
    public String mnemonic() {
        return mnemonic.toLowerCase();
    }

    /**
     * @return The opcode operands.
     */
    @Override
    public Operand[] operands() {
        return new Operand[0];
    }

    /**
     * @return The opcode size in bytes.
     */
    @Override
    public long size() {
        return size;
    }

    @Override
    public String toString() {
        if (operands == null || operands.length == 0)
            return mnemonic;

        StringBuilder sb = new StringBuilder();
        sb.append(mnemonic);
        sb.append(" ");
        for (Operand operand : operands) {
            sb.append(operand.toString());
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();
    }
}
