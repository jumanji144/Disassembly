package un.darknet.disassembly;

import org.slf4j.Logger;
import un.darknet.disassembly.X86.X86Disassembler;
import un.darknet.disassembly.data.Instruction;
import un.darknet.disassembly.data.Program;
import un.darknet.disassembly.exception.DisassemblerException;
import un.darknet.disassembly.exception.InvalidInstructionException;
import un.darknet.disassembly.util.Bytes;

import java.io.IOException;

public class Disassembler {

    public static Logger logger = org.slf4j.LoggerFactory.getLogger(Disassembler.class);

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

    public PlatformDisassembler getBackend() {
        return backend;
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

    public Instruction[] disassemble(int[] code) {
        return disassemble(Bytes.toBytes(code));
    }

    public Instruction[] disassemble(byte[] code) {
        Program program = Program.withCode(code);

        try {
            backend.process(program, 0, code.length);
        } catch (IOException e) {
            throw new DisassemblerException(e.getMessage(), e);
        } catch (InvalidInstructionException e) {
            e.printStackTrace();
        }

        return program.instructions.toArray(new Instruction[0]);
    }

}
