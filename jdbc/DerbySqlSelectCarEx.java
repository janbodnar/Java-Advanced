package com.zetcode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DerbySqlSelectCarEx {

    public static void main(String[] args) {

        Connection con = null;
        PreparedStatement pst = null;

        String url = "jdbc:derby://localhost:1527/testdb";
        String user = "app";
        String password = "app";

        int id = 7;

        try {

            con = DriverManager.getConnection(url, user, password);
            String query = "SELECT Name, Price FROM CARS WHERE Id = ?";

            pst = con.prepareStatement(query);
            pst.setInt(1, id);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                System.out.println(rs.getString(1));
                System.out.println(rs.getInt(2));
            }

        } catch (SQLException ex) {
            Logger.getLogger(DerbySqlSelectCarEx.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                if (pst != null) {
                    pst.close();
                }

                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {

                Logger lgr = Logger.getLogger(DerbySqlSelectCarEx.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }

        }
    }
}

