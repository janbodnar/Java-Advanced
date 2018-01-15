package com.zetcode;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DerbyReadImage {

    public static void main(String[] args) throws SQLException, IOException {

        Connection con = null;
        PreparedStatement pst = null;
        FileOutputStream fos = null;

        String url = "jdbc:derby://localhost:1527/testdb";
        String user = "app";
        String password = "app";

        try {

            con = DriverManager.getConnection(url, user, password);

            String query = "SELECT DATA FROM IMAGES WHERE ID = 1";
            pst = con.prepareStatement(query);

            ResultSet result = pst.executeQuery();
            result.next();

            fos = new FileOutputStream("src/main/resources/sid.jpg");

            Blob blob = result.getBlob("DATA");
            int len = (int) blob.length();

            byte[] buf = blob.getBytes(1, len);

            fos.write(buf, 0, len);

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
