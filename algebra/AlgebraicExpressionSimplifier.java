package algebra;

public interface AlgebraicExpressionSimplifier {
	/**
	 * simplifies the expression
	 * 
	 * @param e
	 *            the algebraic expresion to be simplified
	 * @return the simplefied expression
	 * @throws AlgebraicSimplifyingException
	 * @throws AlgebraicRuntimeException
	 * @since 1.0
	 */
	public AlgebraicExpression simplify(AlgebraicExpression e)
			throws AlgebraicSimplifyingException, AlgebraicRuntimeException;
}
