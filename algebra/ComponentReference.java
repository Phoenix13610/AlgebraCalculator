package algebra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import algebra.operations.Op_Addition;
import algebra.operations.Op_Division;
import algebra.operations.Op_Multiplication;
import algebra.operations.Op_Power;
import algebra.operations.Op_Substraction;
import algebra.vars.Var_Sequence;
import algebra.vars.Var_Set;
import algebra.vars.Var_Special;
import algebra.vars.Var_Variable;
import algebra.vars.sequence.Seq_AbsoluteValue;
import algebra.vars.sequence.Seq_IntegerPart;
import algebra.vars.sequence.Seq_Parenthesis;
import algebra.vars.specialVars.Pi;
import algebra.vars.specialVars.customFunction.Func_Reference;
import algebra.vars.specialVars.functions.DegCosine;
import algebra.vars.specialVars.functions.DegSinus;
import algebra.vars.specialVars.functions.DegTangent;
import algebra.vars.specialVars.functions.RadCosine;
import algebra.vars.specialVars.functions.RadSinus;
import algebra.vars.specialVars.functions.RadTangent;
import algebra.vars.specialVars.functions.Var_SquareRoot;

/**
 * 
 * @author AntoineChevalier
 * @version 1.0
 * @since 1.0
 */
public abstract class ComponentReference {
	public List<Op_Operation> possibleOps = new ArrayList<Op_Operation>();
	public List<Var_Sequence> possibleParenthesis = new ArrayList<Var_Sequence>();
	public List<Var_Base> possibleVar = new ArrayList<Var_Base>();
	public List<Var_Special> specialVar = new ArrayList<Var_Special>();
	public AlgebraicVariablesValues varValues=new AlgebraicVariablesValues();

	public ComponentReference() {
		setReference();
		Collections.unmodifiableList(possibleOps);
		Collections.unmodifiableList(possibleParenthesis);
		Collections.unmodifiableList(possibleVar);
		Collections.unmodifiableList(specialVar);
	}

	/**
	 * set the reference using the add*() functions or just add the components to
	 * either of the lists
	 */
	public abstract void setReference();
	
	/**
	 * adds addition,substraction,multiplication,division and exponentiation
	 * 
	 * @since 1.0
	 */
	protected final void addBasicOps() {
		possibleOps.add(new Op_Addition());
		possibleOps.add(new Op_Substraction());
		possibleOps.add(new Op_Multiplication());
		possibleOps.add(new Op_Division());
		possibleOps.add(new Op_Power());
	}

	/**
	 * adds parenthesis
	 * 
	 * @since 1.0
	 */
	protected final void addParenthesis() {
		possibleParenthesis.add(new Seq_Parenthesis());
		possibleParenthesis.add(new Seq_AbsoluteValue());
		possibleParenthesis.add(new Seq_IntegerPart());
	}

	/**
	 * please enter parenthesis and specials vars first
	 * Var_Variables
	 * 
	 * @since 1.0
	 */
	protected final void addBasicVars() {
		for (Var_Special v : specialVar) {
			possibleVar.add(v);
		}
		possibleVar.add(new Var_Variable());
		possibleVar.add(new Var_Set());
		for (Var_Sequence seq : possibleParenthesis) {
			possibleVar.add(seq);
		}		
		possibleVar.add(new Func_Reference());
		possibleVar.add(new Var_Sequence() {
			@Override
			public char openParenthesis() {
				return 0;
			}
			@Override
			public char closingParenthesis() {
				return 0;
			}
		});
	}

	protected final void addBasicSpecials() {
		specialVar.add(new Var_SquareRoot());
		specialVar.add(new RadCosine());
		specialVar.add(new RadSinus());
		specialVar.add(new RadTangent());
		specialVar.add(new Pi());
	}

	protected final void addBasics() {
		addBasicOps();
		addParenthesis();
		addBasicSpecials();
		addBasicVars();
	}

	protected final void setToDegrees() {
		specialVar.remove(new RadCosine());
		specialVar.remove(new RadSinus());
		specialVar.remove(new RadTangent());
		
		specialVar.add(new DegCosine());
		specialVar.add(new DegSinus());
		specialVar.add(new DegTangent());
		possibleVar=new ArrayList<Var_Base>();
		addBasicVars();
	}

	/**
	 * check if the entered char is a operation
	 * <p>
	 * check if the entered char corresponds to one of the possibleOps type char
	 * 
	 * @param c
	 *            char representing the operation
	 * @return if the char is a operation
	 * @since 1.0
	 */
	public boolean isOperation(char c) {
		for (Op_Operation op : possibleOps) {
			if (op.getType() == c) {
				return true;
			}
		}
		return false;
	}

	/**
	 * check if the entered char is a open parenthesis
	 * <p>
	 * check if the entered char corresponds to one of the openParenthesis char
	 * 
	 * @param c
	 *            char representing the parenthesis
	 * @return if the char is a open parenthesis
	 * @since 1.0
	 * @see Var_Sequence#openParenthesis()
	 */
	public boolean isOpenParenthesis(char c) {
		for (Var_Sequence v : possibleParenthesis) {
			if (v.openParenthesis() == c) {
				return true;
			}
		}
		return false;

	}

	/**
	 * check if the entered char is a closed parenthesis
	 * <p>
	 * check if the entered char corresponds to one of the closedParenthesis char
	 * 
	 * @param c
	 *            char representing the parenthesis
	 * @return if the char is a closed parenthesis
	 * @since 1.0
	 * @see Var_Sequence#closingParenthesis()
	 */
	public boolean isClosedParenthesis(char c) {
		for (Var_Sequence v : possibleParenthesis) {
			if (v.closingParenthesis() == c) {
				return true;
			}
		}
		return false;

	}

	/**
	 * Generate a regex for the operation types to be put between brackets or
	 * something, for example : \+,\-,\*,\/
	 * 
	 * @return a string regex for the operation types
	 * @since 1.0
	 */
	public String generateOpsRegex() {
		String opRegex = "";
		for (Op_Operation op : possibleOps) {
			if ((op.getType() == '+') || (op.getType() == '*') || (op.getType() == '?') || (op.getType() == '^')
					|| (op.getType() == '$') || (op.getType() == '-')) {
				opRegex += "\\" + Character.toString(op.getType());
			} else {
				opRegex += Character.toString(op.getType());
			}
		}
		return opRegex;
	}

	/**
	 * Generate a regex for the parenthesis types to be put between brackets or
	 * something, for example : \(,\)
	 * 
	 * @return a string regex for the parenthesis types
	 * @since 1.0
	 */
	public String generateParenthesisRegex() {
		String result = "";
		for (Var_Sequence seq : possibleParenthesis) {
			result += "\\" + seq.openParenthesis();
			result += "\\" + seq.closingParenthesis();
		}
		return result;
	}

	/**
	 * Generate a regex for white spaces and operation types to be put between
	 * brackets or something, for example : \s\+,\-,\*,\/
	 * 
	 * @return a string regex for the blank and operation types
	 * @since 1.0
	 */
	public String generateBlanksAndOpsRegex() {
		return "\\s" + generateOpsRegex();
	}

	/**
	 * Generate a regex for white spaces operation types and parenthesis to be put
	 * between brackets or something, for example : \s\+,\-,\*,\/
	 * 
	 * @return a string regex for the blank operation types and parenthesis
	 * @since 1.0
	 */
	public String generateBlankOpsAndParenthesisRegex() {
		return "\\s" + generateOpsRegex() + generateParenthesisRegex();
	}

	/**
	 * parse a variable
	 * 
	 * @param s
	 *            a string representing the variable
	 * @param index
	 *            the index of the var component
	 * @return the var component parsed from the string
	 * @throws AlgebraicParsingException
	 * @throws AlgebraicRuntimeException 
	 * @since 1.0
	 */
	public Var_Component generateVarComponent(String s, int index) throws AlgebraicParsingException, AlgebraicRuntimeException {
		for (Var_Base var : possibleVar) {
			if (Pattern.matches(var.getRegex(this), s)) {
				return new Var_Component(var.parseVar(s, this), index);
			}
		}
		throw new AlgebraicParsingException("Unknown variable pattern : " + s);
	}

	/**
	 * parse a operation
	 * 
	 * @param c
	 *            a char representing the operation
	 * @param index
	 *            the index of the op component
	 * @return the op component parsed from the string
	 * @throws AlgebraicParsingException
	 * @since 1.0
	 */
	public Op_Component generateOperation(char c, int index) throws AlgebraicParsingException {
		for (Op_Operation op : possibleOps) {
			if (op.getType() == c) {
				return new Op_Component(op, index);
			}
		}
		throw new AlgebraicParsingException("Unknown Operation type");
	}

	/**
	 * @return the highest priority level of all the possible ops
	 * @since 1.0
	 * @see Op_Operation#priorityLevel()
	 */
	public int highestOpPriorityLevel() {
		int result = 0;
		for (Op_Operation op : possibleOps) {
			if (op.priorityLevel() > result) {
				result = op.priorityLevel();
			}
		}
		return result;
	}

	public char correspondingParenthesis(char parenthesis) throws AlgebraicRuntimeException{
		for (Var_Sequence seq : possibleParenthesis) {
			if(seq.openParenthesis()==parenthesis) {
				return seq.closingParenthesis();
			}else if (seq.closingParenthesis()==parenthesis) {
				return seq.openParenthesis();
			}
		}
		throw new AlgebraicRuntimeException("no corresponding parenthesis");
	}
}
