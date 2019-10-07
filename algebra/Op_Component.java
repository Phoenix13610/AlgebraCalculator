package algebra;

/**
 * 
 * @author AntoineChevalier
 * @since 1.0
 */
public final class Op_Component extends AlgebraicComponent {
	public final Op_Operation operation;

	/**
	 * a basic constructor method
	 * 
	 * @param type
	 *            the operation type
	 * @param index
	 *            the index
	 */
	public Op_Component(Op_Operation type, int index) {
		super(index);
		this.operation = type;
	}

	/**
	 * @return the operations type
	 */
	public char getChar() {
		return operation.type;
	}

	public String toString() {
		return Character.toString(operation.type);
	}
	
	public boolean equals(Object obj) {
		try {
			Op_Component op = (Op_Component) obj;
			return op.operation.equals(operation) && op.index() == index();
		} catch (ClassCastException e) {
			return false;
		}
	}
}
