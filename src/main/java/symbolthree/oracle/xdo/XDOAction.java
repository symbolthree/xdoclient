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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;
import java.util.UUID;
import java.util.Vector;

import javax.swing.UIManager;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import oracle.apps.xdo.common.config.ConfigReader;
import oracle.apps.xdo.dataengine.DataProcessor;
import oracle.apps.xdo.template.EFTProcessor;
import oracle.apps.xdo.template.FOProcessor;
import oracle.apps.xdo.template.FormProcessor;
import oracle.apps.xdo.template.RTFProcessor;
import oracle.apps.xdo.template.ExcelProcessor;

import oracle.apps.xdo.batch.ReportsBatchProcessor;

public class XDOAction implements CONSTANTS{

	private final static SimpleDateFormat timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss");	
	private static Config config = Config.instance();
    private static File tmpDir;
    
    private File xmlDataFile;
    private File publishDocFile;
    private Properties xdoProperties;
    
	public XDOAction() {
		tmpDir = new File(config.getStr(SETTINGS.TEMP_DIR));		
	}

	public static void main (String[] args) {
		if (args.length !=1) {
			showUsage();
			System.exit(0);
		} else {
			File file = new File(args[0]);
			if (!file.exists()) {
				showUsage();
				System.exit(0);
			}
		}
		
		config.loadConfig(new File(args[0]));
		
		XDOAction action = new XDOAction();
		try {
		  Logger.initializeLogging();
		  action.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start() {
		try {
		  if (config.getBoolean(TEMPLATE.XML)) {
			  Logger.log(LOG_INFO, "Start creating XML Data file...");
			  DBConnection.instantiateConnection(true, true);
			  this.createXMLData();
			  Logger.log(LOG_INFO, "XML Data file created successfully.");
		  }
		  
		  if (config.getBoolean(TEMPLATE.DOC)) {
			  Logger.log(LOG_INFO, "Start creating document...");			  
			  this.createDoc();
			  Logger.log(LOG_INFO, "Document created successfully.");			  
		  }
		  
		} catch (Exception e) {
			Logger.logError(e);
		}
	}
	
	private void createXMLData() throws Exception {
		  File dataTemplate = new File(config.getStr(TEMPLATE.XML$DATA_TEMPLATE));
		  File XMLOutputDir =  new File(config.getStr(TEMPLATE.XML$OUTPUT_DIR));
		  
		  File outputXML = null;
		  if (config.getStr(TEMPLATE.XML$FILENAME_PATTERN).equals("UUID")) {
			  outputXML = new File(XMLOutputDir, UUID.randomUUID().toString() + ".xml");
		  } else if (config.getStr(TEMPLATE.XML$FILENAME_PATTERN).equals("Timestamp")) {
			  outputXML = new File(XMLOutputDir, timestamp.format(new java.util.Date()) + ".xml");
		  } else if (config.getStr(TEMPLATE.XML$FILENAME_PATTERN).equals("User-Defined")) {
			  outputXML = new File(XMLOutputDir, config.getStr(TEMPLATE.XML$FILENAME) + ".xml");
		  }

		  setXmlDataFile(outputXML);		  
		  Logger.log(LOG_INFO, "Data XML File is " + outputXML.getAbsolutePath());
		  
	      Logger.log(this, LOG_DEBUG, "Create DB Connection...");
	      Connection conn = DBConnection.getInstance().getConnection(); 

	      com.sun.java.util.collections.Hashtable ht1 = new com.sun.java.util.collections.Hashtable();	      
	      String paramVals = config.getStr(TEMPLATE.XML$PARAMETERS);
	      Hashtable<String,String> ht2 = Config.templateParamVal(paramVals);
	      Enumeration<String> em = ht2.keys();
	      while (em.hasMoreElements()) {
	    	  String key = em.nextElement();
	    	  String val = ht2.get(key);
	    	  if (!val.equals("")) { 
	    		ht1.put(key, val);
	    	    Logger.log(this, LOG_DEBUG, "Setting parameter: " + key + "=" + val);
	    	  } else {
	    		  Logger.log(this, LOG_DEBUG, "Parameter " + key + " is skipped");  
	    	  }
	      }

	      Logger.log(this, LOG_DEBUG, "Start processing...");	      
	      DataProcessor dp = new DataProcessor();
	      //dp.setScalableModeOn();
	      dp.setConnection(conn);
	      dp.setDataTemplate(dataTemplate.getAbsolutePath());
	      dp.setParameters(ht1);
	      dp.setOutput(outputXML.getAbsolutePath());
	      dp.processData();
	      
	      if (config.getBoolean(SETTINGS.PRETTY_FORMAT)) {
	        prettyFormat(outputXML);
	      }
	      Logger.log(this, LOG_DEBUG, "Processing done");
	}
	
	private void createDoc() throws Exception {
		  File xmldata = getXmlDataFile();
	      if (xmldata==null) {
	    	  xmldata = new File(config.getStr(TEMPLATE.DOC$DATA));
	    	  this.setXmlDataFile(xmldata);
	      }
	      
		  Logger.log(LOG_INFO, "XML Data is " + xmldata.getAbsolutePath());

	      String fileExt      = config.getStr(TEMPLATE.DOC$OUTPUT_FORMAT).toLowerCase();
	      String docOutputDir = config.getStr(TEMPLATE.DOC$OUTPUT_DIR);
		  File outputFile = null;
		  
		  if (config.getStr(TEMPLATE.DOC$FILENAME_PATTERN).equals("UUID")) {
			  outputFile = new File(docOutputDir, UUID.randomUUID().toString() + "." + fileExt);
		  } else if (config.getStr(TEMPLATE.DOC$FILENAME_PATTERN).equals("Timestamp")) {
			  outputFile = new File(docOutputDir, timestamp.format(new java.util.Date()) + "." + fileExt);
		  } else if (config.getStr(TEMPLATE.DOC$FILENAME_PATTERN).equals("User-defined")) {
			  outputFile = new File(docOutputDir, config.getStr(TEMPLATE.DOC$FILENAME) + "." + fileExt);
		  }
		  setPublishDocFile(outputFile);
		  Logger.log(LOG_INFO, "Doucment File is " + outputFile.getAbsolutePath());
		  
		  if (config.getStr(TEMPLATE.DOC$TEMPLATE_FORMAT).equals("RTF")) {
			  RTFProcessor();
		  }
	      
		  if (config.getStr(TEMPLATE.DOC$TEMPLATE_FORMAT).equals("PDF")) {
			  PDFFormProcessor();
		  }
		  
		  if (config.getStr(TEMPLATE.DOC$TEMPLATE_FORMAT).equals("ETEXT")) {
			  eTextProcessor();
		  }

		  if (config.getStr(TEMPLATE.DOC$TEMPLATE_FORMAT).equals("XLS")) {
			  xlsProcessor();
		  }
	}
	
	private void setXDOProperties() throws Exception {
	      //xdoProperties = ConfigReader.read(this.getClass().getResourceAsStream("/XDO.cfg"), "UTF-8");
		  FileInputStream fis = new FileInputStream(new File(XDOCLIENT_PROGRAM_CFG_DIR, "XDO.cfg"));
		  xdoProperties = ConfigReader.read(fis, "UTF-8");
		  // put session data
		  
		  
		  /*
	      xdoProperties.setProperty("system-temp-dir", tmpDir.getAbsolutePath());
	      xdoProperties.setProperty("html-image-dir", tmpDir.getAbsolutePath());
	      String uri = "file:///" + tmpDir.getAbsolutePath().replace('\\', '/');
	      if (! uri.endsWith("/")) uri = uri + "/";
	      xdoProperties.setProperty("html-image-base-uri", uri);
	      */
	}
	
	private void RTFProcessor() throws Exception {
		  File rtfTemplate  = new File(config.getStr(TEMPLATE.DOC$TEMPLATE_FILE));
	      File xslFOFile    = new File(tmpDir, UUID.randomUUID().toString() + ".xsl");
	      
	      if (!config.getBoolean(TEMPLATE.DOC$KEEP_XSLFO)) {
	        xslFOFile.deleteOnExit();
	      }
		
	      Logger.log(LOG_INFO, "Start creation XSL-FO...");
	      RTFProcessor rtfProcessor = new RTFProcessor(rtfTemplate.getAbsolutePath());
	      setXDOProperties();
	      rtfProcessor.setConfig(xdoProperties); 
	      rtfProcessor.setOutput(xslFOFile.getAbsolutePath());
	      rtfProcessor.process();
	      
	      String fileExt = config.getStr(TEMPLATE.DOC$OUTPUT_FORMAT);
	      
	      Logger.log(LOG_INFO, "Start XSL-FO to " + fileExt + " file ...");
	      
	      FOProcessor processor = new FOProcessor();
	      processor.setOutputFormat(formatByte(fileExt));
	      
	      String currentLNF = UIManager.getLookAndFeel().getClass().getName();
	      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
	      processor.setConfig(xdoProperties);
	      File xmldata = this.getXmlDataFile();
	      processor.setData(xmldata.getAbsolutePath());      // set XML input file
	      processor.setTemplate(xslFOFile.getAbsolutePath());    // set XSL input file
	      File outputFile = this.getPublishDocFile();
	      processor.setOutput(outputFile.getAbsolutePath());     // set output file
	      processor.generate();
	      UIManager.setLookAndFeel(currentLNF);
	}
	
	private void PDFFormProcessor() throws Exception {
        FormProcessor formProcessor = new FormProcessor();
        //XDOLogImpl fileLog = new XDOLogImpl();
        File xmldata = this.getXmlDataFile();
        formProcessor.setData(new FileInputStream(xmldata));
        File pdfTemplate = new File(config.getStr(TEMPLATE.DOC$TEMPLATE_FILE));
        formProcessor.setTemplate(new FileInputStream(pdfTemplate));
        FileOutputStream fos = new FileOutputStream(this.getPublishDocFile());
        formProcessor.setOutput(fos);
        //formProcessor.setLocale("en-US");
        formProcessor.process();
        fos.close();
	}
	
	private void eTextProcessor() throws Exception {
	    File rtfTemplate  = new File(config.getStr(TEMPLATE.DOC$TEMPLATE_FILE));
        File xslFOFile    = new File(tmpDir, UUID.randomUUID().toString() + ".xsl");
		
        EFTProcessor eftProc = new EFTProcessor();
        eftProc.setTemplate(rtfTemplate.getAbsolutePath());
        File xmldata = this.getXmlDataFile();        
        eftProc.setData(xmldata.getAbsolutePath());
        eftProc.setXSL(xslFOFile.getAbsolutePath());
        eftProc.setConfig(xdoProperties);
	    File outputFile = this.getPublishDocFile();	          
        eftProc.setOutput(outputFile.getAbsolutePath());
        eftProc.process();
	}
	
	private void xlsProcessor() throws Exception {
		File xlsTemplate  = new File(config.getStr(TEMPLATE.DOC$TEMPLATE_FILE));
		
		ExcelProcessor xlsProc = new ExcelProcessor();
		xlsProc.setTemplate(xlsTemplate.getAbsolutePath());
		File xmldata = this.getXmlDataFile();

		xlsProc.setData(xmldata.getAbsolutePath());
		xlsProc.setConfig(xdoProperties);
		
		File outputFile = this.getPublishDocFile();
		xlsProc.setOutput(outputFile.getAbsolutePath());
		
		xlsProc.setLocale(Locale.ENGLISH);
		
		xlsProc.process();
	}
	
	private void burstProcessor() throws Exception {
		
		File xapiFile = new File(config.getStr(TEMPLATE.DOC$BURSTING_FILE));
		File tmpDir   = new File(config.getStr(SETTINGS.TEMP_DIR));
		
		Logger.log(LOG_INFO, "bursting file is " + xapiFile.getAbsolutePath());

		ReportsBatchProcessor processor = new ReportsBatchProcessor(
				xapiFile.getAbsolutePath(), tmpDir.getAbsolutePath());
		Vector vector = processor.process();
	    
		if (vector == null || vector.size() == 0) {
	         Logger.log(LOG_INFO, "no out is generated...");
	       } else {
	         int i = vector.size();
	         for (byte b = 0; b < i; b++)
	           Logger.log(LOG_INFO, (String)vector.elementAt(b) + " is generated..."); 
	    } 
	}
	
	
	private byte formatByte(String fileExt) {
		if (fileExt.equalsIgnoreCase("pdf")) return FOProcessor.FORMAT_PDF;
		if (fileExt.equalsIgnoreCase("rtf")) return FOProcessor.FORMAT_RTF;
		if (fileExt.equalsIgnoreCase("html")) return FOProcessor.FORMAT_HTML;
		if (fileExt.equalsIgnoreCase("xls")) return FOProcessor.FORMAT_EXCEL;
		return FOProcessor.FORMAT_PDF;
	}

	private void prettyFormat(File inputXML) throws Exception {
	    File tempFile = new File(inputXML.getParent(), UUID.randomUUID().toString() + ".xml");
	    FileOutputStream fos = new FileOutputStream(tempFile);
	    SAXBuilder sb = new SAXBuilder();
	    Document doc = sb.build(inputXML);
	    XMLOutputter xout = new XMLOutputter(Format.getPrettyFormat());
	    xout.output(doc, fos);
	    fos.close();
	    String filePath = inputXML.getCanonicalPath();
	    inputXML.delete();
	    tempFile.renameTo(new File(filePath));
	}

	public File getXmlDataFile() {
		return xmlDataFile;
	}

	private void setXmlDataFile(File xmlDataFile) {
		this.xmlDataFile = xmlDataFile;
	}

	public File getPublishDocFile() {
		return publishDocFile;
	}

	private void setPublishDocFile(File publishDocFile) {
		this.publishDocFile = publishDocFile;
	}
	
	private static void showUsage() {
		System.out.println("XDOAction.bat|sh [config_file (." + XDOCLIENT_USER_CFG_EXT  + ")]");
	}
	
}
