package dao;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * class to handle helpful queries (methods) in the database
 */
public class Query {
    public static AppointmentsDAOImpl appointmentsDAO = new AppointmentsDAOImpl();
    public static CustomersDAOImpl customersDAO = new CustomersDAOImpl();
    public static UsersDAOImpl usersDAO = new UsersDAOImpl();

    /**
     * gets the max appointment id and adds 1 to get a new unique id
     *
     * @return a new unique id for appointments
     * @throws SQLException
     */
    public static int getNewApptID() throws SQLException {
        String sql = "SELECT MAX(Appointment_ID) FROM appointments";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        int max = 0;
        while (rs.next()) {
            max = rs.getInt(1);
        }
        rs.close();
        return max + 1;
    }

    /**
     * gets the max customer id and adds 1 to get a new unique id
     *
     * @return a new unique id for customers
     * @throws SQLException
     */
    public static int getNewCustID() throws SQLException {
        String sql = "SELECT MAX(Customer_ID) FROM customers";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        int max = 0;
        while (rs.next()) {
            max = rs.getInt(1);
        }
        rs.close();
        return max + 1;
    }

    /**
     * gets the max user id and adds 1 to get a new unique id
     *
     * @return a new unique id for users
     * @throws SQLException
     */
    public static int getNewUserID() throws SQLException {
        String sql = "SELECT MAX(User_ID) FROM users";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        int max = 0;
        while (rs.next()) {
            max = rs.getInt(1);
        }
        rs.close();
        return max + 1;
    }

    /**
     * @return an observable list of customer ids
     * @throws SQLException
     */
    public static ObservableList<Integer> getAllCustIDs() throws SQLException {
        ObservableList<Integer> custIDs = FXCollections.observableArrayList();
        String sql = "SELECT Customer_ID FROM customers";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int custId = rs.getInt("Customer_ID");
            custIDs.add(custId);
        }
        rs.close();
        return custIDs;
    }

    /**
     * @return an observable list of contact names
     * @throws SQLException
     */
    public static ObservableList<String> getAllContactNames() throws SQLException {
        ObservableList<String> contIDs = FXCollections.observableArrayList();
        String sql = "SELECT Contact_Name FROM contacts";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String contName = rs.getString("Contact_Name");
            contIDs.add(contName);
        }
        rs.close();
        return contIDs;
    }

    /**
     * @param name the name to search in contacts to find the matching contact id
     * @return the contact id
     * @throws SQLException
     */
    public static int getContactIDFromName(String name) throws SQLException {
        String sql = "SELECT Contact_ID FROM contacts WHERE Contact_Name = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, name);

        ResultSet rs = ps.executeQuery();
        int id = 0;
        if (rs.next()) {
            id = rs.getInt("Contact_ID");
        }
        rs.close();
        return id;
    }

    /**
     * @param id the id to search in contacts to find the matching contact name
     * @return the contact name
     * @throws SQLException
     */
    public static String getContactNameFromID(int id) throws SQLException {
        String sql = "SELECT Contact_Name FROM contacts WHERE Contact_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        String name = null;
        if (rs.next()) {
            name = rs.getString("Contact_Name");
        }
        rs.close();
        return name;
    }

    /**
     * @return an observable list of all user ids
     * @throws SQLException
     */
    public static ObservableList<Integer> getAllUserIDs() throws SQLException {
        ObservableList<Integer> userIDs = FXCollections.observableArrayList();
        String sql = "SELECT User_ID FROM users";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int userId = rs.getInt("User_ID");
            userIDs.add(userId);
        }
        rs.close();
        return userIDs;
    }

    /**
     * @return an observable list of all countries
     * @throws SQLException
     */
    public static ObservableList<String> getAllCountries() throws SQLException {
        ObservableList<String> countries = FXCollections.observableArrayList();
        String sql = "SELECT Country FROM countries";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String c = rs.getString("Country");
            countries.add(c);
        }
        rs.close();
        return countries;
    }

    /**
     * @param c the country to scan the database and find the matching country id
     * @return the country id
     * @throws SQLException
     */
    public static int getCountryID(String c) throws SQLException {
        String sql = "SELECT Country_ID FROM countries WHERE Country = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, c);

        ResultSet rs = ps.executeQuery();
        int id = 0;
        if (rs.next()) {
            id = rs.getInt("Country_ID");
        }
        rs.close();
        return id;
    }

    /**
     * gets the country from the division id that is passed through
     *
     * @param c the division id
     * @return the country
     * @throws SQLException
     */
    public static String getCountry(int c) throws SQLException {
        String sql = "SELECT Country FROM countries WHERE Country_ID = (SELECT Country_ID FROM first_level_divisions WHERE Division_ID = ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, c);

        ResultSet rs = ps.executeQuery();
        String country = null;
        if (rs.next()) {
            country = rs.getString("Country");
        }
        rs.close();
        return country;
    }

    /**
     * @param i the country id
     * @return an observable list of divisions with the same country id
     * @throws SQLException
     */
    public static ObservableList<String> getAllDivisions(int i) throws SQLException {
        ObservableList<String> divisons = FXCollections.observableArrayList();
        String sql = "SELECT Division FROM first_level_divisions WHERE Country_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, i);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String d = rs.getString("Division");
            divisons.add(d);
        }
        rs.close();
        return divisons;
    }

    /**
     * @param d the division
     * @return the division id of the provided division
     * @throws SQLException
     */
    public static int getDivID(String d) throws SQLException {
        String sql = "SELECT Division_ID FROM first_level_divisions WHERE Division = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, d);

        ResultSet rs = ps.executeQuery();
        int id = 0;
        if (rs.next()) {
            id = rs.getInt("Division_ID");
        }
        rs.close();
        return id;
    }

    /**
     * @param d the division id
     * @return the division of the provided division id
     * @throws SQLException
     */
    public static String getDivision(int d) throws SQLException {
        String sql = "SELECT Division from first_level_divisions WHERE Division_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, d);

        ResultSet rs = ps.executeQuery();
        String division = null;
        if (rs.next()) {
            division = rs.getString("Division");
        }
        rs.close();
        return division;
    }

    /**
     * checks the database to make sure that the username hasn't been used yet
     *
     * @param n the username
     * @return true if username is unique or false if it is already in the database
     * @throws SQLException
     */
    public static boolean checkUniqueUserName(String n) throws SQLException {
        String sql = "SELECT User_Name FROM users WHERE User_Name = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, n);

        ResultSet rs = ps.executeQuery();
        String name = null;
        if (rs.next()) {
            name = rs.getString("User_Name");
        }
        if (name == null) {
            return true;
        }
        rs.close();
        return false;
    }

    /**
     * checks the database to see how many appointments a certain customer has
     *
     * @param custID the customer id
     * @return an int depending on how many appointments a customer has
     * @throws SQLException
     */
    public static int checkCustAppts(int custID) throws SQLException {
        String sql = "SELECT Appointment_ID FROM appointments WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, custID);

        ResultSet rs = ps.executeQuery();
        int count = 0;
        while (rs.next()) {
            count++;
        }
        rs.close();
        return count;
    }

    /**
     * checks the database to see how many appointments a certain user has upon logging in for the first time
     *
     * @param userId the user id
     * @return an int depending on how many appointments the user has within 15 minutes of logging in
     * @throws SQLException
     */
    public static ArrayList<String> checkUpcomingAppts(int userId) throws SQLException {
        ObservableList<Appointment> apptData = FXCollections.observableArrayList(Query.appointmentsDAO.getALL());
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime check = now.plusMinutes(15);

        ArrayList<String> appts = new ArrayList<String>();

        for (Appointment a :
                apptData) {
            if (a.getUserID() == userId && (a.getStart().isAfter(now) && a.getStart().isBefore(check))) {
                String s = ("Appt Id: " + a.getAppointmentID() + ", Date & Time: " + a.getStart());
                appts.add(s);
            }
        }
        return appts;
    }
}
