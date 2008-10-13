package info.mp3lib.util.cddb;



public class DBException extends Exception {

    private static final long serialVersionUID = 5887466837720458032L;
    
    public DBException(Exception e) {
	super(e);
    }

    public DBException(String strMessage) {
	super(strMessage);
    }

}
