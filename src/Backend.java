
/**
 * An interface to implement a back end for retrieving data via queries
 * 
 * @author Bradley Golden
 *
 * @param <T> A generic type to define a function.
 * @param <U> A generic type to define function parameters.
 */
public interface Backend<T, U> 
{
	T executeQuery(U... args); // executes a query against a database
	Object executeScalarQuery(U... args); // executes a scalar query against a database
}