package un.darknet.disassembly.util;

import un.darknet.disassembly.Disassembler;

import java.util.logging.Logger;

public class Logging {

    public static final Logger LOGGER = Logger.getLogger(Disassembler.class.getName());

    public static void log(String message) {
        LOGGER.info(message);
    }

    public static void log(String message, Throwable e) {
        LOGGER.log(java.util.logging.Level.SEVERE, message, e);
    }

    public static void log(Throwable e) {
        LOGGER.log(java.util.logging.Level.SEVERE, null, e);
    }

    public static void log(String message, Object... args) {
        LOGGER.info(String.format(message, args));
    }

    public static void log(String message, Throwable e, Object... args) {
        LOGGER.log(java.util.logging.Level.SEVERE, String.format(message, args), e);
    }

    public static void warn(String message) {
        LOGGER.warning(message);
    }

    public static void warn(String message, Throwable e) {
        LOGGER.log(java.util.logging.Level.WARNING, message, e);
    }

    public static void warn(String message, Object... args) {
        LOGGER.warning(String.format(message, args));
    }

    public static void warn(String message, Throwable e, Object... args) {
        LOGGER.log(java.util.logging.Level.WARNING, String.format(message, args), e);
    }


}
