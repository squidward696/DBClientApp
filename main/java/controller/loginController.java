package controller;

import application.dbclientapp.MainApplication;
import dao.Query;
import helper.ActivityLog;
import helper.JDBC;
import helper.TimeConversions;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Users;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * controller to manage the login screen
 */
public class loginController implements Initializable {
    private Parent root;
    private Stage stage;
    public TextField usernameTxtFld, passwordTxtFld;
    public Button loginBtn, createNewUserBtn, exitBtn;
    public Label zoneID, signInLabel, globalLabel, usernameLabel, passwordLabel, locationLabel;
    public static Users currentUser;
    public static ZoneId zoneId = ZoneId.systemDefault();


    /**
     * sets the users zone id and checks the users local for de, es, or fr and updates the text accordingly
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        zoneID.setText(zoneId.toString());

        if (Locale.getDefault().getLanguage().equals("de") || Locale.getDefault().getLanguage().equals("es") || Locale.getDefault().getLanguage().equals("fr")) {
            globalLabel.setText(MainApplication.rb.getString("Global"));
            signInLabel.setText(MainApplication.rb.getString("Sign-in"));
            usernameLabel.setText(MainApplication.rb.getString("Username"));
            passwordLabel.setText(MainApplication.rb.getString("Password"));
            locationLabel.setText(MainApplication.rb.getString("Location"));
            usernameTxtFld.setPromptText(MainApplication.rb.getString("UsernamePrompt"));
            passwordTxtFld.setPromptText(MainApplication.rb.getString("PasswordPrompt"));
            loginBtn.setText(MainApplication.rb.getString("Login"));
            createNewUserBtn.setText(MainApplication.rb.getString("Create"));
            exitBtn.setText(MainApplication.rb.getString("Exit"));
        }
    }

    /**
     * attempts to log in a user with the supplied username and password. if a match is found, it takes the user to the
     * "user-view" and alerts the user of any upcoming appointments within 15 minutes
     *
     * @param actionEvent on "login" button clicked with the mouse
     * @throws SQLException
     */
    public void loginHandler(ActionEvent actionEvent) throws SQLException, IOException {
        String user = usernameTxtFld.getText().toString();
        String password = passwordTxtFld.getText().toString();
        currentUser = authenticateUser(user, password);

        if (currentUser == null) {
            String activity = "User \"" + user + "\" gave invalid login attempt at " + TimeConversions.convertSDToUTC(LocalDateTime.now()).format(TimeConversions.formatter) + " UTC\n";
            ActivityLog.activityLog(activity);

            if (Locale.getDefault().getLanguage().equals("de") || Locale.getDefault().getLanguage().equals("es") || Locale.getDefault().getLanguage().equals("fr")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, MainApplication.rb.getString("Pleasecheckandtryagain"));
                alert.setHeaderText(MainApplication.rb.getString("Invalidusernameand/orpassword"));
                alert.showAndWait();
                passwordTxtFld.setText("");
                return;
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please check and try again");
            alert.setHeaderText("Invalid username and/or password");
            alert.showAndWait();
            passwordTxtFld.setText("");
        }

        if (currentUser != null) {
            try {
                String activity = "User \"" + user + "\" successfully logged in at " + TimeConversions.convertSDToUTC(LocalDateTime.now()).format(TimeConversions.formatter) + " UTC\n";
                ActivityLog.activityLog(activity);

                root = FXMLLoader.load(getClass().getResource("user-view.fxml"));
                stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

                ArrayList<String> x = Query.checkUpcomingAppts(currentUser.getUserID());
                if (x.size() > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "You have " + x.size() + " appointment(s) within 15 minutes:\n" + upcomingApptsToString(x));
                    alert.setHeaderText("Welcome user: " + currentUser.getUserName());
                    alert.show();
                    return;
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "No upcoming appointments");
                alert.setHeaderText("Welcome User: " + currentUser.getUserName());
                alert.show();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * checks the database for the unique combination of username and password. it will return a user if a match is found
     *
     * @param userName the username to check for a match in the database
     * @param password the password to check for a match in the database
     * @return the user if a match is found in the database
     * @throws SQLException
     */
    private Users authenticateUser(String userName, String password) throws SQLException {
        Users user = null;
        String sql = "SELECT User_ID, User_Name, Password FROM users WHERE User_Name = ? AND Password = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, userName);
        ps.setString(2, password);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int uID = rs.getInt("User_ID");
            String uName = rs.getString("User_Name");
            String uPassword = rs.getString("Password");

            user = new Users(uID, uName, uPassword);
        }
        rs.close();
        return user;
    }

    /**
     * takes user to "add-user-view" where they can create a new user/login
     *
     * @param actionEvent on "create new user" button clicked with the mouse
     * @throws IOException
     */
    public void newUserHandler(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("add-user-view.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public String upcomingApptsToString(ArrayList<String> list) {
        StringBuilder upcomingAppts = new StringBuilder();
        for (String s :
                list) {
            upcomingAppts.append("- " + s.toString() + "\n");
        }
        return upcomingAppts.toString();
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