import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Scanner;

/**
 * Provides access to a database
 * 
 * @author Bradley Golden
 *
 */
public class DataTier implements Backend<ResultSet, String>{
	private String connectionString; // string used to connect to azure sql database
	// Declare the JDBC objects.
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private PreparedStatement prepStatement;
    private String password;
    private String username;
	
    /**
     * Creates a DataTier object with connection string initialized and all
     * database related instance variables set to null;
     * @param dbTextPath (required) The path of the dbText.txt file that contains your username and password
     */
	public DataTier(String dbTextPath)
	{
		password = "";
		username = "";
		readDBText(dbTextPath); // get password and username from dbTextPath
		
		connectionString = String.format(
            "jdbc:sqlserver://cvfqhjtf8t.database.windows.net:1433;"
            + "database=FoodInspections;"
            + "user=%s;"
            + "password=%s;"
            + "encrypt=true;"
            + "hostNameInCertificate=*.database.windows.net;"
            + "loginTimeout=30;" 
            , username, password);
		
		connection = null;
		statement = null;
		resultSet = null;
		prepStatement = null;
		
	}
	
	/**
	 * Reads a text file and retrieves the first 2 lines of that file.
	 * <p>
	 * This function in particular is used to get the username and password for a
	 * database.
	 * 
	 * @param dbTextPath (required) The path of the file to be read.
	 */
	private void readDBText(String dbTextPath)
	{
		Scanner file;
		try 
		{
			file = new Scanner(new File(dbTextPath));
			
			this.username = file.next();
			this.password = file.next();
		    
			file.close();
		    
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Initializes a database by setting up a driver manager and creating a statement
	 * for future query execution.
	 */
	private void initDB()
	{
		try 
		{
            connection = DriverManager.getConnection(connectionString);
            statement = connection.createStatement();
        }
        catch (Exception e) 
		{
            e.printStackTrace();
        }
	}
	
	/**
	 * Closes a database by checking all possible database instance variables and closing them.
	 */
	private void closeDB()
	{
        // Close the connections after the data has been handled.
        if (resultSet != null) try { resultSet.close(); } catch(Exception e) {}
        if (statement != null) try { statement.close(); } catch(Exception e) {}
        if (connection != null) try { connection.close(); } catch(Exception e) {}
        if (prepStatement != null) try {prepStatement.close(); } catch(Exception e){}
	}
	
	/**
	 * Tests a database connection.
	 * @return True if connection succeeded, false otherwise.
	 * @throws SQLException
	 */
	public boolean testConnection() throws SQLException
	{
		boolean isConnected;
		
		initDB();
		
		isConnected = connection.isValid(30);
		
		closeDB();
		
		return isConnected;
	}
	
	/**
	 * Executes a query against a database.
	 * 
	 * @param args[0] (required) Requested query to execute against the database.
	 * @return Result set of type ResultSet after query successfully executes.
	 * @throws Exception Invalid query.
	 */
	@Override
	public ResultSet executeQuery(String... args) {
		String query;
		String selectSql;
		
		query = args[0];
		
		try 
		{
			initDB(); // initialize the database
			
            // create and execute a SELECT SQL statement.
            selectSql = query;
            resultSet = statement.executeQuery(selectSql);
        }
        catch (Exception e) 
		{
            e.printStackTrace();
            resultSet = null; // query failed
        }
        finally 
        {
            closeDB(); // close database connection
        }
		
		return resultSet;
	}

	/**
	 * Executes a scaler query against a database.
	 * 
	 * @param args[0] (required) Requested query to execute against the database.
	 * @return A scalar of type Object
	 * @throws Exception Invalid query.
	 */
	@Override
	public Object executeScalarQuery(String... args) {
		String query; // the user inputted sql statment
		String selectSql; // the sql statement
		Object result; // the result to be used
		
		query = args[0];
		
		try 
		{
			initDB(); // initialize the database
			
            // create and execute a SELECT SQL statement.
            selectSql = query;
            resultSet = statement.executeQuery(selectSql);
            
            // convert result set to object type and extract first value
            if (resultSet.next() ) 
            {  
                result = resultSet.getObject(0);  
            }
            else
            {
            	result = null; // nothing returned from query
            }
        }
        catch (Exception e) 
		{
            e.printStackTrace();
            result = null; // query failed
        }
        finally 
        {
            closeDB(); // close database connection
        }
		
		return (Object) result;
	}

	/**
	 * Test driver for DataTier
	 * 
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		DataTier dt = new DataTier("/Users/bradleygolden/Development/342-final-project/dbText.txt");
		
		try
		{
			System.out.println("Database connection status: " + dt.testConnection());
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		

	}
}
