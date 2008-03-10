package info.mp3lib.core.types;

import java.io.File;

import org.w3c.dom.Node;

import entagged.audioformats.Tag;

public abstract class AbstractMusicData implements IMusicData {

	protected Tag tag = null;
	protected File file = null;
	protected Node node = null;

	public AbstractMusicData(File file){
		this.file = file;
	}
	
	public AbstractMusicData(Tag tag) {
		this.tag = tag;
	}
	
	public AbstractMusicData(File file, Tag tag) {
		this.file = file;
		this.tag = tag;
	}
	
	@Override
	public String getAbsolutePath() {
		return file.getAbsolutePath();
	}

	@Override
	public File getFile() {
		return file;
	}

	@Override
	public String getFileName() {
		return file.getName();
	}

	@Override
	public long getFileSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Node getNode() {
		// TODO Auto-generated method stub
		return node;
	}

	@Override
	public Tag getTag() {
		return tag;
	}
	
}
