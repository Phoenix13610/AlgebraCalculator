package algebra.operations;

import java.util.ArrayList;
import java.util.List;

import algebra.AlgebraicExpression;
import algebra.AlgebraicRuntimeException;
import algebra.AlgebraicSimplifyingException;
import algebra.Op_Operation;
import algebra.Var_Base;
import algebra.vars.UndefinedVar;
import algebra.vars.Var_Fraction;
import algebra.vars.Var_Sequence;
import algebra.vars.Var_Variable;
import algebra.vars.sequence.Seq_UnOperational;

/**
 * 
 * @author AntoineChevalier
 * @since 1.0
 */
public final class Op_Division extends Op_Operation {

	public Op_Division() {
		super('/');
	}

	/**
	 * @since 1.0
	 * @see Op_Operation#use(Var_Base, Var_Base)
	 */
	@Override
	public Var_Base use(Var_Base before, Var_Base after)
			throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		before = before.simplify();
		after = after.simplify();
		if (isUnOperational(before)) {
			return UnOperational(before, after);
		} else if (isUnOperational(after)) {
			return UnOperational(before, after);
		}
		if (isVar_FractionSubClass(after)) {
			Var_Fraction a = (Var_Fraction) after;
			return new Op_Multiplication().use(before, new Var_Fraction(a.denominator, a.numerator));
		} else if (isVar_VariableSubClass(before) && isVar_VariableSubClass(after)) {
			return divideVar((Var_Variable) before, (Var_Variable) after);
		} else if (before.getDegree() >= after.getDegree()) {
			Var_Sequence first = null;
			Var_Sequence second = null;
			if (isVar_SequenceSubClass(before)) {
				first = (Var_Sequence) before;
			} else {
				first = new Var_Sequence(before) {
					@Override
					public char openParenthesis() {
						return 0;
					}

					@Override
					public char closingParenthesis() {
						return 0;
					}
				};
			}
			if (isVar_SequenceSubClass(after)) {
				second = (Var_Sequence) after;
			} else {
				second = new Var_Sequence(after) {
					@Override
					public char openParenthesis() {
						return 0;
					}

					@Override
					public char closingParenthesis() {
						return 0;
					}
				};
			}
			return seqDivide(first, second);
		} else {
			return new Op_Multiplication().use(before, new Var_Fraction(new Var_Variable(1), after));
		}
	}

	private Var_Base UnOperational(Var_Base before, Var_Base after) {
		Seq_UnOperational result = new Seq_UnOperational(before);
		AlgebraicExpression temp = result.divide(after);
		result = new Seq_UnOperational(temp.vars, temp.ops);
		return result;
	}

	public Var_Base seqDivide(Var_Sequence before, Var_Sequence after)
			throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		if (isUnOperational(before)) {
			return UnOperational(before, after);
		} else if (isUnOperational(after)) {
			return UnOperational(before, after);
		}
		before=before.sort();
		after=after.sort();
		Var_Sequence result = null;
		Var_Base remainder = before.simplify().copy();

		while ((remainder.getDegree() >= after.getDegree()) && (!remainder.equals(new Var_Variable(0)))) {
			if (isVar_SequenceSubClass(remainder)) {
				Var_Sequence sequence = (Var_Sequence) remainder;
				Var_Base divider = use(sequence.vars.get(0).getVar(), after.vars.get(0).getVar());
				if (result == null) {
					result = new Var_Sequence(divider) {
						@Override
						public char openParenthesis() {
							return 0;
						}

						@Override
						public char closingParenthesis() {
							return 0;
						}
					};
				} else {
					AlgebraicExpression temp = result.add(divider);
					result = new Var_Sequence(temp.vars, temp.ops) {
						@Override
						public char openParenthesis() {
							return 0;
						}

						@Override
						public char closingParenthesis() {
							return 0;
						}
					};
				}
				remainder = new Op_Substraction().use(remainder, new Op_Multiplication().use(after, divider))
						.simplify();
			} else {
				throw new AlgebraicRuntimeException("Ass");
			}
		}
		if (!((Var_Sequence) remainder).vars.get(0).getVar().equals(new Var_Variable(0))) {
			AlgebraicExpression temp = result.add(new Op_Division().use(remainder, after));
			result = new Var_Sequence(temp.vars, temp.ops) {
				@Override
				public char openParenthesis() {
					return 0;
				}

				@Override
				public char closingParenthesis() {
					return 0;
				}
			};
		}
		return result;
	}

	/**
	 * Divide a var_variable with another var_variable
	 * 
	 * @param before
	 *            a var_variable
	 * @param after
	 *            a var_variable
	 * @throws AlgebraicSimplifyingException
	 * @throws AlgebraicRuntimeException
	 */
	public Var_Base divideVar(Var_Variable before, Var_Variable after)
			throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		double numMult = before.mult;
		double denomMult = after.mult;
		numMult = numMult / denomMult;
		denomMult = 1;
		// if (!MathAC.isInteger(numMult)) {
		// denomMult *= MathAC.doubleToFraction(numMult).get(1);
		// numMult = MathAC.doubleToFraction(numMult).get(0);
		// }
		// if (!MathAC.isInteger(denomMult)) {
		// numMult *= MathAC.doubleToFraction(denomMult).get(1);
		// denomMult = MathAC.doubleToFraction(denomMult).get(0);
		// }
		// if (MathAC.gcd(numMult, denomMult) > 1) {
		// double d = MathAC.gcd(numMult, denomMult);
		// numMult /= d;
		// denomMult /= d;
		// }
		List<List<UndefinedVar>> temp = undefVarDivision(before.undefVars, after.undefVars);
		List<UndefinedVar> numUndef = temp.get(0);
		List<UndefinedVar> denomUndef = temp.get(1);
		if (denomMult == 1 && denomUndef.isEmpty()) {
			return new Var_Variable(numMult, numUndef);
		} else {
			return new Var_Fraction(new Var_Variable(numMult, numUndef), new Var_Variable(denomMult, denomUndef));
		}
	}

	private List<List<UndefinedVar>> undefVarDivision(List<UndefinedVar> a, List<UndefinedVar> b)
			throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		List<UndefinedVar> numUndef = new ArrayList<UndefinedVar>();
		List<UndefinedVar> denomUndef = new ArrayList<UndefinedVar>();
		for (UndefinedVar v1 : a) {
			boolean added = false;
			for (UndefinedVar v2 : b) {
				if (v1.name.equals(v2.name)) {
					added = true;
					if (isVar_VariableSubClass(v1.expo) && isVar_VariableSubClass(v2.expo)) {
						double d1 = ((Var_Variable) v1.expo).mult;
						double d2 = ((Var_Variable) v2.expo).mult;
						if (d1 - d2 > 0) {
							numUndef.add(new UndefinedVar(v1.name, new Var_Variable(d1 - d2)));
						} else if (d1 - d2 < 0) {
							denomUndef.add(new UndefinedVar(v1.name, new Var_Variable(d2 - d1)));
						}
					} else {
						numUndef.add(new UndefinedVar(v1.name, new Op_Substraction().use(v1.expo, v2.expo)));
					}
				}
			}
			if (!added) {
				numUndef.add(v1);
			}
		}
		for (UndefinedVar undefinedVar : b) {
			if (!containsVarName(a, undefinedVar.name)) {
				denomUndef.add(undefinedVar);
			}
		}
		List<List<UndefinedVar>> result = new ArrayList<List<UndefinedVar>>();
		result.add(numUndef);
		result.add(denomUndef);
		return result;
	}

	private boolean containsVarName(List<UndefinedVar> undefVars, String varName) {
		boolean result = false;
		if (undefVars.isEmpty()) {
			return false;
		}
		for (UndefinedVar v : undefVars) {
			if (v.name.equals(varName)) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * @since 1.0
	 * @see Op_Operation#priorityLevel()
	 */
	@Override
	public int priorityLevel() {
		return 1;
	}

}
