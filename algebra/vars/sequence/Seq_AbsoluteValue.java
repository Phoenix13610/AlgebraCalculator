package algebra.vars.sequence;

import java.util.ArrayList;
import java.util.List;

import algebra.AlgebraicExpression;
import algebra.AlgebraicParsingException;
import algebra.AlgebraicRuntimeException;
import algebra.AlgebraicSimplifyingException;
import algebra.ComponentReference;
import algebra.Op_Component;
import algebra.Op_Operation;
import algebra.Var_Base;
import algebra.Var_Component;
import algebra.Var_UnOperational;
import algebra.vars.Var_Fraction;
import algebra.vars.Var_Sequence;
import algebra.vars.Var_Set;
import algebra.vars.Var_Variable;

/**
 * A sequence for absolute value
 * @author AntoineChevalier
 * @since 1.1
 */
public class Seq_AbsoluteValue extends Var_Sequence implements Var_UnOperational{

	/**
	 * A basic constructor
	 * 
	 * @param vars
	 *            the variable
	 * @param ops
	 *            the operations
	 */
	public Seq_AbsoluteValue(List<Var_Component> vars, List<Op_Component> ops) {
		super(vars, ops);
	}

	/**
	 * A basic contructor for parsing reference
	 */
	public Seq_AbsoluteValue() {
	}

	/**
	 * @see Var_Base#getRegex(ComponentReference
	 */
	public String getRegex(ComponentReference reference) {
		return getREGEX();
	}

	public static String getREGEX() {
		return "^\\<\\S*\\>$";
	}

	@Override
	public Var_Sequence simplify() throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		List<Var_Component> newVars = new ArrayList<Var_Component>();
		Var_Sequence temp = super.simplify();
		for (Var_Component var : temp.vars) {
			if (Op_Operation.isVar_VariableSubClass(var.getVar())) {
				Var_Variable var_Variable = (Var_Variable) var.getVar();
				if(!var_Variable.undefVars.isEmpty()) {
					return this;
				}
			}
			newVars.add(new Var_Component(use(var.getVar()), var.index()));
		}
		return new Var_Sequence(newVars, temp.ops) {
			@Override
			public char openParenthesis() {
				return 0;
			}

			@Override
			public char closingParenthesis() {
				return 0;
			}
		}.simplify();
	}

	public static Var_Base use(Var_Base var) throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		var = var.simplify();
		if (Op_Operation.isVar_FractionSubClass(var)) {
			Var_Fraction fraction = (Var_Fraction) var;
			return new Var_Fraction(use(fraction.numerator), use(fraction.denominator));
		} else if (Op_Operation.isVar_SetSubClass(var)) {
			List<Var_Base> newSet = new ArrayList<Var_Base>();
			Var_Set set = (Var_Set) var;
			for (Var_Base var_Base : set.set) {
				newSet.add(use(var_Base));
			}
			return new Var_Set(newSet);
		} else if (Op_Operation.isVar_SequenceSubClass(var)) {
			Var_Sequence abs = (Var_Sequence) var;
			return (new Seq_AbsoluteValue(abs.vars, abs.ops)).simplify();
		} else if (Op_Operation.isVar_VariableSubClass(var)) {
			Var_Variable var_Variable = (Var_Variable) var;
			if(var_Variable.undefVars.isEmpty()) {
				return new Var_Variable(Math.abs(var_Variable.mult), var_Variable.undefVars).copy();
			}else {
				return var_Variable;
			}
		} else {
			throw new AlgebraicSimplifyingException("WTF is this Var class");
		}
	}

	/**
	 * @see Algebra.vars.Var_Sequence#openParenthesis()
	 */
	@Override
	public char openParenthesis() {
		return '<';
	}

	/**
	 * @see Algebra.vars.Var_Sequence#closingParenthesis()
	 */
	@Override
	public char closingParenthesis() {
		return '>';
	}

	public String toString() {
		return "|" + super.toString() + "|";

	}

	/**
	 * @throws AlgebraicRuntimeException 
	 * @see Var_Sequence#parseVar(String, ComponentReference)
	 */
	public Seq_AbsoluteValue parseVar(String sequence, ComponentReference reference) throws AlgebraicParsingException, AlgebraicRuntimeException {
		AlgebraicExpression temp = AlgebraicExpression.parse(sequence.substring(1, sequence.length() - 1));
		return new Seq_AbsoluteValue(temp.vars, temp.ops);
	}

	/**
	 * @see Var_Sequence#copy()
	 */
	public Seq_AbsoluteValue copy(){
		List<Var_Component> newVars = new ArrayList<Var_Component>();
		for (Var_Component var : vars) {
			newVars.add(new Var_Component(var.getVar().copy(), var.index()));
		}
		return new Seq_AbsoluteValue(newVars, ops);
	}

	/**
	 * @see Var_Sequence#evaluate(String, Var_Base)
	 */
	public Seq_AbsoluteValue evaluate(String name,Var_Base newValue) throws AlgebraicRuntimeException, AlgebraicSimplifyingException {
		Var_Sequence sequence=super.evaluate(name, newValue);
		return new Seq_AbsoluteValue(sequence.vars, sequence.ops);
	}
}
