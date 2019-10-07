package algebra.vars.sequence;

import java.util.List;

import algebra.AlgebraicExpression;
import algebra.AlgebraicRuntimeException;
import algebra.AlgebraicSimplifyingException;
import algebra.Op_Component;
import algebra.Var_Base;
import algebra.Var_Component;
import algebra.Var_UnOperational;
import algebra.vars.Var_Sequence;

/**
 * A sequence that will not be affected by any operations
 * @since 1.2.1
 * @author AntoineChevalier
 *
 */
public class Seq_UnOperational extends Var_Sequence implements Var_UnOperational{
	public Seq_UnOperational() {
		super();
	}

	/**
	 * A basic constructor
	 * @param vars
	 * @param ops
	 */
	public Seq_UnOperational(List<Var_Component> vars, List<Op_Component> ops) {
		super(vars, ops);
	}
/**
 * A basic constructor
 * @param base
 */
	public Seq_UnOperational(Var_Base base) {
		super(base);
	}
/**
 * @see Var_Sequence#openParenthesis()
 * @return
 */
	@Override
	public char openParenthesis() {
		return 0;
	}
	/**
	 * @see Var_Sequence#closingParenthesis()
	 * @return
	 */

	@Override
	public char closingParenthesis() {
		return 0;
	}
/**
 * @see Var_Sequence#simplify()
 * @return
 * @throws AlgebraicSimplifyingException
 * @throws AlgebraicRuntimeException
 */
	public Var_Sequence simplify() throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		boolean toRetun = true;
		List<Var_Component>newVars=copy().vars;
		for (Var_Component var : vars) {
			for (Class inter : var.getVar().getClass().getInterfaces()) {
				if(inter.equals(Var_UnOperational.class)) {
					toRetun=true;
					if(!var.getVar().equals(var.getVar().simplify())) {
						toRetun=false;
						newVars.set(newVars.indexOf(var), new Var_Component(var.getVar().simplify(), var.index()));
					}
				}
			}
		}
		if(toRetun) {
			return this;
		}else {
			return new Var_Sequence(newVars,ops) {
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
	
	public Seq_UnOperational copy() {
		Var_Sequence temp=super.copy();
		return new Seq_UnOperational(temp.vars, temp.ops);
	}
	
	/**
	 * @see Var_Base#evaluate(String, Var_Base)
	 */
	@Override
	public Seq_UnOperational evaluate(String name, Var_Base newValue)
			throws AlgebraicRuntimeException, AlgebraicSimplifyingException {
		AlgebraicExpression temp = super.evaluate(name, newValue);
		return new Seq_UnOperational(temp.vars, temp.ops);
	}

}
