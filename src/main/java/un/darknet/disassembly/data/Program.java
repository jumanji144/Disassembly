package un.darknet.disassembly.data;

import un.darknet.disassembly.Label;

import java.util.ArrayList;
import java.util.List;

/**
 * General class which contains the input and output of a program.
 */
public class Program {

    public String source;
    // TODO: add debug info and elf / pe labels maybe a ProgramResolver?
    public byte[] code;
    public List<Instruction> instructions = new ArrayList<>();
    List<Label> labels = new ArrayList<>();

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

    public void addInstruction(Instruction instruction) {
        instructions.add(instruction);
    }

    public void addLabel(Label label) {
        labels.add(label);
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public String getSource() {
        return source;
    }

    public byte[] getCode() {
        return code;
    }

}
