package un.darknet.disassembly;

public interface Opcode {

    /**
     * @return The opcode mnemonic.
     */
    String mnemonic();

    /**
     * @return The opcode operands.
     */
    String operands();

    /**
     * @return The opcode size in bytes.
     */
    long size();

}
