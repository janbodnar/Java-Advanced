package com.zetcode;

import com.zetcode.utils.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.derby.jdbc.ClientDataSource;

public class DerbyDataSourceEx {

    public static void main(String[] args) throws SQLException {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {

            DataSource ds = getDS("testdb", "app", "app");
            con = ds.getConnection();

            pst = con.prepareStatement("SELECT * FROM Cars");
            rs = pst.executeQuery();

            while (rs.next()) {

                System.out.print(rs.getInt(1));
                System.out.print(": ");
                System.out.println(rs.getString(2));
            }

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(DerbyDataSourceEx.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            DBUtils.closeResultSet(rs);
            DBUtils.closeStatement(pst);
            DBUtils.closeConnection(con);
        }
    }

    public static DataSource getDS(String database, String user,
            String password) {

        ClientDataSource ds = new ClientDataSource();
        ds.setDatabaseName(database);

        if (user != null) {
            ds.setUser(user);
        }

        if (password != null) {
            ds.setPassword(password);
        }

        ds.setServerName("localhost");
        ds.setPortNumber(1527);
        return ds;
    }
}

