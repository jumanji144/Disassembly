package un.darknet.disassembly;


public enum Architecture {

    X86,
    UNKNOWN;

    public static Architecture fromString(String name) {
        try {
            return Architecture.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }


}
