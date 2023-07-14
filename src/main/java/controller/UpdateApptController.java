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
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

/**
 * controller to manage updating an existing appointment
 */
public class UpdateApptController implements Initializable {
    private Parent root;
    private Stage stage;
    private Appointment a;
    public TextField titleTxtFld, descriptionTxtFld, locationTxtFld, apptIDTxtFld;
    public DatePicker startDate;
    public ComboBox typeComboBox, custIDComboBox, userIDComboBox, contactComboBox, startTimeComboBox, durationComboBox;

    /**
     * fills each combo box with the appropriate data
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        typeComboBox.setItems(Appointment.apptTypes);
        startTimeComboBox.setItems(Appointment.apptList());
        durationComboBox.setItems(Appointment.duration);
        try {
            custIDComboBox.setItems(Query.getAllCustIDs().sorted());
            userIDComboBox.setItems(Query.getAllUserIDs().sorted());
            contactComboBox.setItems(Query.getAllContactNames().sorted());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * updates an existing appointment with the new data that has been changed. performs logical checks to make sure
     * that the correct information will be passed through and that there are no issues with appointment overlaps or
     * time constraints
     *
     * @param actionEvent on "update" button clicked with the mouse
     * @throws SQLException
     * @throws IOException
     */
    public void updateHandler(ActionEvent actionEvent) throws SQLException, IOException {
        a.setTitle(titleTxtFld.getText());
        a.setDescription(descriptionTxtFld.getText());
        a.setLocation(locationTxtFld.getText());
        a.setType(typeComboBox.getValue().toString());
        a.setStart(startDate.getValue().atTime((LocalTime) startTimeComboBox.getValue()));
        a.setEnd(a.getStart().plusMinutes(Long.valueOf(Integer.parseInt(durationComboBox.getValue().toString()))));
        a.setCustomerID(Integer.parseInt(custIDComboBox.getValue().toString()));
        a.setUserID(Integer.parseInt(userIDComboBox.getValue().toString()));
        a.setContactID(Query.getContactIDFromName(contactComboBox.getValue().toString()));

        //check for appointment overlap
        for (Appointment x : UserController.apptData
        ) {
            if (a.getStart().isBefore(x.getEnd()) && a.getEnd().isAfter(x.getStart()) && (a.getCustomerID() == x.getCustomerID() && a.getAppointmentID() != x.getAppointmentID())) {
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Check time/duration and try again");
                a.setHeaderText("Appointment overlap for selected customer");
                a.showAndWait();
                return;
            }
        }

        // logical checks for appointment times
        if (a.getStart().compareTo(LocalDateTime.now()) <= 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Start time needs to be after current date/time");
            alert.setHeaderText("Check date and time");
            alert.showAndWait();
            return;
        }

        if (a.getTitle().isEmpty() || a.getDescription().isEmpty() || a.getLocation().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please check and fill-in missing information");
            alert.setHeaderText("Text fields cannot be left blank");
            alert.showAndWait();
            return;
        }

        //check end time to business hours
        if (a.getEnd().toLocalTime().isAfter(TimeConversions.timeConversion(LocalDateTime.from(LocalTime.parse("22:00:00").atDate(a.getEnd().toLocalDate())))) &&
                a.getEnd().toLocalTime().isBefore(TimeConversions.timeConversion(LocalDateTime.from(LocalTime.parse("08:00:00").atDate(a.getEnd().toLocalDate()))))) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Appointment goes outside of business hours\n8am - 10pm (EST)");
            a.setHeaderText("Change appointment duration or start time");
            a.showAndWait();
            return;
        }

        Query.appointmentsDAO.update(a);

        root = FXMLLoader.load(getClass().getResource("user-view.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * returns back to "user-view". does not save any information that may have been altered
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

    /**
     * this method is passes along the appointment that is selected in the "user-view". this allows for specific
     * appointments to be updated and populates the fields with the appointment data
     *
     * @param appointment the appointment that is being "passed-through" to update
     * @throws SQLException
     */
    public void passData(Appointment appointment) throws SQLException {
        a = appointment;

        apptIDTxtFld.setText(Integer.toString(appointment.getAppointmentID()));
        titleTxtFld.setText(appointment.getTitle());
        descriptionTxtFld.setText(appointment.getDescription());
        locationTxtFld.setText(appointment.getLocation());
        typeComboBox.setValue(appointment.getType());
        startDate.setValue(appointment.getStart().toLocalDate());
        startTimeComboBox.setValue(appointment.getStart().toLocalTime());
        durationComboBox.setValue(Duration.between(a.getStart(), a.getEnd()).toMinutes());
        custIDComboBox.setValue(appointment.getCustomerID());
        userIDComboBox.setValue(appointment.getUserID());
        contactComboBox.setValue(Query.getContactNameFromID(appointment.getContactID()));
    }
}
