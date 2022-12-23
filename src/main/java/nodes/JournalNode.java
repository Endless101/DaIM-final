package nodes;


import java.util.Map;

// Node for Journals
public class JournalNode extends AbstractNode {
    public JournalNode(Map<String, String> contents) {
        super("journal", contents);
    }
}
