package un.darknet.disassembly.exception;

import un.darknet.disassembly.data.Opcode;

public class InvalidInstructionException extends Exception{

    long pos;
    Opcode partialOpcode; // maybe partially decoded opcode

    public InvalidInstructionException(long pos, Opcode partialOpcode, Throwable cause){
        super(String.format("Invalid instruction at 0x%x: %s", pos, partialOpcode != null ? partialOpcode : ""), cause);
        this.pos = pos;
        this.partialOpcode = partialOpcode;
    }

    public long getPos(){
        return pos;
    }

    /**
     * Returns a maybe partially decoded opcode, if any.
     * @return the partialOpcode
     */
    public Opcode getPartialOpcode(){
        return partialOpcode;
    }



}
