import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

/**
 * Provides access to a database.
 * 
 * @author Bradley Golden
 *
 */
public class DataTier implements Backend<ResultSet, String>
{
	
	private String connectionString; // string used to connect to azure sql database
	
	//
	// Declare the JDBC objects.
	//
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private PreparedStatement prepStatement;
    
    private String password; // password used to connect to database
    private String username; // username used to connect to database
	
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
            , username, password); // default connection string
		
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
	 * <p>
	 * This function is useful for hiding user login information within this program.
	 * 
	 * @param dbTextPath (required) The path of the file to be read.
	 */
	private void readDBText(String dbTextPath)
	{
		Scanner file;
		try 
		{
			file = new Scanner(new File(dbTextPath));
			
			this.username = file.next(); // get username from the text file
			this.password = file.next(); // get the password form the text file
		    
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
        if (statement != null) try { statement.close(); } catch(Exception e) {}
        if (connection != null) try { connection.close(); } catch(Exception e) {}
        if (prepStatement != null) try {prepStatement.close(); } catch(Exception e){}
	}
	
	/**
	 * Tests a database connection.
	 * @return True if connection succeeded, false otherwise.
	 * @throws SQLException
	 */
	public boolean testConnection()
	{
		boolean isConnected;
		
		initDB();
		
		try 
		{
			isConnected = connection.isValid(30);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			isConnected = false;
		}
		
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
		String query; // the query to be executed
		String selectSql; // same as the query to be executed
		
		query = args[0];
		
		initDB(); // initialize the database
		
		try 
		{
            // create and execute a SELECT SQL statement.
            selectSql = query;
            resultSet = statement.executeQuery(selectSql);
        }
        catch (Exception e) 
		{
            resultSet = null; // query failed
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
		String query; // the user inputed sql statement
		String selectSql; // the sql statement
		Object result; // the result to be used
		
		query = args[0];

		initDB(); // initialize the database
		try 
		{
			
            // create and execute a SELECT SQL statement.
            selectSql = query;
            resultSet = statement.executeQuery(selectSql);
            
            // convert result set to object type and extract first value
            if (resultSet.next() ) 
            {  
                result = resultSet.getObject(1);
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
		
		return (Object) result;
	}
	
	/**
	 * Executes a non query against a database.
	 * <p>
	 * For example, inserting rows into a table is a "non query".
	 * 
	 * @param args[0] (required) Requested query to execute against the database.
	 * @throws Exception Invalid query.
	 */
	public void executeNonQuery(String... args) {
		String query; // the user inputed sql statement
		String nonQuerySql; // the sql statement
		Object result; // the result to be used
		
		query = args[0];

		initDB(); // initialize the database
		try 
		{
            // create and execute a non query SQL statement.
            nonQuerySql = query;
            statement.executeQuery(nonQuerySql);
        }
        catch (Exception e) 
		{
        	e.printStackTrace();
            result = null; // query failed
        }
	}

	/**
	 * Test driver for DataTier
	 * 
	 * @param args Command line arguments
	 */
	public static void main(String[] args) 
	{
		DataTier dt = new DataTier("/Users/bradleygolden/Development/342-final-project/dbText.txt");
		
		System.out.println("Database connection status: " + dt.testConnection());
		
		// TEST Scalar Queries
		Object result = dt.executeScalarQuery("SELECT count(*) FROM FoodInspections");
		System.out.println(result.toString());
		
		// TEST Query
		ResultSet rs = dt.executeQuery("SELECT AKA_Name FROM FoodInspections WHERE Results = 'FAIL'");
		
		try {
			while (rs.next())
			{
				System.out.println(rs.getString("AKA_Name") + "");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// IMPORTANT NOTE: You must use closeDB after every "executeQuery" method call. 
		// You do not need to call closeDB after executeNonQuery and executeScalarQuery
		dt.closeDB(); // close the database and remove access to the result set
	}
}
