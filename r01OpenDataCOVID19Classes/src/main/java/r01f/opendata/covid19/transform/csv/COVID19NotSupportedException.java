package r01f.opendata.covid19.transform.csv;

public class COVID19NotSupportedException extends RuntimeException{
	
	private static final long serialVersionUID = 7867468741473929122L;

	public COVID19NotSupportedException() {
		super();
	}
	
	public COVID19NotSupportedException(String msg) {
         super(msg);
    }

    public COVID19NotSupportedException(Exception e) {
    	super(e);
    }

    public COVID19NotSupportedException(String msg, Exception e) {
    	super(msg, e);
    }
	
}
