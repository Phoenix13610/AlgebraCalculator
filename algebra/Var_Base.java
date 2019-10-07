package algebra;

/**
 * 
 * @author AntoineChevalier
 * @since 1.0
 */
public interface Var_Base {
	/**
	 * 
	 * Completly simplifies the variable and returns it
	 * 
	 * @return the simplified variable
	 * @throws AlgebraicSimplifyingException
	 * @throws AlgebraicRuntimeException
	 * @since 0.2
	 */
	public Var_Base simplify() throws AlgebraicSimplifyingException, AlgebraicRuntimeException;

	/**
	 * Parses the string and returns the var
	 * 
	 * @param s
	 *            the string representing the variable
	 * @param reference
	 *            the component reference
	 * @return the parsed variable
	 * @throws AlgebraicParsingException
	 * @throws AlgebraicRuntimeException 
	 * @since 1.0
	 */
	public Var_Base parseVar(String s, ComponentReference reference) throws AlgebraicParsingException, AlgebraicRuntimeException;

	/**
	 * A regex to be able to parse the variable
	 * 
	 * @param reference
	 *            the component reference
	 * @return the regex string
	 * @since 1.0
	 */
	public String getRegex(ComponentReference reference);

	/**
	 * copy the object
	 * 
	 * @return a copy of this object
	 * @throws AlgebraicSimplifyingException
	 * @throws AlgebraicRuntimeException
	 * @since 0.2
	 */
	public Var_Base copy();

	/**
	 * Replaces a variable with a new value
	 * 
	 * @param name
	 *            the name of the variable to be replaced
	 * @param newValue
	 *            the new value of the said variable
	 * @return a new object with the variable replaced with the new value
	 * @throws AlgebraicRuntimeException
	 * @throws AlgebraicSimplifyingException
	 * @since 1.1
	 */
	public Var_Base evaluate(String name, Var_Base newValue)
			throws AlgebraicRuntimeException, AlgebraicSimplifyingException;

	/**
	 * Get the degree of the variable
	 * 
	 * @return
	 * @throws AlgebraicRuntimeException 
	 */
	public double getDegree() throws AlgebraicRuntimeException;
}
