package Controller;

import Database.AppointmentDB;
import Model.Appointment;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 *
 * Appointments Controller for appointments.fxml
 *
 * */
public class Appointments implements Initializable {

    @FXML
    private Button createBtn, viewBtn, editBtn, deleteBtn, logoutBtn, logsBtn, reportsBtn, customersBtn, searchBtn;

    @FXML
    private TextField searchTF;

    @FXML
    private TableView<Appointment> appointmentTableView;

    @FXML
    private TableColumn<Appointment, String> nameTC, titleTC, startTC, endTC, dayTC;

    @FXML
    private RadioButton monthRB, weekRB;

    private ToggleGroup toggleGroup;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        /*Creates toggle group for RadioButtons*/
        toggleGroup = new ToggleGroup();
        monthRB.setToggleGroup(toggleGroup);
        weekRB.setToggleGroup(toggleGroup);
        toggleGroup.selectToggle(weekRB);

        /*Generates the Appointment table by week as default*/
        try {
            generateAppointmentsWeek();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        /*Listener for toggle group
        * displays weekly or monthly appointments
        * based on radioButton selection*/
        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if(newValue == null) return;

                if (newValue == weekRB){
                    try {
                        generateAppointmentsWeek();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                } else {
                    try {
                        generateAppointmentsMonth();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }

            }
        });

    }

    /**Generates Appointment Table by Week*/
    public void generateAppointmentsWeek() throws SQLException {
        ObservableList<Appointment> appointments = AppointmentDB.getApptByWeek();
        ifNullAppointments(appointments);
        appointmentTableView.setItems(appointments);
        appointmentTableView.getSelectionModel().selectFirst();
        appointmentTableView.refresh();
    }

    /**Generates Appointment Table by Month*/
    public void generateAppointmentsMonth() throws SQLException {
        ObservableList<Appointment> appointments = AppointmentDB.getApptByMonth();
        ifNullAppointments(appointments);
        appointmentTableView.setItems(appointments);
        appointmentTableView.getSelectionModel().selectFirst();
        appointmentTableView.refresh();
    }

    /**Checks appointments list for null
    * makes edit and view button disabled if null*/
    public void ifNullAppointments(ObservableList<Appointment> appointments){
        if (appointments.isEmpty()){
            editBtn.setDisable(true);
            viewBtn.setDisable(true);
        } else {
            editBtn.setDisable(false);
            viewBtn.setDisable(false);
        }
    }

    /**View Button onAction method*/
    @FXML
    void setViewBtn(){
        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/view_appointment.fxml"));
        ViewAppointment controller = new ViewAppointment(selectedAppointment);
        UserSession.displayNewScene(loader, controller);
    }

    /**Search Button onAction method
    * searches for appointments in database
    * for LIKE Customer_Name*/
    @FXML
    void setSearchBtn()  {
        String name = searchTF.getText();

        try {
            ObservableList<Appointment> appointments;
            if (toggleGroup.getSelectedToggle() == weekRB){
                appointments = AppointmentDB.getApptsByCustomerName(name, "7");
            } else {
                appointments = AppointmentDB.getApptsByCustomerName(name, "30");
            }
            ifNullAppointments(appointments);

            appointmentTableView.setItems(appointments);
            appointmentTableView.getSelectionModel().selectFirst();
            appointmentTableView.refresh();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**Logout Button onAction method*/
    @FXML
    public void setLogoutBtn() throws Exception {
        //get userId from user session

        //display login screen
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/login.fxml"));
        Login controller = new Login();
        UserSession.displayScene(fxmlLoader, controller, createBtn);

    }

    /**Edit Button onAction method*/
    @FXML
    void setEditBtn() {

        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();

        //display updateAppointmentView
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/update_appointment.fxml"));
        UpdateAppointment controller = new UpdateAppointment(selectedAppointment);
        UserSession.displayScene(loader, controller, createBtn);
    }

    /**Delete Button onAction method*/
    @FXML
    void setDeleteBtn() throws SQLException {

        Appointment appointment = appointmentTableView.getSelectionModel().getSelectedItem();

        //on confirmation delete from database
        if (MyAlerts.isConfirmed("Delete this item?")){
            if(AppointmentDB.deleteAppointment(appointment.getAppointmentId())){

                //refresh table
                if (toggleGroup.getSelectedToggle() == weekRB){
                    generateAppointmentsWeek();
                } else {
                    generateAppointmentsMonth();
                }

                //respond with success alert
                MyAlerts.info("Appointment with ID:" + appointment.getAppointmentId() + " and Type: " + appointment.getType() + " successfully deleted.");
            } else {
                MyAlerts.error("DATABASE ERROR: Appointment was not deleted.");
            }
        }

    }

    /**Logs Button onAction method*/
    @FXML
    void setLogsBtn(ActionEvent actionEvent) throws IOException {
        //open log file
        File file = new File("userLog.txt");
        Desktop.getDesktop().open(file);
    }

    /**Reports Button onAction method*/
    @FXML
    void setReportsBtn(ActionEvent actionEvent){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/reports.fxml"));
        Reports controller = new Reports();
        UserSession.displayScene(fxmlLoader, controller, createBtn);
    }

    /**Customers Button onAction method*/
    @FXML
    void setCustomersBtn(ActionEvent actionEvent){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/customers.fxml"));
        Customers controller = new Customers();
        UserSession.displayScene(fxmlLoader, controller, createBtn);
    }

    /**Create Button onAction method*/
    @FXML
    void setCreateBtn(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/add_appointment.fxml"));
        AddAppointment controller = new AddAppointment();
        UserSession.displayScene(fxmlLoader, controller, createBtn);
    }





}
