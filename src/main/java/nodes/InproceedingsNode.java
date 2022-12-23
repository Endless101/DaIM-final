package nodes;

import data_io.Writer;

import java.util.Map;

public class InproceedingsNode extends AbstractNode {
    public static Map<String, String> inproceedingsMap() {

        return Writer.CSVTypes.createMap(Writer.CSVTypes.inproceeding);
    }

    public InproceedingsNode(Map<String, String> contents) {
        super("inproceeding", contents);

    }
}
