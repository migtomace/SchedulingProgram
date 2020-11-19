package Controller;

import Database.ReportsDB;
import Model.Appointment;
import Model.Customer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


/**
 *
 * Reports Controller for reports.fxml
 *
 * */
public class Reports implements Initializable {

    @FXML
    private TextArea textArea;

    @FXML
    private RadioButton typeRB, scheduleRB, customerRB;

    @FXML
    private Button backBtn;

    private ToggleGroup toggleGroup;

    private Map<String, Integer> types = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toggleGroup = new ToggleGroup();
        typeRB.setToggleGroup(toggleGroup);
        scheduleRB.setToggleGroup(toggleGroup);
        customerRB.setToggleGroup(toggleGroup);
        toggleGroup.selectToggle(typeRB);
        generateTypeReport();

        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (newValue == null) return;

                if(newValue == typeRB){
                    generateTypeReport();
                } else if (newValue == scheduleRB){
                    generateSchedule();
                } else {
                    generateCustomerReport();
                }
            }
        });

    }

    /**Generates a report of appointment types with their occurrences*/
    private void generateTypeReport(){
        textArea.clear();
        textArea.setText("TYPE : COUNT\n\n");
        try {
            types = ReportsDB.getNumOfApptByType();
            for(Map.Entry<String, Integer> entry : types.entrySet()) {
                String type = entry.getKey();
                int count = entry.getValue();

                textArea.appendText(type + " : " + count + "\n");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**Generates a monthly schedule for the signed in user*/
    private void generateSchedule(){
        ArrayList<Appointment> appointments;
        textArea.clear();
        textArea.setText("SCHEDULE FOR CURRENT MONTH\n\n");
        try {
            appointments = ReportsDB.getMonthlyScheduleReport();
            for(Appointment appt : appointments) {
                String start = appt.getStartTime();
                String end = appt.getEndTime();
                String day = appt.getDay();
                String customerName = appt.getName();
                int id = appt.getAppointmentId();
                int custID = appt.getCustomer().getCustomerId();
                String title = appt.getTitle();

                textArea.appendText(start + " - " + end + " || " + day + "\n"
                + "Appointment ID: " + id + " || " + title + "\n"
                        + "Customer ID: " + custID + " || " + customerName + "\n\n");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**Generates a report with all
    * customers for the month*/
    private void generateCustomerReport(){
        ArrayList<Customer> customers;
        textArea.clear();
        textArea.setText("CUSTOMERS THIS MONTH\n\n" +
                "ID: CUSTOMER NAME || PHONE\n");
        try {
            customers = ReportsDB.getCustomersForMonth();
            for(Customer customer : customers) {

                int customerId = customer.getCustomerId();
                String customerName = customer.getCustomerName();
                String phone = customer.getPhone();

                textArea.appendText(customerId + ": " + customerName + " || " + phone +"\n");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**Back button onAction Method*/
    @FXML
    void setBackBtn(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/appointments.fxml"));
        Appointments controller = new Appointments();
        UserSession.displayScene(loader, controller, backBtn);
    }


}
