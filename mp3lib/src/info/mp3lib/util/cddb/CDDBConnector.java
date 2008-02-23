package info.mp3lib.util.cddb;

import java.io.IOException;

import com.antelmann.cddb.CDDBProtocolException;
import com.antelmann.cddb.CDDBRecord;
import com.antelmann.cddb.CDDBServer;
import com.antelmann.cddb.CDID;
import com.antelmann.cddb.CDInfo;

public class CDDBConnector extends com.antelmann.cddb.FreeDB implements ICDDBConnector  {

	public CDDBConnector(CDDBServer srv) throws IOException {
		super(srv);
		setServer(srv);
		
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object query() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParameters() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] getCategories() throws IOException, CDDBProtocolException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 *  CDDB inherited Methods
	 */
	
	@Override
	public CDDBRecord[] queryCD(CDID arg0) throws IOException,
			CDDBProtocolException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CDInfo readCDInfo(CDDBRecord arg0) throws IOException,
			CDDBProtocolException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeCDInfo(CDInfo arg0) throws IOException,
			CDDBProtocolException {
		// TODO Auto-generated method stub
		
	}

}
