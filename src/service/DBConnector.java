package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    public Connection getConn () throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        String connectionString = "jdbc:mysql://localhost:3306/event_manager";
        String user = "root";
        String pswd = "C!ec666";
        return DriverManager.getConnection(connectionString, user, pswd);

    }
}