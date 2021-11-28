package un.darknet.disassembly.data;

import un.darknet.disassembly.operand.Operand;

public interface Opcode {

    /**
     * @return The opcode mnemonic.
     */
    String mnemonic();

    /**
     * @return The opcode operands.
     */
    Operand[] operands();


    /**
     * @return The opcode size in bytes.
     */
    long size();

}
