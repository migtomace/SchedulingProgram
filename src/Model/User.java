package Model;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *
 * User Model
 *
 * */
public class User {

    private int userId;
    private String userName;
    private String password;
    private Date createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;

    /** Empty Constructor*/
    public User(){

    }

    /** Returns user ID
     * @return userId*/
    public int getUserId() {
        return userId;
    }

    /** Sets user ID
     * @param userId  - int*/
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /** Returns username
     * @return userName*/
    public String getUserName() {
        return userName;
    }

    /** Sets username
     * @param userName - String*/
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /** Returns user password
     * @return password*/
    public String getPassword() {
        return password;
    }

    /** Sets user password
     * @param password - String*/
    public void setPassword(String password) {
        this.password = password;
    }

    /** Return the appointment CreateDate in LocalDateTime
     * @return createDate*/
    public Date getCreateDate() {
        return createDate;
    }

    /** Sets the appointment CreateDate in LocalDateTime
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
