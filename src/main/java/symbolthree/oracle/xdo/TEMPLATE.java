/******************************************************************************
 *
 * Symbolthree XDO Client
 * Copyright (C) 2019 Christopher Ho 
 * All Rights Reserved, http://www.symbolthree.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * E-mail: Christopher.Ho@symbolthree.com
 *
 * ================================================
 *
 * $Archive: /TOOL/XDOCLIENT/src/symplik/oracle/xdo/XDOClient.java $
 * $Author: Christopher Ho $
 * $Date: 9/24/14 5:31a $
 * $Revision: 9 $
******************************************************************************/

package symbolthree.oracle.xdo;

/* String lookup for Template Panel */
public interface TEMPLATE {
	public static String XML                  = "template.xml";
	public static String XML$DATA_TEMPLATE    = "template.xml.data_template";
	public static String XML$OUTPUT_DIR       = "template.xml.output_dir";
	public static String XML$FILENAME_PATTERN = "template.xml.filename_pattern";
	public static String XML$FILENAME         = "template.xml.filename";
	public static String XML$OPEN_FILE        = "template.xml.open_file";
	public static String XML$PARAMETERS       = "template.xml.parameters";
	
	public static String DOC                   = "template.doc";
	public static String DOC$DATA              = "template.doc.data";
	public static String DOC$TEMPLATE_FORMAT   = "template.doc.template_format";
	public static String DOC$TEMPLATE_FILE     = "template.doc.template_file";
	public static String DOC$OUTPUT_DIR        = "template.doc.output_dir";
	public static String DOC$OUTPUT_FORMAT     = "template.doc.output_format";
	public static String DOC$FILENAME_PATTERN  = "template.doc.filename_pattern";
	public static String DOC$FILENAME          = "template.doc.filename"; 
	public static String DOC$OPEN_FILE         = "template.doc.open_file";
	public static String DOC$KEEP_XML          = "template.doc.keep_xml";
	public static String DOC$KEEP_XSLFO        = "template.doc.keep_xslfo";
	
	public static String DOC$BURSTING_FILE     = "template.burst.file";
	
}
