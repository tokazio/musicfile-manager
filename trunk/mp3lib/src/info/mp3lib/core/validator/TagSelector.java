package info.mp3lib.core.validator;

import info.mp3lib.util.cddb.ITagQueryResult;

public interface TagSelector {

	/**
	 * Retrieves the best tag query result matching the giving context and populate the given
	 * result with it
	 * @param context The context from witch selecting the best query
	 * @param result the result to populate, will be null if no query match the given context
	 * @return an IQV denoting the level of match between the query result and the context
	 */
	public int selectTagsAlbum(final Context context, ITagQueryResult result);
}
