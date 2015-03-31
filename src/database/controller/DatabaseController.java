package database.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * The controller of the Database Project, manages messages
 * 
 * @author Zack
 * @version 1.8 3/10/15 Describes Tables
 */
public class DatabaseController
{
	/**
	 * Database connection string, used to connect to the database with the right driver, path, and user info.
	 */
	private String connectionString;
	/**
	 * The database connection
	 */
	private Connection databaseConnection;
	/**
	 * The reference to the AppController
	 */
	private DatabaseAppController baseController;

	private String currentQuery;
	private ArrayList<QueryInfo> queryArray;

	/**
	 * Creates a DatabaseController with a reference to the AppController for sending messages back and forward across the application
	 * 
	 * @param baseController
	 *            the reference to the AppController.
	 */
	public DatabaseController(DatabaseAppController baseController)
	{
		connectionString = "";
		this.baseController = baseController;
		currentQuery = "";
		checkDriver();
		setupConnection();
		queryArray = new ArrayList<QueryInfo>();

	}

	/**
	 * Build a connectionString with the parameters
	 * 
	 * @param pathToDBServer
	 *            The address/path of the database
	 * @param databaseName
	 *            The name of the database
	 * @param userName
	 *            the username of the user
	 * @param password
	 *            the password of the user
	 */
	public void connectionStringBuilder(String pathToDBServer, String databaseName, String userName, String password)
	{
		connectionString = "jdbc:mysql://";
		connectionString += pathToDBServer;
		connectionString += "/" + databaseName;
		connectionString += "?user=" + userName;
		connectionString += "&password=" + password;
	}

	/**
	 * Checks for the needed installed jar driver
	 */
	private void checkDriver()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch (Exception currentException)
		{
			displayErrors(currentException);
			System.exit(1);
		}
	}

	/**
	 * Closes the connection if needed
	 */
	public void closeConnection()
	{
		try
		{
			databaseConnection.close();
		}
		catch (SQLException error)
		{
			displayErrors(error);
		}
	}

	/**
	 * Sets up the connection with your connection string.
	 */
	private void setupConnection()
	{
		try
		{

			connectionStringBuilder("127.0.0.1", "houses", "root", "");
			databaseConnection = DriverManager.getConnection(connectionString);
		}
		catch (SQLException currentException)
		{
			displayErrors(currentException);
		}
	}

	/**
	 * Method to get and show the right errors, either SQL State or SQL Error Code.
	 * 
	 * @param currentException
	 *            the exception that was thrown
	 */
	public void displayErrors(Exception currentException)
	{
		JOptionPane.showMessageDialog(baseController.getAppFrame(), currentException.getMessage());

		if (currentException instanceof SQLException)
		{
			JOptionPane.showMessageDialog(baseController.getAppFrame(), "SQL State: " + ((SQLException) currentException).getSQLState());
			JOptionPane.showMessageDialog(baseController.getAppFrame(), "SQL Error Code: " + ((SQLException) currentException).getErrorCode());
		}
	}

	/**
	 * Displays the Tables in the connected database
	 * 
	 * @return
	 */
	public String displayTables()
	{
		String results = "";
		String query = "SHOW TABLES";

		try
		{
			long startTime = System.currentTimeMillis();
			Statement firstStatement = databaseConnection.createStatement();
			ResultSet answer = firstStatement.executeQuery(query);
			while (answer.next())
			{
				results += answer.getString(1) + "\n";
			}
			long endTime = System.currentTimeMillis();
			queryArray.add(new QueryInfo(query, endTime - startTime));
			answer.close();
			firstStatement.close();
		}
		catch (SQLException currentSQLError)
		{
			displayErrors(currentSQLError);
		}
		return results;
	}

	/**
	 * More advanced method for finding the information in the database table
	 * 
	 * @return the results found and taken from the database
	 */
	public String[][] realInfo()
	{
		String[][] results;
		String query = "SELECT * FROM `houses`";

		try
		{
			Statement firstStatement = databaseConnection.createStatement();
			ResultSet answer = firstStatement.executeQuery(query);
			int columnCount = answer.getMetaData().getColumnCount();
			int rowCount;
			answer.last();
			rowCount = answer.getRow();
			answer.beforeFirst();
			results = new String[rowCount][columnCount];
			long startTime = System.currentTimeMillis();
			while (answer.next())
			{
				for (int col = 0; col < columnCount; col++)
				{
					results[answer.getRow() - 1][col] = answer.getString(col + 1);
				}
			}
			long endTime = System.currentTimeMillis();
			queryArray.add(new QueryInfo(query, endTime - startTime));
			answer.close();
			firstStatement.close();
		}
		catch (SQLException currentSQLError)
		{
			results = new String[][] { { "error processing" } };
			displayErrors(currentSQLError);
		}
		return results;
	}

	/**
	 * Gets the tableInfo
	 * 
	 * @return the table results
	 */
	public String[][] tableInfo()
	{
		String[][] results;

		String query = "SHOW TABLES";

		try
		{
			Statement firstStatement = databaseConnection.createStatement();
			ResultSet answer = firstStatement.executeQuery(query);
			int rowCount;
			answer.last();
			rowCount = answer.getRow();
			answer.beforeFirst();
			results = new String[rowCount][1];
			long startTime = System.currentTimeMillis();
			while (answer.next())
			{
				results[answer.getRow() - 1][0] = answer.getString(1);
			}
			long endTime = System.currentTimeMillis();
			queryArray.add(new QueryInfo(query, endTime - startTime));
			answer.close();
			firstStatement.close();
		}
		catch (SQLException currentSQLError)
		{
			results = new String[][] { { "empty" } };
			displayErrors(currentSQLError);
		}

		return results;
	}

	/**
	 * Gets the meta data
	 * 
	 * @return the column information
	 */
	public String[] getMetaData()
	{
		String[] columnInformation;
		String query = "SELECT * FROM `houses`";
		long startTime = System.currentTimeMillis();
		try
		{
			Statement firstStatement = databaseConnection.createStatement();
			ResultSet answer = firstStatement.executeQuery(query);
			ResultSetMetaData myMeta = answer.getMetaData();

			columnInformation = new String[myMeta.getColumnCount()];
			for (int spot = 0; spot < myMeta.getColumnCount(); spot++)
			{
				columnInformation[spot] = myMeta.getColumnName(spot + 1);
			}
			long endTime = System.currentTimeMillis();
			queryArray.add(new QueryInfo(query, endTime - startTime));
			answer.close();
			firstStatement.close();
		}
		catch (SQLException currentSQLError)
		{
			columnInformation = new String[] { "nada exists" };
			displayErrors(currentSQLError);
		}

		return columnInformation;
	}

	/**
	 * Inserts a object into the table
	 * 
	 * @return the rows to change
	 */
	public int insertSample()
	{
		int rowsAffected = 0;
		String insertQuery = "INSERT INTO 'houses'.'my_houses'" + "('parameters of the database go here')" + "VALUES (your values for each go here');";
		try
		{
			Statement insertStatement = databaseConnection.createStatement();
			rowsAffected = insertStatement.executeUpdate(insertQuery);
			insertStatement.close();
		}
		catch (SQLException currentSQLError)
		{
			displayErrors(currentSQLError);
		}
		return rowsAffected;
	}

	/**
	 * Displays the databases that are in the connection
	 * 
	 * @return
	 */
	public String displayDatabases()
	{
		String results = "";
		String query = "SHOW DATABASES";
		long startTime = System.currentTimeMillis();
		try
		{
			Statement firstStatement = databaseConnection.createStatement();
			ResultSet answer = firstStatement.executeQuery(query);
			while (answer.next())
			{
				results += answer.getString(1) + "\n";
			}
			long endTime = System.currentTimeMillis();
			queryArray.add(new QueryInfo(query, endTime - startTime));
			answer.close();
			firstStatement.close();
		}
		catch (SQLException currentSQLError)
		{
			displayErrors(currentSQLError);
		}
		return results;
	}

	/**
	 * Gets and shows the information from one of the database tables in the database
	 * 
	 * @return
	 */
	public String describeTable()
	{
		String results = "";
		String query = "DESCRIBE houses";
		long startTime = System.currentTimeMillis();
		try
		{
			Statement firstStatement = databaseConnection.createStatement();
			ResultSet answer = firstStatement.executeQuery(query);
			while (answer.next())
			{
				results += answer.getString(1) + "\t" + answer.getString(2) + "\t" + answer.getString(3) + "\t" + "\n";
			}
			long endTime = System.currentTimeMillis();
			queryArray.add(new QueryInfo(query, endTime - startTime));
			answer.close();
			firstStatement.close();
		}
		catch (SQLException currentException)
		{
			displayErrors(currentException);
		}
		return results;
	}

	/**
	 * Select based query for the database controller Checks the query won't destroy data
	 * 
	 * @param query
	 *            The query to be executed on the database
	 * @return The 2D array of results from the query to be displayed in the JTable
	 */
	public String[][] selectQueryResults(String query)
	{
		this.currentQuery = query;
		String[][] results;

		try
		{
			if (checkForDataViolation())
			{
				throw new SQLException("Attempted illegal modification of data", "Messed it up", Integer.MIN_VALUE);
			}

			Statement firstStatement = databaseConnection.createStatement();
			ResultSet answer = firstStatement.executeQuery(query);
			int columnCount = answer.getMetaData().getColumnCount();

			answer.last();
			int rowCount = answer.getRow();
			answer.beforeFirst();
			results = new String[rowCount][columnCount];
			long startTime = System.currentTimeMillis();
			while (answer.next())
			{
				for (int col = 0; col < columnCount; col++)
				{
					results[answer.getRow() - 1][col] = answer.getString(col + 1);
				}
			}
			long endTime = System.currentTimeMillis();
			queryArray.add(new QueryInfo(query, endTime - startTime));
			answer.close();
			firstStatement.close();
		}
		catch (SQLException currentSQLError)
		{
			results = new String[][] { { "error processing" } };
			displayErrors(currentSQLError);
		}
		return results;
	}

	/**
	 * Check to make sure none of the words in the query are SQL keywords
	 * 
	 * @return true if contains keywords, else false
	 */

	private boolean checkForDataViolation()
	{
		if (currentQuery.toUpperCase().contains(" DROP ") || currentQuery.toUpperCase().contains(" TRUNCATE ") || currentQuery.toUpperCase().contains(" SET ") || currentQuery.toUpperCase().contains(" ALTER "))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private boolean checkForStructureViolation(){
		if(currentQuery.toUpperCase().contains(" DATABASE ")){
			return true;
		} else {
			return false;
		}
	}
	
	public void dropStatemnt(){
		String results;
		try{
			if(checkForStructureViolation()){
				throw new SQLException("You can't drop databaes", "cmon", Integer.MIN_VALUE);
			
			}
			
			if(currentQuery.toUpperCase().contains("INDEX")){
				results = "The index was ";
			} else {
				results = "The table was ";
			}
			
			Statement dropStatement = databaseConnection.createStatement();
			int affected = dropStatement.executeUpdate(currentQuery);
			
			dropStatement.close();
			
			if(affected != 0){
				results += "dropped";
			} else {
				results += " not dropped";
			}
			JOptionPane.showMessageDialog(baseController.getAppFrame(), results);
			
		} catch(SQLException dropError){
			displayErrors(dropError);
		}
		
	}
	
	public void alterStatement(){
		String results;
		try{
		
			if(currentQuery.toUpperCase().contains("ALTER")){
				results = "The index name was ";
			} else {
				results = "The table name was ";
			}
			
			Statement alterStatement = databaseConnection.createStatement();
			int affected = alterStatement.executeUpdate(currentQuery);
			
			alterStatement.close();
			
			if(affected == 0){
				results += "altered";
			} else {
				results += " not altered";
			}
			JOptionPane.showMessageDialog(baseController.getAppFrame(), results);
			
		} catch(SQLException alterError){
			displayErrors(alterError);
		}
	}
}
