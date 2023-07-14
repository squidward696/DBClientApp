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
 * controller to manage updating an existing customer
 */
public class UpdateCustController implements Initializable {
    private Parent root;
    private Stage stage;
    private Customers c;
    public TextField custIDTxtFld, custNameTxtFld, addressTxtFld, postalCodeTxtFld, phoneTxtFld;
    public ComboBox countryComboBox, divisionComboBox;

    /**
     * fills each combo box with the appropriate data. use of lambda expression to update divisionComboBox depending
     * on what country has been selected
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryComboBox.setOnAction(actionEvent -> {
            try {
                int selectedCountry = Query.getCountryID(countryComboBox.getValue().toString());
                divisionComboBox.setItems(Query.getAllDivisions(selectedCountry));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * updates an existing customer with the new data that has been changed. performs logical checks to make sure
     * that the correct information will be passed through
     *
     * @param actionEvent on "update" button clicked with the mouse
     * @throws SQLException
     * @throws IOException
     */
    public void updateHandler(ActionEvent actionEvent) throws SQLException, IOException {
        c.setCustomerName(custNameTxtFld.getText());
        c.setAddress(addressTxtFld.getText());
        c.setPostalCode(postalCodeTxtFld.getText());
        c.setPhone(phoneTxtFld.getText());
        try {
            c.setDivisionID(Query.getDivID(divisionComboBox.getValue().toString()));
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Make sure country and division data are selected");
            alert.setHeaderText("Missing information");
            alert.showAndWait();
            return;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // logical checks to make sure that information has been entered
        if (c.getCustomerName().isEmpty() || c.getAddress().isEmpty() || c.getPostalCode().isEmpty() || c.getPhone().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please check and fill-in missing information");
            alert.setHeaderText("Text fields cannot be left blank");
            alert.showAndWait();
            return;
        }
        Query.customersDAO.update(c);

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
     * this method is passes along the customer that is selected in the "user-view". this allows for specific
     * customers to be updated and populates the fields with the customer data
     *
     * @param customer the customer that is being "passed-through" to update
     * @throws SQLException
     */
    public void passData(Customers customer) throws SQLException {
        c = customer;

        countryComboBox.setItems(Query.getAllCountries());
        divisionComboBox.setItems((Query.getAllDivisions(Query.getCountryID(Query.getCountry(c.getDivisionID())))));

        custIDTxtFld.setText(Integer.toString(customer.getCustomerID()));
        custNameTxtFld.setText(customer.getCustomerName());
        addressTxtFld.setText(customer.getAddress());
        postalCodeTxtFld.setText(customer.getPostalCode());
        phoneTxtFld.setText(customer.getPhone());
        countryComboBox.setValue(Query.getCountry(customer.getDivisionID()));
        divisionComboBox.setValue(Query.getDivision(customer.getDivisionID()));
    }

}