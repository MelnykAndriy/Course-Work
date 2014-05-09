package translator.exc;

public class NoSuchAtomException extends Exception {
	private static final long serialVersionUID = 1901949044729079178L;
	public String tokenWhereFound;
	
	public NoSuchAtomException(String tokenWhereFound) {
		this.tokenWhereFound = tokenWhereFound;
	}
		
}
