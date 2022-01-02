package un.darknet.disassembly.util;

public class NumberHelper {

    public static String toHex(Number n) {

        // assume biggest data type
        long l = n.longValue();
        StringBuilder s = new StringBuilder(Long.toHexString(l));

        // get actual number of digits
        int dataSize = getSizeInBytes(n);
        int numDigits = dataSize * 2; // each byte is 2 digits

        // pad with zeros
        while (s.length() < numDigits) {
            s.insert(0, "0");
        }

        // reverse string
        s.reverse();

        return s.toString();

    }

    public static int getSizeInBytes(Number n) {

        switch (n.getClass().getSimpleName()) {
            case "Byte":
                return 1;
            case "Short":
                return 2;
            case "Integer":
                return 4;
            case "Long":
                return 8;
            default:
                return 0;
        }

    }

}
