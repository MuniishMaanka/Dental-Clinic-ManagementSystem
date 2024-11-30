package com2002.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import com2002.utils.Database;

/**
 * The class which handles the address of a patient
 */
public class Address {

	private String houseNumber;
	private String streetName;
	private String district;
	private String city;
	private String postcode;

	/**
	 * This constructor should be called when creating a new address
	 * 
	 * @param houseNumber
	 *            House Number of the address
	 * @param streetName
	 *            Street Name of the address
	 * @param district
	 *            District of the address
	 * @param city
	 *            City of the address
	 * @param postcode
	 *            Postcode of the address
	 * @throws CommunicationsException
	 *             when an error occurs whilst attempting connection
	 * @throws MySQLIntegrityConstraintViolationException
	 *             if address already exists
	 * @throws SQLException
	 *             for any other error, could be incorrect parameters.
	 */
	public Address(String houseNumber, String streetName, String district, String city, String postcode)
			throws CommunicationsException, MySQLIntegrityConstraintViolationException, SQLException {
		if (!dbHasAddress(houseNumber, postcode)) {
			DBQueries.execUpdate("INSERT INTO Address Values('" + houseNumber + "', '" + streetName + "', '" + district
					+ "', '" + city + "', '" + postcode + "')");
			this.houseNumber = houseNumber;

			this.streetName = streetName;
			this.district = district;
			this.city = city;
			this.postcode = postcode;
		} else {
			throw new MySQLIntegrityConstraintViolationException(
					"An address with house number " + houseNumber + " and postcode " + postcode + "already exists.");
		}
	}

	/**
	 * This constructor should be called when searching for an address.
	 * 
	 * @param houseNumber
	 *            House Number of the address.
	 * @param postcode
	 *            Postcode of the address
	 * @throws CommunicationsException
	 *             when an error occurs whilst attempting connection
	 * @throws SQLException
	 *             for any other error, could be incorrect parameters.
	 */
	public Address(String houseNumber, String postcode) throws SQLException, CommunicationsException {
		Connection conn = Database.getConnection();
		try {
			ResultSet rs = DBQueries.execQuery("SELECT * FROM Address WHERE HouseNumber LIKE '%" + houseNumber
					+ "%' AND Postcode LIKE '%" + postcode + "%'", conn);
			if (rs.next()) {
				this.houseNumber = rs.getString("HouseNumber");
				this.streetName = rs.getString("StreetName");
				this.district = rs.getString("District");
				this.city = rs.getString("City");
				this.postcode = rs.getString("Postcode");
			}
		} finally {
			conn.close();
		}
	}

	/**
	 * Checks whether Address table contains a specified address.
	 * 
	 * @param houseNumber
	 *            House Number of the address.
	 * @param postcode
	 *            Postcode of the address
	 * @return True if the address already exists.
	 * @throws SQLException
	 */
	public static boolean dbHasAddress(String houseNumber, String postcode) throws SQLException {
		String found_house = DBQueries.getData("HouseNumber", "Address", "HouseNumber", houseNumber);
		String found_postcode = DBQueries.getData("Postcode", "Address", "Postcode", postcode);
		return found_house == houseNumber && found_postcode == postcode;
	}

	/**
	 * Returns a House Number of a particular address.
	 * 
	 * @return houseNumber The house number of an address.
	 */
	public String getHouseNumber() {
		return this.houseNumber;
	}

	/**
	 * Updates the House Number of an address to a given value/name.
	 * 
	 * @param houseNumber
	 *            The new house number of an address.
	 * @throws CommunicationsException
	 *             when an error occurs whilst attempting connection
	 * @throws SQLException
	 *             for any other error, could be incorrect parameters.
	 */
	public void setHouseNumber(String houseNumber) throws SQLException, CommunicationsException {
		DBQueries.execUpdate(
				"UPDATE Address SET HouseNumber = '" + houseNumber + "' WHERE StreetName = '" + this.streetName
						+ "' AND HouseNumber = '" + this.houseNumber + "' AND Postcode = '" + this.postcode + "'");
		this.houseNumber = houseNumber;
	}

	/**
	 * Returns a Street Name of a particular address.
	 * 
	 * @return streetName The street name of an address.
	 */
	public String getStreetName() {
		return this.streetName;
	}

	/**
	 * Updates the Street Name of an address.
	 * 
	 * @param streetName
	 *            The new street name of an address.
	 * @throws CommunicationsException
	 *             when an error occurs whilst attempting connection
	 * @throws SQLException
	 *             for any other error, could be incorrect parameters.
	 */
	public void setStreetName(String streetName) throws SQLException, CommunicationsException {
		DBQueries.execUpdate(
				"UPDATE Address SET StreetName = '" + streetName + "' WHERE StreetName = '" + this.streetName
						+ "' AND HouseNumber = '" + this.houseNumber + "' AND Postcode = '" + this.postcode + "'");
		this.streetName = streetName;
	}

	/**
	 * Returns a District of a particular address.
	 * 
	 * @return district The district of an address.
	 */
	public String getDistrict() {
		return this.district;
	}

	/**
	 * Updates the District of an address.
	 * 
	 * @param district
	 *            The new district of an address.
	 * @throws CommunicationsException
	 *             when an error occurs whilst attempting connection
	 * @throws SQLException
	 *             for any other error, could be incorrect parameters.
	 */
	public void setDistrict(String district) throws SQLException, CommunicationsException {
		DBQueries.execUpdate("UPDATE Address SET District = '" + district + "' WHERE StreetName = '" + this.streetName
				+ "' AND HouseNumber = '" + this.houseNumber + "' AND Postcode = '" + this.postcode + "'");
		this.district = district;
	}

	/**
	 * Returns a City of a particular address.
	 * 
	 * @return city The city of an address.
	 */
	public String getCity() {
		return this.city;
	}

	/**
	 * Updates the city of an address.
	 * 
	 * @param city
	 *            The new city of an address.
	 * @throws CommunicationsException
	 *             when an error occurs whilst attempting connection
	 * @throws SQLException
	 *             for any other error, could be incorrect parameters.
	 */
	public void setCity(String city) throws SQLException, CommunicationsException {
		DBQueries.execUpdate("UPDATE Address SET City = '" + city + "' WHERE StreetName = '" + this.streetName
				+ "' AND HouseNumber = '" + this.houseNumber + "' AND Postcode = '" + this.postcode + "'");
		this.city = city;
	}

	/**
	 * Returns a Postcode of a particular address.
	 * 
	 * @return postcode The postcode of an address.
	 */
	public String getPostcode() {
		return this.postcode;
	}

	/**
	 * Updates the postcode of an address.
	 * 
	 * @param postcode
	 *            The new postcode of an address.
	 * @throws CommunicationsException
	 *             when an error occurs whilst attempting connection
	 * @throws SQLException
	 *             for any other error, could be incorrect parameters.
	 */
	public void setPostcode(String postcode) throws SQLException, CommunicationsException {
		DBQueries.execUpdate("UPDATE Address SET PostCode = '" + postcode + "' WHERE StreetName = '" + this.streetName
				+ "' AND HouseNumber = '" + this.houseNumber + "' AND Postcode = '" + this.postcode + "'");
		this.postcode = postcode;
	}

	/**
	 * Delete the address
	 * 
	 * @param houseNumber
	 *            to distinguish the address to be deleted
	 * @param postcode
	 *            to distinguish the address to be deleted
	 * @throws SQLException
	 *             for any other error, could be incorrect parameters.
	 */
	public static void deleteAddress(String houseNumber, String postcode) throws SQLException {
		DBQueries.execUpdate("DELETE FROM Address WHERE PostCode LIKE '%" + postcode + "%' AND HouseNumber LIKE '%"
				+ houseNumber + "%'");
	}

	public static ArrayList<Address> getAllAddresses() throws SQLException {
		ArrayList<Address> addresses = new ArrayList<Address>();
		Connection conn = Database.getConnection();
		try {
			ResultSet rs = DBQueries.execQuery("SELECT * FROM Address", conn);
			while (rs.next()) {
				String hnum = rs.getString("HouseNumber");
				String pcode = rs.getString("Postcode");
				addresses.add(new Address(hnum, pcode));
			}
		} finally {
			conn.close();
		}
		return addresses;
	}

}