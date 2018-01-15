package com.zetcode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DerbyWriteImage {

    public static void main(String[] args) throws SQLException, FileNotFoundException {

        Connection con = null;
        PreparedStatement pst = null;
        FileInputStream fin = null;

        String url = "jdbc:derby://localhost:1527/testdb";
        String user = "app";
        String password = "app";

        try {

            con = DriverManager.getConnection(url, user, password);

            File img = new File("src/main/resources/sid.jpg");
            fin = new FileInputStream(img);

            pst = con.prepareStatement("INSERT INTO IMAGES(ID, DATA) VALUES(1, ?)");
            pst.setBinaryStream(1, fin, (int) img.length());
            pst.executeUpdate();

        } finally {

            if (pst != null) {
                pst.close();
            }

            if (con != null) {
                con.close();
            }
        }
    }
}
