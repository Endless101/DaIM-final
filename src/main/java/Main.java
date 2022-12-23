

import databases.DatabaseMDB;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws javax.xml.stream.XMLStreamException, FileNotFoundException {
        data_io.Writer.CSVTypes.createFiles();
        data_io.Reader.streamReader();
        System.out.println("finished");
        DatabaseMDB.main();
    }

}
