package un.darknet.disassembly;

public class GenericOpcode implements Opcode {

    private final String mnemonic;
    private final long size;
    private final String operands;

    public GenericOpcode(String mnemonic, String operands, long size) {
        
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
    public String operands() {
        return operands.toLowerCase();
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
        if(operands == null || operands.isEmpty())
            return mnemonic;

        return String.format("%s %s", mnemonic, operands);
    }
}
