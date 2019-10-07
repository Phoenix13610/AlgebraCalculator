package algebra.vars.specialVars.functions;

import algebra.AlgebraicParsingException;
import algebra.AlgebraicRuntimeException;
import algebra.AlgebraicSimplifyingException;
import algebra.ComponentReference;
import algebra.Op_Operation;
import algebra.Var_Base;
import algebra.vars.Var_Variable;

/**
 * A varaible object that represents a tangent object in degrees
 * 
 * @since 1.2
 * @author AntoineChevalier
 *
 */
public class DegTangent extends RadTangent {

	public DegTangent(Var_Base number) {
		super(number);
	}

	public DegTangent() {
		super();
	}

	public DegTangent parseVar(String name, ComponentReference reference)
			throws AlgebraicParsingException, AlgebraicRuntimeException {
		if (name.contains("tan")) {
			if (name.contains(this.name + "(")) {
				return new DegTangent(reference.generateVarComponent(getBetweenParenthesis(name), 0).getVar());
			}
			name = name.substring(3);
			return new DegTangent((Var_Variable) reference.generateVarComponent(name, 0).getVar());
		} else {
			throw new AlgebraicParsingException(
					"The entered name is not the corresponding name for the variable : " + this.name + " -> " + name);
		}
	}

	public Var_Variable simplify() throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		Var_Base number = this.number.simplify();
		if (Op_Operation.isVar_VariableSubClass(number) && ((Var_Variable) number).undefVars.isEmpty()) {
			return new Var_Variable(Math.tan(Math.toRadians(((Var_Variable) number).mult)));
		} else {
			throw new AlgebraicSimplifyingException("The number must be a number without any undefined variables");
		}
	}

	public DegTangent copy() {
		return new DegTangent(number.copy());
	}

	public boolean equals(Object obj) {
		try {
			DegTangent n = (DegTangent) obj;
			return n.number.equals(number) && n.name.equals(name);
		} catch (ClassCastException e) {
			return false;
		}
	}

}
