package un.darknet.disassembly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringsReader {

    /**
     * Reads the strings from the given data.
     * A string is defined as a sequence of bytes that are not 0x00.
     * @param data The data to read the strings from.
     * @param offset the offset to start reading from
     * @param length the length of the data to read
     * @return a map of the strings and their offsets
     */
    public static Map<Long, String> readStrings(byte[] data, int offset, int length) {

        Map<Long, String> strings = new HashMap<>();
        StringBuilder buffer = new StringBuilder();
        for (long i = offset; i < length; i++) {

            char c = (char) data[(int) i];

            if(Character.isISOControl(c)) {
                if(buffer.length() > 0) {
                    strings.put(i, buffer.toString());
                    buffer = new StringBuilder();
                }
            }else
                buffer.append(c);

        }

        return strings;


    }

    /**
     * Reads the strings from the given data.
     * A string is defined as a sequence of bytes that are not 0x00.
     * But this method will only return unique strings.
     * @param data The data to read the strings from.
     * @param offset the offset to start reading from
     * @param length the length of the data to read
     * @return a map of strings with all of their locations
     */
    public static Map<String, List<Long>> readStringsWithoutDuplicates(byte[] data, int offset, int length) {

        Map<Long, String> strings = readStrings(data, offset, length);

        Map<String, List<Long>> stringsWithoutDuplicates = new HashMap<>();

        for(Map.Entry<Long, String> entry : strings.entrySet()) {
            if(!stringsWithoutDuplicates.containsKey(entry.getValue())) {
                stringsWithoutDuplicates.put(entry.getValue(), new ArrayList<>());
            }
            stringsWithoutDuplicates.get(entry.getValue()).add(entry.getKey());
        }

        // sort the list of offsets
        for(Map.Entry<String, List<Long>> entry : stringsWithoutDuplicates.entrySet()) {
            entry.getValue().sort(Long::compareTo);
        }

        return stringsWithoutDuplicates;

    }

}
