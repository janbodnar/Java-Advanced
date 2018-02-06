package com.zetcode;

import com.zetcode.utils.DBUtils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DerbyColumnHeaders {

    public static void main(String[] args) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        String url = "jdbc:derby://localhost:1527/testdb";
        String user = "app";
        String password = "app";

        try {

            con = DriverManager.getConnection(url, user, password);

            String query = "SELECT NAME, TITLE From AUTHORS, "
                    + "Books WHERE AUTHORS.ID=BOOKS.AUTHOR_ID";
            pst = con.prepareStatement(query);

            rs = pst.executeQuery();

            ResultSetMetaData meta = rs.getMetaData();

            String colname1 = meta.getColumnName(1);
            String colname2 = meta.getColumnName(2);

            String header = String.format("%-21s%s", colname1, colname2);
            System.out.println(header);

            while (rs.next()) {

                String row = String.format("%-21s", rs.getString(1));
                System.out.print(row);
                System.out.println(rs.getString(2));
            }

        } catch (SQLException ex) {
            
            Logger lgr = Logger.getLogger(DerbyColumnHeaders.class.getName());
            lgr.log(Level.FINEST, ex.getMessage(), ex);
        } finally {

            DBUtils.closeResultSet(rs);
            DBUtils.closeStatement(pst);
            DBUtils.closeConnection(con);
        }
    }
}
