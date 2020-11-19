package Database;

import Controller.MyAlerts;
import Controller.UserSession;
import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static Database.ConnectDB.connection;

/**
 *
 * CustomerDB Class
 * contains all queries relative to
 * customer data
 *
 * */

public class CustomerDB {

    public static void insertCustomer(Customer customer) throws SQLException {


        try {
            ConnectDB.stillConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(customer.getDivisionId());

        PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement("INSERT INTO customers (Customer_Name, Address, Phone, Postal_Code, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By)"
                + "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?);");
        preparedStatement.setString(1, customer.getCustomerName());
        preparedStatement.setString(2, customer.getAddress());
        preparedStatement.setString(3, customer.getPhone());
        preparedStatement.setString(4, customer.getPostalCode());
        preparedStatement.setInt(5, customer.getDivisionId());
        preparedStatement.setString(6, UserSession.username);
        preparedStatement.setString(7, UserSession.username);
        preparedStatement.executeUpdate();
    }

    public static Customer getCustomerByID(int customerID) throws SQLException {

        try {
            ConnectDB.stillConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement("SELECT * FROM customers where Customer_ID = ?");
        preparedStatement.setInt(1, customerID);
        ResultSet resultSet = preparedStatement.executeQuery();
        Customer customer = new Customer();
        if (resultSet.next()){
            customer.setCustomerId(customerID);
            customer.setCustomerName(resultSet.getString("Customer_Name"));
            customer.setAddress(resultSet.getString("Address"));
            customer.setPhone(resultSet.getString("Phone"));
            customer.setPostalCode(resultSet.getString("Postal_Code"));
            customer.setDivisionId(resultSet.getInt("Division_ID"));
            customer.setCreateDate(resultSet.getDate("Create_Date"));
            customer.setCreatedBy(resultSet.getString("Created_By"));
            customer.setLastUpdate(resultSet.getTimestamp("Last_Update"));
            customer.setLastUpdateBy(resultSet.getString("Last_Updated_By"));

        } else {
            throw new SQLException();
        }
        return customer;
    }

    public static ObservableList<Customer> getCustomerByName(String customerName) throws SQLException {

        ObservableList<Customer> customers = FXCollections.observableArrayList();

        try {
            ConnectDB.stillConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement("SELECT * FROM customers where Customer_Name LIKE ?");

        preparedStatement.setString(1, "%" + customerName + "%");
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            Customer customer = new Customer();
            customer.setCustomerId(resultSet.getInt("Customer_ID"));
            customer.setCustomerName(resultSet.getString("Customer_Name"));
            customer.setAddress(resultSet.getString("Address"));
            customer.setPhone(resultSet.getString("Phone"));
            customer.setPostalCode(resultSet.getString("Postal_Code"));
            customer.setDivisionId(resultSet.getInt("Division_ID"));
            customer.setCreateDate(resultSet.getDate("Create_Date"));
            customer.setCreatedBy(resultSet.getString("Created_By"));
            customer.setLastUpdate(resultSet.getTimestamp("Last_Update"));
            customer.setLastUpdateBy(resultSet.getString("Last_Updated_By"));
            customers.add(customer);

        }

        if (customers.isEmpty()){
            MyAlerts.warning("That customer does not exist. Did you spell it correctly?");
        }

        return customers;
    }

    public static ObservableList<Customer> getAllCustomers() throws SQLException {

        try {
            ConnectDB.stillConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ObservableList<Customer> customers = FXCollections.observableArrayList();

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM customers;");
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            Customer customer = new Customer();
            customer.setCustomerId(resultSet.getInt("Customer_ID"));
            customer.setCustomerName(resultSet.getString("Customer_Name"));
            customer.setAddress(resultSet.getString("Address"));
            customer.setPhone(resultSet.getString("Phone"));
            customer.setPostalCode(resultSet.getString("Postal_Code"));
            customer.setDivisionId(resultSet.getInt("Division_ID"));
            customer.setCreateDate(resultSet.getDate("Create_Date"));
            customer.setCreatedBy(resultSet.getString("Created_By"));
            customer.setLastUpdate(resultSet.getTimestamp("Last_Update"));
            customer.setLastUpdateBy(resultSet.getString("Last_Updated_By"));
            customers.add(customer);
        }

        return customers;
    }

    public static boolean deleteCustomer(int customerId) {


        try {
            ConnectDB.stillConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            AppointmentDB.deleteAppointmentByCustomerId(customerId);

            try {
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM customers WHERE Customer_ID = ?;");
                preparedStatement.setInt(1, customerId);
                preparedStatement.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                return false;
            }

        } catch (Exception exception){
            exception.printStackTrace();
            return false;
        }
        





        return true;
    }

    public static int updateCustomer(Customer customer) {

        try {
            ConnectDB.stillConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        int response = 0;

        try {
            PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement("UPDATE customers " +
                    "SET Customer_Name = ?, Address = ?, Phone = ?, Postal_Code = ?, Division_ID = ?, Last_Update = CURRENT_TIMESTAMP, Last_Updated_By = ? " +
                    "WHERE Customer_ID = ?;");

            preparedStatement.setString(1, customer.getCustomerName());
            preparedStatement.setString(2, customer.getAddress());
            preparedStatement.setString(3, customer.getPhone());
            preparedStatement.setString(4, customer.getPostalCode());
            preparedStatement.setInt(5, customer.getDivisionId());
            preparedStatement.setString(6, UserSession.username);
            preparedStatement.setInt(7, customer.getCustomerId());



            response = preparedStatement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }


        return response;
    }
}
