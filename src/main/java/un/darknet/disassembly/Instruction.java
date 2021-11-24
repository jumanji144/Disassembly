package un.darknet.disassembly;

import lombok.Data;

@Data
public class Instruction {

    public Instruction(long location, Opcode opcode) {
        this.location = location;
        this.opcode = opcode;
    }

    public long location;

    public Opcode opcode;

    @Override
    public String toString() {
        return opcode.toString();
    }
}
