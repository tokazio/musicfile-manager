/*
 * Entagged Audio Tag library
 * Copyright (c) 2003-2005 Rapha�l Slinckx <raphael@slinckx.net>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *  
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package entagged.audioformats.ape;

import entagged.audioformats.*;
import entagged.audioformats.ape.util.ApeTagReader;
import entagged.audioformats.ape.util.MonkeyInfoReader;
import entagged.audioformats.exceptions.*;
import entagged.audioformats.generic.AudioFileReader;

import java.io.*;

public class MonkeyFileReader extends AudioFileReader {
	
	private ApeTagReader ape = new ApeTagReader();
	private MonkeyInfoReader ir = new MonkeyInfoReader();
	
	protected EncodingInfo getEncodingInfo( RandomAccessFile raf )  throws CannotReadException, IOException {
		return ir.read(raf);
	}
	
	protected Tag getTag( RandomAccessFile raf )  throws CannotReadException, IOException {
		return ape.read(raf);
	}
}