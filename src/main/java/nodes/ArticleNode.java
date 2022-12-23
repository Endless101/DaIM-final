package nodes;

import data_io.Writer;

import java.util.Map;

// Node for Articles
public class ArticleNode extends AbstractNode {

    public static Map<String, String> articleMap() {
        return Writer.CSVTypes.createMap(Writer.CSVTypes.article);
    }

    public ArticleNode(Map<String, String> contents) {
        super("article", contents);
    }


}
