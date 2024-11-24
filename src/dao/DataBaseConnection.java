package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {

    //Declaring and Defining the Variables that are used to connect with database in such way that
    // they cannot be accessed and cannot be modified in the class
    private static final String MY_SQL_URL = "jdbc:mysql://localhost:3306/epms";
    private static final String MY_SQL_USERNAME = "root";
    private static final String MY_SQL_PASSWORD = "2021";

    private static final String SQL_SERVER_URL = "jdbc:sqlserver://;serverName=localhost;" +
            "databaseName=employee_pay_roll;trustServerCertificate=true";
    private static final String SQL_SERVER_USERNAME = "Babu";
    private static final String SQL_SERVER_PASSWORD = "Babu";

    //getDataBaseConnection() method used to establish the connection between java code and the database and after
    // Successful connection it will return the Connection which can be used in further.
    public Connection getDataBaseConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(MY_SQL_URL, MY_SQL_USERNAME, MY_SQL_PASSWORD);
        } catch (SQLException e) {
            System.out.println("An Error Occurred while connecting to Database." + e.getMessage());
        }
        return connection;
    }

    public Connection getDataBaseConnection1() {

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(SQL_SERVER_URL, SQL_SERVER_USERNAME, SQL_SERVER_PASSWORD);
        } catch (SQLException e) {
            System.out.println("An Error Occurred while connecting to Database." + e.getMessage());
        }

        return connection;
    }
}
