import java.sql.ResultSet;

/**
 * Provides access to a database
 * 
 * @author Bradley Golden
 *
 */
public class DataTier implements Backend<ResultSet, String>{
	
	@Override
	public ResultSet executeQuery(String... args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object executeScalarQuery(String... args) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
