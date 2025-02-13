package administrators;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import SQL.SQLMethods;
import members.Members;
import users.Users;

/** Administrators(adminID, clearance) */
// Clearance 1 = manage courses and configs
// Clearance 2 = manage accounts
// Clearance 3 = manage all

/** Administrators(adminID, clearance) */
public class Administrators {
	private static ResultSet result;
	private static PreparedStatement pstate;

	/**
	 * Insert an admin in Admin table
	 * 
	 * @param adminID
	 * @param clearance
	 * @return Returns true if successful,otherwise false
	 */
	public static boolean insert(String adminID, int clearance) {
		if (adminID == null || clearance < 0 || clearance > 3) return false;
		if (adminID == null || clearance < 0 || clearance > 3) return false;
		SQLMethods.mysqlConnect(); // Connect to DB
		try { // Attempt to insert
			pstate = SQLMethods.con.prepareStatement("INSERT INTO Administrators Values (?, ?);");
			pstate.setString(1, adminID);
			pstate.setInt(2, clearance);
			int rowcount = pstate.executeUpdate();
			SQLMethods.closeConnection(); // Close connection
			return (rowcount == 1); // If rowcount == 1, row successfully inserted
		} catch (SQLException e) { // Print error and terminate program
			SQLMethods.mysql_fatal_error("Query error: " + e.toString());
		}
		return false; // Default value: false
	}

	/**
	 * Delete an admin in Admin table
	 * 
	 * @param adminID
	 * @return
	 */
	public static boolean delete(String adminID) {
		/** Check for invalid inputs. If any input is null, return false */
		if (adminID == null) return false;
		SQLMethods.mysqlConnect(); // Connect to DB
		try { // Attempt to delete
			pstate = SQLMethods.con.prepareStatement("DELETE FROM Administrators WHERE adminID = ?;");
			pstate.setString(1, adminID);
			int rowcount = pstate.executeUpdate();
			SQLMethods.closeConnection(); // Close connection
			return (rowcount == 1); // If rowcount == 1, row successfully deleted
		} catch (SQLException e) { // Print error and terminate program
			SQLMethods.mysql_fatal_error("Query error: " + e.toString());
		}
		return false; // Default value: false
	}

	/**
	 * Change clearance of an existing admin with specified adminID
	 * 
	 * @param adminID
	 * @param newClearance
	 * @return true if successful, false if failure
	 */
	public static boolean setClearance(String adminID, int newClearance) {
		/** Check for invalid inputs. If any input is null, return false */
		if (adminID == null || newClearance < 0 || newClearance > 3) return false;
		SQLMethods.mysqlConnect(); // Connect to DB
		try { // Attempt to update
			pstate = SQLMethods.con.prepareStatement("UPDATE Administrators SET clearance = ? WHERE adminID = ?;");
			pstate.setInt(1, newClearance); // New clearance
			pstate.setString(2, adminID); // adminID of admin
			int rowcount = pstate.executeUpdate(); // Execute statement
			SQLMethods.closeConnection(); // Close connection
			return (rowcount == 1); // If rowcount == 1, row successfully updated
		} catch (SQLException e) { // Print error and terminate program
			SQLMethods.mysql_fatal_error("Query error: " + e.toString());
		}
		return false; // Default value: false
	}

	/**
	 * Retrieve clearance of an existing admin with specified adminID
	 * 
	 * @param adminID
	 * @return clearance of an existing admin
	 */
	public static int getClearance(String adminID) {
		/** Check for invalid inputs. If any input is null, return false */
		if (adminID == null) return -1;
		SQLMethods.mysqlConnect(); // Connect to DB
		try { // Attempt to search
			/** Search and retrieve tuple */
			pstate = SQLMethods.con.prepareStatement("SELECT clearance FROM Administrators WHERE adminID = ?;");
			pstate.setString(1, adminID);
			result = pstate.executeQuery(); // Execute query
			/** Extract tuple data */
			result.next();
			int clearance = result.getInt(1); // Get clearance
			result.close(); // Close result
			SQLMethods.closeConnection(); // Close connection
			return clearance; // Return clearance
		} catch (SQLException e) { // Print error and terminate program
			SQLMethods.mysql_fatal_error("Query error: " + e.toString());
		}
		return -1; // Default value: -1
	}

	public static boolean update(String oldAdminID, String newAdminID, int clearance) {
		Members.updateID(oldAdminID, newAdminID); // Update reference in Members
		Users.updateID(oldAdminID, newAdminID); // Update reference in Users
		SQLMethods.mysqlConnect(); // Connect to DB
		try { /** Update admin tuple */
			pstate = SQLMethods.con
					.prepareStatement("UPDATE Administrators SET adminID = ?, clearance = ? WHERE adminID = ?;");
			pstate.setString(1, newAdminID);
			pstate.setInt(2, clearance);
			pstate.setString(3, oldAdminID);
			pstate.executeUpdate();
			int rowcount = pstate.executeUpdate(); // Number of rows affected
			SQLMethods.closeConnection(); // Close connection
			return (rowcount == 1); // If rowcount == 1, row successfully inserted
		} catch (SQLException e) { // Print error and terminate program
			SQLMethods.mysql_fatal_error("Query error: " + e.toString());
		}
		return false; // Default value: false
	}

	/**
	 * Method to retrieve all the administrators and their info
	 * 
	 * @return all the students and their info in an ArrayList
	 */
	public static ArrayList<HashMap<String, String>> getAll() {
		ArrayList<HashMap<String, String>> output = new ArrayList<HashMap<String, String>>();
		SQLMethods.mysqlConnect(); // Connect to DB
		try { // Attempt to search
			pstate = SQLMethods.con.prepareStatement(
					"SELECT * FROM Administrators JOIN Members ON adminID = ID ORDER BY lastname ASC;");
			result = pstate.executeQuery(); // Execute query
			while (result.next()) {
				HashMap<String, String> tuple = new HashMap<String, String>();
				tuple.put("adminID", result.getString("adminID"));
				tuple.put("firstname", result.getString("firstname"));
				tuple.put("lastname", result.getString("lastname"));
				tuple.put("clearance", Integer.toString(result.getInt("clearance")));
				output.add(tuple);
			}
			result.close(); // Close result
			return output; // Successful search
		} catch (SQLException e) { // Print error and terminate program
			SQLMethods.mysql_fatal_error("Query error: " + e.toString());
		}
		return output;
	}

	public static boolean updateID(String oldAdminID, String newAdminID) {
		SQLMethods.mysqlConnect(); // Connect to DB
		try {
			/** Update ID */
			pstate = SQLMethods.con.prepareStatement("UPDATE Administrators SET adminID = ? WHERE adminID = ?;");
			pstate.setString(1, newAdminID);
			pstate.setString(2, oldAdminID);
			int rowcount = pstate.executeUpdate();
			return (rowcount == 1); // If rowcount == 1, row successfully updated
		} catch (SQLException e) { // Print error and terminate program
			SQLMethods.mysql_fatal_error("Query error: " + e.toString());
		}
		return false; // Default value: false
	}

	/* ############################################################ */
	/* #################### Unused Methods Below #################### */
	/* ############################################################ */

	public static ArrayList<ArrayList<String>> searchClearance(String clearance) {
		ArrayList<ArrayList<String>> output = new ArrayList<ArrayList<String>>();
		if (clearance == null) return output; // Check if clearance is null, return empty list if so
		SQLMethods.mysqlConnect(); // Connect to DB
		try { // Attempt to search
			pstate = SQLMethods.con.prepareStatement("SELECT * Administrators WHERE clearance = ?");
			pstate.setString(1, clearance);
			result = pstate.executeQuery(); // Execute query
			SQLMethods.closeConnection(); // Close connection
			while (result.next()) {
				ArrayList<String> tuple = new ArrayList<String>();
				tuple.add(result.getString("ID"));
				tuple.add(result.getString("clearance"));
				output.add(tuple);
			}
			result.close(); // Close result
			return output; // Success
		} catch (SQLException e) {
			SQLMethods.mysql_fatal_error("Query error"); // Print error and exit
		}
		return output; // Return false as a default value
	}
}
