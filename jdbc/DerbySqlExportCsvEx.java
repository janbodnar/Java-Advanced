package com.zetcode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DerbySqlExportCsvEx {

    public static void main(String[] args) throws SQLException {
        Connection con = null;
        Statement st = null;

        String url = "jdbc:derby://localhost:1527/testdb";
        String user = "app";
        String password = "app";

        try {

            con = DriverManager.getConnection(url, user, password);
            String sql = "CALL SYSCS_UTIL.SYSCS_EXPORT_TABLE "
                    + "(null, 'CARS', '/home/janbodnar/myfile.csv', ',' ,'\"', 'UTF-8')";

            st = con.createStatement();
            st.execute(sql);

        } finally {

            if (st != null) {
                st.close();
            }

            if (con != null) {
                con.close();
            }
        }
    }
}
