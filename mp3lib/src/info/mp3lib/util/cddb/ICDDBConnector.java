package info.mp3lib.util.cddb;

public interface ICDDBConnector extends com.antelmann.cddb.CDDB {
	
	public void connect();
	
	public void setParameters();
	
	public Object query();
	
	public void close();
	
}
