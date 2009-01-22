package info.mp3lib.util.string.test;

import info.mp3lib.util.string.CompiledStringMatcher;

/**
 * stub implementation of CompiledStringMatcher interface
 * @author Gab
 */
@Deprecated
public class CompiledStringMatcherDummy implements CompiledStringMatcher {

	private String pattern;
	
	public CompiledStringMatcherDummy(String pattern) {
		this.pattern = pattern;
	}
	
	@Override
	public boolean match(String str) {
		return new StringMatcherDummy().match(pattern, str);
	}

}
