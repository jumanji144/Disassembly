package un.darknet.disassembly.X86;

/**
 * These are the opcodes for the x86_64 architecture.
 *
 * @author Nowilltolife
 */
public class Mnemonics {

    public static Object[] Mnemonics = {


            "ADD", "ADD", "ADD", "ADD", "ADD", "ADD", "PUSH ES", "POP ES",
            "OR", "OR", "OR", "OR", "OR", "OR", "PUSH CS", "", // 0F extended opcodes
            "ADC", "ADC", "ADC", "ADC", "ADC", "ADC", "PUSH SS", "POP SS",
            "SBB", "SBB", "SBB", "SBB", "SBB", "SBB", "PUSH DS", "POP DS",
            "AND", "AND", "AND", "AND", "AND", "AND",
            "PREFIX", // ES segment override
            "DAA",
            "SUB", "SUB", "SUB", "SUB", "SUB", "SUB",
            "PREFIX", // CS segment override
            "DAS",
            "XOR", "XOR", "XOR", "XOR", "XOR", "XOR",
            "PREFIX", // SS segment override
            "AAA",
            "CMP", "CMP", "CMP", "CMP", "CMP", "CMP",
            "PREFIX", // DS segment override
            "AAS",
            // REX PREFIX SPACE (in 32-bit mode is just inc instructions)
            "INC", "INC", "INC", "INC", "INC", "INC", "INC", "INC",
            "DEC", "DEC", "DEC", "DEC", "DEC", "DEC", "DEC", "DEC",
            "PUSH", "PUSH", "PUSH", "PUSH", "PUSH", "PUSH", "PUSH", "PUSH",
            "POP", "POP", "POP", "POP", "POP", "POP", "POP", "POP",
            "PUSHA",
            "POPA",
            "BOUND",
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
            "OUTS", "OUTSB",
            "JO", "JNO", "JB", "JNB", "JZ", "JNZ", "JBE", "JNBE",
            "JS", "JNS", "JP", "JNP", "JL", "JNL", "JLE", "JNLE",
            new String[]{"ADD","OR","ADC","SBB","AND","SUB","XOR","CMP"}, // selection where mod rm decides the mnemonic
            new String[]{"ADD","OR","ADC","SBB","AND","SUB","XOR","CMP"},
            new String[]{"ADD","OR","ADC","SBB","AND","SUB","XOR","CMP"},
            new String[]{"ADD","OR","ADC","SBB","AND","SUB","XOR","CMP"},
            "TEST", "TEST",
            "XCHG", "XCHG",
            "MOV", "MOV", "MOV", "MOV",
            "MOV",
            "LEA",
            "MOV",
            "POP",
            "NOP",
            "XCHG","XCHG","XCHG","XCHG","XCHG","XCHG","XCHG",
            new String[]{"CBW","CWDE","CDQE"},
            new String[]{"CWD","CDQ","CQO"},
            "CALL", "WAIT",
            new String[]{"PUSHF", "PUSHFD", "PUSHFD"},
            new String[]{"POPF", "POPFD", "POPFD"},
            "SAHF", "LAHF",
            "MOV", "MOV", "MOV", "MOV",
            "MOVSB", new String[]{"MOVSW", "MOVSD", "???"},
            "CMPSB", new String[]{"CMPSW", "CMPSD", "???"},
            "TEST", "TEST",
            "STOSB", new String[]{"STOSW", "STOSD", "???"},
            "LODSB", new String[]{"LODSW", "LODSD", "???"},
            "SCASB", new String[]{"SCASW", "SCASD", "???"},
            "MOV", "MOV", "MOV", "MOV", "MOV", "MOV", "MOV", "MOV",
            "MOV", "MOV", "MOV", "MOV", "MOV", "MOV", "MOV", "MOV",
            new String[] {"ROL", "ROR", "RCL", "RCR", "SHL", "SHR", "SAL", "SAR"},
            new String[] {"ROL", "ROR", "RCL", "RCR", "SHL", "SHR", "SAL", "SAR"},
            "RET", "RET"


    };

    public static final String[] Mnemonics_Extended = {

    };

}
