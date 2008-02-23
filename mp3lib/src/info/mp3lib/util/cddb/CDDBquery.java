package info.mp3lib.util.cddb;

import entagged.freedb.Freedb;
import entagged.freedb.FreedbException;
import entagged.freedb.FreedbQueryResult;

public class CDDBquery extends entagged.freedb.Freedb {
   public static void main(String[] args) {
	   CDDBquery freedb = new CDDBquery();
}
  
	public CDDBquery() {
		Freedb freedb = new Freedb();
		String[] serv;
		try {
			serv = freedb.getAvailableServers();
			//http://www.freedb.org/freedb_search.php?words=high+tone&allfields=YES
			//FreedbQueryResult[] r = freedb.query("metallica");
			
			FreedbQueryResult[] queryResult = freedb.query("hightone");
			System.out.println("request result: "+super.read(queryResult[0]));
			/*
			if (r.length == 0) System.out.println("baad");
			for (int i = 0; i < r.length; i++) {
				System.out.println(r[i].getAlbum());
			}
//			freedb.query(album);
 * 
 */
			for (int i = 0; i < serv.length; i++)
				System.out.println(serv[i]);
		} catch (FreedbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String[] getAvailableServers() throws FreedbException {
		// TODO Auto-generated method stub
		return super.getAvailableServers();
	}

}
