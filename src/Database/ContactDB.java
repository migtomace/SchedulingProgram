package Database;

import Model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * ContactDB Class
 * contains all queries relative to
 * Contact data
 *
 * */
public class ContactDB {

    /** Inserts new contacts into database*/
    public static int insertContact(Contact contact) throws SQLException {

        try {
            ConnectDB.stillConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement("INSERT INTO contacts (Contact_Name, Email) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, contact.getName());
        preparedStatement.setString(2, contact.getEmail());
        preparedStatement.executeUpdate();

        ResultSet resultSet = preparedStatement.getGeneratedKeys();

        if (resultSet.next()){
            return resultSet.getInt(1);
        }

        return -1;
    }

    /** Returns Contact Object by given contact_id*/
    public static Contact getContact(int contact_id) throws SQLException {

        try {
            ConnectDB.stillConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement("SELECT * FROM contacts WHERE Contact_ID = ?");
        preparedStatement.setInt(1, contact_id);

        ResultSet resultSet = preparedStatement.executeQuery();
        Contact contact = new Contact();
        if (resultSet.next()){
            contact.setId(contact_id);
            contact.setName(resultSet.getString("Contact_Name"));
            contact.setEmail(resultSet.getString("Email"));
        }

        return contact;
    }

//    /** Deletes contact in database by given contactId*/
//    public static boolean deleteContact(int contactId) {
//
//        try {
//            ConnectDB.stillConnected();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        try {
//            PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement("DELETE FROM contacts WHERE Contact_ID = ?;");
//            preparedStatement.setInt(1, contactId);
//            preparedStatement.executeUpdate();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//            return false;
//        }
//
//        return true;
//    }

    public static ObservableList<Contact> getContacts() throws SQLException {

        try {
            ConnectDB.stillConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ObservableList<Contact> contacts = FXCollections.observableArrayList();

        PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement("SELECT * from contacts");

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            Contact contact = new Contact();
            contact.setName(resultSet.getString("Contact_Name"));
            contact.setEmail(resultSet.getString("Email"));
            contact.setId(resultSet.getInt("Contact_ID"));
            contacts.add(contact);
        }

        return contacts;
    }
}
