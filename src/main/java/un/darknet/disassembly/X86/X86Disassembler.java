package un.darknet.disassembly.X86;

import me.martinez.pe.io.LittleEndianReader;
import un.darknet.disassembly.Architecture;
import un.darknet.disassembly.Bits;
import un.darknet.disassembly.Endianness;
import un.darknet.disassembly.PlatformDisassembler;
import un.darknet.disassembly.data.Program;
import un.darknet.disassembly.decoding.DecoderContext;
import un.darknet.disassembly.exception.DisassemblerException;

import java.io.IOException;
import java.util.Stack;

public class X86Disassembler implements PlatformDisassembler {

    public static final byte DEF_BIT_SIZE = Bits.BITS_32;
    String mnemonic;
    int opcode;
    String operand = "";
    String outOperand = "";
    Stack<String> stack = new Stack<>();
    Stack<Integer> numStack = new Stack<>();
    byte[] bytes;
    int index;
    boolean lockPrefix;
    boolean repeatPrefix;
    boolean repeatPrefixZero;
    String segment = "";
    boolean sizeOverride;
    LittleEndianReader reader;
    boolean instructionWasPrefix;
    byte currentBitSize;

    public X86Disassembler() {

        setBits(DEF_BIT_SIZE);

    }

    void stackSwap() {
        String a = stack.pop();
        String b = stack.pop();
        stack.push(a);
        stack.push(b);
    }


    /**
     * Returns if the disassembler supports the bit size.
     * {@link Bits#atLeast(byte, byte)}
     *
     * @param bits the bit size
     * @return true if the disassembler supports the bit size
     */
    @Override
    public boolean supports(byte bits) {
        if (bits == Bits.BITS_64)
            return false;
        return Bits.atMost(bits, Bits.BITS_32);
    }

    /**
     * Return the current bit size of the disassembler.
     *
     * @return the bit size
     */
    @Override
    public byte getBits() {
        return currentBitSize;
    }

    /**
     * Attempt to set the disassembler's bit size.
     *
     * @param bits the bit size
     * @throws DisassemblerException if the disassembler does not support the bit size
     */
    @Override
    public void setBits(byte bits) throws DisassemblerException {
        if (!supports(bits))
            throw new DisassemblerException("Disassembler does not support " + Bits.friendlyName(bits));
        currentBitSize = bits;
    }

    /**
     * @return The disassembler's endianness.
     */
    @Override
    public Endianness getEndianness() {
        return Endianness.LITTLE;
    }

    /**
     * @return The architecture.
     */
    @Override
    public Architecture getArchitecture() {
        return Architecture.X86;
    }

    long readBytes(int size) {

        long n = 0;
        for (int i = 0; i < size; i++) {
            n += (long) (bytes[++index] & 0xFF) << (i * 8);
        }

        return n;

    }

    public void decodeOpcode(int b) {

        if (b >= Mnemonics.Mnemonics.length) {
            mnemonic = "UNKNOWN";
            operand = "";
            return;
        }

        Object o = Mnemonics.Mnemonics[b];

        if (o instanceof String) {

            mnemonic = (String) o;

            if (mnemonic.equals("PREFIX"))
                instructionWasPrefix = true;

            operand = Operations.ops[b];

        } else {

            mnemonic = "???";
            operand = "???";

        }

    }

    public String buildMem(String register, long displacement, int size) {

        String toFormat = "";

        if (!segment.isEmpty())
            toFormat += segment + ":[";
        else
            toFormat += "[";

        if (!register.isEmpty())
            toFormat += "%s + ";

        if (displacement != 0)
            toFormat += "0x%0" + size + "X";

        toFormat += "]";

        if (register.isEmpty())
            return String.format(toFormat, displacement);

        if (displacement == 0)
            return String.format(toFormat, register);

        return String.format(toFormat, register, displacement);
    }

    public void decodeREGRM(int val) throws IOException {

        // [opcode]0 0  00  000 000
        //         s d  mod reg r/m

        boolean s = (opcode & 0x01) == 0x01;
        boolean d = (opcode & 0x02) == 0x02;

        int mod = (val & 0xC0) >> 6;
        int reg = (val & 0x38) >> 3;
        int rm = val & 0x07;

        boolean disp = mod == 0 && rm == 5;

        int regSize = s ? 2 : 0;
        if (sizeOverride) regSize = 1;

        String register = decodeRegister(reg, regSize);

        stack.push(register);

        if (mod == 3) { // rm is a register

            register = decodeRegister(rm, regSize);

            stack.push(register);

        }

        if (mod == 0 && disp) { // displacement 4 bytes after

            long displacement = reader.readDword();
            stack.push(buildMem("", displacement, 4));

        } else if (mod == 0) {

            register = decodeRegister(rm, regSize);

            stack.push(buildMem(register, 0, 0));

        }

        if (mod == 1 || mod == 2) { // 8 bit displacement

            long displacement = mod == 1 ? reader.readByte() : reader.readDword();
            register = decodeRegister(rm, regSize);
            stack.push(buildMem(register, displacement, 1));

        }

        if (d) {
            stackSwap();
        }
    }

    public void decodeOperand(String operand) throws IOException {

        if (operand == null) {
            outOperand = "";
            return;
        }

        char[] chars = operand.toCharArray();

        for (char c : chars) {

            if (Character.isDigit(c)) numStack.push(c - '0');

            if (c == 'R') { // Read 2 Register

                decodeREGRM(reader.readByte());

            }

            if (c == 'r') {

                int reg = numStack.pop();
                int mode = numStack.pop();

                stack.push(decodeRegister(reg, mode));

            }

            if (c == 'i') {

                int size = numStack.pop();

                long n;
                if (size == 1) n = reader.readByte();
                else if (size == 2) n = reader.readWord();
                else n = reader.readDword();


                stack.push(String.format("0x%X", n));

            }

            if (c == 'U') {

                int reg = opcode & 0x07; // extract first 3 bits

                int size = sizeOverride ? 1 : 2;

                String register = decodeRegister(reg, size);

                stack.push(register);

            }

            if (c == 'a') {

                String elem1 = stack.pop();
                String elem2 = stack.pop();

                outOperand = elem1 + ", " + elem2;

            }

            if (c == 'o') {

                stack.push(outOperand);

            }

            if (c == 's') {

                stackSwap();

            }

            if (c == 'S') {

                int index = numStack.pop();
                segment = Constants.SEGMENTS[index];

            }

            if (c == '-') {
                sizeOverride = true;
                instructionWasPrefix = true;
            }

        }

        if (stack.size() > 0) { // if stack is not empty, then there is an operand
            outOperand = stack.pop();
        }

    }

    public String decodeRegister(int reg, int setting) {

        return Constants.REGISTERS[setting][reg];

    }


    @Override
    public void process(Program program, int start, int length) throws IOException {

        X86Decoder decoder = new X86Decoder(this);
        decoder.feed(program.code, start, length);

        while (decoder.hasNext()) {

            DecoderContext ctx = decoder.next(); // result of decoding

            if (ctx.getInstruction() == null) continue; // error occurred

            program.addInstruction(ctx.getInstruction()); // add instruction to program

        }

        // resolve labels & resolve jumps
        resolveLabels(program);


    }

    private void resolveLabels(Program program) {
    }


}
