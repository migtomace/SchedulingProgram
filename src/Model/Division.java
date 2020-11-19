package Model;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *
 * Division Model
 *
 * */
public class Division {

    private int divisionId, countryID;
    private String division;
    private Date createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;

    /** Empty constructor*/
    public Division(){

    }

    /** Return division ID
     * @return divisionId*/
    public int getDivisionId() {
        return divisionId;
    }

    /** Sets division ID
     * @param divisionId - int*/
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /** Returns division name
     * @return division*/
    public String getDivision() {
        return division;
    }

    /** Sets division name
     * @param division - String*/
    public void setDivision(String division) {
        this.division = division;
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

    /** Sets country ID
     * @param countryID - int*/
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    /** Returns country ID
     * @return countryID*/
    public int getCountryID() {
        return countryID;
    }
}
