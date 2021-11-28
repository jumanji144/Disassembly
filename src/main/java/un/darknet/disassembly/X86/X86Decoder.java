package un.darknet.disassembly.X86;

import un.darknet.disassembly.Bits;
import un.darknet.disassembly.GenericOpcode;
import un.darknet.disassembly.PlatformDisassembler;
import un.darknet.disassembly.data.Instruction;
import un.darknet.disassembly.data.InstructionType;
import un.darknet.disassembly.decoding.Decoder;
import un.darknet.disassembly.decoding.DecoderContext;
import un.darknet.disassembly.operand.Operand;
import un.darknet.disassembly.operand.OperandObject;
import un.darknet.disassembly.util.Logging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static un.darknet.disassembly.X86.Mnemonics.Mnemonics;
import static un.darknet.disassembly.X86.Operations.*;

public class X86Decoder extends Decoder {

    String mnemonic;
    String operation;

    public X86Decoder(PlatformDisassembler platform) {
        super(platform);
    }

    int getSize(DecoderContext ctx) {

        int size = 2; // default: 32-bit
        if (ctx.getFlags().has(PREFIX_OPERAND | PREFIX_ADDRESS)) {
            size = 1; // 16-bit
        }
        if (ctx.getFlags().has(PREFIX_REX) && platform.getBits() >= Bits.BITS_64) // has rex prefix and platform is 64-bit supports 64-bit
            size = 3; // 64-bit
        if (ctx.getFlags().has(PREFIX_LEGACY))
            size = 0; // 8-bit

        return size;

    }

    public String decodeRegister(int reg, int setting) {

        return Constants.REGISTERS[setting][reg];

    }

    public void decodeREGRM(DecoderContext ctx, List<Operand> operands) throws IOException {

        // [opcode]0 0  00  000 000
        //         s d  mod reg r/m

        int val = reader.readByte();

        boolean s = (ctx.getOpcode() & 0x01) == 0x01;
        boolean d = (ctx.getOpcode() & 0x02) == 0x02;

        int mod = (val & 0xC0) >> 6;
        int reg = (val & 0x38) >> 3;
        int rm = val & 0x07;

        boolean disp = mod == 0 && rm == 5;

        if (!s) ctx.getFlags().set(PREFIX_LEGACY); // enable legacy mode
        int regSize = getSize(ctx);

        if(d) {
            String register = decodeRegister(reg, regSize);

            operands.add(new Operand(OperandObject.forRegister(register)));
        }

        if (mod == 3) { // rm is a register

            String register = decodeRegister(rm, regSize);

            operands.add(new Operand(OperandObject.forRegister(register)));

        }

        if (mod == 0 && disp) { // displacement 4 bytes after

            long displacement = reader.readDword();

            Operand op = new Operand(OperandObject.forImmediate(displacement));
            op.types.set(Operand.TYPE_MEMORY);

            operands.add(op);


        } else if (mod == 0) {

            String register = decodeRegister(rm, regSize);

            Operand op = new Operand(OperandObject.forRegister(register));
            op.types.set(Operand.TYPE_MEMORY);
            operands.add(op);

        }

        if (mod == 1 || mod == 2) { // 8 bit displacement

            long displacement = mod == 1 ? reader.readByte() : reader.readDword();
            String register = decodeRegister(rm, regSize);

            OperandObject regObj = OperandObject.forRegister(register);
            OperandObject dispObj = OperandObject.forImmediate(displacement);

            Operand op = new Operand(regObj, dispObj);
            op.types.set(Operand.TYPE_MEMORY);
            operands.add(op);


        }

        if(!d) {
            String register = decodeRegister(reg, regSize);

            operands.add(new Operand(OperandObject.forRegister(register)));
        }


    }


    Operand[] decodeOperands(DecoderContext ctx) throws IOException {

        List<Operand> operands = new ArrayList<>();

        for (char c : operation.toCharArray()) {

            if (Character.isDigit(c)) ctx.push(c - '0');
            else
                switch (c) {

                    case 'r': {

                        int reg = ctx.pop();

                        int mode = getSize(ctx);

                        String register = Constants.REGISTERS[mode][reg];

                        operands.add(new Operand(OperandObject.forRegister(register)));
                        break;

                    }

                    case 'R': {
                        decodeREGRM(ctx, operands);
                        break;
                    }

                    case 'i': {

                        int size = getSize(ctx);

                        long n = 0;
                        if (size == 0) n = reader.readByte();
                        else if (size == 1) n = reader.readWord();
                        else if (size == 2) n = reader.readDword();
                        else if (size == 3) n = reader.readQword();

                        operands.add(new Operand(OperandObject.forImmediate(n)));
                        break;

                    }

                    case 'U': {

                        int reg = ctx.getOpcode() & 0x07; // extract first 3 bits

                        int size = getSize(ctx);

                        String register = Constants.REGISTERS[size][reg];

                        operands.add(new Operand(OperandObject.forRegister(register)));
                        break;

                    }

                    case 'l': {
                        ctx.getFlags().set(PREFIX_LEGACY);
                        break;
                    }

                    default: {
                        Logging.warn("Unhandled operation: " + c);
                    }

                }

        }

        return operands.toArray(new Operand[0]);

    }

    /**
     * Executed when an instruction is a prefix.
     * This will set a prefix flag and then call decode again.
     *
     * @param ctx the current decoder context
     * @throws IOException thrown if an error occurs while reading the stream
     */
    public void decodePrefix(DecoderContext ctx) throws IOException {

        int flag = Operations.prefixToFlag.get(ctx.getOpcode());
        ctx.getFlags().set(flag);

        int newOpcode = reader.readByte();
        //update context
        ctx.setOpcode(newOpcode);
        ctx.setAddress(ctx.getAddress() + 1);

        decode(ctx);

    }

    void decodeOperation(DecoderContext ctx) {

        int opcode = ctx.getOpcode();

        if (opcode >= Mnemonics.length) {
            mnemonic = "UNKNOWN";
            operation = "";
            return;
        }

        Object o = Mnemonics[opcode];

        if (o instanceof String) {

            mnemonic = (String) o;
            operation = Operations.ops[opcode];

        } else {

            mnemonic = "???";
            operation = "???";

        }


    }

    /**
     * Decode an instruction based on the DecoderContext.
     *
     * @param ctx the decoder context
     */
    @Override
    public void decode(DecoderContext ctx) throws IOException {

        decodeOperation(ctx);

        if (mnemonic.equals("PREFIX")) { // is a prefix

            decodePrefix(ctx);
            return;

        }

        Operand[] operands = decodeOperands(ctx);

        if (ctx.getFlags().has(PREFIX_SEGMENT_OVERRIDE)) {

            // find memory reference operand
            for (Operand operand : operands) {
                if (operand.types.has(Operand.TYPE_MEMORY)) {
                    // set segment override
                    operand.segment = ctx.getFlags()
                            .mask(SEGMENT_OVERRIDE_MASK)   // xxxx000000000000
                            .shift(SEGMENT_OVERRIDE_SHIFT) // 000000000000xxxx
                            .get();
                }
            }

        }

        long size = stream.getPos() - ctx.getAddress(); // pos - start

        GenericOpcode op = new GenericOpcode(mnemonic, size, operands);

        InstructionType type = InstructionType.get(mnemonic);

        Instruction instruction = new Instruction(ctx.getAddress(), op, type);

        ctx.setInstruction(instruction); // set the output

    }
}
