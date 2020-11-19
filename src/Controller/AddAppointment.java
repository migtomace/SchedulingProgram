package Controller;

import Database.AppointmentDB;
import Database.ContactDB;
import Database.CustomerDB;
import Model.Appointment;
import Model.Contact;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.ArrayList;
import java.util.ResourceBundle;


/**
 *
 * AddAppointment Controller for add_appointment.fxml
 * implements Initializable
 *
 *
 * */
public class AddAppointment implements Initializable {

    @FXML
    private ComboBox<String> startTimeCB;
    @FXML
    private ComboBox<String> endTimeCB;
    @FXML
    private ComboBox<Contact> contactCB;

    @FXML
    private Button addBtn, cancelBtn, searchBtn, refreshBtn;

    @FXML
    private TextField customerTF, titleTF, descriptionTF, locationTF, typeTF, searchTF;

    @FXML
    private TableView<Customer> customerTableView;

    @FXML
    private TableColumn<Customer, String>  idTC, nameTC;

    @FXML
    private DatePicker dateDP;

    private final ObservableList<String> times = FXCollections.observableArrayList();
    private ArrayList<Integer> unavailableTimes = new ArrayList<>();
    private ObservableList<Contact> contacts;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        {
            try {
                contacts = ContactDB.getContacts();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        customerTF.setDisable(true);

        try {
            generateCustomers();
            generateContacts();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        startTimeCB.setDisable(true);
        endTimeCB.setDisable(true);

        contactCB.setConverter(new StringConverter<Contact>() {
            @Override
            public String toString(Contact contact) {
                if (contact == null) {
                    return null;
                }
                return contact.getName();
            }

            @Override
            public Contact fromString(String contact) {
                return null;
            }
        });

        //set listeners

        startTimeCB.setOnAction(event -> generateEndTimes());

        //limit calendar days to future
        dateDP.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0 );
            }
        });



        dateDP.setOnAction(event -> {
            try {
                endTimeCB.setDisable(true);
                unavailableTimes.clear();
                unavailableTimes = AppointmentDB.getAppointmentTimesByDay(dateDP.getValue());
                generateStartTimes();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        customerTableView.getSelectionModel().getSelectedCells().addListener((ListChangeListener<TablePosition>) c -> {
            customerTF.setText(customerTableView.getSelectionModel().getSelectedItem().getCustomerName());
        });

    }

    /**Generates the Contacts for the ComboBox*/
    private void generateContacts() throws SQLException {
        contactCB.getItems().clear();
        contactCB.setItems(contacts);
    }


    /**Generates the Customer Table*/
    public void generateCustomers() throws SQLException {
        ObservableList<Customer> customers = CustomerDB.getAllCustomers();
        if (customers.isEmpty()){
            addBtn.setDisable(true);
        } else {
            addBtn.setDisable(false);
            customerTableView.setItems(customers);
            customerTableView.getSelectionModel().selectFirst();
            customerTableView.refresh();
        }

    }

    /**generates available start times
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
            boolean bool = true;
            int am = num;
            while (am <= 11 && !unavailableTimes.contains(am)){
                if (am == 11){
                    endTimes.add("12:00 PM");
                } else {
                    endTimes.add((am+1) + ":00 AM");
                }
                am++;
            }

            if (!unavailableTimes.contains(12) && am == 12) {
                endTimes.add("1:00 PM");
                am++;
            }

            if (am == 13){
                while (am < 17 && !unavailableTimes.contains(am)){
                    endTimes.add(am-12 + ":00 PM");
                    am++;
                }
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


    /**Add Button onAction Method*/
    @FXML
    void addBtnAction(){
        //try verify form and then display main
        boolean verified = verifyForm();
        if(verified){

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/appointments.fxml"));
            Appointments controller = new Appointments();
            UserSession.displayScene(fxmlLoader, controller, addBtn);
        }

    }

    /**Search Button onAction Method*/
    @FXML
    void setSearchBtn() throws SQLException {
        String name = searchTF.getText();
        ObservableList<Customer> customers = CustomerDB.getCustomerByName(name);
        customerTableView.setItems(customers);
        customerTableView.getSelectionModel().selectFirst();
        customerTableView.refresh();
    }

    /**converts the localTime and datePicker values
     * to localDateTime and returns the localDateTime value*/
    private LocalDateTime makeLocalDateTime(ComboBox<String> comboBox){
        LocalDate localDate = dateDP.getValue();
        LocalTime localTime = getSelectedLocalTime(comboBox);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);

        return localDateTime;
    }

    /**Verifies form and then updates Appointment*/
    private boolean verifyForm() {
        //check full null values

        if (customerTF.getText().isEmpty()){
            MyAlerts.warning("Select a customer from the table.");
        } else if (titleTF.getText().isEmpty() || descriptionTF.getText().isEmpty() || locationTF.getText().isEmpty() || typeTF.getText().isEmpty() || dateDP.getValue().toString().isEmpty() || startTimeCB.getItems().isEmpty() || endTimeCB.getItems().isEmpty() || contactCB.getItems().isEmpty()){
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
        try {
            Customer customer = CustomerDB.getCustomerByID(customerTableView.getSelectionModel().getSelectedItem().getCustomerId());
            if (!customer.getCustomerName().isEmpty()){
                String name = customer.getCustomerName();


                if (MyAlerts.isConfirmed("Is " + name + " the name of the customer?")) {

                        //insertAppointment into DB

                        try {
                            Appointment appointment = new Appointment();
                            appointment.setCustomer(customer);
                            appointment.setDescription(descriptionTF.getText());
                            appointment.setLocation(locationTF.getText());
                            appointment.setContactID(contactCB.getSelectionModel().getSelectedItem().getId());
                            appointment.setType(typeTF.getText());
                            appointment.setTitle(titleTF.getText());
                            appointment.setStart(ZonedDateTime.of(makeLocalDateTime(startTimeCB), ZoneId.systemDefault()));
                            appointment.setEnd(ZonedDateTime.of(makeLocalDateTime(endTimeCB), ZoneId.systemDefault()));

                            AppointmentDB.insertAppointment(appointment);

                            MyAlerts.info("Your appointment was scheduled for " + dateDP.getValue().toString());

                        } catch (Exception exception){
                            MyAlerts.error("Appointment not added to database");
                            exception.printStackTrace();
                            return false;
                        }

                } else {
                    return false;
                }
            }

        } catch (SQLException throwables) {
            MyAlerts.error("Customer does not exist.");
            return false;
        }




        return true;
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

    /**Cancel Button onAction Method*/
    @FXML
    void setCancelBtn(){


            //if data then confirm cancel
            if (!customerTF.getText().isEmpty() || !titleTF.getText().isEmpty() || !descriptionTF.getText().isEmpty() || !locationTF.getText().isEmpty()|| !typeTF.getText().isEmpty()) {

                confirmCancel();

            } else {

                //no data - continue with cancel
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/appointments.fxml"));
                Appointments controller = new Appointments();
                UserSession.displayScene(fxmlLoader, controller, addBtn);
            }


    }

    /**Cancel confirmation called in setCancelBtn*/
    private void confirmCancel() {
        //confirm cancel
        if (MyAlerts.isConfirmed("Are you sure? Form data will be erased.")) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/appointments.fxml"));
            Appointments controller = new Appointments();
            UserSession.displayScene(fxmlLoader, controller, addBtn);
        }
    }




}
