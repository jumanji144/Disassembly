package un.darknet.disassembly.exception;

public class DisassemblerException extends RuntimeException {

    public DisassemblerException() {
        super();
    }

    public DisassemblerException(String message) {
        super(message);
    }

    public DisassemblerException(String message, Throwable cause) {
        super(message, cause);
    }

    public DisassemblerException(Throwable cause) {
        super(cause);
    }

}
