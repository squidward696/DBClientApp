package controller;

import dao.Query;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * controller to manage adding a new customer
 */
public class AddCustController implements Initializable {
    private Parent root;
    private Stage stage;
    public TextField custIDTxtFld, custNameTxtFld, addressTxtFld, postalCodeTxtFld, phoneTxtFld;
    public ComboBox countryComboBox, divisionComboBox;
    public int custID;

    /**
     * fills each combo box with the appropriate data and creates a unique id for the new customer
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            custID = Query.getNewCustID();
            countryComboBox.setItems(Query.getAllCountries());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        countryComboBox.setOnAction(actionEvent -> {
            try {
                int selectedCountry = Query.getCountryID(countryComboBox.getValue().toString());
                divisionComboBox.setItems(Query.getAllDivisions(selectedCountry));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        custIDTxtFld.setText(String.valueOf(custID));
    }

    /**
     * creates a new customer and adds them into the sql database. performs logical checks to make sure that the
     * information is filled out correctly
     *
     * @param actionEvent on "create" button clicked with the mouse
     * @throws IOException
     * @throws SQLException
     */
    public void createHandler(ActionEvent actionEvent) throws IOException, SQLException {
        String custName = custNameTxtFld.getText();
        String address = addressTxtFld.getText();
        String postalCode = postalCodeTxtFld.getText();
        String phone = phoneTxtFld.getText();
        int divisionID = 0;

        // logical checks to make sure that country/division data have been selected
        try {
            divisionID = Query.getDivID(divisionComboBox.getValue().toString());
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Make sure country and division data are selected");
            alert.setHeaderText("Missing information");
            alert.showAndWait();
            return;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // logical checks to make sure that information has been entered
        if (custName.isEmpty() || address.isEmpty() || postalCode.isEmpty() || phone.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please check and fill-in missing information");
            alert.setHeaderText("Text fields cannot be left blank");
            alert.showAndWait();
            return;
        }
        Customers customer = new Customers(custID, custName, address, postalCode, phone, divisionID);
        Query.customersDAO.insert(customer);

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
