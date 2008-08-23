package info.mp3lib.util.cddb;

public interface ITagQueryResult {
    
    public int compareTo(Object o);

    public String getAlbum();
    
    public String getAlbumComment();

    public String getArtist();

    public String getCategory();

    public String getDiscId();

    public String getGenre();

    public int getQuality();

    public String getTrackComment(int i);

    public int getTrackDuration(int i);

    public int getTrackNumber(int i);

    public int getTracksNumber();

    public String getTrackTitle(int i);

    public String getYear();

    public boolean isExactMatch();

    public void swapTracks(int i1, int i2);

    public String toString();

}
