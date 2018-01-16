package com.zetcode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DerbySqlInsertEx {

    public static void main(String[] args) {

        Connection con = null;
        PreparedStatement pst = null;

        String url = "jdbc:derby://localhost:1527/testdb";
        String user = "app";
        String password = "app";
        
        String carName = args[0];
        int price = Integer.valueOf(args[1]);

        try {

            con = DriverManager.getConnection(url, user, password);

            pst = con.prepareStatement("INSERT INTO CARS(Name, Price) VALUES(?, ?)");
            pst.setString(1, carName);
            pst.setInt(2, price);
            pst.executeUpdate();

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(DerbySqlInsertEx.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            DBUtils.closeStatement(pst);
            DBUtils.closeConnection(con);
        }
    }
}
