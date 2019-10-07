package algebra;
/**
 * 
 * @author AntoineChevalier
 *@since 1.0
 */
public final class Var_Component extends AlgebraicComponent {
	private final Var_Base var;
	/**
	 * a basic constructor method
	 * @param var the varBase
	 * @param index the index
	 */
	public Var_Component(Var_Base var,int index) {
		super(index);
		this.var=var;
	}

	public Var_Base getVar_Base() {
		return var;
	}
	
	public Var_Base getVar() {
		return getVar_Base();
	}
	
	public String toString() {
		return var.toString();
	}
	
	public boolean equals(Object object) {
		try {
			Var_Component component=(Var_Component)object;
			return component.var.equals(var)&&component.index()==index();
		} catch (ClassCastException e) {
			return false;
		}
	}
}
