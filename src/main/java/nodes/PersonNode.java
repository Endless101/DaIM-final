package nodes;


import java.util.Map;

public class PersonNode extends AbstractNode {
    public PersonNode(String type, Map<String, String> contents) {
        super(type, contents);

    }

    public String getName() {
        return contents.get(type);
    }
}
