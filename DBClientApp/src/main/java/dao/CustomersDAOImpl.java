package dao;

import controller.loginController;
import helper.JDBC;
import model.Customers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * class to implement the required methods for customers
 */
public class CustomersDAOImpl implements CustomersDAO {

    /**
     * connects to the database to get a customer with a matching customer id
     *
     * @param id the customer id to search for in the database
     * @return the customer with the matching customer id in the database
     * @throws SQLException
     */
    @Override
    public Customers get(int id) throws SQLException {
        Customers cust = null;
        String sql = "SELECT Customer_Name, Address, Postal_Code, Phone, Division_ID FROM customers WHERE Customer_ID = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String custName = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postalCode = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            int divisionID = rs.getInt("Division_ID");
            int custID = rs.getInt("Customer_ID");

            cust = new Customers(custID, custName, address, postalCode, phone, divisionID);
        }
        rs.close();
        return cust;
    }

    /**
     * connects to the database to get all customers
     *
     * @return all customers found in the database
     * @throws SQLException
     */
    @Override
    public List<Customers> getALL() throws SQLException {
        List<Customers> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int customerID = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postalCode = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            int divisionID = rs.getInt("Division_ID");

            Customers customer = new Customers(customerID, customerName, address, postalCode, phone, divisionID);

            customers.add(customer);
        }
        rs.close();
        return customers;
    }

    /**
     * connects to the database to insert a new customer
     *
     * @param customers the customer to be inserted into the database
     * @return a certain int depending on if a customer was inserted into the database or not
     * @throws SQLException
     */
    @Override
    public int insert(Customers customers) throws SQLException {
        String sql = "INSERT INTO customers(Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        Timestamp timestamp = new Timestamp(new Date().getTime());

        ps.setInt(1, customers.getCustomerID());
        ps.setString(2, customers.getCustomerName());
        ps.setString(3, customers.getAddress());
        ps.setString(4, customers.getPostalCode());
        ps.setString(5, customers.getPhone());
        ps.setTimestamp(6, timestamp);
        ps.setString(7, loginController.currentUser.getUserName());
        ps.setTimestamp(8, timestamp);
        ps.setString(9, loginController.currentUser.getUserName());
        ps.setInt(10, customers.getDivisionID());

        int result = ps.executeUpdate();
        return result;
    }

    /**
     * connects to the database to update an existing customer
     *
     * @param customers the customer to update in the database
     * @return a certain int depending on if a customer was updated or not
     * @throws SQLException
     */
    @Override
    public int update(Customers customers) throws SQLException {
        String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?," +
                "Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        Timestamp timestamp = new Timestamp(new Date().getTime());

        ps.setString(1, customers.getCustomerName());
        ps.setString(2, customers.getAddress());
        ps.setString(3, customers.getPostalCode());
        ps.setString(4, customers.getPhone());
        ps.setTimestamp(5, timestamp);
        ps.setString(6, loginController.currentUser.getUserName());
        ps.setInt(7, customers.getDivisionID());
        ps.setInt(8, customers.getCustomerID());

        int result = ps.executeUpdate();
        return result;
    }

    /**
     * connects to the database to delete an existing customer
     *
     * @param customers the customer to delete in the database
     * @return a certain int depending on if a customer was deleted or not
     * @throws SQLException
     */
    @Override
    public int delete(Customers customers) throws SQLException {
        String sql = "DELETE FROM customers WHERE Customer_ID = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setInt(1, customers.getCustomerID());

        int result = ps.executeUpdate();

        return result;
    }
}
