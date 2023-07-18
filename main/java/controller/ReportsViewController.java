package controller;

import dao.Query;
import helper.TimeConversions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ReportsViewController implements Initializable {
    private Parent root;
    private Stage stage;
    @FXML
    public TableView<Appointment> apptTable;
    @FXML
    public TableColumn AppointmentID, Title, Description, Type, StartDT, EndDT, CustomerID;
    public ComboBox monthSelector, contactSelector;
    public TextArea report1TxtArea, report3TxtArea;
    int before, after;

    public static ObservableList months() {
        ArrayList<Month> m = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            m.add(Month.of(i));
        }
        return FXCollections.observableArrayList(m);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        monthSelector.setItems(months());
        try {
            contactSelector.setItems(Query.getAllContactNames().sorted());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        AppointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        Title.setCellValueFactory(new PropertyValueFactory<>("title"));
        Description.setCellValueFactory(new PropertyValueFactory<>("description"));
        Type.setCellValueFactory(new PropertyValueFactory<>("type"));
        StartDT.setCellValueFactory(new PropertyValueFactory<>("start"));
        EndDT.setCellValueFactory(new PropertyValueFactory<>("end"));
        CustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        report3();
    }

    /**
     * the total number of customer appointments by type and month
     */
    public void report1(Month month) {
        int type1 = 0;
        int type2 = 0;
        int type3 = 0;

        for (Appointment a :
                UserController.apptData) {
            if (a.getStart().getMonth() == month && a.getType().equals("Planning Session")) {
                type1++;
            }
            if (a.getStart().getMonth() == month && a.getType().equals("De-Briefing")) {
                type2++;
            }
            if (a.getStart().getMonth() == month && a.getType().equals("Consultation")) {
                type3++;
            }
        }

        report1TxtArea.setText("Number of planning session appointments: " + type1 + "\nNumber of de-briefing " +
                "appointments: " + type2 + "\nNumber of consultation appointments: " + type3);
    }

    /**
     * a schedule for each contact in your organization that includes appointment ID, title,
     * type and description, start date and time, end date and time, and customer ID
     */
    public void report2(String contact) {
        FilteredList<Appointment> filteredContactAppt = new FilteredList<>(UserController.apptData, x -> {
            try {
                return x.getContactID() == Query.getContactIDFromName(contact);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        apptTable.setItems(filteredContactAppt);
    }

    /**
     * number of appointments before and after 5:00pm EST
     */
    public void report3() {
        before = 0;
        after = 0;

        for (Appointment a : UserController.apptData) {
            if (a.getStart().toLocalTime().isBefore(TimeConversions.convertESTToSD(LocalDateTime.from(LocalTime.parse("17:00:00").atDate(a.getStart().toLocalDate()))).toLocalTime())) {
                before++;
            } else after++;
        }

        report3TxtArea.setText("Number of appointments before 5:00pm -> " + before + "\nNumber of appointments after 5:00pm -> " + after);
    }

    public void returnHandler(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("user-view.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void selectMonth(ActionEvent actionEvent) {
        report1(Month.valueOf(monthSelector.getValue().toString()));
    }

    public void selectContact(ActionEvent actionEvent) {
        report2(contactSelector.getValue().toString());
    }
}
