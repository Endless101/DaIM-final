package nodes;

import java.util.Map;

/**
 * Super class of all possible nodes.
 * Note the terminology "node" is used since the code started as a parser for Neo4j
 */
public class AbstractNode {
    String type;
    // Contents of a node. A mapping between their dblp element and their value.
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
