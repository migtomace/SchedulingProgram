package Model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 *
 * Appointment Model
 *
 * */


public class Appointment {

    private int appointmentId;
    private Customer customer;
    private User user;
    private int contactID;
    private String name, title, description, location, type;
    private ZonedDateTime start, end;
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    //String for table columns
    private String day, endTime, startTime;

    /** Empty constructor */
    public Appointment(){

    }



    /** Returns the day of the month
     * @return day */
    public String getDay() {
        return day;
    }

    /** Sets the day of the month in setStartTime() */
    private void setDay() {
        this.day = start.getMonthValue() + "/" + start.getDayOfMonth() + "/" + start.getYear();
    }

    /** Returns appointment end time in String Format
     * @return endTime*/
    public String getEndTime() {
        return endTime;
    }

    /** Set appointment end time in String Format*/
    public void setEndTime() {
        this.endTime = convertToTwelve(end.getHour());
    }

    /** Returns appointment start time in String Format
     * @return startTime*/
    public String getStartTime() {
        return startTime;
    }

    /** Sets the appointment start time in String Format*/
    public void setStartTime() {
        this.startTime = convertToTwelve(start.getHour());
        setDay();
    }

    /** Returns appointment ID
     * @return appointmentId*/
    public int getAppointmentId() {
        return appointmentId;
    }

    /** Set appointment ID
     * @param appointmentId  - (int) Appointment ID*/
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /** Returns customer object
     * @return customer*/
    public Customer getCustomer() {
        return customer;
    }

    /** Sets customer object and calls setName()
     * @param customer - Customer Object */
    public void setCustomer(Customer customer) {
        this.customer = customer;
        setName(customer.getCustomerName());
    }

    /** Returns user object
     * @return user*/
    public User getUser() {
        return user;
    }

    /** Sets user object
     * @param user - User object*/
    public void setUser(User user) {
        this.user = user;
    }

    /** Returns appointment title
     * @return title*/
    public String getTitle() {
        return title;
    }

    /** Sets appointment title
     * @param title - String appointment title */
    public void setTitle(String title) {
        this.title = title;
    }

    /** Returns appointment description
     * @return description*/
    public String getDescription() {
        return description;
    }

    /** Sets appointment description
     * @param description - String appointment description */
    public void setDescription(String description) {
        this.description = description;
    }

    /** Returns appointment location
     * @return location*/
    public String getLocation() {
        return location;
    }

    /** Sets appointment location
     * @param location - String location */
    public void setLocation(String location) {
        this.location = location;
    }

    /** Return appointment type
     * @return type*/
    public String getType() {
        return type;
    }

    /** Sets appointment type
     * @param type - String type */
    public void setType(String type) {
        this.type = type;
    }

    /** Returns appointment start time in ZonedDateTime Format
     * @return start*/
    public ZonedDateTime getStart() {
        return start;
    }

    /** Sets appointment start time in ZonedDateTime format and then calls SetStartTime()
     * @param start - ZonedDateTime start*/
    public void setStart(ZonedDateTime start) {
        this.start = start;
        setStartTime();
    }

    /** Returns appointment end time in ZonedDateTime Format
     * @return end*/
    public ZonedDateTime getEnd() {
        return end;
    }

    /** Sets appointment end time in ZonedDateTime Format
     * @param end - ZonedDateTime end */
    public void setEnd(ZonedDateTime end) {
        this.end = end;

        setEndTime();
    }

    /** Return the appointment CreateDate in LocalDateTime
     * @return createDate*/
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /** Sets the appointment CreateDate in LocalDateTime
     * @param createDate - LocalDateTime createDate  */
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /** Returns the CreatedBy String
     * @return createdBy*/
    public String getCreatedBy() {
        return createdBy;
    }

    /** Sets the CreatedBy String
     * @param createdBy - String createdBy */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /** Returns the LastUpdate in Timestamp Format
     * @return lastUpdate*/
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    /** Sets the LastUpdate in Timestamp Format
     * @param lastUpdate - Timestamp lastUpdate*/
    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /** Returns the String lastUpdateBy
     * @return lastUpdateBy*/
    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    /** Sets the String lastUpdateBy
     * @param lastUpdateBy - String lastUpdateBy */
    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    /** Returns the name of the customer
     * @return name*/
    public String getName() {
        return name;
    }

    /** Sets the name of the customer
     * @param name - String name*/
    public void setName(String name) {
        this.name = name;
    }

    /** Converts a given integer 0-24 to 12 hour time format as a String
     * @return time
     * @param num - int num*/
    private String convertToTwelve(int num){
        String time = "";

        if (num == 0){
            time = "12:00 AM";
        } else if (num <= 12) {
            time = num + ":00 AM";
        } else {
            num = num - 12;
            time = num + ":00 PM";
        }

        return time;
    }

    /** Returns the Contact ID
     * @return contactID*/
    public int getContactID() {
        return contactID;
    }

    /** Sets the Contact ID
     * @param contactID - int contactID */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }
}
