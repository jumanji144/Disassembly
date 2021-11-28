package un.darknet.disassembly.X86;

import un.darknet.disassembly.Architecture;
import un.darknet.disassembly.Bits;
import un.darknet.disassembly.Endianness;
import un.darknet.disassembly.PlatformDisassembler;
import un.darknet.disassembly.data.Instruction;
import un.darknet.disassembly.data.Program;
import un.darknet.disassembly.decoding.DecoderContext;
import un.darknet.disassembly.exception.DisassemblerException;
import un.darknet.disassembly.labels.Label;
import un.darknet.disassembly.labels.LabelType;
import un.darknet.disassembly.operand.Operand;
import un.darknet.disassembly.operand.OperandObject;

import java.io.IOException;
import java.util.Map;

public class X86Disassembler implements PlatformDisassembler {

    public static final byte DEF_BIT_SIZE = Bits.BITS_32;
    byte currentBitSize;

    public X86Disassembler() {

        setBits(DEF_BIT_SIZE);

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


    @Override
    public void process(Program program, int start, int length) throws IOException {

        X86Decoder decoder = new X86Decoder(this);
        decoder.feed(program.code, start, length);

        while (decoder.hasNext()) {

            DecoderContext ctx = decoder.next(); // result of decoding

            if (ctx.getInstruction() == null) continue; // error occurred

            program.addInstruction(ctx.getInstruction()); // add instruction to program

        }


    }

    @Override
    public Map<Long, Label> resolveLabels(Program program) {

        // iterate over instructions
        for (int i = 0; i < program.instructions.size(); i++) {

            // get instruction
            Instruction instruction = program.instructions.get(i);

            // iterate over operands
            for (Operand operand : instruction.getOperands()) {

                // iterate over operand's objects
                for (OperandObject object : operand.getObjects()) {

                    if (object.type == Operand.TYPE_CONSTANT) { // is a constant reference

                        long location;
                        LabelType type = LabelType.UNKNOWN;

                        switch (instruction.type) {

                            case JUMP_RELATIVE: {

                                location = instruction.location + instruction.getLength() + (long)object.value;
                                type = LabelType.LABEL;
                                break;

                            }

                            case JUMP: {

                                location = (long) object.value;
                                type = LabelType.LABEL;
                                break;

                            }

                            case CALL: {

                                location = (long) object.value;
                                type = LabelType.FUNCTION;
                                break;

                            }

                            case LOGIC: {

                                location = (long) object.value;
                                type = LabelType.DATA;
                                break;

                            }

                            default: {

                                location = instruction.location + instruction.getLength();

                            }

                        }
                        // generate label
                        if (program.getLabels().containsKey(location)) continue; // already resolved

                        // generate label
                        Label label = new Label(location, type);

                        // add label to program
                        program.addLabel(label);

                        // set reference to label
                        object.label = label;

                    }

                }

            }

        }

        return program.getLabels();

    }


}
