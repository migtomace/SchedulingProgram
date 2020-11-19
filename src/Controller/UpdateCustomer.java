package Controller;

import Database.CustomerDB;
import Database.DivisionDB;
import Model.Country;
import Model.Customer;
import Model.Division;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 *
 * UpdateCustomer Controller for update_customer.fxml
 *
 * */
public class UpdateCustomer implements Initializable {

    @FXML
    private TextField nameTF, addressTF, postalcodeTF, phoneTF, idTF;

    @FXML
    private ComboBox<String> countryCB, divisionCB;

    @FXML
    private Button cancelBtn, updateBtn;

    private final Customer selectedCustomer;

    private static final ObservableList<String> countryNames = FXCollections.observableArrayList();
    private static final ObservableList<String> divisionNames = FXCollections.observableArrayList();

    /** Constructor takes selectedCustomer as parameter*/
    public UpdateCustomer(Customer selectedCustomer){
        this.selectedCustomer = selectedCustomer;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /*Single line lambda to get country names
        for each Country in ArrayList countries*/
        UserSession.countries.forEach(country -> countryNames.add(country.getCountry()));
        countryCB.setItems(countryNames);

        /*Single line lambda to get division names
        for each Division in ArrayList divisions*/
        UserSession.divisions.forEach(division -> divisionNames.add(division.getDivision()));
        divisionCB.setItems(divisionNames);
;
        /*Set TextFields to Selected Customer Data*/
        nameTF.setText(selectedCustomer.getCustomerName());
        addressTF.setText(selectedCustomer.getAddress());
        phoneTF.setText(selectedCustomer.getPhone());
        postalcodeTF.setText(selectedCustomer.getPostalCode());
        idTF.setText(selectedCustomer.getCustomerId() + "");
        try {
            Division division = DivisionDB.getDivisionByID(selectedCustomer.getDivisionId());
            divisionCB.getSelectionModel().select(division.getDivision());
            Country country = DivisionDB.getCountryByDivisionID(selectedCustomer.getDivisionId());
            countryCB.getSelectionModel().select(country.getCountry());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        /*Listener for Country Selection
         * OnSelection sets items for Division ComboBox*/
        countryCB.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    divisionCB.setItems(DivisionDB.getDivisionByCountry(newValue));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        /*Listener for Name TextField
         * restricts value length*/
        nameTF.textProperty().addListener((observable, oldValue, newValue) -> {
            int max = 50; //max chars for varchar(50)
            if(newValue.length() > max){ //check if value exceeds max
                nameTF.setText(oldValue);
                MyAlerts.error("Name must contain no more than 50 characters.");
            } else {
                nameTF.setText(newValue); //if everything checks out update value
            }
        });

        /*Listener for Address TextField
         * restricts value length*/
        addressTF.textProperty().addListener((observable, oldValue, newValue) -> {
            int max = 100; //max chars for varchar(100)
            if(newValue.length() > max){ //check if value exceeds max
                addressTF.setText(oldValue);
                MyAlerts.error("Address and Address2 must contain no more than 100 characters.");
            } else {
                addressTF.setText(newValue); //if everything checks out update value
            }
        });

        /*Listener for PostalCode TextField
         * restricts value length*/
        postalcodeTF.textProperty().addListener((observable, oldValue, newValue) -> {
            int max = 50; //max chars for varchar(50)
            if(newValue.length() > max){ //check if value exceeds max
                postalcodeTF.setText(oldValue);
                MyAlerts.error("Postal Code must contain no more than 50 characters.");
            } else {
                postalcodeTF.setText(newValue); //if everything checks out update value
            }
        });

        /*Listener for Phone TextField
         * restricts value length*/
        phoneTF.textProperty().addListener((observable, oldValue, newValue) -> {
            int max = 50; //max chars for varchar(50)
            if(newValue.length() > max){ //check if value exceeds max
                phoneTF.setText(oldValue);
                MyAlerts.error("Phone must contain no more than 50 characters.");
            } else {
                phoneTF.setText(newValue); //if everything checks out update value
            }
        });
    }


    /** Update Button onAction Method*/
    @FXML
    void setUpdateBtn(){

        //If all fields are complete - continue
        try {
            if (!nameTF.getText().isEmpty() && !addressTF.getText().isEmpty() && !postalcodeTF.getText().isEmpty() & !phoneTF.getText().isEmpty() && !divisionCB.getValue().isEmpty() && !countryCB.getValue().isEmpty()){


                Customer customer = new Customer();
                customer.setAddress(addressTF.getText());
                customer.setCustomerName(nameTF.getText());
                customer.setPhone(phoneTF.getText());
                customer.setPostalCode(postalcodeTF.getText());
                customer.setCustomerId(selectedCustomer.getCustomerId());
                for (Division division: UserSession.divisions){
                    if (division.getDivision().equals(divisionCB.getSelectionModel().getSelectedItem())){
                        customer.setDivisionId(division.getDivisionId());
                    }
                }

                //try to updateCustomer
                if(CustomerDB.updateCustomer(customer) > 0) {
                        MyAlerts.info("Customer " + customer.getCustomerName() + " has been updated.");

                        //then return to customers view
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/customers.fxml"));
                        Customers controller = new Customers();
                        UserSession.displayScene(fxmlLoader, controller, cancelBtn);
                    } else {
                        //Database Error in CustomerDB or formatting
                        MyAlerts.info("Customer not updated in database.");
                    }
                } else{
                    //Database Error in AddressDB or formatting
                    MyAlerts.error("Address not inserted into database.");
                }
        }catch (Exception e){
            //city and/or country not selected
            e.printStackTrace();
            MyAlerts.warning("All fields required.");
        }
    }

    /** Cancel Button onAction Method*/
    @FXML
    void setCancelBtn(){

        //if data then confirm cancel
        if (!nameTF.getText().isEmpty() || !addressTF.getText().isEmpty() || !postalcodeTF.getText().isEmpty()|| !phoneTF.getText().isEmpty()) {

            confirmCancel();

        } else {

            //no data - continue with cancel
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/customers.fxml"));
            Customers controller = new Customers();
            UserSession.displayScene(fxmlLoader, controller, cancelBtn);
        }

    }

    /** Cancel confirmation called in setCancelBtn*/
    private void confirmCancel() {
        //confirm cancel

        if (MyAlerts.isConfirmed("Are you sure? Any changes will be erased.")) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/customers.fxml"));
            Customers controller = new Customers();
            UserSession.displayScene(fxmlLoader, controller, cancelBtn);
        }
    }



}
