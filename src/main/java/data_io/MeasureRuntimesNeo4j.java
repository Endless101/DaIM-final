package data_io;

import databases.QueriesN4J;
import org.neo4j.driver.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;

public class MeasureRuntimesNeo4j {

    static Session session;

    Driver driver;

    public MeasureRuntimesNeo4j() {
        driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "password"));
        session = driver.session();
    }

    public void executeQuery(String query) {
        try {
            Result result = session.run(query);
            System.out.println("executed: " + query);
            System.out.println("result: " + result);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws SQLException, FileNotFoundException, InterruptedException {

        int n_repetitions = 30;
        System.out.println("Repeating each measurement " + n_repetitions + " times.");

        File res_file = new File("N4J.csv");

        System.out.println("Starting benchmarks");
        benchmark(n_repetitions, res_file);

        System.out.println("v");
    }


    static public void benchmark(int nrep, File f) throws SQLException, FileNotFoundException, InterruptedException {
        PrintWriter csv_writer = new PrintWriter(new FileOutputStream(f, true));
        MeasureRuntimesNeo4j db = new MeasureRuntimesNeo4j();
        QueriesN4J Neo4jQueries = new QueriesN4J();
        for (int i = 1; i <= nrep; i++) {

            // note: we execute the necessary query and right after a "dummy" test query
            // this is because if we don't do this all the timestamps are like approximately 1 ms:
            // the query sends to Neo4j but the code in this script is further executed before the query has finished computing
            // if we want to execute a new query, the previous has to be finished first so that's why we have to do this to get correct results

            long beforeE1Query = System.nanoTime();
            db.executeQuery(Neo4jQueries.E1);
            db.executeQuery(Neo4jQueries.TEST);
            long afterE1Query = System.nanoTime();
            long totalTimeE1Query = afterE1Query - beforeE1Query;
            csv_writer.print(" E1:" + totalTimeE1Query);

            long beforeE2Query = System.nanoTime();
            db.executeQuery(Neo4jQueries.E2);
            db.executeQuery(Neo4jQueries.TEST);
            long afterE2Query = System.nanoTime();
            long totalTimeE2Query = afterE2Query - beforeE2Query;
            csv_writer.print(" E2:" + totalTimeE2Query);

            long beforeM1Query = System.nanoTime();
            db.executeQuery(Neo4jQueries.M1);
            db.executeQuery(Neo4jQueries.TEST);
            long afterM1Query = System.nanoTime();
            long totalTimeM1Query = afterM1Query - beforeM1Query;
            csv_writer.print(" M1:" + totalTimeM1Query);

            long beforeM2Query = System.nanoTime();
            db.executeQuery(Neo4jQueries.M2);
            db.executeQuery(Neo4jQueries.TEST);
            long afterM2Query = System.nanoTime();
            long totalTimeM2Query = afterM2Query - beforeM2Query;
            csv_writer.print(" M2:" + totalTimeM2Query);

            long beforeM3Query = System.nanoTime();
            db.executeQuery(Neo4jQueries.M3);
            db.executeQuery(Neo4jQueries.TEST);
            long afterM3Query = System.nanoTime();
            long totalTimeM3Query = afterM3Query - beforeM3Query;
            csv_writer.print(" M3:" + totalTimeM3Query);

            long beforeM4Query = System.nanoTime();
            db.executeQuery(Neo4jQueries.M4);
            db.executeQuery(Neo4jQueries.TEST);
            long afterM4Query = System.nanoTime();
            long totalTimeM4Query = afterM4Query - beforeM4Query;
            csv_writer.print(" M4:" + totalTimeM4Query);

            long beforeM5Query = System.nanoTime();
            db.executeQuery(Neo4jQueries.M5);
            db.executeQuery(Neo4jQueries.TEST);
            long afterM5Query = System.nanoTime();
            long totalTimeM5Query = afterM5Query - beforeM5Query;
            csv_writer.print(" M5:" + totalTimeM5Query);

            long beforeM6Query = System.nanoTime();
            db.executeQuery(Neo4jQueries.M6);
            db.executeQuery(Neo4jQueries.TEST);
            long afterM6Query = System.nanoTime();
            long totalTimeM6Query = afterM6Query - beforeM6Query;
            csv_writer.print(" M6:" + totalTimeM6Query);

            csv_writer.println(" ");

            System.out.print(i + " ");
            System.out.flush();
        }
        csv_writer.close();
        db.driver.close();
    }
}