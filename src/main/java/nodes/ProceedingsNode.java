package nodes;

import data_io.Writer;

import java.util.Map;
// Node for Proceedings Node
public class ProceedingsNode extends AbstractNode {
    public static Map<String, String> proceedingsMap() {
        return Writer.CSVTypes.createMap(Writer.CSVTypes.proceedings);
    }

    public ProceedingsNode(Map<String, String> contents) {
        super("proceedings", contents);
    }

}
