package com.zetcode;

import com.zetcode.utils.DBUtils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DerbySqlExportCsvEx {

    public static void main(String[] args) {
        
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

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DerbySqlExportCsvEx.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {

            DBUtils.closeStatement(st);
            DBUtils.closeConnection(con);
        }
    }
}
