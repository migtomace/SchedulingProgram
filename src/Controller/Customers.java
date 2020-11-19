package Controller;


import Database.CustomerDB;
import Model.Customer;
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
 * Customers Controller for customers.fxml
 *
 * */
public class Customers implements Initializable {

    //variables

    @FXML
    private Button createBtn, editBtn, deleteBtn, backBtn, searchBtn, refreshBtn;

    @FXML
    private TextField searchTF;

    @FXML
    private TableView<Customer> customerTableView;

    @FXML
    private TableColumn<Customer, String> idTC, nameTC, phoneTC, addressTC, postalTC;


    //Initializable method

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            generateCustomers();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }



    /**Generate the Customer Table
    * Selects the first row
    * called on initialize*/
    public void generateCustomers() throws SQLException {
        ObservableList<Customer> customers = CustomerDB.getAllCustomers();
        customerTableView.setItems(customers);
        customerTableView.getSelectionModel().selectFirst();
        customerTableView.refresh();
    }

    /**Back Button onAction Method*/
    @FXML
    void setBackBtn(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/appointments.fxml"));
        Appointments controller = new Appointments();
        UserSession.displayScene(fxmlLoader, controller, createBtn);
    }

    /**Delete Button onAction Method*/
    @FXML
    void setDeleteBtn() throws SQLException {

        Customer customer = customerTableView.getSelectionModel().getSelectedItem();
        TextInputDialog textInputDialog = new TextInputDialog("111");

        //on confirmation delete from database
        if (MyAlerts.isAdmin(textInputDialog)){
            if(CustomerDB.deleteCustomer(customer.getCustomerId())){

                //refresh table
                generateCustomers();

                //respond with success alert
                MyAlerts.info("Customer successfully deleted.");
            } else {
                MyAlerts.error("DATABASE ERROR: Customer was not deleted.");
            }
        }

    }

    /**Edit Button onAction Method*/
    @FXML
    void setEditBtn(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/update_customer.fxml"));
        UpdateCustomer controller = new UpdateCustomer(customerTableView.getSelectionModel().getSelectedItem());
        UserSession.displayScene(fxmlLoader, controller,createBtn);
    }

    /**Create Button onAction Method*/
    @FXML
    void setCreateBtn(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/add_customer.fxml"));
        AddCustomer controller = new AddCustomer();
        UserSession.displayScene(fxmlLoader, controller, createBtn);
    }

    /**Search Button onAction Method
    * searches customers in database
    * by LIKE Customer_Name*/
    @FXML
    void setSearchBtn() throws SQLException {
        String name = searchTF.getText();
        ObservableList<Customer> customers = CustomerDB.getCustomerByName(name);
        customerTableView.setItems(customers);
        customerTableView.getSelectionModel().selectFirst();
        customerTableView.refresh();
    }



}
