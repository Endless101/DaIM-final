package data_io;

import com.opencsv.*;
import nodes.AbstractNode;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class Writer {


    public enum CSVTypes {
        article(new String[]{"volume", "number", "pages", "journal", "year", "title", "key"}),
        inproceeding(new String[]{"pages", "year", "booktitle", "title", "key"}),
        conference(new String[]{"conference", "key"}),
        editor(new String[]{"editor", "key"}),
        proceedings(new String[]{"volume", "year", "publisher", "booktitle", "title", "key"}),
        author(new String[]{"author", "key"}),
        journal(new String[]{"journal", "key"});

        final String[] header;

        CSVTypes(String[] header) {
            this.header = header;
        }

        public static void createFiles() {
            for (CSVTypes type : CSVTypes.values()) {
                writeHeader(type.header, type + ".csv");
            }
        }

        public static Map<String, String> createMap(CSVTypes type) {
            Map<String, String> attributes = new HashMap<>();
            for (String attr : type.header) {
                attributes.put(attr, "null");
            }
            return attributes;
        }

    }

    // Create and write the csv headers
    public static void writeHeader(String[] header, String filename) {
        File file = new File(filename);
        if (file.exists()) {
            file.delete();
        }
        if (!file.exists()) {
            try {
                FileWriter outputFile = new FileWriter(file);
                CSVWriter writer = new CSVWriter(outputFile, ',', CSVWriter.NO_QUOTE_CHARACTER);
                writer.writeNext(header);
                writer.flush();
                writer.close();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

        }
    }
    // Write content from a node to csv
    public static void writeContent(AbstractNode node, String filename) {
        File file = new File(filename);
        if (file.exists()) {
            try {
                String[] content = node.getContent();
                FileWriter outputFile = new FileWriter(file, true);
                CSVWriter writer = new CSVWriter(outputFile, ',', CSVWriter.NO_QUOTE_CHARACTER);
                writer.writeNext(content);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


}


