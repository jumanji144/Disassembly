package un.darknet.disassembly.x86_64;

import un.darknet.disassembly.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class X86_64Disassembler implements PlatformDisassembler {

    String mnemonic;
    int opcode;
    String operand;
    String outOperand;
    Stack<String> stack = new Stack<>();
    Stack<Integer> numStack = new Stack<>();
    byte[] bytes;
    int index;
    boolean lockPrefix;
    boolean repeatPrefix;
    boolean repeatPrefixZero;
    String segment;
    boolean sizeOverride;


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
        return Architecture.X86_64;
    }

    long readBytes(int size) {

        long n = 0;
        for (int i = 0; i < size; i++) {
            n += (long) (bytes[++index] & 0xFF) << (i * 8);
        }

        return n;

    }

    public void decodeOpcode(int b) {

        if(b >= Mnemonics.Mnemonics.length) {
            mnemonic = "UNKNOWN";
            return;
        }

        Object o = Mnemonics.Mnemonics[b];

        if(o instanceof String) {

            mnemonic = (String) o;
            operand = Operations.ops[b];

        }else {

            mnemonic = "???";
            operand = "???";

        }

    }

    public void decodeREGRM(int val) {

        boolean size = (opcode & 0x01) == 0x01;
        boolean direction = (opcode & 0x02) == 0x02;

        boolean r = (val & 0x80) == 0x80; // last bit
        boolean m = (val & 0x40) == 0x40; // second last bit

        int reg = (val & 0x38) >> 3;
        int rm = val & 0x07;

        int regMode = size ? 2 : 0;
        if(sizeOverride) regMode = 1;

        String regStr = decodeRegister(reg, regMode);
        String rmStr = decodeRegister(rm, regMode);

        String reg1 = direction ? rmStr : regStr;
        String reg2 = direction ? regStr : rmStr;

        if(r) {

            if(m) {

                stack.push(reg1);

            }else {

                // read displacement
                long n = readBytes(4);
                stack.push(String.format("[%s + 0x%X]", reg1, n));


            }

        }else {

            if(m) {

                // read displacement
                long n = readBytes(1);
                stack.push(String.format("[%s + 0x%X]", reg1, n));

            }else {

                stack.push(String.format("[%s]", decodeRegister(rm, size ? 2 : 0)));

            }

        }

        stack.push(reg2);

    }



    public void decodeOperand(String operand) {

        if(operand == null) {
            outOperand = "";
            return;
        }

        char[] chars = operand.toCharArray();

        for (char c : chars) {

            if(Character.isDigit(c)) numStack.push(c - '0');

            if(c == 'R') { // Read 2 Register

                decodeREGRM(bytes[++index] & 0xFF);

            }

            if(c == 'r') {

                int reg = numStack.pop();
                int mode = numStack.pop();

                stack.push(decodeRegister(reg, mode));

            }

            if(c == 'i') {

                int size = numStack.pop();

                // load immediate
                long n = 0;
                for (int i = 0; i < size; i++) {
                    n += (long) (bytes[++index] & 0xFF) << (i * 8);
                }

                stack.push(String.format("0x%X", n));

            }

            if(c == 'a') {

                String elem1 = stack.pop();
                String elem2 = stack.pop();

                outOperand = elem1 + ", " + elem2;

            }

        }

    }

    public String decodeRegister(int reg, int setting) {

        return Constants.REGISTERS[setting][reg];

    }

    /**
     * Pass in bytes to disassembler and get back a disassembled instructions
     *
     * @param bytes      the source bytes
     * @param start      the start offset
     * @param length     how much to disassemble
     * @return the disassembled instructions
     */
    @Override
    public Instruction[] disassemble(byte[] bytes, int start, int length) {

        this.bytes = bytes;

        List<Instruction> instructions = new ArrayList<>();

        index = start;
        while(index < length) {

            opcode = bytes[index] & 0xFF;

            decodeOpcode(opcode);
            decodeOperand(operand);

            GenericOpcode opcode = new GenericOpcode(mnemonic, outOperand, 1);

            outOperand = "";
            mnemonic = "";

            Instruction instruction = new Instruction(index, opcode);
            instructions.add(instruction);

            index++;


        }

        return instructions.toArray(new Instruction[0]);

    }
}
