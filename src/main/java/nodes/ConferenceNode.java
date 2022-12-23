package nodes;

import java.util.Map;

// Node for Conferences
public class ConferenceNode extends AbstractNode {

    public ConferenceNode(Map<String, String> contents) {
        super("conference", contents);
    }
}
