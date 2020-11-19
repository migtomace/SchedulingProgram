package Controller;

import Database.CountryDB;
import Database.DivisionDB;
import Model.Country;
import Model.Division;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
* This class contains static methods
* and variables that can be accessed
* and reused throughout the program
*
* */
public class UserSession {


    public static String username;
    public static int userId;
    public static Logger logger = Logger.getLogger("login_activity.txt");
    private static Handler handler;
    private static SimpleFormatter simpleFormatter = new SimpleFormatter();

    /** Sets handler for static logger*/
    static {
        try {
            handler = new FileHandler("login_activity.txt", true);
            handler.setFormatter(simpleFormatter);
            logger.addHandler(handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Country> countries;
    public static List<Division> divisions;

    /** Loads static Lists for countries
    * to reduce database calls*/
    public static void loadCountries() throws SQLException {
        countries = CountryDB.getCountries();
    }

    /** Loads static Lists for divisions
     * to reduce database calls*/
    public static void loadDivisions() throws SQLException {
        divisions = DivisionDB.getDivisions();
    }

    /** Creates Dynamic clock for the Login and CreateAccount Scenes*/
    public static void setClock(Text timeTxt, Text dateTxt) {


        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
            timeTxt.setText(LocalDateTime.now().format(formatter));
            formatter = DateTimeFormatter.ofPattern("MMM dd yyyy");
            dateTxt.setText(LocalDateTime.now().format(formatter));

        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }


    /** Displays new scene*/
    public static void displayScene(FXMLLoader loader, Initializable controller, Button button) {
        try {
            loader.setController(controller);
            Parent parent = loader.load();
            Stage stage = (Stage) button.getScene().getWindow();
            Scene scene = new Scene(parent);
            scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
                if (e.getCode() == KeyCode.ENTER) {
                    Node focusOwner = scene.getFocusOwner();
                    if (focusOwner instanceof Button) {
                        ((Button) focusOwner).fire();
                    }
                }
            });
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Displays new scene*/
    public static void displayNewScene(FXMLLoader loader, Initializable controller) {
        try {
            loader.setController(controller);
            Parent parent = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
                if (e.getCode() == KeyCode.ENTER) {
                    Node focusOwner = scene.getFocusOwner();
                    if (focusOwner instanceof Button) {
                        ((Button) focusOwner).fire();
                    }
                }
            });
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
