package un.darknet.disassembly.data;

import un.darknet.disassembly.labels.Label;
import un.darknet.disassembly.operand.OperandObject;

import java.util.*;

/**
 * General class which contains the input and output of a program.
 */
public class Program {

    public String source;
    // TODO: add debug info and elf / pe labels maybe a ProgramResolver?
    public byte[] code;
    public List<Instruction> instructions = new ArrayList<>();
    Map<Object, OperandObject> operandObjectPool;
    Map<Long, Label> labels = new HashMap<>();

    public Program() {
    }

    public Program(String source, byte[] code) {
        this.source = source;
        this.code = code;
    }

    public static Program withCode(byte[] code) {
        Program p = new Program();
        p.code = code;
        return p;
    }

    public static Program withInstructions(Instruction... instructions) {
        Program p = new Program();
        p.instructions = Arrays.asList(instructions);
        return p;
    }

    public void addInstruction(Instruction instruction) {
        instructions.add(instruction);
    }

    public void addLabel(Label label) {
        labels.put(label.address, label);
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public Map<Long, Label> getLabels() {
        return labels;
    }

    public String getSource() {
        return source;
    }

    public byte[] getCode() {
        return code;
    }

}
