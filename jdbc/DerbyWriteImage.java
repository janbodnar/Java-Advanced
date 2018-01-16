package com.zetcode;

import com.zetcode.utils.DBUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DerbyWriteImage {

    public static void main(String[] args) {

        Connection con = null;
        PreparedStatement pst = null;

        String url = "jdbc:derby://localhost:1527/testdb";
        String user = "app";
        String password = "app";

        try {

            con = DriverManager.getConnection(url, user, password);

            String fileName = "src/main/resources/sid.jpg";
            
            File img = new File(fileName);

            try (FileInputStream fin = new FileInputStream(img)) {

                pst = con.prepareStatement("INSERT INTO IMAGES(ID, DATA) VALUES(1, ?)");
                pst.setBinaryStream(1, fin, (int) img.length());
                pst.executeUpdate();
            }

        } catch (SQLException | IOException ex) {

            Logger.getLogger(DerbyWriteImage.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            DBUtils.closeStatement(pst);
            DBUtils.closeConnection(con);
        }
    }
}


