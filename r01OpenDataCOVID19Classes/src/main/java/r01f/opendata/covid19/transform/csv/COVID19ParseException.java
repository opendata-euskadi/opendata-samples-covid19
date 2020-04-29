package r01f.opendata.covid19.transform.csv;


public class COVID19ParseException extends RuntimeException{
	
	
	private static final long serialVersionUID = -5002716866554308485L;

	public COVID19ParseException(String msg) {
         super(msg);
    }

    public COVID19ParseException(Exception e) {
    	super(e);
    }

    public COVID19ParseException(String msg, Exception e) {
    	super(msg, e);
    }
	
}
