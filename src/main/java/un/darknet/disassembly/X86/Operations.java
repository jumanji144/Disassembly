package un.darknet.disassembly.X86;

/**
 * Static class containing all x86_64 operations.
 * Operations are a decoded string that contain instructions on what to decode.
 */
public class Operations {

    /**
     * Operation map:
     * {@code
     *     R: Read 2 registers from memory
     *     r: read 1 specific register (must prefix: arch: 0-3 where 3 is 64bit, reg: 0-7)
     *     a: add 2 from stack and push result (e.g. stack: [rsp, rsi] -> "rsp, rsi")
     *     i: push immediate value (must prefix: size: 0-8 where 8 is 64bit)
     *     S: override segment (must prefix: index: 0-7) (e.g. "0S" -> "ES:[")}
     *     U: load 1 register based on opcode
     */
    public static String[] ops = new String[] {

        "Ra", "Ra", "Ra", "Ra", "1i00ra", "4i20ra", "", "", // ADD + PUSH + POP
        "Ra", "Ra", "Ra", "Ra", "1i00ra", "4i20ra", "", "", // OR + PUSH + POP
        "Ra", "Ra", "Ra", "Ra", "1i00ra", "4i20ra", "", "", // ADC + PUSH + POP
        "Ra", "Ra", "Ra", "Ra", "1i00ra", "4i20ra", "", "", // SBB + PUSH + POP
        "Ra", "Ra", "Ra", "Ra", "1i00ra", "4i20ra", // AND
        "0S", // SEGMENT OVERRIDE ES
        "", // DAA
        "Ra", "Ra", "Ra", "Ra", "1i00ra", "4i20ra", // SUB
        "1S", // SEGMENT OVERRIDE CS
        "", // DAS
        "Ra", "Ra", "Ra", "Ra", "1i00ra", "4i20ra", // XOR
        "2S", // SEGMENT  OVERRIDE SS
        "", // AAA
        "Ra", "Ra", "Ra", "Ra", "1i00ra", "4i20ra", // CMP
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
        "4i",
        "Ra4ioa", // TODO: make imul (something like Ra(size)i(push output)a)
        "1i",
        "Ra1ioa",
        "", "", // TODO: INS, INSB
        "", "", // TODO: OUTS, OUTSB
        "1i","1i","1i","1i","1i","1i","1i","1i",
        "1i","1i","1i","1i","1i","1i","1i","1i",

    };

}
