package Database;

import Controller.MyAlerts;
import Controller.UserSession;
import Model.Appointment;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
*
* AppointmentDB Class
* contains all queries relative to
* appointment data
*
* */
public class AppointmentDB {

    /** Inserts new appointment into database*/
    public static Appointment insertAppointment(Appointment appointment) {

        try {
            ConnectDB.stillConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement("INSERT INTO appointments (Customer_ID, User_ID, Contact_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp(), ?, current_timestamp(), ?)");
            preparedStatement.setInt(1, appointment.getCustomer().getCustomerId());
            preparedStatement.setInt(2, UserSession.userId);
            preparedStatement.setInt(3, appointment.getContactID());
            preparedStatement.setString(4, appointment.getTitle());
            preparedStatement.setString(5, appointment.getDescription());
            preparedStatement.setString(6, appointment.getLocation());
            preparedStatement.setString(7, appointment.getType());

            ZonedDateTime startZonedDateTime = appointment.getStart().withZoneSameInstant(ZoneId.systemDefault());
            ZonedDateTime endZonedDateTime = appointment.getEnd().withZoneSameInstant(ZoneId.systemDefault());
            preparedStatement.setTimestamp(8 ,Timestamp.valueOf(startZonedDateTime.toLocalDateTime()));
            preparedStatement.setTimestamp(9, Timestamp.valueOf(endZonedDateTime.toLocalDateTime()));

            preparedStatement.setString(10, UserSession.username);
            preparedStatement.setString(11, UserSession.username);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return appointment;
    }

    /** Returns all appointments for logged in user for the next seven days*/
    public static ObservableList<Appointment> getApptByWeek() throws SQLException {

        try {
            ConnectDB.stillConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement("SELECT * FROM appointments a WHERE datediff(a.Start, NOW()) < 7 AND a.Start > NOW() AND a.User_ID = ? order by a.Start;");
        preparedStatement.setInt(1, UserSession.userId);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            Appointment appointment = new Appointment();
            appointment.setAppointmentId(resultSet.getInt("Appointment_ID"));
            appointment.setCustomer(CustomerDB.getCustomerByID(resultSet.getInt("Customer_ID")));
            appointment.setContactID(resultSet.getInt("Contact_ID"));
            appointment.setDescription(resultSet.getString("Description"));
            appointment.setLocation(resultSet.getString("Location"));
            appointment.setType(resultSet.getString("Type"));
            appointment.setTitle(resultSet.getString("Title"));
            ZonedDateTime startZonedDateTime = ZonedDateTime.of(resultSet.getTimestamp("Start").toLocalDateTime(), ZoneOffset.UTC);
            ZonedDateTime endZonedDateTime = ZonedDateTime.of(resultSet.getTimestamp("End").toLocalDateTime(), ZoneOffset.UTC);
            appointment.setStart(startZonedDateTime);
            appointment.setEnd(endZonedDateTime);

            //add appointment to list
            appointments.add(appointment);
        }

        resultSet.close();

        return appointments;
    }

    /** Returns all appointments for logged in user for the next 31 days*/
    public static ObservableList<Appointment> getApptByMonth() throws SQLException {

        try {
            ConnectDB.stillConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement("SELECT * FROM appointments a WHERE datediff(a.End, NOW()) < 31 AND a.Start > NOW() AND a.User_ID = ? order by a.Start;");
        preparedStatement.setInt(1, UserSession.userId);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            Appointment appointment = new Appointment();
            appointment.setAppointmentId(resultSet.getInt("Appointment_ID"));
            appointment.setCustomer(CustomerDB.getCustomerByID(resultSet.getInt("Customer_ID")));
            appointment.setContactID(resultSet.getInt("Contact_ID"));
            appointment.setDescription(resultSet.getString("Description"));
            appointment.setLocation(resultSet.getString("Location"));
            appointment.setType(resultSet.getString("Type"));
            appointment.setTitle(resultSet.getString("Title"));
            ZonedDateTime startZonedDateTime = ZonedDateTime.of(resultSet.getTimestamp("Start").toLocalDateTime(), ZoneOffset.UTC);
            ZonedDateTime endZonedDateTime = ZonedDateTime.of(resultSet.getTimestamp("End").toLocalDateTime(), ZoneOffset.UTC);
            appointment.setStart(startZonedDateTime);
            appointment.setEnd(endZonedDateTime);
            //add appointment to list
            appointments.add(appointment);
        }

        resultSet.close();

        return appointments;
    }

    /** Deletes appointment in database by given appointment ID*/
    public static boolean deleteAppointment(int appointmentId) {

        try {
            ConnectDB.stillConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement("DELETE FROM appointments WHERE Appointment_ID = ?;");
            preparedStatement.setInt(1, appointmentId);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }



        return true;
    }

    /** Deletes appointment in database by given customer ID*/
    public static void deleteAppointmentByCustomerId(int customerId) {

        try {
            ConnectDB.stillConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement("DELETE a.*\n" +
                    "FROM appointments a\n" +
                    "WHERE a.Customer_ID = ?;");
            preparedStatement.setInt(1, customerId);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    /** Updates appointment in database*/
    public static int updateAppointment(Appointment appointment) {

        try {
            ConnectDB.stillConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        int response = 0;

        try {
            PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement("UPDATE appointments a " +
                    "SET Customer_ID = ?, User_ID = ?, Title = ?, Description = ?, Location = ?, Contact_ID = ?, a.Type = ?, a.Start = ?, a.End = ?, Last_Update = CURRENT_TIMESTAMP, Last_Updated_By = ? " +
                    "WHERE Appointment_ID = ?;");
            preparedStatement.setInt(1, appointment.getCustomer().getCustomerId());
            preparedStatement.setInt(2, UserSession.userId);
            preparedStatement.setString(3, appointment.getTitle());
            preparedStatement.setString(4, appointment.getDescription());
            preparedStatement.setString(5, appointment.getLocation());
            preparedStatement.setInt(6, appointment.getContactID());
            preparedStatement.setString(7, appointment.getType());
            ZonedDateTime startZonedDateTime = appointment.getStart().withZoneSameInstant(ZoneId.systemDefault());
            ZonedDateTime endZonedDateTime = appointment.getEnd().withZoneSameInstant(ZoneId.systemDefault());
            preparedStatement.setTimestamp(8 ,Timestamp.valueOf(startZonedDateTime.toLocalDateTime()));
            preparedStatement.setTimestamp(9, Timestamp.valueOf(endZonedDateTime.toLocalDateTime()));
            preparedStatement.setString(10, UserSession.username);
            preparedStatement.setInt(11, appointment.getAppointmentId());
            response = preparedStatement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }

        return response;

    }

    /** Returns appointment times by given day*/
    public static ArrayList<Integer> getAppointmentTimesByDay(LocalDate dayDate) throws SQLException {

        try {
            ConnectDB.stillConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<Integer> takenTimesOnGivenDay = new ArrayList<>();

        //get all appointments on given day
        PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement("SELECT a.Start, a.End\n" +
                "FROM appointments a\n" +
                "WHERE day(a.Start) = day(?) AND User_ID = ?;");
        preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.of(dayDate, LocalTime.now())));
        preparedStatement.setInt(2, UserSession.userId);
        ResultSet resultSet = preparedStatement.executeQuery();

        //while results add to list
        while (resultSet.next()) {

            int start = resultSet.getTimestamp("Start").toLocalDateTime().getHour();
            int end = resultSet.getTimestamp("End").toLocalDateTime().getHour();

            //business hours are 9 - 5 OR 9:00 - 17:00
            //anything greater than 12 subtract 12
            if (start > 12) start -= 12;
            if (end > 12) end -= 12;
            if (start == 0) start = 12;
            if (end == 0) end = 12;

            //if arrayList does not contain Time within Range (prevent duplicates)
            //then add Time to arrayList (all taken times for given days)
            for (int i = start; i < end; i++){
                if (!takenTimesOnGivenDay.contains(i)){
                    takenTimesOnGivenDay.add(i);
                }
            } }

        return takenTimesOnGivenDay;
    }

    /** Checks if there is an appointment within fifteen minutes*/
    public static void checkUpcomingAppointment(ResourceBundle rb) throws SQLException {

        try {
            ConnectDB.stillConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement("select a.Start, a.Appointment_ID\n" +
                "from appointments a\n" +
                "where date(a.Start) = date(now()) AND time(a.Start) > time(now()) AND time(a.Start) < date_add(now(), interval 15 MINUTE);");

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            Appointment appointment = new Appointment();
            appointment.setStart(ZonedDateTime.of(resultSet.getTimestamp("Start").toLocalDateTime(), ZoneId.systemDefault()));
            appointment.setAppointmentId(resultSet.getInt("Appointment_ID"));
            MyAlerts.warning(rb.getString("appointment") + " " + appointment.getStartTime() + " " + appointment.getDay() + " " + appointment.getAppointmentId());
        } else {
            MyAlerts.info(rb.getString("none"));
        }

    }

    /** Returns appointments that match given name, filters by week or month*/
    public static ObservableList<Appointment> getApptsByCustomerName(String name, String days) throws SQLException {

        try {
            ConnectDB.stillConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ObservableList<Customer> customers = CustomerDB.getCustomerByName(name);

        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        if(!customers.isEmpty()){

            StringBuilder sql = new StringBuilder("SELECT *\n" +
                    "FROM appointments a\n" +
                    "WHERE datediff(a.Start, NOW()) < ").append(days).append(" AND a.Start > NOW() AND User_ID = ").append(UserSession.userId).append(" AND Customer_ID IN (");

            for (Customer customer: customers){
                sql.append(" ").append(customer.getCustomerId()).append(",");
            }
            sql.deleteCharAt(sql.length()-1);
            sql.append(") ORDER BY a.Start;");

            PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement(sql.toString());

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Appointment appointment = new Appointment();
                appointment.setAppointmentId(resultSet.getInt("Appointment_ID"));
                appointment.setCustomer(CustomerDB.getCustomerByID(resultSet.getInt("Customer_ID")));
                appointment.setContactID(resultSet.getInt("Contact_ID"));
                appointment.setDescription(resultSet.getString("Description"));
                appointment.setLocation(resultSet.getString("Location"));
                appointment.setType(resultSet.getString("Type"));
                appointment.setTitle(resultSet.getString("Title"));
                ZonedDateTime startZonedDateTime = ZonedDateTime.of(resultSet.getTimestamp("Start").toLocalDateTime(), ZoneOffset.UTC);
                ZonedDateTime endZonedDateTime = ZonedDateTime.of(resultSet.getTimestamp("End").toLocalDateTime(), ZoneOffset.UTC);
                appointment.setStart(startZonedDateTime);
                appointment.setEnd(endZonedDateTime);
                //add appointment to list
                appointments.add(appointment);
            }


        }




        return appointments;
    }
}
