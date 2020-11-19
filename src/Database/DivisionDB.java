package Database;

import Controller.UserSession;
import Model.Country;
import Model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 *
 * DivisionDB Class
 * contains all queries relative to
 * division data
 *
 * */
public class DivisionDB {

    /** Searches database for Country with given divisionID and returns Country Object*/
    public static Country getCountryByDivisionID(int divisionId) throws SQLException {

        try {
            ConnectDB.stillConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement("SELECT * FROM countries c LEFT JOIN first_level_divisions d on c.Country_ID = d.COUNTRY_ID WHERE d.Division_ID = ?");

        preparedStatement.setInt(1, divisionId);

        ResultSet resultSet = preparedStatement.executeQuery();
        Country country = new Country();
        if (resultSet.next()){
            country.setCountry(resultSet.getString("Country"));
            country.setCountryId(resultSet.getInt("Country_ID"));
        }

        return country;
    }

    /** Returns all Divisions in an ArrayList*/
    public static ArrayList<Division> getDivisions() throws SQLException {

        try {
            ConnectDB.stillConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<Division> divisions = new ArrayList<>();

        PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement("SELECT * FROM first_level_divisions;");

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            Division division = new Division();
            division.setDivisionId(resultSet.getInt("Division_ID"));
            division.setDivision(resultSet.getString("Division"));
            division.setCountryID(resultSet.getInt("COUNTRY_ID"));
            divisions.add(division);
        }

        resultSet.close();

        return divisions;
    }

    /** Searches database for Division by give divisionID and returns Division Object*/
    public static Division getDivisionByID(int divisionId) throws SQLException {

        try {
            ConnectDB.stillConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement("SELECT * FROM first_level_divisions WHERE Division_ID = ?");
        preparedStatement.setInt(1, divisionId);

        ResultSet resultSet = preparedStatement.executeQuery();

        Division division = new Division();
        if (resultSet.next()){
            division.setDivision(resultSet.getString("Division"));
            division.setDivisionId(resultSet.getInt("Division_ID"));
            division.setCountryID(resultSet.getInt("COUNTRY_ID"));
        }

        return division;
    }

    /** Returns all Country names in an ObservableList that matches given country name*/
    public static ObservableList<String> getDivisionByCountry(String countryName) throws SQLException {

        try {
            ConnectDB.stillConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int id = -1;
        for (Country c : UserSession.countries){
            if(c.getCountry().equals(countryName)) id = c.getCountryId();
        }
        ObservableList<String> divisions = FXCollections.observableArrayList();


        if (id > 0){
            PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement("SELECT * FROM first_level_divisions WHERE COUNTRY_ID = ?");
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                divisions.add(resultSet.getString("Division"));
            }
        }



        return divisions;
    }
}
