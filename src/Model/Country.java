package Model;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *
 * Country Model
 *
 * */
public class Country {

    private int countryId;
    private String country;
    private Date createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;

    /** Empty Constructor*/
    public Country(){
    }

    /** Returns country ID
     * @return countryId*/
    public int getCountryId() {
        return countryId;
    }

    /** Set country ID
     * @param countryId - int countryId*/
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /** Returns the country name
     * @return country*/
    public String getCountry() {
        return country;
    }

    /** Sets country name
     * @param country - String country*/
    public void setCountry(String country) {
        this.country = country;
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

}
