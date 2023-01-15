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

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.swing.UIManager;

import org.apache.commons.i18n.MessageManager;
import org.apache.commons.i18n.XMLMessageProvider;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.jdom2.Comment;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

import com.jgoodies.looks.FontPolicies;
import com.jgoodies.looks.FontPolicy;
import com.jgoodies.looks.FontSets;
import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.looks.windows.WindowsLookAndFeel;

import oracle.apps.fnd.security.Security;

public class Config implements CONSTANTS {

    private static Config cfg = null;
    public static final int ENCRYPTED_PASSWORD_LENGTH = 100;
    public static String    PASSWORD_SEED             = "symbolthree.com";
    final static private    SimpleDateFormat timeFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
    
    private Properties      prop = new Properties();    
    private Locale          locale;
    
    public Config() {
     boolean loaded = false;
      try {
    	  // program config
    	  Properties progCfg = new Properties();
    	  try {
        	FileInputStream is = new FileInputStream(new File(XDOCLIENT_PROGRAM_CFG_DIR, XDOCLIENT_PROGRAM_CFG));
      	    progCfg.loadFromXML(is);
    	    is.close();
    	    prop.putAll(progCfg);
    	    loaded = true;
    	    System.out.println("Program Config file " + XDOCLIENT_PROGRAM_CFG + " loaded.");    	    
    	  } catch (Exception e) {
    		  System.out.println("Use default program config");
    	  }
    	  
	      String cfgFilerStr = System.getProperty(XDOCLIENT_USER_CFG);
	      if (cfgFilerStr!=null) {
	    	  File cfgFile = new File(cfgFilerStr);
	          if (cfgFile.exists()) {
	            FileInputStream fis = new FileInputStream(cfgFile);
	            Properties userCfg = new Properties();
	            userCfg.loadFromXML(fis);
	    	    System.out.println("User Config file " + cfgFile.getAbsolutePath() + " loaded.");
	            fis.close();
	      	    prop.putAll(userCfg);            
	          }
	      }

      } catch (Exception e) {
    	  e.printStackTrace();
      }
      if (!loaded) defaultConfig();
      
      // i18n 
      InputStream fis = this.getClass().getResourceAsStream("/symbolthree/oracle/xdo/I18N.xml");
      XMLMessageProvider.install("string", fis);
      String lang = this.getStr(SETTINGS.LANG);
      if (lang.equals("ZHT")) {
          locale = new Locale("zh", "TW");
      } else if (lang.equals("US")) {
          locale = new Locale("en", "US");
      } else {
          locale = new Locale("en", "US");
	  }

      // XDO.cfg
      File xdoCfg = new File(XDOCLIENT_PROGRAM_CFG_DIR, "XDO.cfg");
      //File xdoCfgTemplate = new File(System.getProperty("user.dir"), "XDO.cfg");
      InputStream xdoCfgTemplate = this.getClass().getResourceAsStream("/XDO.cfg");
      if (!xdoCfg.exists()) {
   	    try {
   	      File tmpDir = new File(this.getStr(SETTINGS.TEMP_DIR));	
          SAXBuilder builder = new SAXBuilder();
          Document doc = null;    	
    	  XPathFactory xpfac = XPathFactory.instance();
    	  Element ele = null;
		  doc = builder.build(xdoCfgTemplate);
		  Namespace ns = Namespace.getNamespace("xdo", "http://xmlns.oracle.com/oxp/config/");
		  XPathExpression<Element> xp = null;
          xp  = xpfac.compile("/xdo:config/xdo:properties/xdo:property[@name='system-temp-dir']", Filters.element(), null, ns);
          ele = xp.evaluateFirst(doc);
          ele.setText(tmpDir.getAbsolutePath());
          xp  = xpfac.compile("/xdo:config/xdo:properties/xdo:property[@name='html-image-dir']", Filters.element(), null, ns);
          ele = xp.evaluateFirst(doc);
          ele.setText(tmpDir.getAbsolutePath());
          xp  = xpfac.compile("/xdo:config/xdo:properties/xdo:property[@name='html-image-base-uri']", Filters.element(), null, ns);
          ele = xp.evaluateFirst(doc);
	      String uri = "file:///" + tmpDir.getAbsolutePath().replace('\\', '/');
	      if (! uri.endsWith("/")) uri = uri + "/";          
          ele.setText(uri);
          XMLOutputter xmlOutput = new XMLOutputter();  
          xmlOutput.setFormat(Format.getPrettyFormat());
          xmlOutput.output(doc, new FileOutputStream(xdoCfg));          
          
   	    } catch (Exception e) {
   	    	e.printStackTrace();
   	    }
      }
    }

    public static Config instance() {
        if (cfg == null) {
        	cfg = new Config();
        }
        return cfg;
    }
    
    public boolean loadConfig(File cfgFile) {
    	try {
          FileInputStream is = new FileInputStream(cfgFile);
          Properties userCfg = new Properties();
          userCfg.loadFromXML(is);
  	      Logger.log(LOG_INFO, "Config file " + cfgFile.getAbsolutePath() + " loaded.");
          is.close();
          prop.putAll(userCfg);
          System.setProperty(XDOCLIENT_USER_CFG, cfgFile.getAbsolutePath());
          return true;
    	} catch (Exception e) {
    		Logger.logError(e);
    		return false;
    	}
    }
    
    public int getInt(String key) {
        String _str = prop.getProperty(key);

        if (_str == null) {
            return 0;
        } else {
            return Integer.valueOf(_str).intValue();
        }
    }

    public String getPassword(String key) {
        String _str = getStr(key);
        //Logger.log(LOG_DEBUG, "key=" + " value=" + _str);
        if ((_str != null) && _str.length() == ENCRYPTED_PASSWORD_LENGTH && _str.startsWith("ZG")) {
            return decryptPwd(_str);
        } else {
            return _str;
        }
    }

    public String getStr(String key) {
        String _str = prop.getProperty(key);

        if (_str == null) {
            return "";
        } else {
            return _str;
        }
    }

    public boolean getBoolean(String key) {
    	if (prop.getProperty(key)==null) return false;
        return Boolean.valueOf(prop.getProperty(key)).booleanValue();
    }

    public void setPwd(String key, char[] pwd) {
    	String pwdStr = new String(pwd);
    	
        if (pwdStr == null || pwd.equals("")) {
        	prop.setProperty(key, "");
        } else if (pwdStr.length()==ENCRYPTED_PASSWORD_LENGTH && pwdStr.startsWith("ZG")) {
        	prop.setProperty(key, pwdStr);
        } else {       
            prop.setProperty(key, encryptPwd(pwdStr));
        }
    }
    
    public void set(String key, String value) {
        if ((value == null) || (value.trim().length() == 0)) {
            prop.setProperty(key, "");
        } else {
            prop.setProperty(key, value.trim());
        }
    }    
    
    public void set(String key, boolean value) {
         prop.setProperty(key, String.valueOf(value));
    }

    public static String encryptPwd(String str) {
        if ((str == null) || (str.length() == 0)) {
            return "";
        }
        Security sec = new Security();

        return sec.f(PASSWORD_SEED, ENCRYPTED_PASSWORD_LENGTH, str);
    }

    public static String decryptPwd(String hash) {
    	Security sec = new Security();
        return sec.k(PASSWORD_SEED, hash);
    }    
    
    public void saveFile() {
    	saveProgamCfgFile();    	
    	saveFile(new File(System.getProperty(XDOCLIENT_USER_CFG)), "USER");
    }
    
    public void saveProgamCfgFile() {
    	saveFile(new File(XDOCLIENT_PROGRAM_CFG_DIR, XDOCLIENT_PROGRAM_CFG), "PROGRAM");    	
    }
    
    public void saveFile(File cfgFile, String cfgType) {
        //File cfgFile = new File(XDOCLIENT_CFG);
        Document doc = new Document();
        doc.setProperty("version", "1.0");
        doc.setProperty("encoding", "UTF-8");
        doc.setDocType(new DocType("properties", "http://java.sun.com/dtd/properties.dtd"));
        
        Element root = new Element("properties");
        String comment = "Saved on " + timeFormat.format(new java.util.Date()) + " by " + System.getProperty("user.name");
        Element comm = new Element("comment").addContent(comment);
        root.addContent(comm);
        
        Enumeration<Object> e = prop.keys();
        List<String> al = new ArrayList<String>();
        while (e.hasMoreElements()) {
        	String key = (String)e.nextElement();
        	if (cfgType.equals("USER")) {
        	  if (! key.startsWith("settings.")) {	
        	    al.add(key);
        	  }
        	} else if (cfgType.equals("PROGRAM")) {
          	  if (key.startsWith("settings.")) {	
          	    al.add(key);
          	  }
        	}
        }
        Collections.sort(al);
        //Collections.reverse(al);
        
        String lastKey = "";
        for (int i=0;i<al.size();i++) {
        	Element ele = new Element("entry");
        	String key = al.get(i);
        	String val = prop.getProperty(key);

        	// add comment to separate setting group
        	if (! lastKey.split("\\.")[0].equals(key.split("\\.")[0])) {
        		root.addContent(new Comment(key.split("\\.")[0].toUpperCase().replace("", " ")));
        	}
    	    ele.setAttribute("key", key);
    	    ele.setText(val);
    	    root.addContent(ele);
    	    lastKey = key;
        }
        doc.addContent(root);
        
        try {
        	File f = new File (XDOCLIENT_PROGRAM_CFG_DIR);
       		if (!f.exists()) f.mkdirs();
	        FileOutputStream fos = new FileOutputStream(cfgFile);
	        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
	        outputter.output(doc, fos);
	        fos.close();
        } catch (Exception ex) {
        	Logger.logError(ex);
        }
    }
    
    public String LOV(String[] valuePair, Object obj, boolean forwardLookup) {
    	return LOV(valuePair, obj.toString(), forwardLookup);
    }
    
    public String LOV(String[] valuePair, String str, boolean forwardLookup) {
    	Hashtable<String, String> ht = new Hashtable<String, String>();
    	for (int i=0;i<valuePair.length;i++) {
    		String key;
    		String val;
    		if (forwardLookup) {
    			key = valuePair[i].split(":")[0];
    			val = valuePair[i].split(":")[1];
    		} else {	
    			key = valuePair[i].split(":")[1];
    			val = valuePair[i].split(":")[0];
    		}
			ht.put(key, val);    		
    	}
    	String val = ht.get(str);
    	return val==null?"":val;
    }
    
    public static Hashtable<String, String> templateParamVal(String input) {
      StringTokenizer st = new StringTokenizer(input, "|");
	  Hashtable<String, String> ht = new Hashtable<String, String>();      
      while (st.hasMoreElements()) {
    	  String token = st.nextToken();
    	  String[] pair = token.split(":");
    	  String param = token.split(":")[0];
    	  String val="";
    	  if (pair.length > 1) {
    	     val = token.split(":")[1];
    	  }
    	  ht.put(param, val);
    	  Logger.log(Config.class, LOG_DEBUG, "Setting parameter: " + param + "=" + val); 
      }
      return ht;
    }
    
    public void defaultConfig() {
  	  prop.setProperty(SETTINGS.LOG_LEVEL, "INFO");
  	  prop.setProperty(SETTINGS.LOG_OUTPUT, "CONSOLE");
  	  prop.setProperty(SETTINGS.TEMP_DIR, XDOCLIENT_PROGRAM_CFG_DIR + FILE_SEP + "tmp");
  	  prop.setProperty(SETTINGS.LANG, "US");
  	  prop.setProperty(TEMPLATE.XML$OUTPUT_DIR, XDOCLIENT_PROGRAM_CFG_DIR + FILE_SEP + "output");
   	  prop.setProperty(TEMPLATE.DOC$OUTPUT_DIR, XDOCLIENT_PROGRAM_CFG_DIR + FILE_SEP + "output");
   	  // create default directories if not present
   	  try {
        File f = new File(XDOCLIENT_PROGRAM_CFG_DIR, "tmp");
        if (! f.exists()) f.mkdirs();
        f = new File(XDOCLIENT_PROGRAM_CFG_DIR, "output");
        if (! f.exists()) f.mkdirs();
   	  } catch (Exception e) {
   		  Logger.logError(e);
   	  }
    }
    
    public void setFontDir() {
    	String xdoFontDir = System.getProperty("XDO_FONT_DIR"); 
    	if (xdoFontDir==null || xdoFontDir.equals("")) {
    		System.setProperty("XDO_FONT_DIR", System.getProperty("user.dir") + File.separator + "fonts");
    	}
    }
    
    public void setLNF() {
	    try {
	    	String lnf = this.getStr(SETTINGS.LNF);
			/** JGoodies fix for unicode characters **/
			if (lnf.startsWith("com.jgoodies")) {
				FontPolicy policy = FontPolicies.createFixedPolicy(FontSets.createDefaultFontSet(Font.decode("arial unicode MS 12")));
		        if (lnf.indexOf("WindowsLookAndFeel") > 0) WindowsLookAndFeel.setFontPolicy(policy);
		        if (lnf.indexOf("PlasticLookAndFeel") > 0) PlasticLookAndFeel.setFontPolicy(policy);
		        if (lnf.indexOf("PlasticXPLookAndFeel") > 0) PlasticXPLookAndFeel.setFontPolicy(policy);
		        if (lnf.indexOf("Plastic3DLookAndFeel") > 0) Plastic3DLookAndFeel.setFontPolicy(policy);
			}
			UIManager.setLookAndFeel(lnf);
		} catch (Exception e) {
			String lnf = UIManager.getSystemLookAndFeelClassName();
			this.set(SETTINGS.LNF, lnf);
			Logger.log(LOG_INFO, "Use default LnF: " + UIManager.getSystemLookAndFeelClassName());
	        try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e1) {
				// do nothing 
			}
		}
    }
    
    // I18N code
    private String getText(String msgKey, Object[] subStrs) {
        try {
            StringTokenizer st   = new StringTokenizer(msgKey, ".");
            String          name = st.nextToken();
            String          key  = st.nextToken();

            if ((msgKey == null) || msgKey.equals("")) {
                return "";
            }

            String rtnStr = MessageManager.getText(name, key, subStrs, locale);

            if ((rtnStr == null) || rtnStr.equals("")) {
                rtnStr = MessageManager.getText(name, key, subStrs, Locale.US);
            }

            if ((rtnStr == null) || rtnStr.equals("")) {
                rtnStr = msgKey;
            }
            
            return rtnStr;

        } catch (Exception e) {
            return msgKey;
        }
    }

    public String getText(String msgKey, String subStr1, String subStr2) {
        return getText(msgKey, new Object[] { subStr1, subStr2 });
    }

    public String getText(String msgKey, String substr1) {
        return getText(msgKey, new Object[] { substr1 });
    }

    public String getText(String msgKey) {
        return getText(msgKey, new Object[0]);
    }
}
