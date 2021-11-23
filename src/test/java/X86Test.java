import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import un.darknet.disassembly.Architecture;
import un.darknet.disassembly.Disassembler;
import un.darknet.disassembly.Endianness;
import un.darknet.disassembly.Instruction;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class X86Test {

    static Disassembler disassembler;

    @BeforeAll
    static void setup() {

        disassembler = new Disassembler(Architecture.X86_64, Endianness.LITTLE);

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

    @Test
    void generalTest() {

        byte[] instructions = {
                0x26, 0x03, 0x05, 0x56, 0x78, 0x56, 0x34 // add eax, [eax + 0x0a]
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

    private static void common(byte[] instructions, String[] expected) {

        Instruction[] actual = disassembler.disassemble(instructions);

        System.out.println("Actual:" + Arrays.toString(actual));

        assertEquals(expected.length, actual.length);

        System.out.println("Expected:" + Arrays.toString(expected));

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
