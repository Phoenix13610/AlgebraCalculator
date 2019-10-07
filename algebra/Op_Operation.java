package algebra;

import algebra.vars.Var_Fraction;
import algebra.vars.Var_Sequence;
import algebra.vars.Var_Set;
import algebra.vars.Var_Variable;

/**
 * 
 * @author AntoineChevalier
 * @since 1.0
 */
public abstract class Op_Operation {
	public final char type;

	/**
	 * a basic constructor
	 * 
	 * @param type
	 *            the type
	 * @since 1.0
	 */
	public Op_Operation(char type) {
		this.type = type;
	}

	public char getType() {
		return type;
	}

	/**
	 * returns if the entered varBase is a sub class of Var_Set
	 * 
	 * @param v
	 *            the var
	 * @since 1.0
	 */
	public static boolean isVar_SetSubClass(Var_Base v) {
		for (Class<? extends Object> class1 = v.getClass(); class1 != null; class1 = class1.getSuperclass()) {
			if (class1.equals(Var_Set.class)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * returns if the entered varBase is a sub class of Var_Sequence
	 * 
	 * @param v
	 *            the var
	 * @since 1.0
	 */
	public static boolean isVar_SequenceSubClass(Var_Base v) {
		for (Class<? extends Object> class1 = v.getClass(); class1 != null; class1 = class1.getSuperclass()) {
			if (class1.equals(Var_Sequence.class)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * returns if the entered varBase is a sub class of Var_Variable
	 * 
	 * @param v
	 *            the var
	 * @since 1.0
	 */
	public static boolean isVar_VariableSubClass(Var_Base v) {
		for (Class<? extends Object> class1 = v.getClass(); class1 != null; class1 = class1.getSuperclass()) {
			if (class1.equals(Var_Variable.class)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * returns if the entered varBase is a sub class of Var_Fraction
	 * 
	 * @param v
	 *            the var
	 * @since 1.0
	 */
	public static boolean isVar_FractionSubClass(Var_Base v) {
		for (Class<? extends Object> class1 = v.getClass(); class1 != null; class1 = class1.getSuperclass()) {
			if (class1.equals(Var_Fraction.class)) {
				return true;
			}
		}
		return false;
	}

	public boolean equals(Object obj) {
		try {
			Op_Operation op = (Op_Operation) obj;
			return op.type == type;
		} catch (ClassCastException e) {
			return false;
		}
	}

	/**
	 * Simplifies both varBases and uses the operation
	 * 
	 * @param before
	 *            the variable before the operation
	 * @param after
	 *            the variable after the operation
	 * @return a varBase being the result of the operation
	 * @throws AlgebraicSimplifyingException
	 * @throws AlgebraicRuntimeException
	 * @since 1.0
	 */
	public abstract Var_Base use(Var_Base before, Var_Base after)
			throws AlgebraicSimplifyingException, AlgebraicRuntimeException;

	/**
	 * the priority of the operation
	 * <p>
	 * it determine's the order of when the operation will be used while
	 * simplifying. It will be used before any operation will a priority level
	 * inferior to this operation's priority level
	 * 
	 * @return the priority of the operation
	 * @since 1.0
	 */
	public abstract int priorityLevel();

	protected boolean isUnOperational(Var_Base variable) {
		for (Class iFace : variable.getClass().getInterfaces()) {
			if (iFace.equals(Var_UnOperational.class)) {
				return true;
			}
		}
		return false;
	}
}
