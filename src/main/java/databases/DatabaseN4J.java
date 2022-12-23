package databases;

import org.neo4j.driver.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class DatabaseN4J {

    static Session session;

    Driver driver;

    static String queriesPath = "resources/insert_data.cypher";

    // Connecting to the Neo4j database
    public DatabaseN4J() {
        // Change credentials to match your database
        driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "password"));
        session = driver.session();
    }

    public static String[] readQueries(String queriesPath) throws IOException {
        File queriesFile = new File(queriesPath);
        byte[] fileBytes = Files.readAllBytes(queriesFile.toPath());
        return (new String(fileBytes)).split(";");
    }

    public void executeQueries() {
        try {
            String[] queries = readQueries(queriesPath);
            for (String q : queries) {
                session.run(q);
                System.out.println("executed: " + q);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        DatabaseN4J db = new DatabaseN4J();
        db.executeQueries();
        db.driver.close();
    }
}
