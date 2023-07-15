package controller;

import dao.Query;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Users;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * controller to manage adding a new user
 */
public class AddUserController implements Initializable {
    private Parent root;
    private Stage stage;
    public TextField usernameTxtFld;
    public TextField passwordTxtFld;
    public Label zoneID;

    /**
     * sets and displays the users zone in the window
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        zoneID.setText(loginController.zoneId.toString());
    }

    /**
     * creates a new user and adds them into the sql database. logical checks to ensure that the username hasn't been
     * taken and that the text fields are filled out
     *
     * @param actionEvent on "create" button clicked with the mouse
     * @throws IOException
     * @throws SQLException
     */
    public void createHandler(ActionEvent actionEvent) throws IOException, SQLException {
        int userID = Query.getNewUserID();
        String username = usernameTxtFld.getText();
        String password = passwordTxtFld.getText();
        Alert alert = null;

        if (username.isEmpty() || password.isEmpty()) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Please enter a valid username & password");
            alert.setHeaderText("Username/password cannot be empty");
            alert.showAndWait();
            return;
        }

        if (Query.checkUniqueUserName(username) == true) {
            Users user = new Users(userID, username, password);
            Query.usersDAO.insert(user);

            root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            return;
        }

        alert = new Alert(Alert.AlertType.INFORMATION, "Please try a different username");
        alert.setHeaderText("Username is already taken");
        alert.showAndWait();
        usernameTxtFld.setText("");
    }

    /**
     * returns back to "user-view". does not save any information that may have been entered in already
     *
     * @param actionEvent on "cancel" button clicked with the mouse
     * @throws IOException
     */
    public void cancelHandler(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * exits the application
     *
     * @param actionEvent on "exit" button clicked with the mouse
     */
    public void exitHandler(ActionEvent actionEvent) {
        System.exit(0);
    }
}
