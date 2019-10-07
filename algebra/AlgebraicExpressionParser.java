package algebra;

/**
 * @author AntoineChevalier
 * @since 1.0
 */
public abstract class AlgebraicExpressionParser {
	protected ComponentReference reference;

	public AlgebraicExpressionParser(ComponentReference reference) {
		this.reference = reference;
	}

	/**
	 * Takes a string and returns a algebraic expression
	 * 
	 * @param e
	 *            string representing the expression
	 * @return a algebraic expression parsed from the string
	 * @throws AlgebraicParsingException
	 * @throws AlgebraicRuntimeException
	 * @since 1.0
	 */
	public abstract AlgebraicExpression parse(String e) throws AlgebraicParsingException, AlgebraicRuntimeException;

	/**
	 * Parse a string and returns a variable component object
	 * 
	 * @param reference
	 *            the component reference
	 * @param s
	 *            the string representing the variable
	 * @param index
	 *            the index of the new var component
	 * @return the parsed var component
	 * @throws AlgebraicParsingException
	 * @throws AlgebraicRuntimeException 
	 * @since 1.0
	 */
	public static Var_Component generateVarComponent(ComponentReference reference, String s, int index)
			throws AlgebraicParsingException, AlgebraicRuntimeException {
		return reference.generateVarComponent(s, index);
	}

	/**
	 * Parse a string and returns a operation component object
	 * 
	 * @param reference
	 *            the component reference
	 * @param s
	 *            the string representing the operation
	 * @param index
	 *            the index of the new op component
	 * @return the parsed op component
	 * @throws AlgebraicParsingException
	 * @since 1.0
	 */
	public static Op_Component generateOperation(ComponentReference reference, char c, int index)
			throws AlgebraicParsingException {
		return reference.generateOperation(c, index);
	}

}
