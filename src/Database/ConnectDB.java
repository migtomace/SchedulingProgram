package Database;


import java.sql.*;

/**
 *
 * ConnectDB is responsible
 * for the MySQL Database Connection
 *
 * */

public class ConnectDB {

    static final String DB_NAME = "WJ06YUD";
    static final String DB_URL = "jdbc:mysql://wgudb.ucertify.com:3306/" + DB_NAME;
    static final String USER = "U06YUD";
    static final String PASS = "53688904848";
    static Connection connection;

    /**Creates connection
    * called in Main and stillConnected()*/
    public static void createConnection() throws SQLException, ClassNotFoundException, Exception
    {
        connection = DriverManager.getConnection(DB_URL, USER, PASS);
        System.out.println("Connected.");
    }

    /**Closes connection called in Main*/
    public static void closeConnection() throws SQLException, ClassNotFoundException, Exception
    {
        connection.close();
        System.out.println("Connection closed.");
    }

    /**Checks connection and reconnects if connection is lost
    * called in all DB methods before attempting to connect to Database*/
    public static void stillConnected() throws SQLException, Exception {
        if (connection.isClosed()) createConnection();
    }

}
