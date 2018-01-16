package com.zetcode;

import com.zetcode.utils.DBUtils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DerbyTransaction {

    public static void main(String[] args) {

        Connection con = null;
        Statement st = null;

        String url = "jdbc:derby://localhost:1527/testdb";
        String user = "app";
        String password = "app";

        try {

            con = DriverManager.getConnection(url, user, password);

            st = con.createStatement();

            con.setAutoCommit(false);

            st.executeUpdate("UPDATE AUTHORS SET NAME = 'Leo Tolstoy' "
                    + "WHERE Id = 1");
            st.executeUpdate("UPDATE BOOKS SET TITLE = 'War and Peace' "
                    + "WHERE Id = 1");
            st.executeUpdate("UPDATE BOOKS SET TITL = 'Anna Karenina' "
                    + "WHERE Id = 2");

            con.commit();

        } catch (SQLException ex) {
            
            if (con != null) {

                try {

                    con.rollback();
                } catch (SQLException ex1) {

                    Logger lgr = Logger.getLogger(DerbyTransaction.class.getName());
                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                }
            }

            Logger lgr = Logger.getLogger(DerbyTransaction.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {

            DBUtils.closeStatement(st);
            DBUtils.closeConnection(con);
        }
    }
}

