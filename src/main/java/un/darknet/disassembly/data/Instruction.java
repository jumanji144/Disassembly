package un.darknet.disassembly.data;

public class Instruction {

    public long location;
    public Opcode opcode;

    public Instruction(long location, Opcode opcode) {
        this.location = location;
        this.opcode = opcode;
    }

    @Override
    public String toString() {
        return opcode.toString();
    }
}
