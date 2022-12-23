package data_io;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Reader {
    /**
     * Read all the data from the dblp.xml file into a stream
     * and parse it line by line using the XML Stream Reader library
     * @throws FileNotFoundException
     * @throws javax.xml.stream.XMLStreamException
     */
    public static void streamReader() throws FileNotFoundException, javax.xml.stream.XMLStreamException {
        System.setProperty("entityExpansionLimit", "0");
        FileInputStream inputStream = new FileInputStream("resources/dblp.xml");
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLEventReader reader = inputFactory.createXMLEventReader(inputStream);
        while (reader.hasNext()) {
            XMLEvent currentEvent = reader.nextEvent();
            if (currentEvent.isStartElement()) {
                Parser.parseElements(reader, currentEvent.asStartElement().getName().getLocalPart(), currentEvent);
            }

        }
    }
}


