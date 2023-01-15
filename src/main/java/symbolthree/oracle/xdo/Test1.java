package symbolthree.oracle.xdo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import oracle.apps.xdo.common.log.Logger;
import oracle.apps.xdo.common.log.XDOLogImpl;
import oracle.apps.xdo.template.FormProcessor;
//import oracle.dss.graph.Graph;
import oracle.jdbc.OracleDriver;
//import oracle.xml.parser.v2.XMLParser;

public class Test1 {
	
	Config config = Config.instance();
	
	public static void main(String[] args) {
      Test1 test = new Test1();
      try {
        //test.getVersion();
        test.PDFFormProcess();
      } catch (Exception e) {
    	  e.printStackTrace();
      }
	}
	
	private void run() {
		System.out.println(config.getStr(CONNECTION.DIRECT$PASSWORD));
		System.out.println(config.getPassword(CONNECTION.DIRECT$PASSWORD));
		config.setPwd(CONNECTION.DIRECT$PASSWORD, "password".toCharArray());
		System.out.println(config.getPassword(CONNECTION.DIRECT$PASSWORD));
	}
	
	private void PDFFormProcess() throws Exception {
		OutputStream logFile = new FileOutputStream(new File("C:/WORK/XDOClient/form/log.txt"));
		InputStream xmlInput = new FileInputStream(new File("C:/WORK/XDOClient/form/HRReport/HRReport.xml"));
		InputStream pdfTemplate = new FileInputStream(new File("C:/WORK/XDOClient/form/HRReport/HRReport.pdf"));
		OutputStream pdfOutput = new FileOutputStream(new File("C:/WORK/XDOClient/form/HRReport/HRReport_out.pdf"));
		
        FormProcessor formProcessor = new FormProcessor();
        XDOLogImpl fileLog = new XDOLogImpl();
        fileLog.setDestination(logFile);
        Logger.setLog(fileLog);
        Logger.setLevel(Logger.STATEMENT);
        formProcessor.setData(xmlInput);
        formProcessor.setTemplate(pdfTemplate);
        formProcessor.setOutput(pdfOutput);
        formProcessor.setLocale("en-US");
        boolean result = formProcessor.process();
	}
	
	private void getVersion() throws Exception { 
		Class metainfo = Class.forName("oracle.apps.xdo.common.MetaInfo");
        Field versionField = metainfo.getDeclaredField("VERSION_SHORT");
        String s1  = (String)versionField.get(null);
        System.out.println("BI Publisher Core=" + s1);        
       
        /* 
        Graph graph = new Graph();
        String s2 = graph.getVersion();
        //String s2  = (String)versionField.get(null);
        System.out.println("BI Beans Graph=" + s2);

        String s3 = XMLParser.getReleaseVersion();
        System.out.println("Oracle XML Developers Kit=" + s3.substring(26));    
        
        InputStream is = this.getClass().getResourceAsStream("/META-INF/jdom-info.xml");
        SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(is);
		XPathFactory xpfac = XPathFactory.instance();		
        XPathExpression<Element> xp  = xpfac.compile("/info/version", Filters.element()); 
        String s4 = xp.evaluateFirst(doc).getText();
        System.out.println("JDOM=" + s4);
        */        
        System.out.println("Oracle JDBC Driver= " + OracleDriver.getDriverVersion());
        
        System.out.println("JRE Version= " + System.getProperty("java.version"));
        
        getManifestInfo();
	}
	
	public  String getManifestInfo() {
	    Enumeration resEnum;
	    try {
	        resEnum = this.getClass().getClassLoader().getResources(JarFile.MANIFEST_NAME);
	        while (resEnum.hasMoreElements()) {
	            try {
	                URL url = (URL)resEnum.nextElement();
	                InputStream is = url.openStream();
	                if (is != null) {
	                    Manifest manifest = new Manifest(is);
	                    Attributes mainAttribs = manifest.getMainAttributes();
	                    String title = mainAttribs.getValue("Implementation-Title");	                    
	                    String version = mainAttribs.getValue("Implementation-Version");
	                    if(title.toUpperCase().indexOf("COMMON") >= 0 ||
	                       title.startsWith("JGoodies")) {
	                    	System.out.println(title + "=" + version);
	                    }
	                }
	            }
	            catch (Exception e) {
	            }
	        }
	    } catch (IOException e1) {
	    }
	    return null; 
	}	
  
}
