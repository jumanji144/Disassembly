package un.darknet.disassembly.x86_64;

public class Constants {

    public static String[][] REGISTERS = {

            // 8-bit registers
            {

                "AL", "CL", "DL", "BL", "AH", "CH", "DH", "BH"

            },

            // 16-bit registers
            {

                "AX", "CX", "DX", "BX", "SP", "BP", "SI", "DI"

            },

            // 32-bit registers
            {

                "EAX", "ECX", "EDX", "EBX", "ESP", "EBP", "ESI", "EDI"

            },

            // 64-bit registers
            {

                "RAX", "RCX", "RDX", "RBX", "RSP", "RBP", "RSI", "RDI", "R8", "R9", "R10", "R11", "R12", "R13", "R14", "R15"

            }

    };

    public static String[] SEGMENTS = {

        "ES", "CS", "SS", "DS", "FS", "GS"

    };

    public static String[] FLAGS = {

        "CF", "PF", "AF", "ZF", "SF", "TF", "IF", "DF", "OF"

    };


}
