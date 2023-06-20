package parksys.dao;

public class DataAccessException extends Exception {
	private static final long serialVersionUID = 1L;

	//Tratamento de erros
	
	public DataAccessException(Throwable cause) {
		super(cause);
	}
	
	public DataAccessException(String message) {
		super(message);
	}
}
