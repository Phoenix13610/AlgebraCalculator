package algebra;

/**
 * 
 * @author AntoineChevalier
 * @since 1.0
 */
public class AlgebraicComponent {
	private int index;
	protected AlgebraicComponent(int index) {
		this.index = index;
	}
	public void setIndex(int index) {
		this.index = index;
	}

	public int index() {
		return index;
	}
}
