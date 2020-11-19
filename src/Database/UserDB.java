package Database;

import Controller.UserSession;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * UserDB Class
 * contains all queries relative to
 * user data
 *
 * */
public class UserDB {

    /** Inserts user into database
     * @param password - String
     * @param username - String*/
    public static void insertUser(String username, String password) throws SQLException {

        try {
            ConnectDB.stillConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement("INSERT INTO users (User_Name, Password, Create_Date, Created_By, Last_Update, Last_Updated_By)"
                + "VALUES (?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?);", Statement.RETURN_GENERATED_KEYS);

        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        preparedStatement.setString(3, username);
        preparedStatement.setString(4, username);
        int result = preparedStatement.executeUpdate();

        if (result > 0){
            getUser(username, password);
        }

    }

    /** Checks if username is taken in database
     * @param username - String
     * @return bool*/
    public static boolean isUsernameTaken(String username) throws SQLException {

        try {
            ConnectDB.stillConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean bool = false;
        PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement("SELECT * FROM users WHERE User_Name = ?");
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) bool = true;


        return bool;
    }

    /** Checks if login credentials are valid
     * @param password - String
     * @param username - String
     * @return bool*/
    public static boolean isCredentials(String username, String password) throws SQLException {

        try {
            ConnectDB.stillConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean bool = false;
        PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement("SELECT * FROM users WHERE User_Name = ? AND Password = ?");
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) bool = true;

        if (bool) getUser(username, password);

        if (bool){
            UserSession.logger.info(username + " logged in successful.");
        } else {
            UserSession.logger.info(username + " login unsuccessful.");
        }

        return bool;
    }

    /** Gets user object from database and stores it in UserSession
     * @param password - String
     * @param username - String */
    public static void getUser(String username, String password) throws SQLException {

        try {
            ConnectDB.stillConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement("SELECT * FROM users WHERE User_Name = ? AND Password = ?");
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            UserSession.username = resultSet.getString("User_Name");
            UserSession.userId = resultSet.getInt("User_ID");
        }

    }
}
