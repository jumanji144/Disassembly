package un.darknet.disassembly.data;

public enum InstructionType {

    JUMP,
    JUMP_RELATIVE,
    LOGIC,
    CALL,
    OTHER;

    public static InstructionType get(String mnemonic) {

        switch (mnemonic) {
            case "JO": case "JNO": case "JB": case "JNB": case "JZ": case "JNZ": case "JBE": case "JNBE":
            case "JS": case "JNS": case "JP": case "JNP": case "JL": case "JNL": case "JLE": case "JNLE":
                return InstructionType.JUMP_RELATIVE;
            case "JMP":
                return InstructionType.JUMP;
            case "CALL":
                return InstructionType.CALL;
            case "AND": case "OR": case "XOR": case "NOT": case "TEST": case "SHL": case "SHR": case "SAR": case "ROL": case "ROR":
                return InstructionType.LOGIC;
            default:
                return InstructionType.OTHER;
        }

    }

}
