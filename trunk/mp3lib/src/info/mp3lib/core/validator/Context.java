package info.mp3lib.core.validator;

public interface Context {

    public int getArtistIQV();
    
    public int getAlbumIQV();
    
    public int getTracksIQV();
    
    public String getAlbumName();
    
    public String getArtistName();
    
    public String[] getTracksName();
    
    public int[] getTracksLength();
    
    public int getTracksCount();
}
