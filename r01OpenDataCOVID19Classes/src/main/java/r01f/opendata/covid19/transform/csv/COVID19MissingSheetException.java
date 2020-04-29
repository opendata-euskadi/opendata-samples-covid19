package r01f.opendata.covid19.transform.csv;

public class COVID19MissingSheetException extends RuntimeException{
	
	private static final long serialVersionUID = -4766803196742230497L;

	public COVID19MissingSheetException(String msg) {
         super(msg);
    }

    public COVID19MissingSheetException(Exception e) {
    	super(e);
    }

    public COVID19MissingSheetException(String msg, Exception e) {
    	super(msg, e);
    }
	
}
