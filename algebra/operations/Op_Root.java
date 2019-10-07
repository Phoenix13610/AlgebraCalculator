package algebra.operations;

import algebra.AlgebraicRuntimeException;
import algebra.AlgebraicSimplifyingException;
import algebra.Op_Operation;
import algebra.Var_Base;
import algebra.vars.Var_Variable;

public class Op_Root extends Op_Operation{

	public Op_Root() {
		super('√');
	}

	@Override
	public Var_Variable use(Var_Base before, Var_Base after)
			throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		if(isVar_VariableSubClass(before)&&isVar_VariableSubClass(after)) {
			Var_Variable first=(Var_Variable)before;
			Var_Variable second=(Var_Variable)after;
			if(first.undefVars.isEmpty()&&second.undefVars.isEmpty()) {
				double f=first.mult;
				double s=second.mult;
				return new Var_Variable(Math.pow(Math.E, Math.log(s)/f));
			}else {
				throw new AlgebraicSimplifyingException("Both variables must be numbers : "+before+", "+after);
			}
		}else {
			throw new AlgebraicSimplifyingException("Both variables must be numbers : "+before+", "+after);
		}
	}

	@Override
	public int priorityLevel() {
		return 2;
	}
	
}
