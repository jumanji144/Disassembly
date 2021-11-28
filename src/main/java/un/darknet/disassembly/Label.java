package un.darknet.disassembly;

import un.darknet.disassembly.data.Instruction;

public class Label {

    public String name;
    public LabelType type;
    public long address;
    public Instruction[] references;

}
