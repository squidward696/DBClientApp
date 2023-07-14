package application.dbclientapp;

import helper.JDBC;
import helper.TimeConversions;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * javadoc folder is in this project in src folder.
 * lambda expressions are found in UpdateCustController in the initialize method and UserController's method, onRBSort.
 */
public class MainApplication extends Application {
    public static ResourceBundle rb;

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Scheduling Program");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws SQLException {
        JDBC.openConnection();

        //Locale.setDefault(new Locale("fr"));
        if (Locale.getDefault().getLanguage().equals("de") || Locale.getDefault().getLanguage().equals("es") || Locale.getDefault().getLanguage().equals("fr")) {
            rb = ResourceBundle.getBundle("Nat", Locale.getDefault());
        }

        LocalDateTime now = LocalDateTime.now();
        System.out.println("Current time is: " + now.toString());
        System.out.println("Eastern time is: " + TimeConversions.dateTimeConversion(now));
        launch();

        JDBC.closeConnection();
    }
}