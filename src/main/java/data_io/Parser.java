package data_io;

import nodes.*;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.util.*;

public class Parser {

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

    public static void parseElements(XMLEventReader reader, String type, XMLEvent currentEvent) {

        try {
            DatabaseEntities entityType = DatabaseEntities.valueOf(type);

            AbstractNode node;
            if (entityType == DatabaseEntities.proceedings || entityType == DatabaseEntities.article || entityType == DatabaseEntities.inproceedings) {

                Map<String, String> contents = parse(reader, currentEvent, currentEvent);

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

    public static List<String> parseSingleElement(XMLEventReader reader, XMLEvent currentEvent, XMLEvent startEvent) {
        List<String> typeAndContent = new ArrayList<>();
        if (currentEvent.isStartElement() && !currentEvent.asStartElement().getName().getLocalPart().equals(startEvent.asStartElement().getName().getLocalPart())) {
            try {
                DatabaseEntities elementType = DatabaseEntities.valueOf(currentEvent.asStartElement().getName().getLocalPart());

                while (reader.hasNext()) {
                    if (currentEvent.isEndElement()) {
                        break;
                    } else {

                        if (currentEvent.isCharacters() && !currentEvent.asCharacters().isIgnorableWhiteSpace()) {
                            String elementContent = currentEvent.asCharacters().getData();
                            if (elementType == DatabaseEntities.editor || elementType == DatabaseEntities.author) {
                                PersonNode pnode = parsePerson(elementType, elementContent, startEvent);
                                Writer.writeContent(pnode, pnode.getType() + ".csv");
                            } else {
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

    private static PersonNode parsePerson(DatabaseEntities personType, String name, XMLEvent startEvent) {
        String key = startEvent.asStartElement().getAttributeByName(new QName("key")).toString().replaceFirst("key=", "");
        Map<String, String> personContents = new HashMap<>();
        personContents.put("key", key);
        personContents.put(personType.toString(), name);
        return new PersonNode(personType.toString(), personContents);
    }

    public static Map<String, String> parse(XMLEventReader reader, XMLEvent currentEvent, XMLEvent startElement) throws XMLStreamException {
        String key = startElement.asStartElement().getAttributeByName(new QName("key")).toString().replaceFirst("key=", "");
        Map<String, String> contents;
        String entityType = startElement.asStartElement().getName().getLocalPart();
        if ("article".equals(entityType)) {
            contents = ArticleNode.articleMap();
        } else if ("proceedings".equals(entityType)) {
            contents = ProceedingsNode.proceedingsMap();
        } else {
            contents = InproceedingsNode.inproceedingsMap();
        }
        contents.put("key", key);
        while (!(currentEvent.isEndElement() &&
                (currentEvent.asEndElement().getName().getLocalPart().equals(DatabaseEntities.proceedings.toString()) ||
                        currentEvent.asEndElement().getName().getLocalPart().equals(DatabaseEntities.article.toString()) ||
                        currentEvent.asEndElement().getName().getLocalPart().equals(DatabaseEntities.inproceedings.toString())))) {
            if (currentEvent.isStartElement()) {
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




