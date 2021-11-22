package un.darknet.disassembly.x86_64;

/**
 * Static class containing all x86_64 operations.
 * Operations are a decoded string that contain instructions on what to decode.
 */
public class Operations {

    /**
     * Operation map:
     * <p>
     *     R: Read 2 registers from memory
     *     r: read 1 specific register (must prefix: arch: 0-3 where 3 is 64bit, reg: 0-7)
     *     a: add 2 from stack and push result (e.g. stack: [rsp, rsi] -> "rsp, rsi")
     *     i: push immediate value (must prefix: size: 0-8 where 8 is 64bit)
     */
    public static String[] ops = new String[] {

        "0Ra", "Ra", "0Ra", "Ra", "1i00ra", "4i20ra", "", "",
        "0Ra", "Ra", "0Ra", "Ra", "1i00ra", "4i20ra", "", ""

    };

}
