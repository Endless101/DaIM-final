package data_io;

import databases.QueriesMDB;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MeasureRuntimesMariaDB {

    public static void main(String[] args) throws SQLException, FileNotFoundException {

        final String USER = "jan";
        final String PASS = "password";
        Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/databaseDIM", USER, PASS);

        int n_repetitions = 30;
        System.out.println("Repeating each measurement " + n_repetitions + " times.");


        File res_file = new File("MDB.csv");

        System.out.println("Starting benchmarks");
        benchmark(n_repetitions, connection, res_file);

        System.out.println("v");
    }


    static public void benchmark(int nrep, Connection connection, File f) throws SQLException, FileNotFoundException {
        PrintWriter csv_writer = new PrintWriter(new FileOutputStream(f, true));
        Statement statement = connection.createStatement();
        QueriesMDB MariaDBqueries = new QueriesMDB();
        for (int i = 1; i <= nrep; i++) {

            long beforeE1Query = System.nanoTime();
            statement.executeQuery(MariaDBqueries.E1);
            long afterE1Query = System.nanoTime();
            long totalTimeE1Query = afterE1Query - beforeE1Query;
            csv_writer.print(" E1:" + totalTimeE1Query);

            long beforeE2Query = System.nanoTime();
            statement.executeQuery(MariaDBqueries.E2);
            long afterE2Query = System.nanoTime();
            long totalTimeE2Query = afterE2Query - beforeE2Query;
            csv_writer.print(" E2:" + totalTimeE2Query);

            long beforeM1Query = System.nanoTime();
            statement.executeQuery(MariaDBqueries.M1);
            long afterM1Query = System.nanoTime();
            long totalTimeM1Query = afterM1Query - beforeM1Query;
            csv_writer.print(" M1:" + totalTimeM1Query);

            long beforeM2Query = System.nanoTime();
            statement.executeQuery(MariaDBqueries.M2);
            long afterM2Query = System.nanoTime();
            long totalTimeM2Query = afterM2Query - beforeM2Query;
            csv_writer.print(" M2:" + totalTimeM2Query);

            long beforeM3Query = System.nanoTime();
            statement.executeQuery(MariaDBqueries.M3);
            long afterM3Query = System.nanoTime();
            long totalTimeM3Query = afterM3Query - beforeM3Query;
            csv_writer.print(" M3:" + totalTimeM3Query);

            long beforeM4Query = System.nanoTime();
            statement.executeQuery(MariaDBqueries.M4);
            long afterM4Query = System.nanoTime();
            long totalTimeM4Query = afterM4Query - beforeM4Query;
            csv_writer.print(" M4:" + totalTimeM4Query);

            long beforeM5Query = System.nanoTime();
            statement.executeQuery(MariaDBqueries.M5);
            long afterM5Query = System.nanoTime();
            long totalTimeM5Query = afterM5Query - beforeM5Query;
            csv_writer.print(" M5:" + totalTimeM5Query);

            long beforeM6Query = System.nanoTime();
            statement.executeQuery(MariaDBqueries.M6);
            long afterM6Query = System.nanoTime();
            long totalTimeM6Query = afterM6Query - beforeM6Query;
            csv_writer.print(" M6:" + totalTimeM6Query);

            csv_writer.println(" ");

            System.out.print(i + " ");
            System.out.flush();
        }
        csv_writer.close();
    }
}