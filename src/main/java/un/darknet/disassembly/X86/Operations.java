package un.darknet.disassembly.X86;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Static class containing all x86_64 operations.
 * Operations are a decoded string that contain instructions on what to decode.
 */
public class Operations {

    // instruction prefixes
    public static final int PREFIX_OPERAND = 0b0000000000000001;

    // flag structure
    // 0000                                      0000                         00000000
    // segment override                          rex prefix                   flags
    // determines which segment override to use  determine which rex is used  determine which flags are set

    public static final int PREFIX_ADDRESS =            0b0000000000000010;
    public static final int PREFIX_SEGMENT_OVERRIDE =   0b0000000000000100;
    public static final int PREFIX_LOCK =               0b0000000000001000;
    public static final int PREFIX_REP =                0b0000000000010000;
    public static final int PREFIX_REPNE =              0b0000000000100000;
    public static final int PREFIX_REX =                0b0000000001000000;
    public static final int PREFIX_LEGACY =             0b0000000010000000; // prefix for legacy instructions (8-bit)

    // REX prefixes
    public static final int REX_W =                     0b0000000100000000;
    public static final int REX_R =                     0b0000001000000000;
    public static final int REX_X =                     0b0000010000000000;
    public static final int REX_B =                     0b0000100000000000;

    // Segment override prefixes
    public static final int SEGMENT_OVERRIDE_ES =       0b0000000000000000;
    public static final int SEGMENT_OVERRIDE_CS =       0b0001000000000000;
    public static final int SEGMENT_OVERRIDE_SS =       0b0010000000000000;
    public static final int SEGMENT_OVERRIDE_DS =       0b0011000000000000;
    public static final int SEGMENT_OVERRIDE_FS =       0b0100000000000000;
    public static final int SEGMENT_OVERRIDE_GS =       0b0101000000000000;
    public static final int SEGMENT_OVERRIDE_MASK =     0b1111000000000000;
    public static final int SEGMENT_OVERRIDE_SHIFT =    12;

    /**
     * Operation map:
     * {@code
     * R: Read 2 registers from memory
     * r: read 1 specific register (must prefix: arch: 0-3 where 3 is 64bit, reg: 0-7)
     * a: add 2 from stack and push result (e.g. stack: [rsp, rsi] -> "rsp, rsi")
     * i: push immediate value (must prefix: size: 0-8 where 8 is 64bit)
     * S: override segment (must prefix: index: 0-7) (e.g. "0S" -> "ES:[")}
     * U: load 1 register based on opcode
     * O: set overflow to object in stack
     */
    public static String[] ops = new String[]{

            "R", "R", "R", "R", "l0ri", "0ri", "", "", // ADD + PUSH + POP
            "R", "R", "R", "R", "l0ri", "0ri", "", "", // OR + PUSH + POP
            "R", "R", "R", "R", "l0ri", "0ri", "", "", // ADC + PUSH + POP
            "R", "R", "R", "R", "l0ri", "0ri", "", "", // SBB + PUSH + POP
            "R", "R", "R", "R", "l0ri", "0ri", // AND
            "0S", // SEGMENT OVERRIDE ES
            "", // DAA
            "R", "R", "R", "R", "l0ri", "0ri", // SUB
            "1S", // SEGMENT OVERRIDE CS
            "", // DAS
            "R", "R", "R", "R", "l0ri", "0ri", // XOR
            "2S", // SEGMENT  OVERRIDE SS
            "", // AAA
            "R", "R", "R", "R", "l0ri", "0ri", // CMP
            "3S", // SEGMENT  OVERRIDE DS
            "", // AAS
            // TODO: 64-bit REX
            "U", "U", "U", "U", "U", "U", "U", "U", // INC
            "U", "U", "U", "U", "U", "U", "U", "U", // DEC
            "U", "U", "U", "U", "U", "U", "U", "U", // PUSH
            "U", "U", "U", "U", "U", "U", "U", "U", // POP
            "", // PUSHA
            "", // POPA
            "", //  BOUND
            "4S", // SEGMENT  OVERRIDE FS
            "5S", // SEGMENT  OVERRIDE GS
            "-", // switch to 16-bit register
            ",", // switch to 16-bit address
            "",
            "i",
            "Ri", // TODO: make imul (something like Ra(size)i(push output)a)
            "li",
            "Rli",
            "", "",
            "", "",
            "li", "li", "li", "li", "li", "li", "li", "li",
            "li", "li", "li", "li", "li", "li", "li", "li",
            "lR", "R", "lR", "0OR", // 0 -> [0] O: 0 -> overflow; imm -> 8-bit immediate
            "lR", "R",
            "lR", "R",
            "lR", "R",
            "lR", "R"

    };

    public static Map<Integer, Integer> prefixToFlag = ImmutableMap.<Integer, Integer>builder()
            .put(0x26, PREFIX_SEGMENT_OVERRIDE | SEGMENT_OVERRIDE_ES)
            .put(0x2e, PREFIX_SEGMENT_OVERRIDE | SEGMENT_OVERRIDE_CS)
            .put(0x36, PREFIX_SEGMENT_OVERRIDE | SEGMENT_OVERRIDE_SS)
            .put(0x3e, PREFIX_SEGMENT_OVERRIDE | SEGMENT_OVERRIDE_DS)
            .put(0x64, PREFIX_SEGMENT_OVERRIDE | SEGMENT_OVERRIDE_FS)
            .put(0x65, PREFIX_SEGMENT_OVERRIDE | SEGMENT_OVERRIDE_GS)
            .put(0x66, PREFIX_OPERAND)
            .put(0x67, PREFIX_ADDRESS)
            .put(0xF0, PREFIX_LOCK)
            .put(0xF2, PREFIX_REPNE)
            .put(0xF3, PREFIX_REP)
            .put(0x40, PREFIX_REX)
            .put(0x41, PREFIX_REX | REX_W)
            .put(0x42, PREFIX_REX | REX_R)
            .put(0x43, PREFIX_REX | REX_R | REX_W)
            .put(0x44, PREFIX_REX | REX_B)
            .put(0x45, PREFIX_REX | REX_B | REX_W)
            .put(0x46, PREFIX_REX | REX_B | REX_R)
            .put(0x47, PREFIX_REX | REX_B | REX_R | REX_W)
            .put(0x48, PREFIX_REX | REX_X)
            .put(0x49, PREFIX_REX | REX_X | REX_W)
            .put(0x4a, PREFIX_REX | REX_X | REX_R)
            .put(0x4b, PREFIX_REX | REX_X | REX_R | REX_W)
            .put(0x4c, PREFIX_REX | REX_X | REX_B)
            .put(0x4d, PREFIX_REX | REX_X | REX_B | REX_W)
            .put(0x4e, PREFIX_REX | REX_X | REX_B | REX_R)
            .put(0x4f, PREFIX_REX | REX_X | REX_B | REX_R | REX_W)
            .build();

}
