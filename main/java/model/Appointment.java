package model;

import helper.TimeConversions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * represents an appointment
 */
public class Appointment {
    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int customerID;
    private int userID;
    private int contactID;
    public static ObservableList<String> apptTypes = FXCollections.observableArrayList("Planning Session", "De-Briefing", "Consultation");
    public static ObservableList<Integer> duration = FXCollections.observableArrayList(15, 30, 45, 60, 75, 90, 105, 120);

    /**
     * loop to get all appointment times
     *
     * @return EST appointment times converted to users systemDefault time
     */
    public static ObservableList apptTimesList() {
        ArrayList<LocalTime> localTimes = new ArrayList<>();
        ArrayList<LocalTime> localTimesEST = new ArrayList<>();

        localTimes.add(LocalTime.of(8, 0));
        for (int i = 0; i < 55; i++) {
            localTimes.add(localTimes.get(i).plusMinutes(15));
        }
        for (LocalTime lt :
                localTimes) {
            localTimesEST.add(TimeConversions.convertESTToSD(LocalDateTime.from(lt.atDate(LocalDate.now()))).toLocalTime());
        }
        return FXCollections.observableArrayList(localTimesEST);
    }
//    public static ObservableList<String> times = FXCollections.observableArrayList("08:00:00", "08:30:00", "09:00:00", "09:30:00", "10:00:00",
//            "10:30:00", "11:00:00", "11:30:00", "12:00:00", "12:30:00", "13:00:00", "13:30:00", "14:00:00", "14:30:00", "15:00:00", "15:30:00", "16:00:00",
//            "16:30:00", "17:00:00", "17:30:00", "18:00:00", "18:30:00", "19:00:00", "19:30:00", "20:00:00", "20:30:00", "21:00:00", "21:30:00");

    public Appointment(int appointmentID, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerID, int userID, int contactID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
    }

    /**
     * @return the appointment id
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * @param appointmentID the appointment id to set
     */
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    /**
     * @return the appointment title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the appointment title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the appointment description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the appointment description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the appointment location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the appointment location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the appointment type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the appointment type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the appointment start date and time
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * @param start the appointment start date and time to set
     */
    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    /**
     * @return the appointment end date and time
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * @param end the appointment end date and time
     */
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    /**
     * @return the customer id
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * @param customerID the customer id to set
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * @return the user id
     */
    public int getUserID() {
        return userID;
    }

    /**
     * @param userID the user id to set
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * @return the contact id
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * @param contactID the contact id to set
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }
}
