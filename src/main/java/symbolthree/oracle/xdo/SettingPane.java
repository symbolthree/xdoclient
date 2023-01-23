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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import com.jgoodies.forms.factories.DefaultComponentFactory;

public class SettingPane extends XDOClientPane implements ActionListener {
	private static final long serialVersionUID  = -7420797244441651529L;
	private DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
	private Config       config                 = Config.instance();
	
	private JComponent  settingSEP = compFactory.createSeparator(config.getText("settings.program_config")); 
	private JLabel      tempDirLBL             = new JLabel();
	private MSTextField tempDirTF              = new MSTextField(50);
	private JButton     tempDirBTN             = new JButton();
	private JLabel      uiLangLBL              = new JLabel();
	private JComboBox   uiLangCOMBO            = new JComboBox();
	private JLabel      lnfLBL                 = new JLabel();
	private JComboBox   lnfCOMBO               = new JComboBox();
	private JLabel      logLevelLBL            = new JLabel();
	private JComboBox  logLevelCOMBO           = new JComboBox(new String[] {"INFO", "WARN", "DEBUG"});
	private JLabel      logOutputLBL           = new JLabel();
	private JComboBox  logOutputCOMBO          = new JComboBox(new String[] {"CONSOLE", "FILE"});
	private JCheckBox   prettyFormatCHK        = new JCheckBox();
	private JCheckBox   ebsPasswordCHK         = new JCheckBox();
	private JLabel      fileOpneWithLBL        = new JLabel();
	private JComboBox  fileExtCOMBO            = new JComboBox(new String[] {"", "HTML", "PDF", "RTF", "TXT", "XML", "XLS"});  
	private MSTextField fileOpenWithTF         = new MSTextField(30);
	private JButton     fileOpenWithBTN        = new JButton();
	private JButton     fileOpenWithSaveBTN    = new JButton();
	
	private JComponent  xdoConfigSEP           = compFactory.createSeparator(config.getText("settings.xdo_config"));
	private JScrollPane xdoConfigScrollPane    = new JScrollPane();
	private JTable      xdoCfgTABLE;
	
	private XPathFactory xpfac                 = XPathFactory.instance();
	
	public SettingPane() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints GC = new GridBagConstraints();
		GC.fill=GridBagConstraints.HORIZONTAL;
    	GC.anchor=GridBagConstraints.EAST;
    	GC.insets = new Insets(2,2,2,2);
    	
    	GC.gridx=1;
    	GC.gridy=1;
    	GC.gridwidth=6;
		this.add(settingSEP, GC);
		GC.gridwidth=1;
		
		//---- tempDirLBL ----
    	GC.gridx=1;
    	GC.gridy=2;
		tempDirLBL.setText(config.getText("settings.temp_dir"));
		this.add(tempDirLBL, GC);
		GC.gridx=2;
		GC.gridwidth=4;
		this.add(tempDirTF, GC);
		GC.gridwidth=1;
		
		//---- tempDirBTN ----
		GC.gridx=6;	
		tempDirBTN = GUIHelper.createButton(config.getText("settings.select"), "open.gif");
		tempDirBTN.addActionListener(this);
		this.add(tempDirBTN, GC);

		//---- fileOpneWithLBL ----
		fileOpneWithLBL.setText(config.getText("settings.open_with"));
		GC.gridx=1;
    	GC.gridy=3;
		this.add(fileOpneWithLBL, GC);
		GC.gridx=2;
		fileExtCOMBO.addActionListener(this);
		this.add(fileExtCOMBO, GC);
		GC.gridx=3;
		GC.gridwidth=2;
		fileOpenWithTF.setEnabled(false);
		this.add(fileOpenWithTF, GC);
		GC.gridwidth=1;
		
		GC.gridx=5;
		fileOpenWithBTN = GUIHelper.createButton(config.getText("settings.select"), "open.gif");
		fileOpenWithBTN.addActionListener(this);
		fileOpenWithBTN.setEnabled(false);
		this.add(fileOpenWithBTN, GC);
		GC.gridx=6;
		fileOpenWithSaveBTN = GUIHelper.createButton(config.getText("settings.save"), "save.gif");
		fileOpenWithSaveBTN.setEnabled(false);
		fileOpenWithSaveBTN.addActionListener(this);
		this.add(fileOpenWithSaveBTN, GC);
		
		//---- prettyFormatCHK ----
		GC.gridx=2;
    	GC.gridy=4;
    	GC.gridwidth=2;
		prettyFormatCHK.setText(config.getText("settings.pretty_format"));
		this.add(prettyFormatCHK, GC);

		//---- ebsPasswordCHK ----
		GC.gridx=2;
    	GC.gridy=5;
    	GC.gridwidth=2;
    	ebsPasswordCHK.setText(config.getText("settings.validate_ebs"));
    	ebsPasswordCHK.addActionListener(this);
		this.add(ebsPasswordCHK, GC);
    	GC.gridwidth=1;
		
		//---- lnfLBL ----
		GC.gridx=1;
    	GC.gridy=6;
		lnfLBL.setText(config.getText("settings.lnf"));
		this.add(lnfLBL, GC);
		GC.gridx=2;
		GC.gridwidth=3;
		for (int i=0;i<LOOK_AND_FEELS.length;i++) {
			lnfCOMBO.addItem(LOOK_AND_FEELS[i].split(":")[0]);
		}
		lnfCOMBO.addActionListener(this);
		this.add(lnfCOMBO, GC);
		GC.gridwidth=1;
		
		//---- uiLangLBL ----
		GC.gridx=5;		
		uiLangLBL.setText(config.getText("settings.ui_lang"));
		this.add(uiLangLBL, GC);
		GC.gridx=6;		
		for (int i=0;i<PROGRAM_LANGUAGE.length;i++) {
			uiLangCOMBO.addItem(PROGRAM_LANGUAGE[i].split(":")[0]);
		}
		this.add(uiLangCOMBO, GC);

		//---- logLevelLBL ----
		GC.gridx=1;
    	GC.gridy=7;
		logLevelLBL.setText(config.getText("settings.log_level"));
		this.add(logLevelLBL, GC);
		GC.gridx=2;
		this.add(logLevelCOMBO, GC);

		//---- logOutputLBL ----
		GC.gridx=3;		
		logOutputLBL.setText(config.getText("settings.log_output"));
		this.add(logOutputLBL, GC);
		GC.gridx=4;		
		this.add(logOutputCOMBO, GC);

		GC.gridx=2;
		GC.gridy=8;
		GC.gridwidth=5;
		JLabel l = new JLabel(config.getText("settings.restart_needed"));
		l.setForeground(Color.BLUE);
		this.add(l, GC);
		
		GC.gridx=1;
    	GC.gridy=9;
    	GC.gridwidth=6;
		this.add(xdoConfigSEP, GC);
		
		//======== xdoConfigScrollPane ========
		GC.gridx=1;
    	GC.gridy=10;
    	GC.gridwidth=6;
    	populateXDOCfg();
		xdoConfigScrollPane.setViewportView(xdoCfgTABLE);		
		this.add(xdoConfigScrollPane, GC);
	}
	
	private void populateXDOCfg() {
        String[] columnNames = {"Key", "Value"};
        
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
			private static final long serialVersionUID = 4886287779669939039L;
			@Override
        	   public boolean isCellEditable(int row, int column) {
        	       return column == 1;
        	   }
        };
        xdoCfgTABLE = new JTable();
        xdoCfgTABLE.setModel(tableModel);
        
        SAXBuilder builder = new SAXBuilder();
        Document doc = null;
		try {
			// doc = builder.build(this.getClass().getResourceAsStream("/XDO.cfg"));
			doc = builder.build(new File(XDOCLIENT_PROGRAM_CFG_DIR, "XDO.cfg"));
			Namespace ns = Namespace.getNamespace("xdo", "http://xmlns.oracle.com/oxp/config/");
	        XPathExpression<Element> xp  = xpfac.compile("/xdo:config/xdo:properties/xdo:property", Filters.element(), null, ns);
	        List<Element> list = xp.evaluate(doc);
	        Iterator<Element> itr = list.iterator();
	        
	        while (itr.hasNext()) {
	          Element ele = itr.next(); 
	          String key = ele.getAttributeValue("name");
	          String val = ele.getText();
	          tableModel.addRow(new Object[]{key, val});
	        }
	                
	        xdoCfgTABLE.setPreferredScrollableViewportSize(new Dimension(500, 120));
	        xdoCfgTABLE.setAutoCreateRowSorter(true);
	        RowSorter<? extends TableModel> sorter = xdoCfgTABLE.getRowSorter();
	        List<SortKey> sortKeys = new ArrayList<SortKey>();
	        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
	        sorter.setSortKeys(sortKeys);
	        
			xdoCfgTABLE.setFillsViewportHeight(true);

		} catch (Exception e) {
			Logger.log(LOG_ERROR, "Unable to load XDO.cfg");
		}
			
		
	}

	@Override
	void initComponents() {
		tempDirTF.setText(config.getStr(SETTINGS.TEMP_DIR));
		
		String lang = config.getStr(SETTINGS.LANG);
    	uiLangCOMBO.setSelectedItem(config.LOV(PROGRAM_LANGUAGE, lang, false));		
		
		String lnf = config.getStr(SETTINGS.LNF);
    	lnfCOMBO.setSelectedItem(config.LOV(LOOK_AND_FEELS, lnf, false));		

    	prettyFormatCHK.setSelected(config.getBoolean(SETTINGS.PRETTY_FORMAT));
        ebsPasswordCHK.setSelected(config.getBoolean(SETTINGS.VALIDATE_EBS_USER));
		logLevelCOMBO.setSelectedItem(config.getStr(SETTINGS.LOG_LEVEL));
		logOutputCOMBO.setSelectedItem(config.getStr(SETTINGS.LOG_OUTPUT));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==fileExtCOMBO) {
            if (fileExtCOMBO.getSelectedIndex() != 0) {
            	String ext = (String)fileExtCOMBO.getSelectedItem();
            	fileOpenWithTF.setText(config.getStr("settings.file_open." + ext.toLowerCase()));
    			fileOpenWithTF.setEnabled(true);
    			fileOpenWithBTN.setEnabled(true);
    			fileOpenWithSaveBTN.setEnabled(true);
            } else {
            	fileOpenWithTF.setText("");
    			fileOpenWithTF.setEnabled(false);
    			fileOpenWithBTN.setEnabled(false);
    			fileOpenWithSaveBTN.setEnabled(false);
            }
		}
		
		if (e.getSource()==tempDirBTN) {
			JFileChooser fc = GUIHelper.dirChooser(tempDirTF.getText());
			int rtnVal = fc.showOpenDialog(SettingPane.this);
			if (rtnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                tempDirTF.setText(file.getAbsolutePath());
			}			
		}
		
		if (e.getSource()==fileOpenWithBTN) {
			JFileChooser fc = GUIHelper.filterFileChooser("", fileOpenWithTF.getText());
			int rtnVal = fc.showOpenDialog(SettingPane.this);
			if (rtnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                fileOpenWithTF.setText(file.getAbsolutePath());
			}			
		}
		
		if (e.getSource()==fileOpenWithSaveBTN) {
			String ext = (String) fileExtCOMBO.getSelectedItem();
			String program = fileOpenWithTF.getText();
			config.set("settings.file_open." + ext.toLowerCase(), program);
			fileExtCOMBO.setSelectedIndex(0);
		}
		
		if (e.getSource()==ebsPasswordCHK) {
			config.set(SETTINGS.VALIDATE_EBS_USER, ebsPasswordCHK.isSelected());
		}
	}

	@Override
	protected void saveProperties() {
		config.set(SETTINGS.TEMP_DIR, tempDirTF.getText());
		
		String lang = (String) uiLangCOMBO.getSelectedItem();
    	config.set(SETTINGS.LANG, config.LOV(PROGRAM_LANGUAGE, lang, true));
		
		String lnf = (String) lnfCOMBO.getSelectedItem();
		config.set(SETTINGS.LNF, config.LOV(LOOK_AND_FEELS, lnf, true));

        config.set(SETTINGS.PRETTY_FORMAT, prettyFormatCHK.isSelected());
        config.set(SETTINGS.VALIDATE_EBS_USER, ebsPasswordCHK.isSelected());
        config.set(SETTINGS.LOG_LEVEL, (String)logLevelCOMBO.getSelectedItem());
        config.set(SETTINGS.LOG_OUTPUT, (String)logOutputCOMBO.getSelectedItem());
	}

	@Override
	protected boolean validateFields() {
		return true;
	}
	
}
