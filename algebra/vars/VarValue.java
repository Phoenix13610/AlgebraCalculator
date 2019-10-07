package algebra.vars;

import java.util.regex.Pattern;

import algebra.AlgebraicParsingException;
import algebra.AlgebraicRuntimeException;
import algebra.AlgebraicSimplifyingException;
import algebra.ComponentReference;
import algebra.Var_Base;

/**
 * A shortcut for the {@link Var_Base#evaluate(String, Var_Base)}
 * 
 * @author AntoineChevalier
 * @since 1.3.0.1
 */
public final class VarValue {
	public final String name;
	public final Var_Base value;

	public VarValue(String name, Var_Base value) {
		this.name = name;
		this.value = value;
	}

	public final Var_Base use(Var_Base replacable) throws AlgebraicRuntimeException, AlgebraicSimplifyingException {
		return replacable.evaluate(name, value);
	}

	public static VarValue parse(String value, ComponentReference reference)
			throws AlgebraicParsingException, AlgebraicRuntimeException {
		value = value.trim();
		if (value.contains("=")) {
			String[] components = value.split("=");
			if (components.length != 2 || Pattern.matches("^.*[1-9].*$", components[0])) {
				throw new AlgebraicParsingException("This is not a varible value : " + value);
			} else {
				return new VarValue(components[0], reference.generateVarComponent(components[1], 0).getVar());
			}
		} else {
			throw new AlgebraicParsingException("This is not a varible value : " + value);
		}
	}
	
	public String toString() {
		return name+"="+value;
	}
}
