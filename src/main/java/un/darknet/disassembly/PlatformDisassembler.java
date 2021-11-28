package un.darknet.disassembly;

import un.darknet.disassembly.data.Program;
import un.darknet.disassembly.exception.DisassemblerException;

import java.io.IOException;

public interface PlatformDisassembler {


    /**
     * Returns if the disassembler supports the bit size.
     * {@link Bits#atLeast(byte, byte)}
     *
     * @param bits the bit size
     * @return true if the disassembler supports the bit size
     */
    boolean supports(byte bits);

    /**
     * Return the current bit size of the disassembler.
     *
     * @return the bit size
     */
    byte getBits();

    /**
     * Attempt to set the disassembler's bit size.
     *
     * @param bits the bit size
     * @throws DisassemblerException if the disassembler does not support the bit size
     */
    void setBits(byte bits) throws DisassemblerException;

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
     * @param start   the start offset
     * @param length  how much to disassemble
     */
    void process(Program program, int start, int length) throws IOException;

}
