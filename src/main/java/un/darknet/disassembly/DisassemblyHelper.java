package un.darknet.disassembly;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DisassemblyHelper {

    public Instruction[] disassembleX86(byte[] code) {

        Disassembler disassembler = new Disassembler(Architecture.X86, Endianness.LITTLE);

        disassembler.setBits(Bits.BITS_32);

        return disassembler.disassemble(code);

    }

}
