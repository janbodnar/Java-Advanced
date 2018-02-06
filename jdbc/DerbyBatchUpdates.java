package com.zetcode;

import com.zetcode.utils.DBUtils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DerbyBatchUpdates {

    public static void main(String[] args) {

        Connection con = null;
        Statement st = null;

        String url = "jdbc:derby://localhost:1527/testdb";
        String user = "app";
        String password = "app";

        try {

            con = DriverManager.getConnection(url, user, password);

            con.setAutoCommit(false);
            st = con.createStatement();

            st.addBatch("DELETE FROM CARS");
            st.addBatch("INSERT INTO CARS(NAME, PRICE) VALUES('Audi', 52642)");
            st.addBatch("INSERT INTO CARS(NAME, PRICE) VALUES('Mercedes', 57127)");
            st.addBatch("INSERT INTO CARS(NAME, PRICE) VALUES('Skoda', 9000)");
            st.addBatch("INSERT INTO CARS(NAME, PRICE) VALUES('Volvo', 29000)");
            st.addBatch("INSERT INTO CARS(NAME, PRICE) VALUES('Bentley', 350000)");
            st.addBatch("INSERT INTO CARS(NAME, PRICE) VALUES('Citroen', 21000)");
            st.addBatch("INSERT INTO CARS(NAME, PRICE) VALUES('Hummer', 41400)");
            st.addBatch("INSERT INTO CARS(NAME, PRICE) VALUES('Volkswagen', 21600)");
            st.addBatch("INSERT INTO CARS(NAME, PRICE) VALUES('Jaguar', 95000)");

            int counts[] = st.executeBatch();

            con.commit();

            System.out.println("Committed " + counts.length + " updates");

        } catch (SQLException ex) {

            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex2) {

                Logger lgr = Logger.getLogger(DerbyBatchUpdates.class.getName());
                lgr.log(Level.FINEST, ex2.getMessage(), ex2);
            }

            Logger lgr = Logger.getLogger(DerbyBatchUpdates.class.getName());
            lgr.log(Level.FINEST, ex.getMessage(), ex);
            
        } finally {

            DBUtils.closeStatement(st);
            DBUtils.closeConnection(con);
        }
    }
}
