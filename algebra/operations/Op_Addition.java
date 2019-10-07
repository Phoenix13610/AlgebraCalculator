package algebra.operations;

import java.util.ArrayList;
import java.util.List;

import algebra.AlgebraicExpression;
import algebra.AlgebraicRuntimeException;
import algebra.AlgebraicSimplifyingException;
import algebra.Op_Component;
import algebra.Op_Operation;
import algebra.Var_Base;
import algebra.Var_Component;
import algebra.vars.UndefinedVar;
import algebra.vars.Var_Fraction;
import algebra.vars.Var_Sequence;
import algebra.vars.Var_Set;
import algebra.vars.Var_Variable;

/**
 * 
 * @author AntoineChevalier
 * @since 1.0
 */
public final class Op_Addition extends Op_Operation {

	public Op_Addition() {
		super('+');
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
		if(isUnOperational(before)) {
			return UnOperational(before,after);
		}else if (isUnOperational(after)) {
			return UnOperational(before,after);
		}
		if (isVar_FractionSubClass(before)) {
			return addFrac((Var_Fraction) before, after);
		} else if (isVar_FractionSubClass(after)) {
			return addFrac((Var_Fraction) after, before);
		} else if (isVar_SetSubClass(before)) {
			return addSet((Var_Set) before, after);
		} else if (isVar_SetSubClass(after)) {
			return addSet((Var_Set) after, before);
		} else if (isVar_SequenceSubClass(before)) {
			return addSeq((Var_Sequence) before, after);
		} else if (isVar_SequenceSubClass(after)) {
			return addSeq((Var_Sequence) after, before);
		} else if (isVar_VariableSubClass(before)) {
			return addVar((Var_Variable) before, after);
		} else if (isVar_VariableSubClass(after)) {
			return addVar((Var_Variable) after, before);
		} else {
			throw new AlgebraicSimplifyingException("WTF is this Var class");
		}
	}

	/**
	 * Add a fraction with another variable
	 * 
	 * @param before
	 *            the fraction
	 * @param after
	 *            a variable
	 * @throws AlgebraicSimplifyingException
	 * @throws AlgebraicRuntimeException
	 */
	public Var_Base addFrac(Var_Fraction before, Var_Base after)
			throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		Op_Multiplication mult = new Op_Multiplication();
		if (isVar_FractionSubClass(after)) {
			Var_Fraction temp = (Var_Fraction) after;
			return new Var_Fraction(
					use(mult.use(before.numerator, temp.denominator), mult.use(temp.numerator, before.denominator)),
					mult.use(before.denominator, temp.denominator));
		} else {
			return new Var_Fraction(use(before.numerator, mult.use(before.denominator, after)), before.denominator);
		}
	}

	/**
	 * Add a set with another variable
	 * 
	 * @param before
	 *            the set
	 * @param after
	 *            a variable
	 * @throws AlgebraicSimplifyingException
	 * @throws AlgebraicRuntimeException
	 */
	public Var_Base addSet(Var_Set before, Var_Base after)
			throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		throw new AlgebraicRuntimeException(
				"Please kindly ask the dev to get off his ass and implement sets operations");
		// List<Var_Base> set = new ArrayList<Var_Base>();
		// for (Var_Base var : before.set) {
		// set.add(use(var, after));
		// }
		// if (before.getClass().equals(Set_Continuous.class)) {
		// Set_Continuous temp = (Set_Continuous) before;
		// return new Set_Continuous(set, temp.inclusiveStart, temp.inclusiveEnd);
		// }
		// return new Var_Set(set);
	}

	/**
	 * Add a sequence with another variable
	 * 
	 * @param before
	 *            the sequence
	 * @param after
	 *            a variable
	 * @throws AlgebraicSimplifyingException
	 * @throws AlgebraicRuntimeException
	 */
	public Var_Base addSeq(Var_Sequence before, Var_Base after)
			throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		if (isVar_SequenceSubClass(after)) {
			return addSeq(before, (Var_Sequence) after);
		}
		List<Var_Component> vars = new ArrayList<Var_Component>();
		boolean added = false;
		Var_Base temp = null;
		for (Var_Component var : before.vars) {
			temp = use(var.getVar(), after);
			if (!(isVar_SequenceSubClass(temp) && ((((Var_Sequence) temp).vars.size() == 1)
					|| ((Var_Sequence) temp).vars.get(0).getVar().equals(var.getVar()))) && !added) {
				vars.add(new Var_Component(temp, var.index()));
				added = true;
			} else {
				vars.add(var);
			}
		}
		if (!added) {
			vars.remove(vars.size() - 1);
			vars.add(new Var_Component(temp, before.vars.get(before.vars.size() - 1).index()));
		}
		return new Var_Sequence(vars, before.ops) {
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

	private Var_Base addSeq(Var_Sequence before, Var_Sequence after)
			throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		Var_Sequence res = before.copy();
		for (Var_Component iterable_element : after.vars) {
			res = (Var_Sequence) addSeq(res, iterable_element.getVar());
		}
		return res;
	}
	
	private Var_Base UnOperational(Var_Base before, Var_Base after) throws AlgebraicSimplifyingException{
		Var_Sequence result = new Var_Sequence(before) {
			@Override
			public char openParenthesis() {
				return 0;
			}
			@Override
			public char closingParenthesis() {
				return 0;
			}
		};
		AlgebraicExpression temp=result.add(after);
		result= new Var_Sequence(temp.vars,temp.ops) {
			@Override
			public char openParenthesis() {
				return 0;
			}
			@Override
			public char closingParenthesis() {
				return 0;
			}
		};
		return result;
	}

	/**
	 * Add two var_variables together
	 * 
	 * @param before
	 *            the first variable
	 * @param after
	 *            the second variable
	 * @throws AlgebraicRuntimeException
	 */
	public Var_Base addVar_Var(Var_Variable before, Var_Variable after) throws AlgebraicRuntimeException {
		if (before.mult == 0) {
			return after;
		} else if (after.mult == 0) {
			return before;
		}
		boolean sameUndefVars = true;
		for (UndefinedVar undefV : before.undefVars) {
			if (!after.undefVars.contains(undefV)) {
				sameUndefVars = false;
			}
		}
		for (UndefinedVar undefV : after.undefVars) {
			if (!before.undefVars.contains(undefV)) {
				sameUndefVars = false;
			}
		}

		if (sameUndefVars) {
			double mult = before.mult;
			mult += after.mult;
			return new Var_Variable(mult, before.undefVars);
		} else {
			List<Var_Component> vars = new ArrayList<Var_Component>();
			vars.add(new Var_Component(before, 0));
			vars.add(new Var_Component(after, 2));
			List<Op_Component> ops = new ArrayList<Op_Component>();
			ops.add(new Op_Component(new Op_Addition(), 1));
			return new Var_Sequence(vars, ops) {
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
	}

	/**
	 * Add a var_variable with another variable
	 * 
	 * @param before
	 *            the var_variable
	 * @param after
	 *            a variable
	 * @throws AlgebraicSimplifyingException
	 * @throws AlgebraicRuntimeException
	 */
	public Var_Base addVar(Var_Variable before, Var_Base after)
			throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		if (isVar_SetSubClass(after)) {
			return addSet((Var_Set) after, before);
		} else if (isVar_SequenceSubClass(after)) {
			return addSeq((Var_Sequence) after, before);
		} else if (isVar_VariableSubClass(after)) {
			return addVar_Var(before, (Var_Variable) after);
		} else {
			throw new AlgebraicSimplifyingException("WTF is this Var class");
		}
	}

	/**
	 * @see Op_Operation#priorityLevel()
	 */
	public int priorityLevel() {
		return 0;
	}
}
