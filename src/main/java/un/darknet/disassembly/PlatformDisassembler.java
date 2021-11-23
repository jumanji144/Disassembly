package un.darknet.disassembly;

import un.darknet.disassembly.exception.DisassemblerException;

import java.io.IOException;

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
     * Pass in a program and it will be disassembled.
     * This method builds the instructions but also directly resolves labels.
     *
     * @param program the program to disassemble
     * @param start the start offset
     * @param length how much to disassemble
     */
    void process(Program program, int start, int length) throws IOException;

}
