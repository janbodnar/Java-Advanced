package com.zetcode;

import com.zetcode.utils.DBUtils;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DerbySelectAllCarsPropertiesEx {

    private static Properties getConnectionData() {

        Properties props = new Properties();

        String fileName = "src/main/resources/db.properties";

        try (FileInputStream in = new FileInputStream(fileName)) {
            props.load(in);
        } catch (IOException ex) {
            Logger lgr = Logger.getLogger(DerbySelectAllCarsPropertiesEx.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return props;
    }

    public static void main(String[] args) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        Properties props = getConnectionData();

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.passwd");

        try {

            con = DriverManager.getConnection(url, user, password);
            pst = con.prepareStatement("SELECT * FROM Cars");
            rs = pst.executeQuery();

            while (rs.next()) {

                System.out.print(rs.getInt(1));
                System.out.print(": ");
                System.out.println(rs.getString(2));
            }

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(DerbySelectAllCarsPropertiesEx.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            DBUtils.closeResultSet(rs);
            DBUtils.closeStatement(pst);
            DBUtils.closeConnection(con);
        }
    }
}


