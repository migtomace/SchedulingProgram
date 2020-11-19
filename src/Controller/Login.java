package Controller;

import Database.AppointmentDB;
import Database.UserDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import java.net.URL;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * Login Controller for login.fxml
 *
 * */
public class Login implements Initializable {


    private ResourceBundle rb;

    @FXML
    private Button loginBtn, clearBtn;

    @FXML
    private TextField usernameTF;

    @FXML
    private Text loginTxt, timeTxt, dateTxt, divisionT;

    @FXML
    private Label usernameLbl, passwordLbl;

    @FXML
    private PasswordField passwordPF;

    @FXML
    private Hyperlink termsLink, createAccountLink;

    private String selectedLocale;




    @Override
    public void initialize(URL location, ResourceBundle resources) {

        
        UserSession.setClock(timeTxt, dateTxt);

        Locale locale = Locale.getDefault();
        selectedLocale = locale.getLanguage();
        rb = ResourceBundle.getBundle("LanguageResourceBundle/bundle", locale);
        translateScene();

    }


    /**Translates scene based on User Locale*/
    private void translateScene(){

        String first_level_division = ZoneId.systemDefault().toString();
        String[] array = first_level_division.split("/");
        String string = array[1];
        //set texts
        divisionT.setText(string);
        loginBtn.setText(rb.getString("login"));
        clearBtn.setText(rb.getString("clear"));
        usernameLbl.setText(rb.getString("username"));
        passwordLbl.setText(rb.getString("password"));
        loginTxt.setText(rb.getString("title"));
        termsLink.setText(rb.getString("terms"));
        createAccountLink.setText(rb.getString("createAccount"));
    }

    /**Login Button onAction Method*/
    @FXML
    void setLoginBtn(ActionEvent actionEvent) throws Exception {

        //if not null - continue
        if (!usernameTF.getText().isEmpty() && !passwordPF.getText().isEmpty()){

            //verify user login credentials
            if (UserDB.isCredentials(usernameTF.getText(), passwordPF.getText())){
                MyAlerts.info(rb.getString("welcome") + " " + usernameTF.getText());

                //then display appointments screen
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/appointments.fxml"));
                Appointments controller = new Appointments();

                AppointmentDB.checkUpcomingAppointment(rb);


                UserSession.displayScene(loader, controller, clearBtn);

            } else {
                MyAlerts.error(rb.getString("wrong"));
            }


        } else {
            MyAlerts.error(rb.getString("empty"));
        }


    }

    /**Create Account Link onAction Method*/
    @FXML
    void setCreateAccountLink(ActionEvent actionEvent){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/create_account.fxml"));
        CreateAccount controller = new CreateAccount();
        UserSession.displayScene(loader, controller, clearBtn);
    }

    /**Clear Button onAction Method*/
    @FXML
    void clearForm(ActionEvent event){
        usernameTF.clear();
        passwordPF.clear();
    }
}
