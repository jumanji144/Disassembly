package un.darknet.disassembly.util;

public class Bytes {

    /**
     * Will cast every value in the array directly to a byte
     * @param array The int array
     * @return The byte array
     */
    public static byte[] toBytes(int[] array) {
        byte[] bytes = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            bytes[i] = (byte) array[i];
        }
        return bytes;
    }

}
