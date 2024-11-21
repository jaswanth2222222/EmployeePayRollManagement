package Connection;

import java.sql.Connection;
import java.sql.DriverAction;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {

    //Declaring and Defining the Variables that are used to connect with database in such way that
    // they cannot be accessed and cannot be modified in the class

    private static final String URL = "jdbc:mysql://localhost:3306/EmployeePayRoll";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1234";

    //getDataBaseConnection() method used to establish the connection between java code and the database and after
    // Successful connection it will return the Connection which can be used in further.
    public Connection getDataBaseConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return connection;
    }

    public Connection getSqlServerConnection() {

        String url = "jdbc:sqlserver://localhost:1433;databaseName=EmployeePayRoll";
        String username = "Babu";
        String password = "Babu";

        try {
            // Establish the connection
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connection established successfully!");

            // Perform database operations here (e.g., querying the database)

            // Close the connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
