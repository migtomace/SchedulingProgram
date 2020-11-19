package Controller;

import Database.AppointmentDB;
import Database.ContactDB;
import Database.CustomerDB;
import Model.Appointment;
import Model.Contact;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 *
 * UpdateAppointment Controller for update_appointment.fxml
 *
 * */
public class UpdateAppointment implements Initializable {

    @FXML
    private ComboBox<String> startTimeCB, endTimeCB;

    @FXML
    private Button updateBtn, cancelBtn;

    @FXML
    private TextField customerTF, contactTF, titleTF, descriptionTF, locationTF, typeTF;

    @FXML
    private DatePicker dateDP;

    private final ObservableList<String> times = FXCollections.observableArrayList();
    private ArrayList<Integer> unavailableTimes = new ArrayList<>();

    private final Appointment selectedAppointment;

    /*Constructor take selectedAppointment as parameter*/
    public UpdateAppointment(Appointment selectedAppointment){
        this.selectedAppointment = selectedAppointment;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        customerTF.setText(selectedAppointment.getCustomer().getCustomerName());
        titleTF.setText(selectedAppointment.getTitle());
        descriptionTF.setText(selectedAppointment.getDescription());
        locationTF.setText(selectedAppointment.getLocation());
        try {
            Contact contact = ContactDB.getContact(selectedAppointment.getContactID());
            contactTF.setText(contact.getEmail());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        typeTF.setText(selectedAppointment.getType());
        dateDP.setValue(selectedAppointment.getStart().toLocalDate());

        generateStartTimes();

        try {
            unavailableTimes = AppointmentDB.getAppointmentTimesByDay(dateDP.getValue());
            String[] start = selectedAppointment.getStartTime().split(":");
            String[] end = selectedAppointment.getEndTime().split(":");
            int startInt = Integer.parseInt(start[0]);
            int endInt = Integer.parseInt(end[0]);

            if(unavailableTimes.contains(startInt)){
                unavailableTimes.remove(Integer.valueOf(startInt));
            }
            if (unavailableTimes.contains(startInt)){
                unavailableTimes.remove(Integer.valueOf(endInt));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        endTimeCB.setDisable(true);
        //set listeners


        titleTF.textProperty().addListener((observable, oldValue, newValue) -> {
            int max = 255; //max chars for varchar(255)
            if(newValue.length() > max){ //check if value exceeds max
                titleTF.setText(oldValue);
                MyAlerts.error("Title must contain no more than 255 characters.");
            } else {
                titleTF.setText(newValue); //if everything checks out update value
            }
        });

        //limit calendar days to future
        dateDP.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0 );
            }
        });


        /*single line lambda expression calls local method setDateAction()
        When date is selected*/
        dateDP.setOnAction(e -> setDateAction());

        /*single line lambda expression calls local method setStartTimeAction()
        When date is selected*/
        startTimeCB.setOnAction(e -> setStartTimeAction());
    }


    private void setStartTimeAction(){
        if (!startTimeCB.getValue().isEmpty()){
            endTimeCB.setValue("");
            generateEndTimes();
        }
    }

    /**Creates arrays for available start and end times*/
    private void setDateAction(){
        try {
            endTimeCB.setDisable(true);
            unavailableTimes.clear();
            unavailableTimes = AppointmentDB.getAppointmentTimesByDay(dateDP.getValue());
            try {
                unavailableTimes = AppointmentDB.getAppointmentTimesByDay(dateDP.getValue());
                String[] start = selectedAppointment.getStartTime().split(":");
                String[] end = selectedAppointment.getEndTime().split(":");
                int startInt = Integer.parseInt(start[0]);
                int endInt = Integer.parseInt(end[0]);

                if(unavailableTimes.contains(startInt)){
                    unavailableTimes.remove(Integer.valueOf(startInt));
                }
                if (unavailableTimes.contains(startInt)){
                    unavailableTimes.remove(Integer.valueOf(endInt));
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            generateStartTimes();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**generates available starts times
    * and displays them in ComboBox*/
    private void generateStartTimes() {

        times.clear();

        for (int i = 9; i <= 12; i++){
            if (i == 12 && !unavailableTimes.contains(i)){
                times.add(("12:00 PM"));
            } else if (!unavailableTimes.contains(i)){
                times.add(i + ":00 AM");
            }
        }

        for (int i = 1; i<= 5; i++){
            if (!unavailableTimes.contains(i)){
                times.add(i + ":00 PM");
            }
        }
        startTimeCB.setItems(times);
        startTimeCB.setDisable(false);
    }

    /**generates available end times based off of start times
    * and displays them in ComboBox*/
    private void generateEndTimes() {

        ObservableList<String> endTimes = FXCollections.observableArrayList();

        //if pm list times up until next appointment

        String start = startTimeCB.getValue();
        String[] getIntFromString = start.split(":");
        int num = Integer.parseInt(getIntFromString[0]);


        if (num >= 1 && num <= 5){ ////////////////if PM then check that NUM-5
            int pm = num;
            while (pm <= 5 && !unavailableTimes.contains(pm)){
                endTimes.add((pm+1) + ":00 PM");
                pm++;
            }
        } else if (num >= 9 && num <= 11){ ////////////if AM then check that NUM-11 is Available, noon, 1-5
            int am = num;
            while (am <= 11 && !unavailableTimes.contains(am)){
                if (am == 11){
                    endTimes.add("12:00 PM");
                } else {
                    endTimes.add((am+1) + ":00 AM");
                }
                am++;
            }

            if (!unavailableTimes.contains(12)) endTimes.add("1:00 PM");

            int pm = 1;
            while (pm <= 5 && !unavailableTimes.contains(pm)){
                endTimes.add((pm+1) + ":00 PM");
                pm++;
            }

        } else if (num == 12) { ////////////if 12 set all PM times
            if (!unavailableTimes.contains(12)) endTimes.add("1:00 PM");
            int pm = 1;
            while (pm <= 5 && !unavailableTimes.contains(pm)){
                endTimes.add((pm+1) + ":00 PM");
                pm++;
            }
        }

        endTimeCB.setItems(endTimes);
        endTimeCB.setDisable(false);
    }

    /**This method is called as onAction for Update Button*/
    @FXML
    void setUpdateBtn(){
        //try verify form and then display main
        boolean verified = verifyForm();
        if(verified){

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/appointments.fxml"));
            Appointments controller = new Appointments();
            UserSession.displayScene(fxmlLoader, controller, updateBtn);
        }

    }

    /**Verifies form and then updates Appointment*/
    private boolean verifyForm() {

        //check full null values
        if (customerTF.getText().isEmpty() || contactTF.getText().isEmpty() || titleTF.getText().isEmpty() || descriptionTF.getText().isEmpty() || locationTF.getText().isEmpty() || typeTF.getText().isEmpty() || dateDP.getValue().toString().isEmpty() || startTimeCB.getItems().isEmpty() || endTimeCB.getItems().isEmpty()){
            MyAlerts.error("All fields required");
            return false;
        }

        //check that endTime is after startTime
        //if end is AM and Start is PM, then Alert
        //OR if end.removeText < start.removeText, then alert
        String start = startTimeCB.getValue();
        String end = endTimeCB.getValue();
        int startRemove = Integer.parseInt(start.replace(":00 AM", "").replace(":00 PM", ""));
        int endRemove = Integer.parseInt(end.replace(":00 AM", "").replace(":00 PM", ""));

        if (start.contains(":00 PM") && end.contains(":00 AM") || (start.contains(":00 AM") && end.contains(":00 AM") && endRemove < startRemove) || ((start.contains(":00 PM") && startRemove != 12) && end.contains(":00 PM") && endRemove < startRemove)){
            MyAlerts.error("Appointment end time must succeed start time.");
            return false;
        }
        //check that customerID is correct

                if (MyAlerts.isConfirmed("Are you sure you want to update " + customerTF.getText() + "?")) {
                    Customer customer = new Customer();
                    try {
                        ObservableList<Customer> customers = CustomerDB.getCustomerByName(customerTF.getText());
                        customer = customers.get(0);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    //updateAppointment in DB

                    try {
                        Appointment appointment = new Appointment();
                        appointment.setAppointmentId(selectedAppointment.getAppointmentId());
                        appointment.setCustomer(customer);
                        appointment.setDescription(descriptionTF.getText());
                        appointment.setLocation(locationTF.getText());
                        appointment.setType(typeTF.getText());
                        appointment.setTitle(titleTF.getText());
                        Contact contact = new Contact();
                        contact.setEmail(contactTF.getText());
                        contact.setName(customer.getCustomerName());
                        int contactID = ContactDB.insertContact(contact);
                        appointment.setContactID(contactID);
                        appointment.setStart(ZonedDateTime.of(makeLocalDateTime(startTimeCB), ZoneId.systemDefault()));
                        appointment.setEnd(ZonedDateTime.of(makeLocalDateTime(endTimeCB), ZoneId.systemDefault()));

                        if (AppointmentDB.updateAppointment(appointment) > 0){
                            MyAlerts.info("Your appointment was scheduled for " + dateDP.getValue().toString());
                        } else {
                            MyAlerts.error("Appointment not updated in database");
                            return false;
                        }



                    } catch (Exception exception){
                        MyAlerts.error("Appointment not updated in database");
                        exception.printStackTrace();
                        return false;
                    }

                } else {
                    return false;
                }




        return true;
    }


    /*converts the localTime and datePicker values
    * to localDateTime and returns the localDateTime value*/
    private LocalDateTime makeLocalDateTime(ComboBox<String> comboBox){
        LocalDate localDate = dateDP.getValue();
        LocalTime localTime = getSelectedLocalTime(comboBox);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);

        return localDateTime;
    }

    /**Converts the selected time to localTime
    * and returns a LocalTime value*/
    private LocalTime getSelectedLocalTime(ComboBox<String> comboBox){
        String string = comboBox.getValue();

        String[] splitByColon = string.split(":");
        int hour = Integer.parseInt(splitByColon[0]);
        String[] splitBySpace = splitByColon[1].split(" ");
        String timeOfDay = splitBySpace[1];

        //check if am or pm
        if (timeOfDay.equals("PM")){
            if (hour == 12){
                hour = 0;
            } else {
                hour = hour + 12;
            }
        }

        if (hour < 10) {
            string = "0" + hour;
        } else {
            string = hour + "";
        }


        return LocalTime.parse(string + ":00:00");
    }


    /**Method is called as onAction for Cancel Button*/
    @FXML
    void setCancelBtn(){


        //if data then confirm cancel
        if (!customerTF.getText().isEmpty() || contactTF.getText().isEmpty() || !titleTF.getText().isEmpty() || !descriptionTF.getText().isEmpty() || !locationTF.getText().isEmpty() || !typeTF.getText().isEmpty()) {

            confirmCancel();

        } else {
            //no data - continue with cancel
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/appointments.fxml"));
            Appointments controller = new Appointments();
            UserSession.displayScene(fxmlLoader, controller, updateBtn);
        }


    }

    /**cancel confirmation called in setCancelBtn*/
    private void confirmCancel() {
        //confirm cancel
        if (MyAlerts.isConfirmed("Are you sure? Any changes will be erased.")) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/appointments.fxml"));
            Appointments controller = new Appointments();
            UserSession.displayScene(fxmlLoader, controller, updateBtn);
        }
    }


}
