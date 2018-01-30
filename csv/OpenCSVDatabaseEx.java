package com.zetcode;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OpenCSVDatabaseEx {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/testdb?useSsl=false";
        String user = "testuser";
        String password = "test623";

        String fileName = "src/main/resources/cars.csv";
        Path myPath = Paths.get(fileName);

        try (Connection con = DriverManager.getConnection(url, user, password);
                PreparedStatement pst = con.prepareStatement("SELECT * FROM Cars");
                ResultSet rs = pst.executeQuery()) {

            try (CSVWriter writer = new CSVWriter(Files.newBufferedWriter(myPath,
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
