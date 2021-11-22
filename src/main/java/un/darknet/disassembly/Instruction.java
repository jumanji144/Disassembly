package un.darknet.disassembly;

public class Instruction {

    public Instruction(long location, Opcode opcode) {
        this.location = location;
        this.opcode = opcode;
    }

    public long location;

    public Opcode opcode;

    public String getMnemonic() {
        return opcode.mnemonic();
    }

    public String getOperands() {
        return opcode.operands();
    }

    @Override
    public String toString() {
        return opcode.toString();
    }
}
