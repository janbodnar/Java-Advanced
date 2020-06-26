package com.zetcode;

import com.opencsv.CSVWriter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OpenCSVDatabaseEx {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/mydb?serverTimezone=UTC&useSsl=false";
        String user = "root";
        String password = "andrea";

        String fileName = "src/main/resources/cars.csv";
        Path myPath = Paths.get(fileName);

        try (var con = DriverManager.getConnection(url, user, password);
             var pst = con.prepareStatement("SELECT * FROM cars");
             var rs = pst.executeQuery()) {

            try (var writer = new CSVWriter(Files.newBufferedWriter(myPath,
                    StandardCharsets.UTF_8), CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END)) {

                writer.writeAll(rs, true);
            }

        } catch (SQLException | IOException ex) {
            Logger.getLogger(OpenCSVDatabaseEx.class.getName()).log(
                    Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
