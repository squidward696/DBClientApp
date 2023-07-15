package dao;

import controller.loginController;
import helper.JDBC;
import model.Appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * class to implement the required methods for appointments
 */
public class AppointmentsDAOImpl implements AppointmentsDAO {

    /**
     * connects to the database to get an appointment with a matching appointment id
     *
     * @param id the appointment id to search for in the database
     * @return the appointment with the matching appointment id in the database
     * @throws SQLException
     */
    @Override
    public Appointment get(int id) throws SQLException {
        Appointment appt = null;
        String sql = "SELECT Appointment_ID, Title, Description, Type, Start, End, Customer_ID, User_ID, Contact_ID " +
                "FROM appointments WHERE Appointment_ID = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int apptID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String desc = rs.getString("Description");
            String loc = rs.getString("Location");
            String type = rs.getString("Type");
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            int custID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contID = rs.getInt("Contact_ID");

            appt = new Appointment(apptID, title, desc, loc, type, start, end, custID, userID, contID);
        }
        rs.close();
        return appt;
    }

    /**
     * connects to the database to get all appointments
     *
     * @return all appointments found in the database
     * @throws SQLException
     */
    @Override
    public List<Appointment> getALL() throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int apptID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String desc = rs.getString("Description");
            String loc = rs.getString("Location");
            String type = rs.getString("Type");
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            int custID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contID = rs.getInt("Contact_ID");

            Appointment appointment = new Appointment(apptID, title, desc, loc, type, start, end, custID, userID, contID);

            appointments.add(appointment);
        }
        return appointments;
    }

    /**
     * connects to the database to insert a new appointment
     *
     * @param appointment the appointment to be inserted into the database
     * @return a certain int depending on if an appointment was inserted into the database or not
     * @throws SQLException
     */
    @Override
    public int insert(Appointment appointment) throws SQLException {
        String sql = "INSERT INTO appointments(Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        Timestamp timestamp = new Timestamp(new Date().getTime());

        ps.setInt(1, appointment.getAppointmentID());
        ps.setString(2, appointment.getTitle());
        ps.setString(3, appointment.getDescription());
        ps.setString(4, appointment.getLocation());
        ps.setString(5, appointment.getType());
        ps.setTimestamp(6, Timestamp.valueOf(appointment.getStart()));
        ps.setTimestamp(7, Timestamp.valueOf(appointment.getEnd()));
        ps.setTimestamp(8, timestamp);
        ps.setString(9, loginController.currentUser.getUserName());
        ps.setTimestamp(10, timestamp);
        ps.setString(11, loginController.currentUser.getUserName());
        ps.setInt(12, appointment.getCustomerID());
        ps.setInt(13, appointment.getUserID());
        ps.setInt(14, appointment.getContactID());

        int result = ps.executeUpdate();
        return result;
    }

    /**
     * connects to the database to update an existing appointment
     *
     * @param appointment the appointment to update in the database
     * @return a certain int depending on if an appointment was updated or not
     * @throws SQLException
     */
    @Override
    public int update(Appointment appointment) throws SQLException {
        String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?," +
                "Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ?," +
                "Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        Timestamp timestamp = new Timestamp(new Date().getTime());

        ps.setString(1, appointment.getTitle());
        ps.setString(2, appointment.getDescription());
        ps.setString(3, appointment.getLocation());
        ps.setString(4, appointment.getType());
        ps.setTimestamp(5, Timestamp.valueOf(appointment.getStart()));
        ps.setTimestamp(6, Timestamp.valueOf(appointment.getEnd()));
        ps.setTimestamp(7, timestamp);
        ps.setString(8, loginController.currentUser.getUserName());
        ps.setInt(9, appointment.getCustomerID());
        ps.setInt(10, appointment.getUserID());
        ps.setInt(11, appointment.getContactID());
        ps.setInt(12, appointment.getAppointmentID());

        int result = ps.executeUpdate();
        return result;
    }

    /**
     * connects to the database to delete an existing appointment
     *
     * @param appointment the appointment to delete in the database
     * @return a certain int depending on if an appointment was deleted or not
     * @throws SQLException
     */
    @Override
    public int delete(Appointment appointment) throws SQLException {
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setInt(1, appointment.getAppointmentID());

        int result = ps.executeUpdate();

        return result;
    }
}
