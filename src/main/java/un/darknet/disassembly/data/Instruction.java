package un.darknet.disassembly.data;

import un.darknet.disassembly.operand.Operand;

public class Instruction {

    public long location;
    public InstructionType type;
    public Opcode opcode;

    public Instruction(long location, Opcode opcode, InstructionType type) {
        this.location = location;
        this.opcode = opcode;
        this.type = type;
    }

    public Operand[] getOperands() {
        return opcode.operands();
    }

    public String getMnemonic() {
        return opcode.mnemonic();
    }

    public long getLength() {
        return opcode.size();
    }

    @Override
    public String toString() {
        return opcode.toString();
    }

}
