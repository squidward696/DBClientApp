package controller;

import dao.Query;
import helper.TimeConversions;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ResourceBundle;

/**
 * controller to manage adding a new appointment
 */
public class AddApptController implements Initializable {
    private Parent root;
    private Stage stage;
    public TextField titleTxtFld, descriptionTxtFld, locationTxtFld, apptIDTxtFld;
    public DatePicker startDate;
    public ComboBox typeComboBox, custIDComboBox, userIDComboBox, contactComboBox, startTimeComboBox, durationComboBox;
    public int apptID;

    /**
     * fills each combo box with the appropriate data and creates a unique id for the new appointment
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        typeComboBox.setItems(Appointment.apptTypes);
        startTimeComboBox.setItems(Appointment.apptTimesList());
        durationComboBox.setItems(Appointment.duration);
        try {
            apptID = Query.getNewApptID();
            custIDComboBox.setItems(Query.getAllCustIDs().sorted());
            userIDComboBox.setItems(Query.getAllUserIDs().sorted());
            contactComboBox.setItems(Query.getAllContactNames().sorted());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        apptIDTxtFld.setText(String.valueOf(apptID));
    }

    /**
     * creates a new appointment and adds it into the sql database. goes through a series of logical checks to ensure that
     * correct information is passed through and that there are no issues with appointment overlaps or time constraints
     *
     * @param actionEvent on "create" button clicked with the mouse
     * @throws IOException
     * @throws SQLException
     */
    public void createHandler(ActionEvent actionEvent) throws IOException, SQLException {
        int apptID = Query.getNewApptID();
        String title = titleTxtFld.getText();
        String desc = descriptionTxtFld.getText();
        String loc = locationTxtFld.getText();
        String type = null;
        LocalDateTime start = null;
        LocalDateTime end = null;
        int custID = 0;
        int userID = 0;
        int contact = 0;

        try {
            type = typeComboBox.getValue().toString();
            start = startDate.getValue().atTime((LocalTime) startTimeComboBox.getValue());
            end = start.plusMinutes(Long.valueOf(Integer.parseInt(durationComboBox.getValue().toString())));
            custID = Integer.parseInt(custIDComboBox.getValue().toString());
            userID = Integer.parseInt(userIDComboBox.getValue().toString());
            contact = Query.getContactIDFromName(contactComboBox.getValue().toString());
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Make sure fields are filled out/selected");
            alert.setHeaderText("Missing information");
            alert.showAndWait();
        } catch
        (SQLException e) {
            throw new RuntimeException(e);
        }

        // logical checks to make sure that information is filled-in
        if (title.isEmpty() || desc.isEmpty() || loc.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please check and fill-in missing information");
            alert.setHeaderText("Text fields cannot be left blank");
            alert.showAndWait();
            return;
        }

        // logical checks for appointment times
        if (start.compareTo(LocalDateTime.now()) <= 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Start time needs to be after current date/time");
            alert.setHeaderText("Check date and time");
            alert.showAndWait();
            return;
        }
        if (end == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Select appointment duration");
            alert.setHeaderText("Check values");
            alert.showAndWait();
            return;
        }

        Appointment a = new Appointment(apptID, title, desc, loc, type, start, end, custID, userID, contact);

        // check for appointment overlap
        for (Appointment x : UserController.apptData
        ) {
            if (a.getStart().isBefore(x.getEnd()) && a.getEnd().isAfter(x.getStart()) && x.getCustomerID() == custID) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Check time/duration and try again");
                alert.setHeaderText("Appointment overlap");
                alert.showAndWait();
                return;
            }
        }

        // check end time to business hours
        if (a.getEnd().toLocalTime().isAfter(TimeConversions.convertESTToSD(LocalDateTime.from(LocalTime.parse("22:00:00").atDate(end.toLocalDate()))).toLocalTime()) ||
                a.getEnd().toLocalTime().isBefore(TimeConversions.convertESTToSD(LocalDateTime.from(LocalTime.parse("08:00:00").atDate(end.toLocalDate()))).toLocalTime())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Appointment goes outside of business hours\n8am - 10pm (EST)");
            alert.setHeaderText("Change appointment duration or start time");
            alert.showAndWait();
            return;
        }

        // convert time to EST and then UTC before inserting into the database
        a.setStart(TimeConversions.convertSDToEST(a.getStart()));
        a.setEnd(TimeConversions.convertSDToEST(a.getEnd()));
        a.setStart(TimeConversions.convertESTToUTC(a.getStart()));
        a.setEnd(TimeConversions.convertESTToUTC(a.getEnd()));

        Query.appointmentsDAO.insert(a);

        root = FXMLLoader.load(getClass().getResource("user-view.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * returns back to user-view. does not save any information that may have been entered in already
     *
     * @param actionEvent on "cancel" button clicked with the mouse
     * @throws IOException
     */
    public void cancelHandler(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("user-view.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
