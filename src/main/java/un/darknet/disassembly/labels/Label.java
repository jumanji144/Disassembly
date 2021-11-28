package un.darknet.disassembly.labels;

import un.darknet.disassembly.data.Instruction;

public class Label {

    public Label(long address, LabelType type)  {

        this.address = address;
        this.type = type;

        // generate name based on address and naming scheme
        String prefix = type.prefix;
        String name = LabelScheme.generate(address);

        this.name = prefix + name;

    }

    public String name;
    public LabelType type;
    public long address;
    public Instruction[] references;

    @Override
    public String toString() {
        return name;
    }
}
