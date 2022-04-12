import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import un.darknet.disassembly.*;
import un.darknet.disassembly.X86.X86Decoder;
import un.darknet.disassembly.data.Instruction;
import un.darknet.disassembly.data.Opcode;
import un.darknet.disassembly.data.Program;
import un.darknet.disassembly.decoding.DecoderContext;
import un.darknet.disassembly.exception.InvalidInstructionException;
import un.darknet.disassembly.labels.Label;
import un.darknet.disassembly.labels.LabelScheme;
import un.darknet.disassembly.operand.Operand;
import un.darknet.disassembly.operand.OperandObject;
import un.darknet.disassembly.util.Bytes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static un.darknet.disassembly.operand.Operand.*;

public class X86Test {

    static Disassembler disassembler;

    @BeforeAll
    static void setup() {

        disassembler = new Disassembler(Architecture.X86, Endianness.LITTLE);

    }

    public static List<Arguments> getOneByteData() {

        return Arrays.asList(
                Arguments.of(0x06, "PUSH ES"),
                Arguments.of(0x07, "POP ES"),
                Arguments.of(0x0e, "PUSH CS"),
                Arguments.of(0x16, "PUSH SS"),
                Arguments.of(0x1e, "PUSH DS"),
                Arguments.of(0x27, "DAA"),
                Arguments.of(0x2f, "DAS"),
                Arguments.of(0x37, "AAA"),
                Arguments.of(0x3f, "AAS"),
                Arguments.of(0x60, "PUSHA"),
                Arguments.of(0x61, "POPA"),
                Arguments.of(0x62, "BOUND"),
                Arguments.of((byte) 0x90, "NOP"),
                Arguments.of((byte) 0x98, "CWDE"),
                Arguments.of((byte) 0x99, "CDQ"),
                Arguments.of((byte) 0x9b, "WAIT"),
                Arguments.of((byte) 0x9c, "PUSHFD"),
                Arguments.of((byte) 0x9d, "POPFD"),
                Arguments.of((byte) 0x9e, "SAHF"),
                Arguments.of((byte) 0x9f, "LAHF"),
                Arguments.of((byte) 0xa4, "MOVSB"),
                Arguments.of((byte) 0xa5, "MOVSD"),
                Arguments.of((byte) 0xa6, "CMPSB"),
                Arguments.of((byte) 0xa7, "CMPSD"),
                Arguments.of((byte) 0xaa, "STOSB"),
                Arguments.of((byte) 0xab, "STOSD"),
                Arguments.of((byte) 0xac, "LODSB"),
                Arguments.of((byte) 0xad, "LODSD"),
                Arguments.of((byte) 0xae, "SCASB"),
                Arguments.of((byte) 0xaf, "SCASD")
        );

    }

    @Test
    @Order(1)
    public void testDecoder() throws IOException, InvalidInstructionException {

        X86Decoder decoder = new X86Decoder(disassembler.getBackend());

        decoder.feed(new byte[] {0x03, 0x05, 0x56, 0x78, 0x56, 0x34}, 0, 6);

        DecoderContext context = decoder.next();

        assertEquals(context.getInstruction().toString(), "ADD EAX, [0x34567856]");

    }

    @Test
    @Order(2)
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

    private static void common(int[] instructions, String[] expected) {
        common(Bytes.toBytes(instructions), expected);
    }

    private static void common(byte[] instructions, String[] expected) {

        Instruction[] actual = disassembler.disassemble(instructions);

        try {
            assertEquals(expected.length, actual.length);
        } catch (AssertionError e) {
            System.out.println("Expected: " + expected.length + " Actual: " + actual.length);
            // print out the actual instructions
            for (Instruction instruction : actual) {
                System.out.println(instruction.opcode.toString());
            }

            throw e;
        }

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

        // get bytes from resource file
        byte[] instructions = getBytesFromResource("/testprogram.bin");

        disassembler.setBits(Bits.BITS_64);

        Instruction[] actual = disassembler.disassemble(instructions);

        for (Instruction instruction : actual) {

            long instructionStart = instruction.location;
            long instructionEnd = instructionStart + instruction.getLength();

            // get all bytes in the instruction
            byte[] instructionBytes = Arrays.copyOfRange(instructions, (int) instructionStart, (int) instructionEnd);

            for(byte instructionByte : instructionBytes) {
                System.out.printf("%02X ", instructionByte);
            }

            System.out.println(instruction.opcode.toString());

        }

    }

    private byte[] getBytesFromResource(String s) {

        try {

            InputStream inputStream = getClass().getResourceAsStream(s);

            byte[] bytes = new byte[inputStream.available()];

            inputStream.read(bytes);

            return bytes;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @ParameterizedTest
    @MethodSource("getAllRegRMInstructions")
    @Order(3)
    void testRegModRM(String expectedOp, byte prefix) {

        commonRegModRMTest(expectedOp, prefix);

    }

    @Test
    public void testCallInstruction() {

        byte[] instructions = {
                (byte) 0x9A, 0x10, 0x10, 0x00, 0x00, (byte) 0x99, 0x00

        };

        String[] expected = {
                "CALL 0x99:0x1010"
        };

        common(instructions, expected);

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
                (byte) 0x8F, (byte) 0xc0, // POP EAX
                (byte) 0x8F, 0x00, // POP [EAX]
        };

        String[] expected = {

                "PUSH EAX",
                "POP EAX",
                "PUSH 0x46123456",
                "PUSH 0x34",
                "POP EAX",
                "POP [EAX]"

        };

        common(instructions, expected);

    }

    @ParameterizedTest
    @MethodSource("getConditionalJumpData")
    public void testConditionalJumps(int opcode, String expected) {

        common(new byte[] {(byte)opcode, 0x10}, new String[] {expected + " 0x10"});

    }

    @Test
    public void testGroupREGRM() {

        byte[] instructions = {

                (byte) 0x80, (byte) 0xc1, 0x10, // ADD CL, 0x10
                (byte) 0x81, (byte) 0xc1, 0x10, 0x00, 0x00, 0x00, // ADD ECX, 0x10
                (byte) 0x81, 0x01, 0x10, 0x00, 0x00, 0x10, // ADD [ECX], 0x10000010
                (byte) 0x83, (byte) 0xc0, 0x10, // ADD EAX, 0x10


        };

        String[] expected = {

                "ADD CL, 0x10",
                "ADD ECX, 0x10",
                "ADD [ECX], 0x10000010",
                "ADD EAX, 0x10"

        };

        common(instructions, expected);

    }

    @Test
    public void testTest() {

        byte[] instructions = {
                (byte) 0x84, (byte) 0xc0, // TEST AL, AL
                (byte) 0x85, (byte) 0xc0, // TEST EAX, EAX
                (byte) 0x85, 0x00, // TEST [EAX], EAX
                (byte) 0xA8, 0x10, // TEST AL, 0x10
                (byte) 0xA9, 0x10, 0x00, 0x00, 0x00, // TEST EAX, 0x10
        };

        String[] expected = {
                "TEST AL, AL",
                "TEST EAX, EAX",
                "TEST [EAX], EAX",
                "TEST AL, 0x10",
                "TEST EAX, 0x10",
        };

        common(instructions, expected);

    }

    @Test
    public void testXCHG() {

        byte[] instructions = {
                (byte) 0x86, (byte) 0xC9, // XCHG CL, CL
                (byte) 0x87, (byte) 0xC9, // XCHG ECX, ECX
                (byte) 0x87, 0x09, // XCHG [ECX], ECX
                (byte) 0x91, // XCHG ECX, EAX
                (byte) 0x92, // XCHG EDX, EAX
                0x66, (byte) 0x91, // XCHG CX,AX
        };

        String[] expected = {
                "XCHG CL, CL",
                "XCHG ECX, ECX",
                "XCHG ECX, [ECX]",
                "XCHG ECX, EAX",
                "XCHG EDX, EAX",
                "XCHG CX, AX"
        };

        common(instructions, expected);

    }

    @Test
    public void testMOV() {

        byte[] instructions = {
                (byte) 0x88, (byte) 0xC1, // MOV CL, AL
                (byte) 0x89, (byte) 0xC1, // MOV ECX, EAX
                (byte) 0x89, 0x01, // MOV [ECX], EAX
                (byte) 0x8A, (byte) 0xC1, // MOV AL, CL
                (byte) 0x8B, (byte) 0xC1, // MOV EAX, ECX
                (byte) 0x8B, 0x01, // MOV EAX, [ECX]
                (byte) 0x8C, (byte) 0xC0, // MOV eax, es
                (byte) 0x8E, (byte) 0xC0, // MOV es, eax
                (byte) 0xA0, 0x00, 0x00, // MOV AL, [0x0]
                (byte) 0xA1, 0x00, 0x00, 0x00, 0x00, // MOV EAX, [0x0]
                (byte) 0xA2, 0x00, 0x00, // MOV [EAX], AL
                (byte) 0xA3, 0x00, 0x00, 0x00, 0x00, // MOV [EAX], EAX
                (byte) 0xb0, 0x01, // mov al, 0x01
                (byte) 0xb1, 0x02, // mov cl, 0x02
                (byte) 0xb2, 0x03, // mov dl, 0x03
                (byte) 0xb3, 0x04, // mov bl, 0x04
                (byte) 0xb4, 0x05, // mov ah, 0x05
                (byte) 0xb5, 0x06, // mov ch, 0x06
                (byte) 0xb6, 0x07, // mov dh, 0x07
                (byte) 0xb7, 0x08, // mov bh, 0x08
                (byte) 0xb8, 0x09, 0x0a, 0x0b, 0x0c, // mov eax, 0x0c0b0a09
                (byte) 0xb9, 0x0d, 0x0e, 0x0f, 0x10, // mov ecx, 0x100f0e0d
                (byte) 0xba, 0x11, 0x12, 0x13, 0x14, // mov edx, 0x140d0c0b
                (byte) 0xbb, 0x15, 0x16, 0x17, 0x18, // mov ebx, 0x180f0e0d
                (byte) 0xbc, 0x19, 0x1a, 0x1b, 0x1c, // mov esp, 0x1c0b0a09
                (byte) 0xbd, 0x1d, 0x1e, 0x1f, 0x20, // mov ebp, 0x200f0e0d
                (byte) 0xbe, 0x21, 0x22, 0x23, 0x24, // mov esi, 0x240d0c0b
                (byte) 0xbf, 0x25, 0x26, 0x27, 0x28, // mov edi, 0x280f0e0d
        };

        String[] expected = {
                "MOV CL, AL",
                "MOV ECX, EAX",
                "MOV [ECX], EAX",
                "MOV AL, CL",
                "MOV EAX, ECX",
                "MOV EAX, [ECX]",
                "MOV ES, AX",
                "MOV ES, AX",
                "MOV AL, [0x0]",
                "MOV EAX, [0x0]",
                "MOV [0x0], AL",
                "MOV [0x0], EAX",
                "MOV AL, 0x1",
                "MOV CL, 0x2",
                "MOV DL, 0x3",
                "MOV BL, 0x4",
                "MOV AH, 0x5",
                "MOV CH, 0x6",
                "MOV DH, 0x7",
                "MOV BH, 0x8",
                "MOV EAX, 0xC0B0A09",
                "MOV ECX, 0x100F0E0D",
                "MOV EDX, 0x14131211",
                "MOV EBX, 0x18171615",
                "MOV ESP, 0x1C1B1A19",
                "MOV EBP, 0x201F1E1D",
                "MOV ESI, 0x24232221",
                "MOV EDI, 0x28272625"
        };

        common(instructions, expected);

    }

    /**
     * Test for opcodes which only have 1 byte of data
     */
    @ParameterizedTest
    @MethodSource("getOneByteData")
    public void oneInstructionTest(int opcode, String expected) {

        common(new byte[]{(byte)opcode}, new String[]{expected});


    }



    @Test
    public void testLEA() {

        byte[] instructions = {
                (byte) 0x8d, (byte) 0x05, 0x00, 0x00, 0x00, 0x00, // lea eax, [0x00000000]
                (byte) 0x8d, 0x01, // lea eax, [ecx]
                (byte) 0x8d, 0x41, 0x00, // lea eax, [ecx + 0x00]
                (byte) 0x8d, (byte) 0x81, 0x00, 0x00, 0x00, 0x01 , // lea eax, [ecx + 0x1000000]
        };

        String[] expected = {
                "LEA [0x0], EAX",
                "LEA [ECX], EAX",
                "LEA [ECX + 0x0], EAX",
                "LEA [ECX + 0x1000000], EAX"
        };

        common(instructions, expected);

    }

    @Test
    public void testLabels() {

        LabelScheme.globalScheme = LabelScheme.ADDRESS;

        byte[] instructions = {

                0x70, 0x20 // JB 0x20

        };

        Instruction[] insn = disassembler.disassemble(instructions);

        Program program = Program.withInstructions(insn);

        Map<Long, Label> labels = disassembler.getBackend().resolveLabels(program);

        assertEquals(1, labels.size());

        assertEquals(insn[0].toString(), "JO label_00000022");


    }

    @Test
    public void testRexPrefix() {

        disassembler.setBits(Bits.BITS_64);

        int[] instructions = {
                0x48, 0xB8, 0xF0, 0xDE, 0xBC,
                0x9A, 0x78, 0x56, 0x34, 0x12,
                0x48, 0x01, 0xc3, // add rbx, rax
        };

        Instruction[] insn = disassembler.disassemble(instructions);

        assertEquals(2, insn.length);
        assertEquals("MOV RAX, 0x123456789ABCDEF0", insn[0].toString());
        assertEquals("ADD RBX, RAX", insn[1].toString());

    }

    @Test
    public void testRol() {

        int[] instructions = {
                0xC0, 0x00, 0x00, // rol byte ptr [eax], 0
                0xC0, 0xC0, 0x00, // rol al, 0
                0xC0, 0xC9, 0x0a, // ror cl, 0x0a
                0xC0, 0xff, 0x0a, // sar bh, 0x0a
                0xC1, 0xC0, 0x0a, // rol eax, 0xa
                0xC1, 0xC9, 0x0a, // ror ecx, 0xa
                0xC1, 0xff, 0x0a, // sar edi, 0xa
                0xC1, 0xea, 0x0a, // shr edx, 0xa
        };

        String[] expected = {
                "ROL [AL], 0x0",
                "ROL AL, 0x0",
                "ROR CL, 0xA",
                "SAR BH, 0xA",
                "ROL EAX, 0xA",
                "ROR ECX, 0xA",
                "SAR EDI, 0xA",
                "SHR EDX, 0xA"
        };

        common(instructions, expected);

    }


}
