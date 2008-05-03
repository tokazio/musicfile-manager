package info.mp3lib.core.business;

import java.io.File;

public interface IBusinessPlan {
	
	/**
	 * Read some data from a file
	 */ 
	public void read(File file);
	
	/**
	 * Execute some tests to validate or not read data
	 */
	public void validate();
	
	/**
	 * Manage data if required
	 */
	public void manage();
	
	
	
//	public void write();
	
}
