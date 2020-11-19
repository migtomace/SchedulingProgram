package Controller;


import javafx.scene.control.*;
import java.util.Optional;


/**
* This Class contains custom
* static alert methods
* to reduce redundant code
*
* */
public class MyAlerts {


    /** Static method that prompts user to enter an admin code*/
    public static boolean isAdmin(TextInputDialog textInputDialog){

        String code = "111";

        textInputDialog.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textInputDialog.getEditor().setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        textInputDialog.setHeaderText("Admin privileges needed! Deletes appointments company wide!");
        Optional<String> result = textInputDialog.showAndWait();
        if (result.isPresent() && result.get().equals(code)) {
            return true;
        } else if (result.isPresent() && !result.get().equals(code)){
            warning("Incorrect code.");
        }

        return false;
    }

    /** Static method that prompts user with given warning message */
    public static void warning(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING, message);
        alert.show();
    }

    /** Static method that prompts user with given info message */
    public static void info(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.show();
    }

    /** Static method that prompts user with given message and waits for confirmation
     * then returns a boolean
     * */
    public static boolean isConfirmed(String message){
        ButtonType yesBtn = new ButtonType("YES", ButtonBar.ButtonData.OK_DONE);
        ButtonType noBtn = new ButtonType("NO", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert cancelConfirmation = new Alert(Alert.AlertType.CONFIRMATION, message, yesBtn, noBtn);
        cancelConfirmation.showAndWait();

        return cancelConfirmation.getResult().getText().equals("YES");
    }

    /** Static method that prompts user with given error message */
    public static void error(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.show();
    }

}
