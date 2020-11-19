package Controller;

import Database.UserDB;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 *
 * CreateAccount Controller for create_account.fxml
 *
 * */
public class CreateAccount implements Initializable {

    @FXML
    private TextField usernameTF;

    @FXML
    private Text timeTxt, dateTxt;

    @FXML
    private PasswordField passwordPF, confirmPF;

    @FXML
    private Button clearBtn, createBtn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        UserSession.setClock(timeTxt, dateTxt);

        /*Listener for Username to restrict length*/
        usernameTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                int max = 50; //max chars for varchar(50)
                if(newValue.length() > max){ //check if value exceeds max
                    usernameTF.setText(oldValue);
                    MyAlerts.error("Username must contain no more than 50 characters.");
                } else {
                    usernameTF.setText(newValue); //if everything checks out update value
                }
            }
        });

        /*Listener for Password to restrict length*/
        passwordPF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                int max = 50; //max chars for varchar(255)
                if(newValue.length() > max){ //check if value exceeds max
                    passwordPF.setText(oldValue);
                    MyAlerts.error("Password must contain no more than 50 characters.");
                } else {
                    passwordPF.setText(newValue); //if everything checks out update value
                }
            }
        });

        /*Listener for Confirm Password to restrict length*/
        confirmPF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                int max = 50; //max chars for varchar(50)
                if(newValue.length() > max){ //check if value exceeds max
                    confirmPF.setText(oldValue);
                    MyAlerts.error("Password must contain no more than 50 characters.");
                } else {
                    confirmPF.setText(newValue); //if everything checks out update value
                }
            }
        });
    }


    /**Create Button onAction Method*/
    @FXML
    void setCreateBtn(ActionEvent actionEvent) throws SQLException {

        //if all text fields are filled - continue
        if (!usernameTF.getText().isEmpty() && !passwordPF.getText().isEmpty() && !confirmPF.getText().isEmpty()){

            //if user name is not taken - continue
            if (!UserDB.isUsernameTaken(usernameTF.getText())){

                //if password and confirm match - continue
                if (passwordPF.getText().equals(confirmPF.getText())){
                    UserDB.insertUser(usernameTF.getText(), passwordPF.getText());
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/appointments.fxml"));
                    Appointments controller = new Appointments();
                    MyAlerts.info("Account successfully created.");
                    UserSession.displayScene(loader, controller, clearBtn);
                } else {
                    MyAlerts.error("Passwords do not match.");
                }
            } else {
                MyAlerts.error("This username is taken please choose another username.");
            }


        } else {
            MyAlerts.error("All fields are required.");
        }



    }

    /**Login Link onAction Method*/
    @FXML
    void setLoginLink(ActionEvent actionEvent){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/login.fxml"));
        Login controller = new Login();
        UserSession.displayScene(loader, controller, clearBtn);
    }

    /**Clear Button onAction Method*/
    @FXML
    private void clearForm(ActionEvent event){
        usernameTF.clear();
        passwordPF.clear();
        confirmPF.clear();
    }



}
