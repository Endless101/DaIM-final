

import databases.DatabaseMDB;
import databases.DatabaseN4J;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws javax.xml.stream.XMLStreamException, FileNotFoundException {
        // Parse data
        data_io.Writer.CSVTypes.createFiles();
        data_io.Reader.streamReader();
        System.out.println("finished");
        // Please make sure both databases are running and the credentials are correctly configured in their respective classes
        // Load data into Neo4j
        DatabaseN4J.main();
        //load data into mariadb
        DatabaseMDB.main();
    }

}
