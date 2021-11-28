import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import un.darknet.disassembly.*;
import un.darknet.disassembly.X86.Operations;
import un.darknet.disassembly.X86.X86Decoder;
import un.darknet.disassembly.data.Instruction;
import un.darknet.disassembly.data.Opcode;
import un.darknet.disassembly.decoding.DecoderContext;
import un.darknet.disassembly.operand.Operand;
import un.darknet.disassembly.operand.OperandObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static un.darknet.disassembly.operand.Operand.*;

public class X86Test {

    static Disassembler disassembler;

    @BeforeAll
    static void setup() {

        disassembler = new Disassembler(Architecture.X86, Endianness.LITTLE);

    }

    @Test
    public void testDecoder() throws IOException {

        X86Decoder decoder = new X86Decoder(disassembler.getBackend());

        decoder.feed(new byte[] {0x03, 0x05, 0x56, 0x78, 0x56, 0x34}, 0, 6);

        DecoderContext context = decoder.next();

        assertEquals(context.getInstruction().toString(), "ADD EAX, [0x34567856]");

    }

    @Test
    void testOpcode() {

        OperandObject regObject = OperandObject.forRegister("EAX");
        OperandObject constObject = OperandObject.forImmediate(0x102030);

        Operand op = new Operand(regObject, constObject);

        op.types.set(TYPE_MEMORY);

        assertEquals("[EAX + 0x102030]", op.toString());

        Operand op2 = new Operand(regObject);

        Opcode opcode = new GenericOpcode("ADD", 2, op2, op);

        assertEquals("ADD EAX, [EAX + 0x102030]", opcode.toString());

    }


    public static List<Arguments> getAllRegRMInstructions() {

        return Arrays.asList(
                Arguments.of("ADD", (byte) 0x01),
                Arguments.of("OR", (byte) 0x09),
                Arguments.of("ADC", (byte) 0x11),
                Arguments.of("SBB", (byte) 0x19),
                Arguments.of("AND", (byte) 0x21),
                Arguments.of("SUB", (byte) 0x29),
                Arguments.of("XOR", (byte) 0x31),
                Arguments.of("CMP", (byte) 0x39));

    }

    private static void common(byte[] instructions, String[] expected) {

        Instruction[] actual = disassembler.disassemble(instructions);

        assertEquals(expected.length, actual.length);

        for (int i = 0; i < actual.length; i++) {
            Assertions.assertEquals(expected[i], actual[i].toString());
        }

    }

    private static void commonRegModRMTest(String expectedOp, byte prefix) {

        byte[] instructions = {

                prefix, (byte) 0xc0, // add eax, eax
                prefix, (byte) 0xc1, // add ecx, eax
                prefix, (byte) 0xc2, // add edx, eax
                prefix, (byte) 0xc3, // add ebx, eax
                (byte) (prefix + 1), (byte) 0xc0, // add al, al
                (byte) (prefix + 1), (byte) 0xc1, // add cl, al
                (byte) (prefix + 1), (byte) 0xc2, // add dl, al
                (byte) (prefix + 1), (byte) 0xc3, // add bl, al
                (byte) (prefix + 2), (byte) 0xc0, // add eax, eax
                (byte) (prefix + 2), (byte) 0xc1, // add ecx, eax
                (byte) (prefix + 2), (byte) 0xc2, // add edx, eax
                (byte) (prefix + 2), (byte) 0xc3, // add ebx, eax
                (byte) (prefix + 3), (byte) 0xc0, // add al, c0
                (byte) (prefix + 4), 0x00, 0x10, 0x20, 0x30 // add [0x102030], eax

        };

        String[] expected = {

                expectedOp + " EAX, EAX",
                expectedOp + " ECX, EAX",
                expectedOp + " EDX, EAX",
                expectedOp + " EBX, EAX",
                expectedOp + " AL, AL",
                expectedOp + " AL, CL",
                expectedOp + " AL, DL",
                expectedOp + " AL, BL",
                expectedOp + " EAX, EAX",
                expectedOp + " EAX, ECX",
                expectedOp + " EAX, EDX",
                expectedOp + " EAX, EBX",
                expectedOp + " AL, 0xC0",
                expectedOp + " EAX, 0x30201000"

        };

        common(instructions, expected);

    }

    public static List<Arguments> getConditionalJumpData() {

        return Arrays.asList(
                Arguments.of(0x70, "JO"),
                Arguments.of(0x71, "JNO"),
                Arguments.of(0x72, "JB"),
                Arguments.of(0x73, "JNB"),
                Arguments.of(0x74, "JZ"),
                Arguments.of(0x75, "JNZ"),
                Arguments.of(0x76, "JBE"),
                Arguments.of(0x77, "JNBE"),
                Arguments.of(0x78, "JS"),
                Arguments.of(0x79, "JNS"),
                Arguments.of(0x7a, "JP"),
                Arguments.of(0x7b, "JNP"),
                Arguments.of(0x7c, "JL"),
                Arguments.of(0x7d, "JNL"),
                Arguments.of(0x7e, "JLE"),
                Arguments.of(0x7f, "JNLE"));


    }

    @Test
    void generalTest() {

        byte[] instructions = {
                0x6B, (byte) 0xC1, 0x18  // add eax, [eax + 0x0a]
        };

        String[] expected = {
                "ADD EAX, EBX",
                "OR al, 5a",
                "ADD EAX, 50604323",
                "OR EAX, eax",
        };

        Instruction[] actual = disassembler.disassemble(instructions);

        for (Instruction instruction : actual) {

            System.out.println(instruction.opcode.toString());

        }

    }

    @ParameterizedTest
    @MethodSource("getAllRegRMInstructions")
    void testRegModRM(String expectedOp, byte prefix) {

        commonRegModRMTest(expectedOp, prefix);

    }

    @Test
    public void prefixInstructionTest() {

        byte[] instructions = {

                0x26, 0x03, 0x05, 0x56, 0x78, 0x56, 0x34, // ADD EAX, ES:[0x34567856]
                0x2E, 0x03, 0x05, 0x56, 0x78, 0x56, 0x34, // ADD EAX, CS:[0x34567856]
                0x36, 0x03, 0x05, 0x56, 0x78, 0x56, 0x34, // ADD EAX, SS:[0x34567856]

        };

        String[] expected = {
                "ADD EAX, ES:[0x34567856]",
                "ADD EAX, CS:[0x34567856]",
                "ADD EAX, SS:[0x34567856]"
        };

        common(instructions, expected);

    }

    @Test
    public void testIncDec32() {

        disassembler.setBits(Bits.BITS_32);

        byte[] instructions = {
                0x40, // INC EAX
                0x48, // DEC EAX
        };

        String[] expected = {

                "INC EAX",
                "DEC EAX"

        };

        common(instructions, expected);

    }

    @Test
    public void pushPopTest() {

        disassembler.setBits(Bits.BITS_32);

        byte[] instructions = {
                0x50, // PUSH EAX
                0x58, // POP EAX
                0x68, 0x56, 0x34, 0x12, 0x46,// PUSH 0x123456
                0x6A, 0x34, // PUSH 0x34
        };

        String[] expected = {

                "PUSH EAX",
                "POP EAX",
                "PUSH 0x46123456",
                "PUSH 0x34"

        };

        common(instructions, expected);

    }

    @ParameterizedTest
    @MethodSource("getConditionalJumpData")
    public void testConditionalJumps(int opcode, String expected) {

        common(new byte[] {(byte)opcode, 0x10}, new String[] {expected + " 0x10"});

    }

    /**
     * Test for opcodes which only have 1 byte of data
     */
    @Test
    public void oneInstructionTest() {

        byte[] instructions = {
                0x06,  // push es
                0x07,  // pop es
                0x0e,  // push cs
                0x16,  // push ss
                0x1e,  // push ds
                0x27,  // daa
                0x2f,  // das
                0x37,  // aaa
        };

        String[] expected = {
                "PUSH ES",
                "POP ES",
                "PUSH CS",
                "PUSH SS",
                "PUSH DS",
                "DAA",
                "DAS",
                "AAA"
        };

        common(instructions, expected);


    }


}
