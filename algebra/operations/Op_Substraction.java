package algebra.operations;

import algebra.AlgebraicRuntimeException;
import algebra.AlgebraicSimplifyingException;
import algebra.Op_Operation;
import algebra.Var_Base;
import algebra.vars.Var_Variable;

/**
 * 
 * @author AntoineChevalier
 * @since 1.0
 */
public final class Op_Substraction extends Op_Operation {

	public Op_Substraction() {
		super('-');
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
		return new Op_Addition().use(before, new Op_Multiplication().use(after, new Var_Variable(-1)));
	}

	/**
	 * @since 1.0
	 * @see Op_Operation#priorityLevel()
	 */
	@Override
	public int priorityLevel() {
		return 0;
	}

}
