package un.darknet.disassembly;

import un.darknet.disassembly.exception.DisassemblerException;
import un.darknet.disassembly.x86_64.X86_64Disassembler;

public class Disassembler {

    private final Architecture architecture;
    private final Endianness endianness;

    public Disassembler(Architecture architecture, Endianness endianness) {
        this.architecture = architecture;
        this.endianness = endianness;
    }

    public Architecture getArchitecture() {
        return architecture;
    }

    public Endianness getEndianness() {
        return endianness;
    }

    public Instruction[] disassemble(byte[] code) {
        Program program = Program.withCode(code);

        switch (architecture) {
            case X86_64: {


                X86_64Disassembler disassembler = new X86_64Disassembler();

                disassembler.process(program, 0, code.length);

                return program.instructions.toArray(new Instruction[0]);
            }
        }



        return new Instruction[0];
    }

}
