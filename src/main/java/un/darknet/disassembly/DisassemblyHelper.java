package un.darknet.disassembly;


import un.darknet.disassembly.data.Instruction;

public class DisassemblyHelper {

    public static Instruction[] disassembleX86(byte[] code) {

        Disassembler disassembler = new Disassembler(Architecture.X86, Endianness.LITTLE);

        disassembler.setBits(Bits.BITS_32);

        return disassembler.disassemble(code);

    }

}
