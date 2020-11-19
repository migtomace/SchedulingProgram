package Model;

import Database.CountryDB;
import Database.DivisionDB;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *
 * Customer Model
 *
 * */
public class Customer {

    private int customerId;
    private int divisionId;
    private String customerName, address, phone, postalCode, division, country;
    private Date createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;

    /** Empty Constructor*/
    public Customer(){

    }


    /** Returns division ID
     * @return divisionId*/
    public int getDivisionId() {
        return divisionId;
    }

    /** Sets division ID
     * @param divisionId - int divisionId*/
    public void setDivisionId(int divisionId) throws SQLException {
        this.divisionId = divisionId;
        setDivisionString();
    }

    /** sets division name as String then set sets country name*/
    public void setDivisionString() throws SQLException {
        Division division = DivisionDB.getDivisionByID(divisionId);
        this.division = division.getDivision();
        this.country = CountryDB.getCountryByID(division.getCountryID());
    }

    /** Returns customers country as String
     * @return country*/
    public String getCountry(){ return this.country; }

    /** Returns customers division as String
     * @return division*/
    public String getDivision(){
        return this.division;
    }

    /** Returns customers phone number
     * @return phone*/
    public String getPhone() {
        return phone;
    }

    /** Sets customers phone number
     * @param phone - String phone*/
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /** Returns customers ID
     * @return customerId*/
    public int getCustomerId() {
        return customerId;
    }

    /** Sets customers ID
     * @param customerId - int customerId*/
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /** Returns customers name
     * @return customerName*/
    public String getCustomerName() {
        return customerName;
    }

    /** Sets customers name
     * @param customerName - String*/
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /** Returns customers address
     * @return address*/
    public String getAddress() {
        return address;
    }

    /** Sets customers address
     * @param address - String*/
    public void setAddress(String address) {
        this.address = address;
    }

    /** Return the appointment CreateDate
     * @return createDate*/
    public Date getCreateDate() {
        return createDate;
    }

    /** Sets the appointment CreateDate
     * @param createDate - LocalDateTime createDate  */
    public void setCreateDate(Date createDate) {
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

    /** Returns customers postal code
     * @return postalCode*/
    public String getPostalCode() {
        return postalCode;
    }

    /** Sets customers postal code
     * @param postalCode - String postalCode*/
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
