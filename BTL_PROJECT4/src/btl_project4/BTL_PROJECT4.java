/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package btl_project4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 *
 * @author Admin
 */
public class BTL_PROJECT4 {

     /**
     * @param args the command line arguments
     */
    public Connection getConnection() throws SQLException {
        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", "root");
        connectionProps.put("password", "");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/btl_db2", connectionProps);
        return conn;
    }

    public boolean executeUpdate(Connection conn, String command) throws SQLException {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(command); // This will throw a SQLException if it fails
            return true;
        } finally {

            // This will run whether we throw an exception or not
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    public ResultSet executeQuery(Connection conn, String command) throws SQLException {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(command); // This will throw a SQLException if it fails
            return rs;
        } catch (SQLException e) {
            System.out.println("ERROR: Could not query the database");
            e.printStackTrace();
            return null;
        }
    }

    public void run() {
        // Connect DB
        Connection conn = null;
        try {
            conn = this.getConnection();
            System.out.println("Connected to database");
        } catch (SQLException e) {
            System.out.println("ERROR: Could not connect to the database");
            e.printStackTrace();
            return;
        }
    }

      public static void main(String[] args) {
        BTL_PROJECT4 app = new BTL_PROJECT4();
        app.run();
    }
    
}
