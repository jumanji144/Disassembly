package un.darknet.disassembly;

import lombok.SneakyThrows;
import un.darknet.disassembly.X86.X86Disassembler;

public class Disassembler {

    private final Architecture architecture;
    private final Endianness endianness;
    private PlatformDisassembler backend;

    public Disassembler(Architecture architecture, Endianness endianness) {
        this.architecture = architecture;
        this.endianness = endianness;
        switch (architecture) {
            case X86: {
                backend = new X86Disassembler();
            }
        }
    }

    public Architecture getArchitecture() {
        return architecture;
    }

    public Endianness getEndianness() {
        return endianness;
    }

    public void setBits(byte bits) {
        backend.setBits(bits);
    }

    public boolean bitsAreAtLeast(byte bits) {

        return Bits.atLeast(backend.getBits(), bits);

    }

    public boolean bitsAreAtMost(byte bits) {

        return Bits.atMost(backend.getBits(), bits);

    }

    @SneakyThrows
    public Instruction[] disassemble(byte[] code) {
        Program program = Program.withCode(code);

        backend.process(program, 0, code.length);

        return program.instructions.toArray(new Instruction[0]);
    }

}
