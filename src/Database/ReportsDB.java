package Database;

import Controller.UserSession;
import Model.Appointment;
import Model.Customer;;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * ReportsDB Class
 * contains all queries relative to
 * reporting data
 *
 * */
public class ReportsDB {

    /** Generates data for Type Reports, Stores and returns Types and Count in Map(String, Integer)*/
    public static Map<String, Integer> getNumOfApptByType() throws SQLException {

        try {
            ConnectDB.stillConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, Integer> types = new HashMap<>();

        PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement("SELECT a.Type, COUNT(a.Type) as typeCount\n" +
                "FROM appointments a\n" +
                "WHERE MONTH(a.Start) = MONTH(NOW()) AND YEAR(NOW()) = YEAR(a.Start) AND User_ID = ?\n" +
                "GROUP BY a.Type;");
        preparedStatement.setInt(1, UserSession.userId);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            types.put(resultSet.getString("Type"), resultSet.getInt("typeCount"));
        }

        return types;
    }

    /** Generates data for Monthly Schedule and returns appointments for logged in user in ArrayList*/
    public static ArrayList<Appointment> getMonthlyScheduleReport() throws SQLException {

        try {
            ConnectDB.stillConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<Appointment> appts = new ArrayList<>();

        PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement("SELECT a.Appointment_ID, a.Start, a.End, a.Title, c.Customer_Name, c.Customer_ID\n" +
                "FROM appointments a\n" +
                "LEFT JOIN customers c\n" +
                "ON a.Customer_ID = c.Customer_ID\n" +
                "WHERE MONTH(a.Start) = MONTH(NOW()) AND YEAR(NOW()) = YEAR(a.Start) AND a.User_ID = ?\n" +
                "ORDER BY DAY(a.Start);");

        preparedStatement.setInt(1, UserSession.userId);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            Appointment appointment = new Appointment();
            appointment.setAppointmentId(resultSet.getInt("Appointment_ID"));
            appointment.setStart(ZonedDateTime.of(resultSet.getTimestamp("Start").toLocalDateTime(), ZoneId.systemDefault()));
            appointment.setEnd(ZonedDateTime.of(resultSet.getTimestamp("End").toLocalDateTime(), ZoneId.systemDefault()));
            appointment.setName(resultSet.getString("Customer_Name"));
            appointment.setTitle(resultSet.getString("Title"));
            appointment.setCustomer(CustomerDB.getCustomerByID(resultSet.getInt("Customer_ID")));
            appts.add(appointment);
        }



        return appts;
    }

    /** Generates data for Monthly Customers Report and returns in ArrayList*/
    public static ArrayList<Customer> getCustomersForMonth() throws SQLException {

        try {
            ConnectDB.stillConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<Customer> customers = new ArrayList<>();
        ArrayList<Integer> custId = new ArrayList<>();

        PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement("SELECT c.Customer_Name, c.Address, c.Postal_Code, c.Division_ID, c.Phone, c.Customer_ID\n" +
                "FROM customers c\n" +
                "LEFT JOIN appointments a\n" +
                "ON c.Customer_ID = a.Customer_ID\n" +
                "WHERE MONTH(a.Start) = MONTH(NOW()) AND YEAR(NOW()) = YEAR(a.Start) AND User_ID = ?\n" +
                "ORDER BY a.Start;");
        preparedStatement.setInt(1, UserSession.userId);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            Customer customer = new Customer();
            customer.setCustomerId(resultSet.getInt("Customer_ID"));
            customer.setCustomerName(resultSet.getString("Customer_Name"));
            customer.setAddress(resultSet.getString("Address"));
            customer.setPostalCode(resultSet.getString("Postal_Code"));
            customer.setPhone(resultSet.getString("Phone"));
            customer.setDivisionId(resultSet.getInt("Division_ID"));
            if (!custId.contains(customer.getCustomerId())){
                custId.add(customer.getCustomerId());
                customers.add(customer);
            }

        }

        return customers;
    }
}
