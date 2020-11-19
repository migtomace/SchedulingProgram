package Controller;

import Database.ContactDB;
import Model.Appointment;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 *
 * ViewAppointment Controller for view_appointment.fxml
 *
 *
 * */
public class ViewAppointment implements Initializable {

    @FXML
    private Button editBtn, doneBtn;

    @FXML
    private Text aidT, cidT, nameT, titleT, descriptionT, locationT, contactT, typeT, dateT, startT, endT;

    private Appointment selectedAppointment;

    /**Constructor that accepts the selected appointment*/
    public ViewAppointment(Appointment selectedAppointment){
        this.selectedAppointment = selectedAppointment;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setAllText();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**Sets all Text values with data from selected appointment
    * called on initialize*/
    private void setAllText() throws SQLException {
        aidT.setText("Appointment ID: " + selectedAppointment.getAppointmentId());
        cidT.setText("Customer ID: " + selectedAppointment.getCustomer().getCustomerId());
        nameT.setText("Customer: " + selectedAppointment.getCustomer().getCustomerName());
        titleT.setText("Title: " + selectedAppointment.getTitle());
        descriptionT.setText("Description: " + selectedAppointment.getDescription());
        locationT.setText("Location: " + selectedAppointment.getLocation());
        String email = ContactDB.getContact(selectedAppointment.getContactID()).getEmail();
        contactT.setText("Contact: " + email);
        typeT.setText("Type: " + selectedAppointment.getType());
        dateT.setText("Date: " + selectedAppointment.getDay());
        startT.setText("Start: " + selectedAppointment.getStartTime());
        endT.setText("End: " + selectedAppointment.getEndTime());
    }

    /**Done Button on Action Method*/
    @FXML
    void setDoneBtn(){
        Stage stage = (Stage) doneBtn.getScene().getWindow();
        stage.close();

    }

}
