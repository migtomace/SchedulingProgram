package Database;

import Model.Country;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * CountryDB Class
 * contains all queries relative to
 * country data
 *
 * */
public class CountryDB {

    /** Returns all countries in an ArrayList*/
    public static ArrayList<Country> getCountries() throws SQLException {

        try {
            ConnectDB.stillConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<Country> countries = new ArrayList<>();

        PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement("SELECT * FROM countries;");

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            Country country = new Country();
            country.setCountryId(resultSet.getInt("Country_ID"));
            country.setCountry(resultSet.getString("Country"));
            countries.add(country);
        }

        resultSet.close();

        return countries;
    }

    /** Returns country name by country ID*/
    public static String getCountryByID(int countryID) throws SQLException {


        try {
            ConnectDB.stillConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String country = "";

        PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement("SELECT Country FROM countries WHERE Country_ID = ?;");
        preparedStatement.setInt(1, countryID);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()){
            country = resultSet.getString("Country");
        }

        resultSet.close();

        return country;
    }
}
