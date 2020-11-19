package MySchedule;

import Controller.UserSession;
import Database.ConnectDB;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) throws Exception {
        //creates connection
        ConnectDB.createConnection();
        //loads countries from database and stores them in static arraylist
        UserSession.loadCountries();
        //loads divisions from database and stores them in static arraylist
        UserSession.loadDivisions();

        //launches program
        launch(args);

        //closes connection
        ConnectDB.closeConnection();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/login.fxml"));
        Controller.Login controller = new Controller.Login();
        fxmlLoader.setController(controller);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ENTER) {
                Node focusOwner = scene.getFocusOwner();
                if (focusOwner instanceof Button) {
                    ((Button) focusOwner).fire();
                }
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
