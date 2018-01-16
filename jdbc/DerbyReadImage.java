package com.zetcode;

import com.zetcode.utils.DBUtils;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DerbyReadImage {

    public static void main(String[] args) {

        Connection con = null;
        PreparedStatement pst = null;

        String url = "jdbc:derby://localhost:1527/testdb";
        String user = "app";
        String password = "app";

        try {

            con = DriverManager.getConnection(url, user, password);

            String query = "SELECT DATA FROM IMAGES WHERE ID = 1";
            pst = con.prepareStatement(query);

            ResultSet result = pst.executeQuery();
            result.next();

            try (FileOutputStream fos = new FileOutputStream("src/main/resources/sid.jpg")) {

                Blob blob = result.getBlob("DATA");
                int len = (int) blob.length();

                byte[] buf = blob.getBytes(1, len);

                fos.write(buf, 0, len);
            }

        } catch (SQLException | IOException ex) {
            Logger.getLogger(DerbyReadImage.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
   
            DBUtils.closeStatement(pst);
            DBUtils.closeConnection(con);
        }
    }
}

