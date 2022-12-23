package nodes;

import java.util.Map;


public class AbstractNode {
    String type;
    Map<String, String> contents;

    public String getType() {
        return type;
    }

    public String[] getContent() {
        return contents.values().toArray(new String[0]);
    }

    @Override
    public String toString() {
        return "nodes.AbstractNode{" +
                "type='" + type + '\'' +
                ", contents=" + contents +
                '}';
    }

    AbstractNode(String type, Map<String, String> contents) {
        this.type = type;
        contents.replaceAll((k, v) -> clean(contents.get(k)));
        this.contents = contents;
    }

    private String clean(String s) {

        return "'" + s.replace("'", " ") + "'";
    }
}
