package algebra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import algebra.operations.Op_Addition;
import algebra.operations.Op_Division;
import algebra.operations.Op_Multiplication;
import algebra.operations.Op_Substraction;
import algebra.vars.VarValue;
import algebra.vars.Var_Sequence;
import algebra.vars.Var_Variable;

/**
 * A immutable object representing a algebraic expression
 * 
 * @author AntoineChevalier
 * @version 1.3.0.1
 */
public class AlgebraicExpression {

	public static ComponentReference reference = new ComponentReference() {
		@Override
		public void setReference() {
			addBasics();
			setToDegrees();
		}
	};
	public final List<Var_Component> vars;
	public final List<Op_Component> ops;
	public final int length;

	/**
	 * A constructor for the Algebraic Expression object
	 * 
	 * @param components
	 *            A list of the components
	 * @throws AlgebraicRuntimeException
	 *             If one of the components isn't a known class
	 */
	public AlgebraicExpression(List<AlgebraicComponent> components) throws AlgebraicRuntimeException {
		vars = new ArrayList<Var_Component>();
		ops = new ArrayList<Op_Component>();
		for (int i = 0; i < components.size(); i++) {
			components.get(i).setIndex(i);
		}
		for (AlgebraicComponent algebricComponent : components) {
			if (algebricComponent.getClass().equals(Var_Component.class)) {
				vars.add((Var_Component) algebricComponent);
			} else if (algebricComponent.getClass().equals(Op_Component.class)) {
				ops.add((Op_Component) algebricComponent);
			} else {
				throw new AlgebraicRuntimeException("Unknown Algebraic Component");
			}
		}
		Collections.unmodifiableList(this.vars);
		Collections.unmodifiableList(this.ops);
		length = vars.size() + ops.size();
	}

	/**
	 * A constructor for the Algebraic Expression object
	 * 
	 * @param vars
	 *            a list of the variables components
	 * @param ops
	 *            a list of the operation components
	 */
	public AlgebraicExpression(List<Var_Component> vars, List<Op_Component> ops) {
		this.vars = vars;
		Collections.unmodifiableList(this.vars);
		this.ops = ops;
		Collections.unmodifiableList(this.ops);
		length = vars.size() + ops.size();
	}

	/**
	 * basic constructor
	 * 
	 * @param base
	 *            the first variable
	 */
	public AlgebraicExpression(Var_Base base) {
		this(Arrays.asList(new Var_Component[] { new Var_Component(base, 0) }), new ArrayList<Op_Component>());
	}

	/**
	 * Takes in a String and returns a AlgebraicExpression object
	 * 
	 * @param expression
	 *            A String representing the expression
	 * @return An object representing the expression
	 * @throws AlgebraicParsingException
	 *             if a the inputted String is not a legal expression
	 * @since 1.0
	 */
	public static AlgebraicExpression parse(String expression)
			throws AlgebraicParsingException, AlgebraicRuntimeException {
		return new AlgebraicExpressionParser(reference) {
			@Override
			public AlgebraicExpression parse(String e) throws AlgebraicParsingException, AlgebraicRuntimeException {
				e = e.trim();
				List<Var_Component> vars = new ArrayList<Var_Component>();
				List<Op_Component> ops = new ArrayList<Op_Component>();
				int[] insideParenthesis = new int[reference.possibleParenthesis.size()];
				for (int i = 0; i < insideParenthesis.length; i++) {
					insideParenthesis[i] = 0;
				}
				boolean waitForVar = true;
				int index = 0;
				int startIndex = 0;
				for (int i = 0; i < e.length(); i++) {
					if (reference.isOperation(e.charAt(i)) && !waitForVar) {
						if (!insideParenthesis(insideParenthesis)) {
							String var;
							char op;
							op = e.charAt(i);
							var = e.substring(startIndex, i);
							vars.add(generateVarComponent(reference, var, index));
							index++;
							ops.add(generateOperation(reference, op, index));
							index++;
							startIndex = i + 1;
							waitForVar = true;
						}
					} else {
						if (reference.isOpenParenthesis(e.charAt(i))) {
							insideParenthesis[indexPossParenthesisChar(e.charAt(i))]++;
							waitForVar = false;
						} else if (reference.isClosedParenthesis(e.charAt(i))) {
							insideParenthesis[indexPossParenthesisChar(e.charAt(i))]--;
							if (insideParenthesis[indexPossParenthesisChar(e.charAt(i))] < 0) {
								throw new AlgebraicParsingException("Uncompleted parenthesis");
								// e = reference.correspondingParenthesis(e.charAt(i)) + e.substring(0, i + 1);
								// return parse(e);
							}
							waitForVar = false;
						} else {
							waitForVar = false;
						}
					}
				}
				if (insideParenthesis(insideParenthesis)) {
					throw new AlgebraicParsingException("Uncompleted parenthesis");
					// List<Character> toClose = new ArrayList<Character>();
					// for (int i = 0; i < insideParenthesis.length; i++) {
					// if (insideParenthesis[i] > 0) {
					// for (int j = 0; j < insideParenthesis[i]; j++) {
					// toClose.add(reference.possibleParenthesis.get(i).closingParenthesis());
					// }
					// }
					// }
					// int[] order = new int[toClose.size()];
					// int orderIndex = 0;
					// for (int i = startIndex; i < e.length(); i++) {
					// for (int j = 0; j < toClose.size(); j++) {
					// if (e.charAt(i) == reference.correspondingParenthesis(toClose.get(j))) {
					// order[orderIndex] = j;
					// orderIndex++;
					// break;
					// }
					// }
					// }
					// for (int i = order.length - 1; i >= 0; i--) {
					// e += toClose.get(order[i]);
					// }
				}
				vars.add(generateVarComponent(reference, e.substring(startIndex), index));
				return new AlgebraicExpression(vars, ops);
			}

			private int indexPossParenthesisChar(char c) throws AlgebraicRuntimeException {
				for (int i = 0; i < reference.possibleParenthesis.size(); i++) {
					Var_Sequence v = reference.possibleParenthesis.get(i);
					if (v.openParenthesis() == c || v.closingParenthesis() == c) {
						return i;
					}
				}
				throw new AlgebraicRuntimeException("the char isn't in the possibleOps list");
			}

			private boolean insideParenthesis(int[] list) {
				for (int i : list) {
					if (i != 0) {
						return true;
					}
				}
				return false;
			}
		}.parse(expression);
	}

	/**
	 * Takes in a String and returns a AlgebraicExpression object
	 * 
	 * @param expression
	 *            A String representing the expression
	 * @param reference
	 *            the component reference if the default one is not correct
	 * @return An object representing the expression
	 * @throws AlgebraicParsingException
	 *             if a the inputted String is not a legal expression
	 * @since 1.0
	 */
	public static AlgebraicExpression parse(String expression, ComponentReference reference)
			throws AlgebraicParsingException, AlgebraicRuntimeException {
		AlgebraicExpression.reference = reference;
		return parse(expression);
	}

	/**
	 * @return A list of all the components
	 * @since 1.0
	 */
	public List<AlgebraicComponent> toComponentList() {
		List<AlgebraicComponent> list = new ArrayList<AlgebraicComponent>();
		int varIndex = 0;
		int opsIndex = 0;
		for (int index = 0; index < length; index++) {
			if (!vars.isEmpty() && (index == vars.get(varIndex).index())) {
				list.add(vars.get(varIndex));
				varIndex++;
				if (varIndex >= vars.size()) {
					varIndex--;
				}
			} else if (!ops.isEmpty() && (index == ops.get(opsIndex).index())) {
				list.add(ops.get(opsIndex));
				opsIndex++;
				if (opsIndex >= ops.size()) {
					opsIndex--;
				}
			}
		}
		return list;
	}

	/**
	 * Copy the object
	 * 
	 * @return An exact copy of this object
	 * @throws AlgebraicSimplifyingException
	 * @throws AlgebraicRuntimeException
	 *             since 0.2
	 */
	public AlgebraicExpression copy() {
		List<Var_Component> vars = new ArrayList<Var_Component>();
		for (Var_Component var : this.vars) {
			vars.add(new Var_Component(var.getVar().copy(), var.index()));
		}
		return new AlgebraicExpression(vars, ops);
	}

	public String toString() {
		String result = "";
		for (AlgebraicComponent alC : toComponentList()) {
			result += alC.toString();
		}
		return result;
	}

	/**
	 * @param index
	 *            the index of the varComponent
	 * @return the varComponent at the given index
	 * @throws AlgebraicRuntimeException
	 * @since 1.0
	 */
	public Var_Component getVar(int index) throws AlgebraicRuntimeException {
		AlgebraicComponent component = toComponentList().get(index);
		if (component.getClass().equals(Var_Component.class)) {
			return (Var_Component) component;
		} else {
			System.out.println(this);
			throw new AlgebraicRuntimeException(
					"Class cast exception, the component at designated index is not a Variable Component");
		}
	}

	/**
	 * 
	 * @param index
	 *            the index of the opComponent
	 * @return the opComponent at the given index
	 * @throws AlgebraicRuntimeException
	 * @since 1.0
	 */
	public Op_Component getOp(int index) throws AlgebraicRuntimeException {
		AlgebraicComponent component = toComponentList().get(index);
		if (component.getClass().equals(Op_Component.class)) {
			return (Op_Component) component;
		} else {
			throw new AlgebraicRuntimeException(
					"Class cast exception, the component at designated index is not a Operation Component");
		}
	}

	/**
	 * replaces this components throughout the indexs ranges with the new components
	 * <p>
	 * removes each components throughout the indexs and add each component at the
	 * start index
	 * 
	 * @param start
	 *            the start index
	 * @param end
	 *            the end index
	 * @param newComponents
	 *            A list of the new components
	 * @return A algebraic expression with the modifications
	 * @throws AlgebraicRuntimeException
	 * @since 1.0
	 */
	public AlgebraicExpression replaceComponents(int start, int end, List<AlgebraicComponent> newComponents)
			throws AlgebraicRuntimeException {
		List<AlgebraicComponent> list = toComponentList();
		for (int i = 0; i < end - start + 1; i++) {
			list.remove(start);
		}
		for (int i = 0; i < newComponents.size(); i++) {
			newComponents.get(i).setIndex(start + i);
		}
		for (int i = 0; i < newComponents.size(); i++) {
			list.add(start + i, newComponents.get(i));
		}
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setIndex(i);
		}
		return new AlgebraicExpression(list);
	}

	/**
	 * replaces this components throughout the indexs ranges with the new component
	 * <p>
	 * removes each components throughout the indexs and add the component at the
	 * start index
	 * 
	 * @param start
	 *            the start index
	 * @param end
	 *            the end index
	 * @param newComponents
	 *            The new component
	 * @return A algebraic expression with the modifications
	 * @throws AlgebraicRuntimeException
	 * @since 1.0
	 */
	public AlgebraicExpression replaceComponents(int start, int end, AlgebraicComponent newComponents)
			throws AlgebraicRuntimeException {
		List<AlgebraicComponent> list = toComponentList();
		for (int i = 0; i < end - start + 1; i++) {
			list.remove(start);
		}
		list.add(start, newComponents);

		int index = 0;
		for (AlgebraicComponent algebraicComponent : list) {
			algebraicComponent.setIndex(index);
			index++;
		}
		return new AlgebraicExpression(list);
	}

	/**
	 * Replaces a variable with a new value
	 * <p>
	 * Calls the evaluate method of each variable
	 * 
	 * @param name
	 *            the name of the variable to be replaced
	 * @param newValue
	 *            the new value of the said variable
	 * @return a new object with the variable replaced with the new value
	 * @see Var_Base#evaluate(String, Var_Base)
	 * @throws AlgebraicRuntimeException
	 * @throws AlgebraicSimplifyingException
	 * @since 1.1
	 */
	public AlgebraicExpression evaluate(String name, Var_Base newValue)
			throws AlgebraicRuntimeException, AlgebraicSimplifyingException {
		List<Var_Component> newVars = new ArrayList<Var_Component>();
		for (Var_Component var_Component : vars) {
			newVars.add(new Var_Component(var_Component.getVar().evaluate(name, newValue), var_Component.index()));
		}
		return new AlgebraicExpression(newVars, ops);
	}

	public AlgebraicExpression evaluate(AlgebraicVariablesValues values)
			throws AlgebraicRuntimeException, AlgebraicSimplifyingException {
		AlgebraicExpression temp = copy();
		for (VarValue v : values.varValues) {
			temp = temp.evaluate(v.name, v.value);
		}
		return temp;
	}

	public AlgebraicExpression evaluate() throws AlgebraicRuntimeException, AlgebraicSimplifyingException {
		return evaluate(reference.varValues);
	}
	
	/**
	 * Completly simplifies the expression and returns it
	 * <p>
	 * Cycles though the operations and use the Op_OperationType#use(Var_Base,
	 * Var_Base)
	 * 
	 * @return the simplified expression
	 * @throws AlgebraicSimplifyingException
	 * @throws AlgebraicRuntimeException
	 * @see Op_Operation#use(Var_Base, Var_Base)
	 * @since 1.0
	 */
	public AlgebraicExpression simplify() throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		return new AlgebraicExpressionSimplifier() {
			@Override
			public AlgebraicExpression simplify(AlgebraicExpression e)
					throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
				List<Var_Component> list = new ArrayList<Var_Component>();
				// e.vars.parallelStream().forEach(new solveAction<Var_Component>() {
				// @Override
				// public void accept(Var_Component v) throws AlgebraicSimplifyingException,
				// AlgebraicRuntimeException {
				// list.add(new Var_Component(v.getVar().simplify(), v.index()));
				// }
				// });
				// int index=0;
				// for (Var_Component var : list) {
				// var.setIndex(index);
				// index+=2;
				// }
				for (Var_Component var_Component : e.vars) {
					list.add(new Var_Component(var_Component.getVar().simplify(), var_Component.index()));
				}

				AlgebraicExpression expr = new AlgebraicExpression(list, ops);
				boolean used = false;
				for (int i = highestPriority(); i >= 0; i--) {
					for (Op_Component op : expr.ops) {
						if (!used) {
							if (op.operation.priorityLevel() == i) {
								expr = expr.replaceComponents(op.index() - 1, op.index() + 1,
										new Var_Component(op.operation.use(expr.getVar(op.index() - 1).getVar(),
												expr.getVar(op.index() + 1).getVar()), 0));
								used = true;
							}
						} else {
							return expr.simplify();
						}
					}
				}
				expr = expr.integrateSequences();

				list = expr.vars;
				for (Var_Component var_Component : list) {
					if (Op_Operation.isVar_VariableSubClass(var_Component.getVar())) {
						Var_Variable var = (Var_Variable) var_Component.getVar();
						if (var.mult == 0) {
							if (var_Component.index() != 0) {
								List<AlgebraicComponent> temp = expr.toComponentList();
								temp.remove(var_Component.index() - 1);
								temp.remove(var_Component.index() - 1);
								return new AlgebraicExpression(temp).simplify();
							}
						} else {
							list.set(list.indexOf(var_Component),
									new Var_Component(var.simplifyVariable(), var_Component.index()));
						}
					}
				}
				return expr;
			}

			private int highestPriority() {
				int max = 0;
				for (Op_Component op_Component : ops) {
					if (max < op_Component.operation.priorityLevel()) {
						max = op_Component.operation.priorityLevel();
					}
				}
				return max;
			}
		}.simplify(this);
	}

	/**
	 * Removes all sequences and integrates them into the expression
	 * 
	 * @return
	 * @throws AlgebraicSimplifyingException
	 * @throws AlgebraicRuntimeException
	 */
	public AlgebraicExpression integrateSequences() throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		AlgebraicExpression newExpr = copy();
		// for (Op_Component op : newExpr.ops) {
		// if (op.operation.priorityLevel() != 0) {
		// throw new AlgebraicSimplifyingException(
		// "there cannot be any non addition or substraction operations left in
		// expression");
		// }
		// }
		for (Var_Component var : newExpr.vars) {
			if (var.getVar().getClass().toString().contains("Var_Sequence")) {
				newExpr = newExpr.replaceComponents(var.index(), var.index(),
						((Var_Sequence) var.getVar()).toComponentList());
			}
		}
		for (Var_Component var : newExpr.vars) {
			if (var.getVar().getClass().toString().contains("Var_Sequence")) {
				return newExpr.integrateSequences();
			}
		}
		return newExpr;
	}

	/**
	 * Add a variable to the expression
	 * 
	 * <p>
	 * 
	 * add a Op_Component with a addition object and the variable to this object
	 * 
	 * @param variable
	 * @return a new Algebraic expression object with the modification brought upon
	 *         it
	 * @since 1.2
	 */
	public AlgebraicExpression add(Var_Base variable) {
		List<Var_Component> var = copy().vars;
		List<Op_Component> op = copy().ops;
		op.add(new Op_Component(new Op_Addition(), length));
		var.add(new Var_Component(variable, length + 1));
		return new AlgebraicExpression(var, op);
	}

	/**
	 * Substract a variable to the expression
	 * 
	 * <p>
	 * 
	 * add a Op_Component with a substraction object and the variable to this object
	 * 
	 * @param variable
	 * @return a new Algebraic expression object with the modification brought upon
	 *         it
	 * @since 1.2
	 */
	public AlgebraicExpression substract(Var_Base variable) {
		List<Var_Component> var = copy().vars;
		List<Op_Component> op = copy().ops;
		op.add(new Op_Component(new Op_Substraction(), length));
		var.add(new Var_Component(variable, length + 1));
		return new AlgebraicExpression(var, op);
	}

	/**
	 * Multiply the last variable with a entered variable
	 * 
	 * <p>
	 * 
	 * add a Op_Component with a multiplication object and the variable to this
	 * object
	 * 
	 * @param variable
	 * @return a new Algebraic expression object with the modification brought upon
	 *         it
	 * @since 1.2
	 */
	public AlgebraicExpression multiply(Var_Base variable) {
		List<Var_Component> var = copy().vars;
		List<Op_Component> op = copy().ops;
		op.add(new Op_Component(new Op_Multiplication(), length));
		var.add(new Var_Component(variable, length + 1));
		return new AlgebraicExpression(var, op);
	}

	/**
	 * Multiply the last variable with a entered variable
	 * 
	 * <p>
	 * 
	 * add a Op_Component with a division object and the variable to this object
	 * 
	 * @param variable
	 * @return a new Algebraic expression object with the modification brought upon
	 *         it
	 * @since 1.2
	 */
	public AlgebraicExpression divide(Var_Base variable) {
		List<Var_Component> var = copy().vars;
		List<Op_Component> op = copy().ops;
		op.add(new Op_Component(new Op_Division(), length));
		var.add(new Var_Component(variable, length + 1));
		return new AlgebraicExpression(var, op);
	}

	public AlgebraicExpression sort() throws AlgebraicRuntimeException, AlgebraicSimplifyingException {
		List<Var_Component> newVars = copy().vars;
		for (Op_Component op_Component : ops) {
			if (op_Component.operation.priorityLevel() != 0) {
				throw new AlgebraicRuntimeException(
						"There cannot be any operation with a priority level different than 0");
			} else if (op_Component.operation.equals(new Op_Substraction())) {
				vars.set(vars.indexOf(toComponentList().get(op_Component.index() + 1)), new Var_Component(
						new Op_Multiplication().use(new Var_Variable(-1), getVar(op_Component.index() + 1).getVar()),
						op_Component.index() + 1));
			}
		}
		AlgebraicExpression result = null;
		int max = 0;
		for (Var_Component var_Component : newVars) {
			if (var_Component.getVar().getDegree() > max) {
				max = (int) var_Component.getVar().getDegree();
			}
		}

		for (int i = max; i >= 0; i--) {
			for (Var_Component var_Component : newVars) {
				if (var_Component.getVar().getDegree() == i) {
					if (result == null) {
						result = new AlgebraicExpression(var_Component.getVar());
					} else {
						result = result.add(var_Component.getVar());
					}
				}
			}
		}
		return result;
	}
}

interface solveAction<T> extends Consumer<T> {
	public void accept(Var_Component v) throws AlgebraicSimplifyingException, AlgebraicRuntimeException;
}