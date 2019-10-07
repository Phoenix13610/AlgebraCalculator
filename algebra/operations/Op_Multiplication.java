package algebra.operations;

import java.util.ArrayList;
import java.util.List;

import algebra.AlgebraicExpression;
import algebra.AlgebraicRuntimeException;
import algebra.AlgebraicSimplifyingException;
import algebra.Op_Operation;
import algebra.Var_Base;
import algebra.Var_Component;
import algebra.vars.UndefinedVar;
import algebra.vars.Var_Fraction;
import algebra.vars.Var_Sequence;
import algebra.vars.Var_Set;
import algebra.vars.Var_Variable;
import algebra.vars.sequence.Seq_UnOperational;

/**
 * 
 * @author AntoineChevalier
 * @since 1.0
 */
public final class Op_Multiplication extends Op_Operation {

	public Op_Multiplication() {
		super('*');
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
			return multFract((Var_Fraction) before, after);
		} else if (isVar_FractionSubClass(after)) {
			return multFract((Var_Fraction) after, before);
		} else if (isVar_SetSubClass(before)) {
			return multSet((Var_Set) before, after);
		} else if (isVar_SetSubClass(after)) {
			return multSet((Var_Set) after, before);
		} else if (isVar_SequenceSubClass(before)) {
			return multSeq((Var_Sequence) before, after);
		} else if (isVar_SequenceSubClass(after)) {
			return multSeq((Var_Sequence) after, before).simplify().simplify();
		} else if (isVar_VariableSubClass(before)) {
			return multVar((Var_Variable) before, after);
		} else if (isVar_VariableSubClass(after)) {
			return multVar((Var_Variable) after, before);
		} else {
			throw new AlgebraicSimplifyingException("WTF is this Var class");
		}
	}

	/**
	 * multiply a fraction with another variable
	 * 
	 * @param before
	 *            the fraction
	 * @param after
	 *            a variable
	 * @throws AlgebraicSimplifyingException
	 * @throws AlgebraicRuntimeException
	 */
	public Var_Base multFract(Var_Fraction before, Var_Base after)
			throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		if (isVar_FractionSubClass(after)) {
			Var_Fraction temp = (Var_Fraction) after;
			return new Var_Fraction(use(before.numerator, temp.numerator), use(before.denominator, temp.denominator));
		} else {
			return new Var_Fraction(use(before.numerator, after), before.denominator);
		}
	}

	/**
	 * multiply a set with another variable
	 * 
	 * @param before
	 *            the set
	 * @param after
	 *            a variable
	 * @throws AlgebraicSimplifyingException
	 * @throws AlgebraicRuntimeException
	 */
	public Var_Base multSet(Var_Set before, Var_Base after)
			throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		throw new AlgebraicRuntimeException(
				"Please kindly ask the dev to get off his ass and implement sets operations");
		// List<Var_Base> set = new ArrayList<Var_Base>();
		// for (Var_Base var : before.set) {
		// set.add(use(var, after));
		// }
		// if(before.getClass().equals(Set_Continuous.class)) {
		// Set_Continuous temp=(Set_Continuous)before;
		// return new Set_Continuous(set,temp.inclusiveStart,temp.inclusiveEnd);
		// }
		// return new Var_Set(set);
	}

	/**
	 * multiply a sequence with another variable
	 * 
	 * @param before
	 *            the sequence
	 * @param after
	 *            a variable
	 * @throws AlgebraicSimplifyingException
	 * @throws AlgebraicRuntimeException
	 */
	public Var_Base multSeq(Var_Sequence before, Var_Base after)
			throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		if (isVar_SetSubClass(after)) {
			return multSet((Var_Set) after, before);
		}
		List<Var_Component> vars = new ArrayList<Var_Component>();
		for (Var_Component var : before.vars) {
			vars.add(new Var_Component(use(var.getVar(), after), var.index()));
		}
		int index = 0;
		for (Var_Component v : vars) {
			v.setIndex(index);
			index += 2;
		}
		AlgebraicExpression temp = new AlgebraicExpression(vars, before.ops).integrateSequences().simplify();
		return new Var_Sequence(temp.vars, temp.ops) {
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
	
	private Var_Base UnOperational(Var_Base before, Var_Base after) throws AlgebraicSimplifyingException{
		Seq_UnOperational result = new Seq_UnOperational(before);
		AlgebraicExpression temp=result.multiply(after);
		result= new Seq_UnOperational(temp.vars,temp.ops);
		return result;
	}

	/**
	 * multiply a var_variable with another variable
	 * 
	 * @param before
	 *            the var_variable
	 * @param after
	 *            a variable
	 * @throws AlgebraicSimplifyingException
	 * @throws AlgebraicRuntimeException
	 */
	public Var_Base multVar(Var_Variable before, Var_Base after)
			throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		if (isVar_FractionSubClass(after)) {
			return multFract((Var_Fraction) after, before);
		} else if (isVar_SetSubClass(after)) {
			return multSet((Var_Set) after, before);
		} else if (isVar_SequenceSubClass(after)) {
			return multSeq((Var_Sequence) after, before);
		} else {
			Var_Variable var = (Var_Variable) after;
			double mult = before.mult * var.mult;
			List<UndefinedVar> undef = before.undefVars;
			for (UndefinedVar v1 : var.undefVars) {
				boolean added = false;
				for (int i = 0; i < undef.size(); i++) {
					UndefinedVar v2 = undef.get(i);
					if (v2.name.equals(v1.name) && !added) {
						added = true;
						undef.remove(v2);
						undef.add(new UndefinedVar(v1.name, new Op_Addition().use(v1.expo, v2.expo)));
					}
				}
				if (!added) {
					undef.add(v1);
				}
			}
			return new Var_Variable(mult, undef);
		}
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
