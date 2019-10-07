package algebra.vars;

import algebra.AlgebraicParsingException;
import algebra.AlgebraicRuntimeException;
import algebra.AlgebraicSimplifyingException;
import algebra.ComponentReference;
import algebra.Op_Operation;
import algebra.Var_Base;
import algebra.operations.Op_Multiplication;

/**
 * An object representing a fraction with a numerator and denominator
 * 
 * @author AntoineChevalier
 * @since 1.0
 */
public class Var_Fraction implements Var_Base {
	public final Var_Base numerator;
	public final Var_Base denominator;

	/**
	 * A basic constructor for reference
	 * 
	 * @throws AlgebraicSimplifyingException
	 * @throws AlgebraicRuntimeException
	 */
	public Var_Fraction() throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		this(null, null);
	}

	/**
	 * A basic constructor
	 * 
	 * @param numerator
	 *            the numerator
	 * @param denominator
	 *            the denominator
	 * @throws AlgebraicSimplifyingException
	 * @throws AlgebraicRuntimeException
	 */
	public Var_Fraction(Var_Base numerator, Var_Base denominator)
			throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		if (Op_Operation.isVar_FractionSubClass(numerator)) {
			Var_Fraction temp = (Var_Fraction) numerator;
			numerator = temp.numerator;
			denominator = new Op_Multiplication().use(denominator, temp.denominator);
		}
		if (Op_Operation.isVar_FractionSubClass(denominator)) {
			Var_Fraction temp = (Var_Fraction) denominator;
			denominator = temp.denominator;
			numerator = new Op_Multiplication().use(numerator, temp.numerator);
		}
		this.numerator = numerator;
		this.denominator = denominator;
	}

	/**
	 * @see Var_Base#simplify()
	 */
	@Override
	public Var_Base simplify() throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
//		return new Op_Division().use(numerator, denominator);
		return new Var_Fraction(numerator.simplify(), denominator.simplify());
	}

	/**
	 * @see Var_Base#parseVar(String, ComponentReference)
	 */
	@Override
	public Var_Base parseVar(String s, ComponentReference reference) throws AlgebraicParsingException {
		return null;
	}

	/**
	 * @see Var_Base#getRegex(ComponentReference)
	 */
	@Override
	public String getRegex(ComponentReference reference) {
		return null;
	}

	/**
	 * copy the object
	 * 
	 * @see Var_Base#copy()
	 */
	@Override
	public Var_Fraction copy() {
		try {
			return new Var_Fraction(numerator.copy(), denominator.copy());
		} catch (Exception e) {
			// not possible
			return null;
		}
	}

	public boolean equals(Object obj) {
		try {
			Var_Fraction fraction = (Var_Fraction) obj;
			return fraction.numerator.equals(numerator) && fraction.denominator.equals(denominator);
		} catch (ClassCastException e) {
			return false;
		}
	}

	public String toString() {
		return "((" + numerator + ")/(" + denominator + "))";
	}

	/**
	 * @see Var_Base#evaluate(String, Var_Base)
	 */
	@Override
	public Var_Fraction evaluate(String name, Var_Base newValue)
			throws AlgebraicRuntimeException, AlgebraicSimplifyingException {
		return new Var_Fraction(numerator.evaluate(name, newValue), denominator.evaluate(name, newValue));
	}

	@Override
	public double getDegree() throws AlgebraicRuntimeException {
		return numerator.getDegree()-denominator.getDegree();
	}
}
