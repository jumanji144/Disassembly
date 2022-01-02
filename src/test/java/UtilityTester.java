import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import un.darknet.disassembly.util.NumberHelper;

public class UtilityTester {

    @Test
    public void testHex() {

        String byt = "0x11";
        Assertions.assertEquals(byt, NumberHelper.toHex(0x11));

    }

}
