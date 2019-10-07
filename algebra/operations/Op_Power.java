package algebra.operations;

import algebra.AlgebraicRuntimeException;
import algebra.AlgebraicSimplifyingException;
import algebra.Op_Operation;
import algebra.Var_Base;
import algebra.vars.Var_Set;
import algebra.vars.Var_Variable;

/**
 * 
 * @author AntoineChevalier
 * @since 1.0
 */
public final class Op_Power extends Op_Operation {

	public Op_Power() {
		super('^');
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
		if (isVar_FractionSubClass(after)) {
			throw new AlgebraicSimplifyingException("Did not implement the exponentiation of fractions yet");
		} else if (isVar_SetSubClass(after)) {
			return exponientSet(before, (Var_Set) after);
		} else if (isVar_SequenceSubClass(after)) {
			throw new AlgebraicSimplifyingException("The exponent canno't be a sequence");
		} else if (isVar_VariableSubClass(after)) {
			return exponientVar(before, (Var_Variable) after);
		} else {
			throw new AlgebraicSimplifyingException("WTF is this Var class");
		}
	}

	private Var_Base UnOperational(Var_Base before, Var_Base after) throws AlgebraicSimplifyingException{
		throw new AlgebraicSimplifyingException("Cannot add onto a non parenthesis sequence");
	}

	/**
	 * Exponentiate a variable with a set
	 * 
	 * @param before
	 *            a variable
	 * @param after
	 *            the set
	 * @throws AlgebraicSimplifyingException
	 * @throws AlgebraicRuntimeException
	 */
	public Var_Base exponientSet(Var_Base before, Var_Set after)
			throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		throw new AlgebraicRuntimeException("Please kindly ask the dev to get off his ass and implement sets operations");
//		List<Var_Base> set = new ArrayList<Var_Base>();
//		for (Var_Base var_Base : after.set) {
//			set.add(use(before, var_Base));
//		}
//		if(before.getClass().equals(Set_Continuous.class)) {
//			Set_Continuous temp=(Set_Continuous)before;
//			return new Set_Continuous(set,temp.inclusiveStart,temp.inclusiveEnd);
//		}
//		return new Var_Set(set);
	}

	/**
	 * Exponentiate a variable with a var_variable
	 * 
	 * @param before
	 *            a variable
	 * @param after
	 *            the var_variable
	 * @throws AlgebraicSimplifyingException
	 * @throws AlgebraicRuntimeException
	 */
	public Var_Base exponientVar(Var_Base before, Var_Variable after)
			throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		if (!after.undefVars.isEmpty()) {
			throw new AlgebraicSimplifyingException("There cannot be any undefined variables in the exponent");
		} else {
			Op_Multiplication mult = new Op_Multiplication();
			double pow = after.mult;
			if(pow>15) {
				throw new AlgebraicRuntimeException("the exponent cannot be higher than 15, the program takes too long to simplify");
			}
			Var_Base result = new Var_Variable(1);
			for (int i = 0; i < pow; i++) {
				result = mult.use(result.copy(), before.copy());
			}
			return result;
		}
	}

	/**
	 * @since 1.0
	 * @see Op_Operation#priorityLevel()
	 */
	@Override
	public int priorityLevel() {
		return 2;
	}

}
