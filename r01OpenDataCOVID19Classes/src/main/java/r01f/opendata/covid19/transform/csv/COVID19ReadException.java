package r01f.opendata.covid19.transform.csv;

public class COVID19ReadException extends RuntimeException{
	
	private static final long serialVersionUID = 5977207355975184433L;

	public COVID19ReadException(String msg) {
         super(msg);
    }

    public COVID19ReadException(Exception e) {
    	super(e);
    }

    public COVID19ReadException(String msg, Exception e) {
    	super(msg, e);
    }
	
}
