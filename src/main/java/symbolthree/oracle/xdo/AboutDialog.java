/******************************************************************************
 *
 * Symbolthree XDO Client
 * Copyright (C) 2023 Christopher Ho 
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
******************************************************************************/

package symbolthree.oracle.xdo;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import oracle.jdbc.OracleDriver;
import oracle.xml.parser.v2.XMLParser;
import oracle.xml.XDKVersion;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

public class AboutDialog extends JDialog implements ActionListener, CONSTANTS {

	private static final long serialVersionUID = -3764422597260664250L;
	private Properties versions = new Properties();
	private Config config       = Config.instance();
	private JButton closeBTN    = new JButton(config.getText("menu.close"));	
	
	public AboutDialog() {
		
		initVersions();
		
		this.setTitle(config.getText("menu.about"));
        //this.setSize(450, 300);
        //BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);		
        this.setLayout(new GridBagLayout());
    	GridBagConstraints GC = new GridBagConstraints();
    	
		JLabel label1  = new JLabel("XDO Client ");
		label1.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
		JLabel version = new JLabel(getXDOClientVersion());
		version.setFont(new Font(Font.DIALOG, Font.ITALIC, 12));
		JLabel label2 = new JLabel("Copyright(c) 2023 Christopher Ho | symbolthree.com");
		JLabel label3 = new JLabel("<html><a href='#'>Home Page</a></html>");
		label3.setCursor(new Cursor(Cursor.HAND_CURSOR));
		label3.addMouseListener(new MouseAdapter() {
			@Override
            public void mouseClicked(MouseEvent e) {
			  try {
				Desktop.getDesktop().browse(new URI(config.getText("menu.about_link")));
              } catch (Exception ex) {
			  }
			}
		});
		JSeparator sep = new JSeparator();
		
		GC.gridx=0;
		GC.gridy=0;
		this.add(label1, GC);
		GC.gridy=1;
		this.add(version, GC);
		GC.gridy=2;
		this.add(label2, GC);
		GC.gridy=3;
		this.add(label3, GC);
		GC.gridy=4;
		this.add(sep, GC);
        
        JScrollPane jsp = new JScrollPane();
        JTable table = new JTable();
        String[] columnNames = {"Component", "Version"};
        
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
			private static final long serialVersionUID = 4886287779669939039L;
			@Override
        	   public boolean isCellEditable(int row, int column) {
        	       return false;
        	   }
        };
        
        table.setModel(tableModel);
        Enumeration<?> enumKey = versions.keys();
        while (enumKey.hasMoreElements()) {
          String key = (String)enumKey.nextElement();
          String val = versions.getProperty(key);
          tableModel.addRow(new Object[]{key, val});
        }
        
        table.setAutoCreateRowSorter(true);
        RowSorter<? extends TableModel> sorter = table.getRowSorter();
        List<SortKey> sortKeys = new ArrayList<SortKey>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);

        JPanel    panel     = new JPanel();        
        table.setFillsViewportHeight(true);
        jsp.setViewportView(table);
        panel.add(jsp); 
        GC.gridy=5;
        this.add(panel, GC);
        
        closeBTN.addActionListener(this);
        closeBTN.setAlignmentX(Component.CENTER_ALIGNMENT);
        GC.gridy=6;
        this.add(closeBTN, GC);

        this.pack();
	}

	private String getXDOClientVersion() {
        InputStream is        = this.getClass().getResourceAsStream("/build.properties");
        Properties  buildProp = new Properties();
        try {
            buildProp.load(is);
        String MAJOR_VER = buildProp.getProperty("build.version");
        String MINOR_VER = buildProp.getProperty("build.number");
        String TIMSETAMP = buildProp.getProperty("build.time").substring(0,10);
        String ver = "Version " + MAJOR_VER + " build " + MINOR_VER + " (" + TIMSETAMP + ")";
        return ver;
        
        } catch (Exception e) {
           return "unknown";
        }

	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==closeBTN) {
        	this.setVisible(false);			
		}
		
	}
	
	private void initVersions() {
	  try {
		Class<?> metainfo = Class.forName("oracle.apps.xdo.common.MetaInfo");
        Field versionField = metainfo.getDeclaredField("VERSION_SHORT");
        String s1  = (String)versionField.get(null);
        addVersion("BI Publisher Core", s1);        
        
       /*
        Graph graph = new Graph();
        String s2 = graph.getVersion();
        addVersion("BI Beans Graph", s2);
       */
        
        String s3 = XMLParser.getReleaseVersion();
        String xdkVersion = s3.substring(26).trim();
        
        Logger.log(this, LOG_DEBUG, "Oracle XDK version=[" + xdkVersion + "]");
        
        if (xdkVersion.equals("null")) {
        	xdkVersion = XDKVersion.MAJORVSN + "." + XDKVersion.MINORVSN + "." + 
                         XDKVersion.MIDTIERVSN + "." + XDKVersion.PATCHMAJORVSN + "." + 
        			     XDKVersion.PATCHMINORVSN;
        }
        addVersion("Oracle XML Developers Kit",  xdkVersion);
        
        InputStream is = this.getClass().getResourceAsStream("/META-INF/jdom-info.xml");
        SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(is);
		XPathFactory xpfac = XPathFactory.instance();		
        XPathExpression<Element> xp  = xpfac.compile("/info/version", Filters.element()); 
        String s4 = xp.evaluateFirst(doc).getText();
        is.close();
        
        addVersion("JDOM", s4);
        
        addVersion("Oracle JDBC Driver", OracleDriver.getDriverVersion());
        
        addVersion("JRE Version", System.getProperty("java.version"));
        
        addVersion("Java Home", System.getProperty("java.home"));
        
        readManifest();
        
		} catch (Exception e) {
			Logger.logError(e);
		}
	}
	
	private void addVersion(String key, String val) {
		if (val==null) val="";
		versions.setProperty(key, val);
	}
	
	private void readManifest() throws Exception {
	    Enumeration<?> resEnum;
        resEnum = this.getClass().getClassLoader().getResources(JarFile.MANIFEST_NAME);
        while (resEnum.hasMoreElements()) {
	        URL url = (URL)resEnum.nextElement();
	        InputStream is = url.openStream();
	        if (is != null) {
	            Manifest manifest = new Manifest(is);
	            Attributes mainAttribs = manifest.getMainAttributes();
	            String title = mainAttribs.getValue("Implementation-Title");	                    
	            String version = mainAttribs.getValue("Implementation-Version");
	            if (title!=null) {
	              if(title.toUpperCase().indexOf("COMMON") >= 0 ||
	                 title.startsWith("JGoodies")) {
	            	  addVersion(title ,version);
	              }
	            }
	        }
        }
	}
}
