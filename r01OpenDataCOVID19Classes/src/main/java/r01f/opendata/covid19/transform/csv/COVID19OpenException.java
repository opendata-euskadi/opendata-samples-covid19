package r01f.opendata.covid19.transform.csv;

public class COVID19OpenException extends RuntimeException{
	
	
	private static final long serialVersionUID = 2244067261157329098L;

	public COVID19OpenException(String msg) {
         super(msg);
    }

    public COVID19OpenException(Exception e) {
    	super(e);
    }

    public COVID19OpenException(String msg, Exception e) {
    	super(msg, e);
    }
	
}
