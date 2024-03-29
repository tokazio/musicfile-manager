/*
 * Entagged Audio Tag library
 * Copyright (c) 2003-2005 Rapha�l Slinckx <raphael@slinckx.net>
 * Copyright (c) 2004-2005 Christian Laireiter <liree@web.de>
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
package entagged.audioformats.ogg.util;

import entagged.audioformats.*;
import entagged.audioformats.ogg.*;
import entagged.audioformats.exceptions.*;

import java.io.*;

public class VorbisTagReader {
	
	private OggTagReader oggTagReader = new OggTagReader();
	
	public Tag read( RandomAccessFile raf ) throws CannotReadException, IOException {
		long oldPos = 0;
		//----------------------------------------------------------
		
		//Check wheter we have an ogg stream---------------
		raf.seek( 0 );
		byte[] b = new byte[4];
		raf.read(b);
		
		String ogg = new String(b);
		if( !ogg.equals("OggS") )
			throw new CannotReadException("OggS Header could not be found, not an ogg stream");
		//--------------------------------------------------
		
		//Parse the tag ------------------------------------
		raf.seek( 0 );

		//Supposing 1st page = codec infos
		//			2nd page = comment+decode info
		//...Extracting 2nd page
		
		//1st page to get the length
		b = new byte[4];
		oldPos = raf.getFilePointer();
		raf.seek(26);
		int pageSegments = raf.readByte()&0xFF; //unsigned
		raf.seek(oldPos);
		
		b = new byte[27 + pageSegments];
		raf.read( b );

		OggPageHeader pageHeader = new OggPageHeader( b );

		raf.seek( raf.getFilePointer() + pageHeader.getPageLength() );

		//2nd page extraction
		oldPos = raf.getFilePointer();
		raf.seek(raf.getFilePointer() + 26);
		pageSegments = raf.readByte()&0xFF; //unsigned
		raf.seek(oldPos);
		
		b = new byte[27 + pageSegments];
		raf.read( b );
		pageHeader = new OggPageHeader( b );

		b = new byte[7];
		raf.read( b );
		
		String vorbis = new String(b, 1, 6);
		if(b[0] != 3 || !vorbis.equals("vorbis"))
			throw new CannotReadException("Cannot find comment block (no vorbis header)");

		//Begin tag reading
		OggTag tag = oggTagReader.read(raf);
		
		byte isValid = raf.readByte();
		if ( isValid == 0 )
			throw new CannotReadException("Error: The OGG Stream isn't valid, could not extract the tag");
		
		return tag;
	}
}

