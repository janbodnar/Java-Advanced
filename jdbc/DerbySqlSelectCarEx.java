package com.zetcode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DerbySqlSelectCarEx {

    public static void main(String[] args) throws SQLException {

        Connection con = null;
        PreparedStatement pst = null;

        String url = "jdbc:derby://localhost:1527/testdb";
        String user = "app";
        String password = "app";

        int id = 11;

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
