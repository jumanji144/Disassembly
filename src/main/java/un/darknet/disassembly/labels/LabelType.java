package un.darknet.disassembly.labels;

public enum LabelType {

    FUNCTION("func_"),
    BLOCK("block_"),
    DATA("data_"),
    LABEL("label_"),
    UNKNOWN("unknown_");

    final String prefix;


    LabelType(String prefix) {
        this.prefix = prefix;
    }
}
