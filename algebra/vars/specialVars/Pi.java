package algebra.vars.specialVars;

import algebra.AlgebraicParsingException;
import algebra.AlgebraicRuntimeException;
import algebra.AlgebraicSimplifyingException;
import algebra.ComponentReference;
import algebra.Var_Base;
import algebra.vars.Var_Special;
import algebra.vars.Var_Variable;

public class Pi extends Var_Special{
	public final String name="π";
	
	public Pi() {
		super("π");
	}
	
	public Var_Variable simplify() {
		return new Var_Variable(Math.PI);
	}

	public Pi parseVar(String name,ComponentReference reference) throws AlgebraicParsingException, AlgebraicRuntimeException{
		if(name.equals("π")||name.equals("Pi")){
			return new Pi();
		}else {
			throw new AlgebraicParsingException("The entered name is not the corresponding name for the variable : "+this.name+" -> "+name);
		}
	}

	public String toString() {
		return name;
	}

	@Override
	public String getRegex(ComponentReference reference) {
		return "π|Pi";
	}

	@Override
	public Var_Base copy() {
		return new Pi();
	}

	@Override
	public Var_Base evaluate(String name, Var_Base newValue)
			throws AlgebraicRuntimeException, AlgebraicSimplifyingException {
		return new Pi();
	}

}
