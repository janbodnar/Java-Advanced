package com.zetcode;

import com.zetcode.utils.DBUtils;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.derby.jdbc.ClientDataSource;

public class DerbyDataSourceEx2 {
    
    public static void main(String[] args) throws IOException {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {

            DataSource ds = getDS();
            con = ds.getConnection();

            pst = con.prepareStatement("SELECT * FROM Cars");
            rs = pst.executeQuery();

            while (rs.next()) {

                System.out.print(rs.getInt(1));
                System.out.print(": ");
                System.out.println(rs.getString(2));
            }

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(DerbyDataSourceEx2.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            DBUtils.closeResultSet(rs);
            DBUtils.closeStatement(pst);
            DBUtils.closeConnection(con);
        }
    }
    
    public static DataSource getDS() throws IOException  {
        
        Properties props = new Properties();
        
        try (FileInputStream fis = new FileInputStream("src/main/resources/db.properties")) {
            props.load(fis);
        }
        
        ClientDataSource ds = new ClientDataSource();
        ds.setDatabaseName(props.getProperty("derby.database"));
        
        ds.setUser(props.getProperty("derby.username"));
        ds.setPassword(props.getProperty("derby.password"));
        
        ds.setServerName(props.getProperty("derby.server"));
        ds.setPortNumber(Integer.valueOf(props.getProperty("derby.port")));
        return ds;
    }
}

