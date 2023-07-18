package controller;

import dao.Query;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Customers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * controller to manage the "user-view" (main screen) and has lambda code
 */
public class UserController implements Initializable {
    private Parent root;
    private Stage stage;
    public Label currentUserTxt;
    public ToggleGroup schedulingRBtn;
    public RadioButton monthRB, weekRB, allRB;
    public static ObservableList<Appointment> apptData;
    public static ObservableList<Customers> custData;
    @FXML
    public TableView<Appointment> apptTable;
    @FXML
    public TableView<Customers> custTable;
    @FXML
    public TableColumn<Appointment, Integer> AppointmentID, CustomerID, UserID, ContactID;
    @FXML
    public TableColumn<Appointment, String> Title, Description, Location, Type;
    @FXML
    public TableColumn<Appointment, Timestamp> StartDT, EndDT;
    @FXML
    public TableColumn<Customers, Integer> CustomerId, DivisionID;
    @FXML
    public TableColumn<Customers, String> CustomerName, Address, PostalCode, Phone;

    /**
     * sets each cell in the table views and populates them with data from the database
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentUserTxt.setText(loginController.currentUser.getUserName());

        AppointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        Title.setCellValueFactory(new PropertyValueFactory<>("title"));
        Description.setCellValueFactory(new PropertyValueFactory<>("description"));
        Location.setCellValueFactory(new PropertyValueFactory<>("location"));
        Type.setCellValueFactory(new PropertyValueFactory<>("type"));
        StartDT.setCellValueFactory(new PropertyValueFactory<>("start"));
        EndDT.setCellValueFactory(new PropertyValueFactory<>("end"));
        CustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        UserID.setCellValueFactory(new PropertyValueFactory<>("userID"));
        ContactID.setCellValueFactory(new PropertyValueFactory<>("contactID"));

        CustomerId.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        CustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        Address.setCellValueFactory(new PropertyValueFactory<>("address"));
        PostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        Phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        DivisionID.setCellValueFactory(new PropertyValueFactory<>("divisionID"));

        refreshTables();
    }

    /**
     * takes user to "add-appt-view" where they can create new appointments
     *
     * @param actionEvent on appointments "add" button clicked with the mouse
     * @throws IOException
     */
    public void addApptHandler(ActionEvent actionEvent) throws IOException {
        try {
            root = FXMLLoader.load(getClass().getResource("add-appt-view.fxml"));
            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * takes user to "update-appt-view" where they can update existing appointments
     *
     * @param actionEvent on appointments "update" button clicked with the mouse
     * @throws IOException
     */
    public void updateApptHandler(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("update-appt-view.fxml"));
        root = loader.load();

        try {
            UpdateApptController updateApptController = loader.getController();
            updateApptController.passData(apptTable.getSelectionModel().getSelectedItem());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please select an appointment to update");
            alert.setHeaderText("No appointment is selected");
            alert.showAndWait();
            throw new IOException("No Appointment is selected to update");
        }

        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * if an appointment is selected, it gives the user a popup to confirm the cancellation the selected appointment
     *
     * @param actionEvent on appointments "delete" button clicked with the mouse
     * @throws IOException
     */
    public void deleteApptHandler(ActionEvent actionEvent) throws IOException {
        Appointment appt = apptTable.getSelectionModel().getSelectedItem();
        if (appt == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an appointment to cancel");
            alert.setHeaderText("No appointment is selected");
            alert.showAndWait();
            throw new IOException("No appointment is selected");
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to cancel this (" + appt.getType() + ") appointment?");
        alert.setHeaderText("Cancel appointment: \"" + appt.getTitle() + "\"");
        alert.showAndWait().filter(ButtonType.OK::equals).ifPresent(ButtonType -> {
            try {
                Query.appointmentsDAO.delete(appt);
                refreshTables();
                Alert a = new Alert(Alert.AlertType.INFORMATION, "(" + appt.getType() + ") appointment has been canceled");
                a.setHeaderText("Appointment ID: " + appt.getAppointmentID() + " cancelled");
                a.showAndWait();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * takes user to "add-cust-view" where they can create new customers
     *
     * @param actionEvent on customers "add" button clicked with the mouse
     * @throws IOException
     */
    public void addCustHandler(ActionEvent actionEvent) throws IOException {
        try {
            root = FXMLLoader.load(getClass().getResource("add-cust-view.fxml"));
            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * takes user to "update-cust-view" where they can update existing customers
     *
     * @param actionEvent on customers "update" button clicked with the mouse
     * @throws IOException
     */
    public void updateCustHandler(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("update-cust-view.fxml"));
        root = loader.load();

        try {
            UpdateCustController updateCustController = loader.getController();
            updateCustController.passData(custTable.getSelectionModel().getSelectedItem());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please select a customer to update");
            alert.setHeaderText("No customer is selected");
            alert.showAndWait();
            throw new IOException("No Customer is selected to update");
        }

        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * if a customer is selected, it gives the user a popup to confirm the deletion of the selected customer.
     * checks to make sure that the selected customer has no existing appointments before deleting
     *
     * @param actionEvent on customers "delete" button clicked with the mouse
     * @throws IOException
     */
    public void deleteCustHandler(ActionEvent actionEvent) throws IOException, SQLException {
        Customers c = custTable.getSelectionModel().getSelectedItem();
        if (c == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a customer to delete");
            alert.setHeaderText("No customer is selected");
            alert.showAndWait();
            throw new IOException("No customer is selected");
        }

        // logical checks to make sure that selected customer has no appointments
        int x = Query.checkCustAppts(c.getCustomerID());
        if (x > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "All appointments must be deleted before this customer can be deleted");
            alert.setHeaderText("\"" + c.getCustomerName() + "\" has " + x + " appointment(s)");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this customer?");
        alert.setHeaderText("Delete customer: \"" + c.getCustomerName() + "\"");
        alert.showAndWait().filter(ButtonType.OK::equals).ifPresent(ButtonType -> {
            try {
                Query.customersDAO.delete(c);
                refreshTables();
                Alert a = new Alert(Alert.AlertType.INFORMATION, "This customer has been removed");
                a.setHeaderText("Customer: \"" + c.getCustomerName() + "\"");
                a.showAndWait();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * queries the database to get appointment and customer data and then populates the tables with this data
     */
    private void refreshTables() {
        try {
            apptData = FXCollections.observableArrayList(Query.appointmentsDAO.getALL());
            custData = FXCollections.observableArrayList(Query.customersDAO.getALL());

            apptTable.setItems(apptData);
            custTable.setItems(custData);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * filters appointments by month, week, or all depending on which radio button is selected and contains lambda.
     * use of lambda expression to sort through appointments found in the observable list, apptData, to filter both by
     * month and by week.
     *
     * @param actionEvent on "month", "week", or "all" radio buttons selected with the mouse
     */
    public void onRBSort(ActionEvent actionEvent) {
        FilteredList<Appointment> filteredMonthList = new FilteredList<>(apptData, x -> x.getStart().getMonth() == LocalDateTime.now().getMonth());
        FilteredList<Appointment> filteredWeekList = new FilteredList<>(apptData, x -> x.getStart().isBefore(LocalDateTime.now().plusWeeks(1)) && x.getStart().isAfter(LocalDateTime.now()));

        if (monthRB.isSelected()) {
            apptTable.setItems(filteredMonthList);
        }
        if (weekRB.isSelected()) {
            apptTable.setItems(filteredWeekList);
        }
        if (allRB.isSelected()) {
            apptTable.setItems(apptData);
        }
    }

    /**
     * takes the user back to "login-view" so that a new user can log in, exit the program, or create a new user
     *
     * @param actionEvent on "logout" button clicked with the mouse
     * @throws IOException
     */
    public void logoutHandler(ActionEvent actionEvent) throws IOException {
        loginController.currentUser = null;
        try {
            root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * takes the user to the "reports-view" to view available reports
     *
     * @param actionEvent on "reports" button clicked with the mouse
     */
    public void reportsHandler(ActionEvent actionEvent) {
        try {
            root = FXMLLoader.load(getClass().getResource("reports-view.fxml"));
            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
