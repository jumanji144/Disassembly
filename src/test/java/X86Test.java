import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import un.darknet.disassembly.Architecture;
import un.darknet.disassembly.Disassembler;
import un.darknet.disassembly.Endianness;
import un.darknet.disassembly.Instruction;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class X86Test {

    static Disassembler disassembler;

    @BeforeAll
    static void setup() {

        disassembler = new Disassembler(Architecture.X86_64, Endianness.LITTLE);

    }

    @Test
    void generalTest() {

        byte[] instructions = {
                0x03, (byte) 0x80, (byte) 0xAC, (byte) 0xA8, 0x08, 0x00 // add eax, [eax + 0x0a]
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

    @Test
    void addTest() {

        byte[] instructions = {

                0x01, (byte) 0xc0, // add eax, eax
                0x01, (byte) 0xc1, // add ecx, eax
                0x01, (byte) 0xc2, // add edx, eax
                0x01, (byte) 0xc3, // add ebx, eax
                0x02, (byte) 0xc0, // add al, al
                0x02, (byte) 0xc1, // add cl, al
                0x02, (byte) 0xc2, // add dl, al
                0x02, (byte) 0xc3, // add bl, al
                0x03, (byte) 0xc0, // add eax, eax
                0x03, (byte) 0xc1, // add ecx, eax
                0x03, (byte) 0xc2, // add edx, eax
                0x03, (byte) 0xc3, // add ebx, eax
                0x04, (byte) 0xc0, // add al, c0
                0x05, 0x00, 0x10, 0x20, 0x30 // add [0x102030], eax

        };

        String[] expected = {

                "ADD EAX, EAX",
                "ADD ECX, EAX",
                "ADD EDX, EAX",
                "ADD EBX, EAX",
                "ADD AL, AL",
                "ADD AL, CL",
                "ADD AL, DL",
                "ADD AL, BL",
                "ADD EAX, EAX",
                "ADD EAX, ECX",
                "ADD EAX, EDX",
                "ADD EAX, EBX",
                "ADD AL, 0xC0",
                "ADD EAX, 0x30201000"

        };

        Instruction[] actual = disassembler.disassemble(instructions);

        assertEquals(expected.length, actual.length);

        System.out.println("Expected:" + Arrays.toString(expected));

        for (int i = 0; i < actual.length; i++) {
            Assertions.assertEquals(expected[i], actual[i].toString());
        }

        System.out.println("Actual:" + Arrays.toString(actual));
    }

    @Test
    void orTest() {

        byte[] instructions = {

                0x09, (byte) 0xc0, // add eax, eax
                0x09, (byte) 0xc1, // add ecx, eax
                0x09, (byte) 0xc2, // add edx, eax
                0x09, (byte) 0xc3, // add ebx, eax
                0x0A, (byte) 0xc0, // add al, al
                0x0A, (byte) 0xc1, // add cl, al
                0x0A, (byte) 0xc2, // add dl, al
                0x0A, (byte) 0xc3, // add bl, al
                0x0B, (byte) 0xc0, // add eax, eax
                0x0B, (byte) 0xc1, // add ecx, eax
                0x0B, (byte) 0xc2, // add edx, eax
                0x0B, (byte) 0xc3, // add ebx, eax
                0x0C, (byte) 0xc0, // add al, c0
                0x0D, 0x00, 0x10, 0x20, 0x30 // add [0x102030], eax

        };

        String[] expected = {

                "OR EAX, EAX",
                "OR ECX, EAX",
                "OR EDX, EAX",
                "OR EBX, EAX",
                "OR AL, AL",
                "OR AL, CL",
                "OR AL, DL",
                "OR AL, BL",
                "OR EAX, EAX",
                "OR EAX, ECX",
                "OR EAX, EDX",
                "OR EAX, EBX",
                "OR AL, 0xC0",
                "OR EAX, 0x30201000"

        };

        Instruction[] actual = disassembler.disassemble(instructions);

        assertEquals(expected.length, actual.length);

        System.out.println("Expected:" + Arrays.toString(expected));

        for (int i = 0; i < actual.length; i++) {
            Assertions.assertEquals(expected[i], actual[i].toString());
        }

        System.out.println("Actual:" + Arrays.toString(actual));
    }

}
