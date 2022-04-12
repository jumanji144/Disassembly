package un.darknet.disassembly.data;

import un.darknet.disassembly.operand.Operand;

public class InvalidOpcode implements Opcode{
    @Override
    public String mnemonic() {
        return "???";
    }

    @Override
    public Operand[] operands() {
        return new Operand[0];
    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public String toString() {
        return mnemonic();
    }
}
