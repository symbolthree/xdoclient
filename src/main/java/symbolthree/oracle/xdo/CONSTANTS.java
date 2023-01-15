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

import java.io.File;

public interface CONSTANTS {

	public static final String PROGRAM_TITLE = "XDO Client 2.0";
	
	public static final String[] APPS_LANGUAGES = {
        "AMERICAN", "ALBANIAN", "ARABIC", "BRAZILIAN PORTUGUESE", "BULGARIAN", "CANADIAN FRENCH", "CATALAN", "CROATIAN",
        "CZECH", "DANISH", "DUTCH", "EGYPTIAN", "ENGLISH", "FINNISH", "FRENCH", "GERMAN", "GREEK", "HEBREW",
        "HUNGARIAN", "ICELANDIC", "INDONESIAN", "ITALIAN", "JAPANESE", "KOREAN", "LATIN AMERICAN SPANISH", "LITHUANIAN", "NORWEGIAN",
        "POLISH", "PORTUGUESE", "ROMANIAN", "RUSSIAN", "SIMPLIFIED CHINESE", "SLOVAK", "SLOVENIAN", "SPANISH",
        "SWEDISH", "THAI", "TRADITIONAL CHINESE", "TURKISH", "UKRAINIAN", "VIETNAMESE"};
    
    public static final String LINE_SEP          = System.getProperty("line.separator");
    public static final String FILE_SEP          = File.separator;
    
    //public static final String XDOCLIENT_PROGRAM_CFG = "/XDOClient.xcfg";
    //public static final String XDOCLIENT_PROGRAM_CFG_DIR  = System.getProperty("user.home") + FILE_SEP + "symbolthree" + FILE_SEP + "xdoclient";
    public static final String XDOCLIENT_PROGRAM_CFG_DIR  = System.getProperty("user.dir");    
    public static final String XDOCLIENT_PROGRAM_CFG  = "XDOClient.xcfg";
    public static final String XDOCLIENT_USER_CFG     = "symbolthree.oracle.xdo.config";
    public static final String XDOCLIENT_USER_CFG_EXT = "xdoc";
    
    public static final String[] LOOK_AND_FEELS  = { 
    	    "JGoodiesWindow:com.jgoodies.looks.windows.WindowsLookAndFeel",
    	    "JGoodiesPlastic:com.jgoodies.looks.plastic.PlasticLookAndFeel",
    	    "JGoodiesPlastic3D:com.jgoodies.looks.plastic.Plastic3DLookAndFeel",
    	    "JGoodiesPlasticXP:com.jgoodies.looks.plastic.PlasticXPLookAndFeel",
            "GTKLookAndFeel:com.sun.java.swing.plaf.gtk.GTKLookAndFeel",    	    
            "MetalLookAndFeel:javax.swing.plaf.metal.MetalLookAndFeel",
            "MotifLookAndFeel:com.sun.java.swing.plaf.motif.MotifLookAndFeel",
            "WindowsLookAndFeel:com.sun.java.swing.plaf.windows.WindowsLookAndFeel",
            "WindowsClassicLookAndFeel:com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel",
            "NimbusLookAndFeel:com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"};

    public static final String[] DOC_TEMPLATE_FORMAT = {"RTF:RTF","XLS:XLS", "PDF Forms:PDF", "RTF (eText):ETEXT"}; 
    
    public static final String[] DOC_OUTPUT_FORMAT   = {"PDF:PDF", "RTF:RTF", "HTML:HTML", "Excel:XLS", "eText:TXT"};
    
    public static final String[] FILENAME_PATTERN    = {"Timestamp","UUID","User-Defined"};
    
    public static final String[] PROGRAM_LANGUAGE  = { "English:US", 
                                                       "繁體中文:ZHT" };
    public static int            LOG_ERROR             = 3;
    public static int            LOG_WARN              = 2;    
    public static int            LOG_INFO              = 1;
    public static int            LOG_DEBUG             = 0;    
    public static String         LOG_OUTPUT_FILE       = "FILE";
    public static String         LOG_OUTPUT_FILENAME   = "XDOClient.log";
    public static String         LOG_OUTPUT_SYSTEM_OUT = "CONSOLE";
    
    public static String         ICON_PATH             = "/symbolthree/oracle/xdo/icon/";

 }
