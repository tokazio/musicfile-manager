package info.mp3lib.config;

/**
 * Thrown when the configuration file is not found or can not be properly read (Fatal)
 * @author Gab
 */
public class ConfigurationException extends RuntimeException {
	
	private static final long serialVersionUID = -3649298929946420653L;
	
	public ConfigurationException(final String message) {
		super(message);
	}
}
