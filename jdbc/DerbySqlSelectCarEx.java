package com.zetcode;

import com.zetcode.utils.DBUtils;
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
        ResultSet rs = null;

        String url = "jdbc:derby://localhost:1527/testdb";
        String user = "app";
        String password = "app";

        int id = 7;

        try {

            con = DriverManager.getConnection(url, user, password);
            String query = "SELECT Name, Price FROM CARS WHERE Id = ?";

            pst = con.prepareStatement(query);
            pst.setInt(1, id);

            rs = pst.executeQuery();

            if (rs.next()) {

                System.out.println(rs.getString(1));
                System.out.println(rs.getInt(2));
            }

        } catch (SQLException ex) {
            Logger.getLogger(DerbySqlSelectCarEx.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            DBUtils.closeResultSet(rs);
            DBUtils.closeStatement(pst);
            DBUtils.closeConnection(con);
        }
    }
}



