package r01f.opendata.covid19.transform.csv;

public class COVID19CloseException extends RuntimeException{
	
	private static final long serialVersionUID = 3785494472901045814L;

	public COVID19CloseException(String msg) {
         super(msg);
    }

    public COVID19CloseException(Exception e) {
    	super(e);
    }

    public COVID19CloseException(String msg, Exception e) {
    	super(msg, e);
    }
	
}
