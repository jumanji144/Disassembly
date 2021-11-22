package un.darknet.disassembly;

public interface PlatformDisassembler {


    /**
     * @return The disassembler's endianness.
     */
    Endianness getEndianness();

    /**
     * @return The architecture.
     */
    Architecture getArchitecture();

    /**
     * Pass in bytes to disassembler and get back a disassembled instructions
     * @param bytes the source bytes
     * @param start the start offset
     * @param length how much to disassemble
     * @return the disassembled instructions
     */
    Instruction[] disassemble(byte[] bytes, int start, int length);
}
