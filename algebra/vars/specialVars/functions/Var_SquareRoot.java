package algebra.vars.specialVars.functions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import algebra.AlgebraicParsingException;
import algebra.AlgebraicRuntimeException;
import algebra.AlgebraicSimplifyingException;
import algebra.ComponentReference;
import algebra.Op_Operation;
import algebra.Var_Base;
import algebra.vars.Var_Sequence;
import algebra.vars.Var_Variable;
import algebra.vars.specialVars.Var_Function;

public class Var_SquareRoot extends Var_Function {

	public Var_SquareRoot(Var_Base number) {
		super("√", number);
	}

	public Var_SquareRoot() {
		this(new Var_Variable(0));
	}

	public Var_Variable simplify() throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		Var_Base number = this.number.simplify();
		if(Op_Operation.isVar_VariableSubClass(number)) {
			Var_Variable first=(Var_Variable)number;
			if(first.undefVars.isEmpty()) {
				double f=first.mult;
				return new Var_Variable(Math.sqrt(f));
			}else {
				throw new AlgebraicSimplifyingException("Both variables must be numbers : "+number);
			}
		}else {
			if(Op_Operation.isVar_SequenceSubClass(number)&&((Var_Sequence)number).length==1) {
				number=((Var_Sequence)number).vars.get(0).getVar();
				return new Var_SquareRoot(number).simplify();
			}
			throw new AlgebraicSimplifyingException("Both variables must be numbers : "+number);
		}
	}

	public Var_SquareRoot parseVar(String name, ComponentReference reference)
			throws AlgebraicParsingException, AlgebraicRuntimeException {
		if (name.contains("√")||name.contains("sqrt")) {
			Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(name);
			while (m.find()) {
				return new Var_SquareRoot(reference.generateVarComponent(m.group(1), 0).getVar());
			}
			throw new AlgebraicRuntimeException("There are no variable in the parenthesis");
		} else {
			throw new AlgebraicParsingException(
					"The entered name is not the corresponding name for the variable : " + this.name + " -> " + name);
		}
	}

	@Override
	public Var_SquareRoot copy() {
		return new Var_SquareRoot(number.copy());
	}

	@Override
	public Var_SquareRoot evaluate(String name, Var_Base newValue)
			throws AlgebraicRuntimeException, AlgebraicSimplifyingException {
		return new Var_SquareRoot(number.evaluate(name, newValue));
	}

	public boolean equals(Object obj) {
		try {
			Var_SquareRoot n = (Var_SquareRoot) obj;
			return n.number.equals(number) && n.name.equals(name);
		} catch (ClassCastException e) {
			return false;
		}
	}
	
	@Override
	public String getRegex(ComponentReference reference) {
		return "^(√)|(sqrt)\\(\\S*\\)$";
	}

}
