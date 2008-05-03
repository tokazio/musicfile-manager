package info.mp3lib.core.types;

import java.io.File;

import org.w3c.dom.Node;

import entagged.audioformats.AudioFile;
import entagged.audioformats.AudioFileIO;
import entagged.audioformats.Tag;
import entagged.audioformats.exceptions.CannotReadException;
import entagged.audioformats.exceptions.CannotWriteException;

public class MusicData extends IMusicData {

	private static final long serialVersionUID = 1L;
	
	protected AudioFile file = null;
	protected Node node = null;
	
	public MusicData(File file) throws CannotReadException {
		this.file = AudioFileIO.read(file);
	}
	
	public MusicData(File file, Node node) throws CannotReadException {
		this.file = AudioFileIO.read(file);
		this.node = node;
	}
	
	/*
	protected MusicData(File file, Tag tag) throws CannotReadException {
		this.file = AudioFileIO.read(file);
		this.tag = tag;
	}
	*/
	
	@Override
	public String getAbsolutePath() {
		return file.getAbsolutePath();
	}

	@Override
	public AudioFile getFile() {
		return file;
	}

	@Override
	public String getFileName() {
		return file.getName();
	}

	@Override
	public long getFileSize() {
		return file.length();
	}

	@Override
	public Node getNode() {
		return node;
	}

	@Override
	public Tag getTag() {
		return this.file.getTag();
	}

	@Override
	public void setTagByCDDB() {
		// TODO implementation to retrieve CDDB tag info
	}

	@Override
	public void write() {
		writeXML();
	}
	
	@Override
	void writeFile() throws CannotWriteException {
		AudioFileIO.write(this.file);
	}

	@Override
	void writeXML() {
		System.out.println(this.getClass()+": writeXML() implementation to do in an inherited format..");
	}
	
}
