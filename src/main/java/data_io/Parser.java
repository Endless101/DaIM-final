package data_io;

import nodes.*;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.util.*;

public class Parser {


    /**
     * Parses and constructs nodes depending on the type that was passed with it,
     * @param reader XML stream reader
     * @param type type of element we need to parse
     * @param currentEvent The current XML event we are on. (Further reading on event types:  <a href="https://docs.oracle.com/javase/8/docs/api/index.html?javax/xml/stream/events/package-summary.html"> XML EVENTS</a>)
     */
    public static void parseElements(XMLEventReader reader, String type, XMLEvent currentEvent) {

        try {
            DatabaseEntities entityType = DatabaseEntities.valueOf(type);

            AbstractNode node;
            if (entityType == DatabaseEntities.proceedings || entityType == DatabaseEntities.article || entityType == DatabaseEntities.inproceedings) {
                // parse the data and and return a map
                Map<String, String> contents = parse(reader, currentEvent, currentEvent);

                // Construct a node depending on the type
                String key = currentEvent.asStartElement().getAttributeByName(new QName("key")).toString().replaceFirst("key=", "");
                if (entityType == DatabaseEntities.article) {
                    node = new ArticleNode(contents);
                } else if (entityType == DatabaseEntities.proceedings) {
                    node = new ProceedingsNode(contents);
                } else {
                    node = new InproceedingsNode(contents);
                }
                parseConferenceOrJournal(key, contents);
                Writer.writeContent(node, node.getType() + ".csv");
            }
        } catch (Exception ignored) {

        }
    }



    /**
     * Parses a person element into a person node.
     * @param personType Editor author
     * @param name name of the person
     * @param startEvent Event that we are currently parsing
     * @return returns a PersonNode
     */
    private static PersonNode parsePerson(DatabaseEntities personType, String name, XMLEvent startEvent) {
        String key = startEvent.asStartElement().getAttributeByName(new QName("key")).toString().replaceFirst("key=", "");
        Map<String, String> personContents = new HashMap<>();
        personContents.put("key", key);
        personContents.put(personType.toString(), name);
        return new PersonNode(personType.toString(), personContents);
    }

    /**
     * A loop that will start from an xml start element for article/proceeding/inproceeding and loop through it
     * while parsing relevant information for that element. The loop stops untill we reach end element of the article/proceeding/inproceeding we want to parse.
      * @param reader XML stream reader
     * @param currentEvent The current event we are parsing
     * @param startElement the start element we are parsing
     * @return A map with all relevant content
     * @throws XMLStreamException
     */
    public static Map<String, String> parse(XMLEventReader reader, XMLEvent currentEvent, XMLEvent startElement) throws XMLStreamException {
        String key = startElement.asStartElement().getAttributeByName(new QName("key")).toString().replaceFirst("key=", "");
        Map<String, String> contents;
        String entityType = startElement.asStartElement().getName().getLocalPart();
        // Create relevent map with correct keys
        if ("article".equals(entityType)) {
            contents = ArticleNode.articleMap();
        } else if ("proceedings".equals(entityType)) {
            contents = ProceedingsNode.proceedingsMap();
        } else {
            contents = InproceedingsNode.inproceedingsMap();
        }
        contents.put("key", key);
        // Loop unless we encounter a relevant end element
        while (!(currentEvent.isEndElement() &&
                (currentEvent.asEndElement().getName().getLocalPart().equals(DatabaseEntities.proceedings.toString()) ||
                        currentEvent.asEndElement().getName().getLocalPart().equals(DatabaseEntities.article.toString()) ||
                        currentEvent.asEndElement().getName().getLocalPart().equals(DatabaseEntities.inproceedings.toString())))) {
            if (currentEvent.isStartElement()) {
                // Parse relevant elements
                List<String> parsedElement = parseSingleElement(reader, currentEvent, startElement);
                if (!parsedElement.isEmpty()) {
                    String type = parsedElement.get(0);
                    String content = parsedElement.get(1).replace(',', ' ');
                    if (contents.containsKey(type))
                        contents.put(type, content);
                }
            }
            currentEvent = reader.nextEvent();
        }
        return contents;
    }



    /**
     * Parses a single XML element that has no child elements from start to finish.
     * @param reader XML stream reader
     * @param currentEvent The current XML event
     * @param startEvent The event we are currently parsing
     * @return return a pair with type and content
     */
    public static List<String> parseSingleElement(XMLEventReader reader, XMLEvent currentEvent, XMLEvent startEvent) {
        List<String> typeAndContent = new ArrayList<>();
        if (currentEvent.isStartElement() && !currentEvent.asStartElement().getName().getLocalPart().equals(startEvent.asStartElement().getName().getLocalPart())) {
            try {
                DatabaseEntities elementType = DatabaseEntities.valueOf(currentEvent.asStartElement().getName().getLocalPart());
                // Loop untill we encounter an end element
                while (reader.hasNext()) {
                    if (currentEvent.isEndElement()) {
                        break;
                    } else {
                        // Extract information
                        if (currentEvent.isCharacters() && !currentEvent.asCharacters().isIgnorableWhiteSpace()) {
                            String elementContent = currentEvent.asCharacters().getData();
                            if (elementType == DatabaseEntities.editor || elementType == DatabaseEntities.author) {
                                PersonNode pnode = parsePerson(elementType, elementContent, startEvent);
                                Writer.writeContent(pnode, pnode.getType() + ".csv");
                            } else {
                                // Add to a pair
                                typeAndContent.add(elementType.toString());
                                typeAndContent.add(elementContent);
                                break;
                            }
                        }
                    }
                    currentEvent = reader.nextEvent();
                }
            } catch (Exception ignored) {
            }
        }
        return typeAndContent;
    }


    /**
     * Writes a journal or article to their respective CSV file depending on the key of the article/proceeding/inproceeding
     * @param key Key to seperate journals from conferences
     * @param contents Already parsed contents
     */
    public static void parseConferenceOrJournal(String key, Map<String, String> contents) {
        String name = contents.get("booktitle");
        Map<String, String> newContents = new HashMap<>();
        newContents.put("key", key);
        AbstractNode node;
        if (key.startsWith("'conf")) {
            newContents.put("conference", name);
            node = new ConferenceNode(newContents);
            Writer.writeContent(node, node.getType() + ".csv");
        } else if (key.startsWith("'journal")) {
            newContents.put("journal", contents.get("journal"));
            node = new JournalNode(newContents);
            Writer.writeContent(node, node.getType() + ".csv");
        }
    }

    /**
     * Different types of DatabaseEntities that we can encounter.
     */
    public enum DatabaseEntities {
        inproceedings,
        article,
        proceedings,
        editor,
        title,
        booktitle,
        publisher,
        volume,
        year,
        author,
        journal,
        number,
        pages
    }
}




