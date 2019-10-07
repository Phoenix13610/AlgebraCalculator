package algebra.vars.specialVars.functions;

import algebra.AlgebraicParsingException;
import algebra.AlgebraicRuntimeException;
import algebra.AlgebraicSimplifyingException;
import algebra.ComponentReference;
import algebra.Op_Operation;
import algebra.Var_Base;
import algebra.vars.Var_Variable;

/**
 * A varaible object that represents a cosine object
 * 
 * @since 1.2
 * @author AntoineChevalier
 *
 */
public class DegCosine extends RadCosine {

	public DegCosine(Var_Base number) {
		super(number);
	}

	public DegCosine() {
		super();
	}

	public DegCosine parseVar(String name, ComponentReference reference)
			throws AlgebraicParsingException, AlgebraicRuntimeException {
		if (name.contains("cos")) {
			if (name.contains(this.name + "(")) {
				return new DegCosine(reference.generateVarComponent(getBetweenParenthesis(name), 0).getVar());
			}
			name = name.substring(3);
			return new DegCosine((Var_Variable) reference.generateVarComponent(name, 0).getVar());
		} else {
			throw new AlgebraicParsingException(
					"The entered name is not the corresponding name for the variable : " + this.name + " -> " + name);
		}
	}

	public Var_Variable simplify() throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		Var_Base number = this.number.simplify();
		if (Op_Operation.isVar_VariableSubClass(number) && ((Var_Variable) number).undefVars.isEmpty()) {
			return new Var_Variable(Math.cos(Math.toRadians(((Var_Variable) number).mult)));
		} else {
			throw new AlgebraicSimplifyingException("The number must be a number without any undefined variables");
		}
	}

	public DegCosine copy() {
		return new DegCosine(number.copy());
	}

	public boolean equals(Object obj) {
		try {
			DegCosine n = (DegCosine) obj;
			return n.number.equals(number) && n.name.equals(name);
		} catch (ClassCastException e) {
			return false;
		}
	}

}
