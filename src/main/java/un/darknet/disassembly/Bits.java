package un.darknet.disassembly;

public class Bits {

    public static final byte BITS_64 = 0b111;
    public static final byte BITS_32 = 0b11;
    public static final byte BITS_16 = 0b1;
    public static final byte BITS_8 = 0b0;

    public static boolean atLeast(byte bits, byte value) {

        return bits >= value;

    }

    public static boolean atMost(byte bits, byte value) {

        return bits <= value;

    }

    public static boolean exactly(byte bits, byte value) {

        return bits == value;

    }

    public static String friendlyName(byte bits) {

        switch (bits) {
            case BITS_64:
                return "64 bits";
            case BITS_32:
                return "32 bits";
            case BITS_16:
                return "16 bits";
            case BITS_8:
                return "8 bits";
        }

        return "UNKNOWN bits";
    }


}
