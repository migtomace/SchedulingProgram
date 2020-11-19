package Controller;

import Database.CustomerDB;
import Database.DivisionDB;
import Model.*;
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

public class AddCustomer implements Initializable {

    @FXML
    private TextField nameTF, addressTF, postalcodeTF, phoneTF;

    @FXML
    private ComboBox<String> countryCB, divisionCB;

    @FXML
    private Button cancelBtn, addBtn;

    private final ObservableList<String> countryNames = FXCollections.observableArrayList();
    private final ObservableList<String> divisionNames = FXCollections.observableArrayList();

    /**Single line lambda to get country names
     * for each Country in ArrayList countries
     *
     * Single line lambda to get division names
     * for each Division in ArrayList divisions*/
    @Override
    public void initialize(URL location, ResourceBundle resources) {


        UserSession.countries.forEach(country -> countryNames.add(country.getCountry()));
        countryCB.setItems(countryNames);

        UserSession.divisions.forEach(division -> divisionNames.add(division.getDivision()));
        divisionCB.setItems(divisionNames);


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
            int max = 50; //max chars for varchar(45)
            if(newValue.length() > max){ //check if value exceeds max
                nameTF.setText(oldValue);
                MyAlerts.error("Name must contain no more than 50 characters.");
            } else {
                nameTF.setText(newValue); //if everything checks out update value
            }
        });

        /*Listener for address TextField
         * restricts value length*/
        addressTF.textProperty().addListener((observable, oldValue, newValue) -> {
            int max = 100; //max chars for varchar(100)
            if(newValue.length() > max){ //check if value exceeds max
                addressTF.setText(oldValue);
                MyAlerts.error("Address must contain no more than 100 characters.");
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

    /*Add Button onAction Method*/
    @FXML
    void setAddBtn(){

        //If all fields are complete - continue
        try {
            if (!nameTF.getText().isEmpty()
                    && !addressTF.getText().isEmpty()
                    && !postalcodeTF.getText().isEmpty()
                    && !phoneTF.getText().isEmpty()
                    && !divisionCB.getValue().isEmpty()
                    && !countryCB.getValue().isEmpty()){

                //try to insertAddress
                Customer customer = new Customer();
                customer.setAddress(addressTF.getText());
                customer.setCustomerName(nameTF.getText());
                customer.setPhone(phoneTF.getText());
                customer.setPostalCode(postalcodeTF.getText());

                for (Division division: UserSession.divisions){
                    if (division.getDivision().equals(divisionCB.getSelectionModel().getSelectedItem())){
                        customer.setDivisionId(division.getDivisionId());
                    }
                }

                //try to insertCustomer
                try {
                    CustomerDB.insertCustomer(customer);
                    MyAlerts.info("Customer " + customer.getCustomerName() + " has been created.");

                    //then return to customers view
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/customers.fxml"));
                    Customers controller = new Customers();
                    UserSession.displayScene(fxmlLoader, controller, cancelBtn);
                } catch (SQLException throwables) {
                    //Database Error in CustomerDB or formatting
                    MyAlerts.error("Customer not inserted into database.");
                    throwables.printStackTrace();
                }
            } else {
                //null values
                MyAlerts.warning("All fields required.");
            }
        }catch (Exception e){
            //city and/or country not selected
            e.printStackTrace();
            MyAlerts.warning("All fields required.");
        }





    }

    /*Cancel Button onAction Method*/
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

    /*Cancel confirmation called in setCancelBtn*/
    private void confirmCancel() {
        //confirm cancel

        if (MyAlerts.isConfirmed("Are you sure? Form data will be erased.")) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/customers.fxml"));
            Customers controller = new Customers();
            UserSession.displayScene(fxmlLoader, controller, cancelBtn);
        }
    }



}
