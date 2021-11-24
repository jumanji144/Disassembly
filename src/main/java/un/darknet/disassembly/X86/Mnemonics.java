package un.darknet.disassembly.X86;

/**
 * These are the opcodes for the x86_64 architecture.
 *
 * @author Nowilltolife
 */
public class Mnemonics {

    public static Object[] Mnemonics = {


            "ADD","ADD","ADD","ADD","ADD","ADD","PUSH ES","POP ES",
            "OR" ,"OR" ,"OR" ,"OR" ,"OR" ,"OR" ,"PUSH CS",""      ,
            "ADC","ADC","ADC","ADC","ADC","ADC","PUSH SS","POP SS",
            "SBB","SBB","SBB","SBB","SBB","SBB","PUSH DS","POP DS",
            "AND","AND","AND","AND","AND","AND",
            "PREFIX", // ES segment override
            "DAA",
            "SUB","SUB","SUB","SUB","SUB","SUB",
            "PREFIX", // CS segment override
            "DAS",
            "XOR","XOR","XOR","XOR","XOR","XOR",
            "PREFIX", // SS segment override
            "AAA",
            "CMP","CMP","CMP","CMP","CMP","CMP",
            "PREFIX", // DS segment override
            "AAS",
            // REX PREFIX SPACE (in 32-bit mode is just inc instructions)
            "INC","INC","INC","INC","INC","INC","INC","INC",
            "DEC","DEC","DEC","DEC","DEC","DEC","DEC","DEC",
            "PUSH","PUSH","PUSH","PUSH","PUSH","PUSH","PUSH","PUSH",
            "POP","POP","POP","POP","POP","POP","POP","POP",
            "PUSHA",
            "POPA",
            "PREFIX",
            "PREFIX",
            "PREFIX",
            "PREFIX",
            "PREFIX",
            "PREFIX",
            "PUSH",
            "IMUL",
            "PUSH",
            "IMUL",
            "INS", "INSB",
            "OUTS","OUTSB",
            "JO","JNO","JB","JNB","JZ","JNZ","JBE","JNBE",
            "JS","JNS","JP","JNP","JL","JNL","JLE","JNLE",


    };

}
