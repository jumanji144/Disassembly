package un.darknet.disassembly.decoding;

import un.darknet.disassembly.data.Instruction;
import un.darknet.disassembly.util.Flags;

import java.util.Stack;

public class DecoderContext {

    int opcode;
    long address;

    Flags flags = new Flags();
    Stack<Object> stack = new Stack<>();
    Instruction instruction;
    Object override; // used for any type of override

    @SuppressWarnings("unchecked")
    public <T> T pop() {
        return (T) stack.pop();
    }

    @SuppressWarnings("unchecked")
    public <T> T push(T t) {
        return (T) stack.push(t);
    }

    public int getOpcode() {
        return opcode;
    }

    public void setOpcode(int opcode) {
        this.opcode = opcode;
    }

    public long getAddress() {
        return address;
    }

    public void setAddress(long address) {
        this.address = address;
    }

    public Stack<Object> getStack() {
        return stack;
    }

    public void setStack(Stack<Object> stack) {
        this.stack = stack;
    }

    public Instruction getInstruction() {
        return instruction;
    }

    public void setInstruction(Instruction instruction) {
        this.instruction = instruction;
    }

    public Flags getFlags() {
        return flags;
    }

    public void setFlags(Flags flags) {
        this.flags = flags;
    }

    public Object getOverride() {
        return override;
    }

    public void setOverride(Object override) {
        this.override = override;
    }

}
