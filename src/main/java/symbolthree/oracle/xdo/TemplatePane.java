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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.jgoodies.forms.factories.DefaultComponentFactory;

public class TemplatePane extends XDOClientPane implements ActionListener {
	private static final long serialVersionUID = 6006859727121213048L;

	private Config             config               = Config.instance();
	
	private DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
	private JCheckBox          xmldataCHK           = new JCheckBox();
	private JComponent         xmldataSEP           = compFactory.createSeparator(config.getText("template.create_xml_data"));
	private MSTextField        dataTemplateTF       = new MSTextField("", 30);
	private JLabel             dataTemplateLBL      = new JLabel();
	private JButton            dataTemplateFileBTN  = new JButton();
	private JButton            dataTemplateEditBTN  = new JButton();
	private JButton            xmlOutDirBTN         = new JButton();
	private MSTextField        xmlOutDirTF          = new MSTextField("", 30);
	private JLabel             xmlOutDirLBL         = new JLabel();
	private JLabel             xmlFilePatternLBL    = new JLabel();
	private MSTextField        xmlFileNameTF        = new MSTextField("", 15);
	private JCheckBox          xmlOpenCHK           = new JCheckBox();
	private JComboBox          xmlFilePatternCOMBO  = new JComboBox(FILENAME_PATTERN);
	private MSTextField        fillParamsTF         = new MSTextField("", 30);
	private JLabel             fillparamsLBL        = new JLabel();
	private JButton            fillParamsBTN        = new JButton();
	
	private JComponent         generateDocSEP       = compFactory.createSeparator(config.getText("template.gen_doc"));
	private JCheckBox          generateDocCHK       = new JCheckBox();
	private MSTextField        xmlDataFileTF        = new MSTextField("", 50);
	private JButton            xmlDataFileBTN       = new JButton();
	private JButton            xmlDataFileEditBTN   = new JButton();
	private JLabel             xmlDataFileLBL       = new JLabel();
	private JLabel             templateFileLBL      = new JLabel();
	private JLabel             templateFormatLBL    = new JLabel();
	private JComboBox          templateFormatCOMBO  = new JComboBox();	
	private MSTextField        templateFileTF       = new MSTextField("", 50);
	private JButton            templateFileBTN      = new JButton();
	private JButton            templateFileEditBTN  = new JButton();
	private JLabel             docOutputDirLBL      = new JLabel();
	private JLabel             docOutputFormatLBL   = new JLabel();
	private JComboBox  docOutputFormatCOMBO         = new JComboBox();
	private JLabel             docFilePatternLBL    = new JLabel();
	private JComboBox  docFilePatternCOMBO          = new JComboBox(FILENAME_PATTERN);
	private MSTextField        docOutputDirTF       = new MSTextField("", 50);
	private JButton            docOutputDirBTN      = new JButton();
	private MSTextField        docFileNameTF        = new MSTextField("", 25);
	private JCheckBox          docFileOpenCHK       = new JCheckBox();
	private JCheckBox          keepXSLCHK           = new JCheckBox();
	private JCheckBox          keepXMLCHK           = new JCheckBox();
	private JComponent         templateSEP          = compFactory.createSeparator("");
	
	public TemplatePane() {
		this.setLayout(new GridBagLayout());
    	GridBagConstraints GC = new GridBagConstraints();		
		GC.fill=GridBagConstraints.HORIZONTAL;
    	GC.anchor=GridBagConstraints.EAST;
    	GC.insets = new Insets(2,3,2,3);
    	
    	GC.gridx=1;
    	GC.gridy=1;
    	xmldataCHK.addActionListener(this);
		this.add(xmldataCHK, GC);
		GC.gridx=2;
    	GC.gridwidth=5;
    	GC.anchor=GridBagConstraints.WEST;
		this.add(xmldataSEP, GC);
		GC.gridwidth=1;
		GC.anchor=GridBagConstraints.EAST;
		
		//---- dataTemplate ----
		dataTemplateLBL.setText(config.getText("template.data_template"));
		GC.gridx=2;
		GC.gridy=2;
		this.add(dataTemplateLBL, GC);
		GC.gridx=3;
		GC.gridwidth=2;
		this.add(dataTemplateTF, GC);
		GC.gridwidth=1;

		dataTemplateFileBTN=GUIHelper.createButton(config.getText("template.select"), "open.gif");
		dataTemplateFileBTN.addActionListener(this);
		GC.gridx=5;
		this.add(dataTemplateFileBTN, GC);

		dataTemplateEditBTN = GUIHelper.createButton(config.getText("template.edit"), "edit.gif");
		dataTemplateEditBTN.addActionListener(this);
		GC.gridx=6;
    	GC.anchor=GridBagConstraints.WEST;
		this.add(dataTemplateEditBTN, GC);
    	GC.anchor=GridBagConstraints.EAST;

		//---- xmlOutDir ----
		xmlOutDirLBL.setText(config.getText("template.xml_output_dir"));
		GC.gridx=2;
		GC.gridy=3;
		this.add(xmlOutDirLBL, GC);
		GC.gridx=3;
		GC.gridwidth=2;
		this.add(xmlOutDirTF, GC);
		GC.gridwidth=1;
		
		xmlOutDirBTN = GUIHelper.createButton(config.getText("template.select"), "open.gif");
		xmlOutDirBTN.addActionListener(this);
		GC.gridx=5;
		this.add(xmlOutDirBTN, GC);

		//---- xmlFilePattern ----
		xmlFilePatternLBL.setText(config.getText("template.filename_pattern"));
		GC.gridx=2;
		GC.gridy=4;		
		this.add(xmlFilePatternLBL, GC);
		GC.gridx=3;
    	GC.anchor=GridBagConstraints.WEST;
    	xmlFilePatternCOMBO.addActionListener(this);
		this.add(xmlFilePatternCOMBO, GC);
		GC.gridx=4;
		this.add(xmlFileNameTF, GC);
		xmlOpenCHK.setText(config.getText("template.open_file"));
		GC.gridx=5;
		GC.gridwidth=2;
		this.add(xmlOpenCHK, GC);
		GC.gridwidth=1;
    	GC.anchor=GridBagConstraints.EAST;
    	
		//---- fillParamsBTN ----
		fillparamsLBL.setText(config.getText("template.parameter_values"));
		GC.gridx=2;
		GC.gridy=5;		
		this.add(fillparamsLBL, GC);
		GC.gridx=3;
		GC.gridwidth=2;
		this.add(fillParamsTF, GC);
		GC.gridwidth=1;
		GC.gridx=5;
    	GC.anchor=GridBagConstraints.WEST;
		fillParamsBTN = GUIHelper.createButton(config.getText("template.edit"), "edit.gif");
		fillParamsBTN.addActionListener(this);
		this.add(fillParamsBTN, GC);
    	GC.anchor=GridBagConstraints.EAST;

		// ---- Generate Doc ----
		GC.gridx=1;
		GC.gridy=7;
		generateDocCHK.addActionListener(this);
		this.add(generateDocCHK, GC);
		GC.gridx=2;
		GC.gridwidth=5;		
    	GC.anchor=GridBagConstraints.WEST;
		this.add(generateDocSEP, GC);
		GC.gridwidth=1;
    	GC.anchor=GridBagConstraints.EAST;
    	
		//---- xmlDataFileBTN ----
		GC.gridx=2;
		GC.gridy=8;
		xmlDataFileLBL.setText(config.getText("template.xml_data_file"));
		this.add(xmlDataFileLBL, GC);
		GC.gridx=3;
		GC.gridwidth=2;
		this.add(xmlDataFileTF, GC);
		GC.gridwidth=1;
		
		GC.gridx=5;		
		xmlDataFileBTN = GUIHelper.createButton(config.getText("template.select"), "open.gif");
		xmlDataFileBTN.addActionListener(this);
		this.add(xmlDataFileBTN, GC);
		GC.gridx=6;
    	GC.anchor=GridBagConstraints.WEST;
		xmlDataFileEditBTN = GUIHelper.createButton(config.getText("template.edit"), "edit.gif");
		xmlDataFileEditBTN.addActionListener(this);
		this.add(xmlDataFileEditBTN, GC);
    	GC.anchor=GridBagConstraints.EAST;
		
		//---- templateFormat ----
		GC.gridx=2;
		GC.gridy=9;
		templateFormatLBL.setText(config.getText("template.template_format"));
		this.add(templateFormatLBL, GC);
		GC.gridx=3;
    	GC.anchor=GridBagConstraints.WEST;
    	
    	for (int i=0;i<DOC_TEMPLATE_FORMAT.length;i++) {
    	  templateFormatCOMBO.addItem(DOC_TEMPLATE_FORMAT[i].split(":")[0]);
    	}
    	templateFormatCOMBO.addActionListener(this);
		this.add(templateFormatCOMBO, GC);
    	GC.anchor=GridBagConstraints.EAST;		

		//---- templateFile ----
		GC.gridx=2;
		GC.gridy=10;
		templateFileLBL.setText(config.getText("template.template_file"));
		this.add(templateFileLBL, GC);
		GC.gridx=3;
		GC.gridwidth=2;
		this.add(templateFileTF, GC);
		GC.gridwidth=1;

		GC.gridx=5;
		templateFileBTN = GUIHelper.createButton(config.getText("template.select"), "open.gif");
		templateFileBTN.addActionListener(this);
		this.add(templateFileBTN, GC);
		GC.gridx=6;
    	GC.anchor=GridBagConstraints.WEST;		
		templateFileEditBTN = GUIHelper.createButton(config.getText("template.edit"), "edit.gif");
		templateFileEditBTN.addActionListener(this);
		this.add(templateFileEditBTN, GC);
    	GC.anchor=GridBagConstraints.EAST;
		
		//---- DocOutputDir ----
		GC.gridx=2;
		GC.gridy=11;
		docOutputDirLBL.setText(config.getText("template.doc_output_dir"));
		this.add(docOutputDirLBL, GC);
		GC.gridx=3;
		GC.gridwidth=2;
		this.add(docOutputDirTF, GC);
		GC.gridwidth=1;
		GC.gridx=5;
		docOutputDirBTN = GUIHelper.createButton(config.getText("template.select"), "open.gif");
		docOutputDirBTN.addActionListener(this);
		this.add(docOutputDirBTN, GC);
		
		//---- docOutputFormat ----
		GC.gridx=2;
		GC.gridy=12;
		docOutputFormatLBL.setText(config.getText("template.doc_output_format"));
		this.add(docOutputFormatLBL, GC);
		GC.gridx=3;
    	GC.anchor=GridBagConstraints.WEST;
    	for (int i=0;i<DOC_OUTPUT_FORMAT.length;i++) {
    	  docOutputFormatCOMBO.addItem(DOC_OUTPUT_FORMAT[i].split(":")[0]);
    	}
		this.add(docOutputFormatCOMBO, GC);
    	GC.anchor=GridBagConstraints.EAST;		

		//---- docFilePattern ----
		GC.gridx=2;
		GC.gridy=13;
		docFilePatternLBL.setText(config.getText("template.filename_pattern"));
		this.add(docFilePatternLBL, GC);
		GC.gridx=3;
    	GC.anchor=GridBagConstraints.WEST;
    	docFilePatternCOMBO.addActionListener(this);
		this.add(docFilePatternCOMBO, GC);
		GC.gridx=4;
		this.add(docFileNameTF, GC);
		GC.gridx=5;
		GC.gridwidth=2;
		docFileOpenCHK.setText(config.getText("template.open_file"));
		this.add(docFileOpenCHK, GC);
		GC.gridwidth=1;
    	//GC.anchor=GC.EAST;
		
		//--- keep file ----
		GC.gridx=3;
		GC.gridy=14;
		keepXMLCHK.setText(config.getText("template.keep_xml_file"));		
		this.add(keepXMLCHK, GC);
		GC.gridx=4;
		keepXSLCHK.setText(config.getText("template.keep_xsl_file"));		
		this.add(keepXSLCHK, GC);
		GC.anchor=GridBagConstraints.WEST;
		
		GC.gridx=1;
		GC.gridy=15;
		GC.gridwidth=6;
		GC.insets = new Insets(10,2,10,2);
		this.add(templateSEP, GC);		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Logger.log(this, LOG_DEBUG, e.getSource().getClass().getName() + "[" + e.getActionCommand() + "]");
		
		if (e.getSource() instanceof JButton && e.getActionCommand().equals(config.getText("template.select"))) {
			setFileChoosers(e);
		}
		if (e.getSource() == xmldataCHK) {
			toggleXMLComponents(xmldataCHK.isSelected());
		}
		if (e.getSource() == generateDocCHK) {
			toggleDocComponents(generateDocCHK.isSelected());
		}
		
		if (e.getSource() == dataTemplateEditBTN) GUIHelper.openFile(dataTemplateTF.getText());
		if (e.getSource() == xmlDataFileEditBTN) GUIHelper.openFile(xmlDataFileTF.getText());
		if (e.getSource() == templateFileEditBTN) GUIHelper.openFile(templateFileTF.getText());
		
		if (e.getSource()==fillParamsBTN) {
			if (! dataTemplateTF.getText().equals("")) {
			  config.set(TEMPLATE.XML$DATA_TEMPLATE, dataTemplateTF.getText());
			  
              TemplateParamsDialog f =  new TemplateParamsDialog(
            		                        (JFrame)this.getRootPane().getParent(),
            		                         config.getText("template.parameters"));
              f.setLocationRelativeTo(this);              
              f.setVisible(true);
              if (f.isUpdateVals()) {
                fillParamsTF.setText(f.getParameterVals());
              }
			} else {
				GUIHelper.showErrorDialog(this, config.getText("template.error_data_template"));
			}
		}
		
		if (e.getSource()==xmlFilePatternCOMBO) {
			toggleXMLFileName();
		}
		
		if (e.getSource()==docFilePatternCOMBO) {
			toggleDocOutputFileName();
		}
		
		if (e.getSource()==templateFormatCOMBO) {
			String selected = config.LOV(DOC_TEMPLATE_FORMAT, templateFormatCOMBO.getSelectedItem(), true);
			
			if (selected.equals("PDF")) {
				docOutputFormatCOMBO.setSelectedItem("PDF");
				docOutputFormatCOMBO.setEnabled(false);
			} else if (selected.equals("ETEXT")){
				docOutputFormatCOMBO.setSelectedItem("eText");
				docOutputFormatCOMBO.setEnabled(false);				
			} else {
				docOutputFormatCOMBO.setEnabled(true);			
			}
		}
	}

	private void toggleXMLComponents(boolean val) {
		dataTemplateLBL.setEnabled(val);
		dataTemplateTF.setEnabled(val);
		dataTemplateFileBTN.setEnabled(val);
		dataTemplateEditBTN.setEnabled(val);
		xmlOutDirLBL.setEnabled(val);
		xmlOutDirTF.setEnabled(val);
		xmlOutDirBTN.setEnabled(val);
		xmlFilePatternLBL.setEnabled(val);
		xmlFileNameTF.setEnabled(val);
		xmlFilePatternCOMBO.setEnabled(val);
		xmlOpenCHK.setEnabled(val);
		fillParamsBTN.setEnabled(val);
		fillParamsTF.setEnabled(val);
		fillparamsLBL.setEnabled(val);
		
		toggleXMLFileName();
		toggleXMLDataRequired();
	}

	private void toggleDocComponents(boolean val) {
		xmlDataFileTF.setEnabled(val);
		xmlDataFileBTN.setEnabled(val);
		xmlDataFileEditBTN.setEnabled(val);
		xmlDataFileLBL.setEnabled(val);
		templateFileLBL.setEnabled(val);
		templateFormatLBL.setEnabled(val);
		templateFileTF.setEnabled(val);
		templateFormatCOMBO.setEnabled(val);
		templateFileBTN.setEnabled(val);
		templateFileEditBTN.setEnabled(val);
		docOutputDirLBL.setEnabled(val);
		docOutputFormatLBL.setEnabled(val);
		docOutputFormatCOMBO.setEnabled(val);
		docFilePatternLBL.setEnabled(val);
		docFilePatternCOMBO.setEnabled(val);
		docOutputDirTF.setEnabled(val);
		docOutputDirBTN.setEnabled(val);
		docFileNameTF.setEnabled(val);
		docFileOpenCHK.setEnabled(val);
		keepXSLCHK.setEnabled(val);
		keepXMLCHK.setEnabled(val);
		
		toggleDocOutputFileName();
		toggleXMLDataRequired();
	}	
	
	private void toggleXMLDataRequired() {
		boolean xmlFileNeeded  = false;
		boolean keepXMLEnabled = false;
		
		if (xmldataCHK.isSelected() && generateDocCHK.isSelected()) {
			xmlFileNeeded  = false;
			keepXMLEnabled = true;
		} 
		if (xmldataCHK.isSelected() && !generateDocCHK.isSelected()) {
			xmlFileNeeded = false;
			keepXMLEnabled = false;
		}
		if (!xmldataCHK.isSelected() && generateDocCHK.isSelected()) {
			xmlFileNeeded = true;
			keepXMLEnabled = false;
		}
		xmlDataFileTF.setEnabled(xmlFileNeeded);
		xmlDataFileBTN.setEnabled(xmlFileNeeded);
		xmlDataFileEditBTN.setEnabled(xmlFileNeeded);
		xmlDataFileLBL.setEnabled(xmlFileNeeded);
		keepXMLCHK.setEnabled(keepXMLEnabled);
	}
	
	private void setFileChoosers(ActionEvent e) {
		if (e.getSource()==dataTemplateFileBTN) {
			JFileChooser fc = GUIHelper.filterFileChooser("XML", dataTemplateTF.getText());
			int rtnVal = fc.showOpenDialog(TemplatePane.this);
			if (rtnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                dataTemplateTF.setText(file.getAbsolutePath());
			}
		}

		if (e.getSource()==xmlDataFileBTN) {
			JFileChooser fc = GUIHelper.filterFileChooser("XML", xmlDataFileTF.getText() );
			int rtnVal = fc.showOpenDialog(TemplatePane.this);
			if (rtnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                xmlDataFileTF.setText(file.getAbsolutePath());
			}
		}		
			
		if (e.getSource()==templateFileBTN) {
			String ext = config.LOV(DOC_TEMPLATE_FORMAT, templateFormatCOMBO.getSelectedItem(), true);
			if (ext.equals("ETEXT")) ext = "RTF"; 
			JFileChooser fc = GUIHelper.filterFileChooser(ext, templateFileTF.getText());
			int rtnVal = fc.showOpenDialog(TemplatePane.this);
			if (rtnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                templateFileTF.setText(file.getAbsolutePath());
			}
		}
		
		if (e.getSource()==xmlOutDirBTN) {
			JFileChooser fc = GUIHelper.dirChooser(xmlOutDirTF.getText());
			int rtnVal = fc.showOpenDialog(TemplatePane.this);
			if (rtnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                xmlOutDirTF.setText(file.getAbsolutePath());
			}
		}
		
		if (e.getSource()==docOutputDirBTN) {
			JFileChooser fc = GUIHelper.dirChooser(docOutputDirTF.getText());
			int rtnVal = fc.showOpenDialog(TemplatePane.this);
			if (rtnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                docOutputDirTF.setText(file.getAbsolutePath());
			}
		}
	}

	private void toggleXMLFileName() {
		if (xmlFilePatternCOMBO.getSelectedItem().equals("UUID") ||
			xmlFilePatternCOMBO.getSelectedItem().equals("Timestamp")) {
				xmlFileNameTF.setEnabled(false);
			} else {
				xmlFileNameTF.setEnabled(true);
			}
	}
	
	private void toggleDocOutputFileName() {
		if (docFilePatternCOMBO.getSelectedItem().equals("UUID") ||
			docFilePatternCOMBO.getSelectedItem().equals("Timestamp")) {
			docFileNameTF.setEnabled(false);
		} else {
			docFileNameTF.setEnabled(true);
		}
	}
	
	@Override
	void initComponents() {
		boolean val;
		val = Config.instance().getBoolean(TEMPLATE.XML);
		toggleXMLComponents(val);
		xmldataCHK.setSelected(val);
		dataTemplateTF.setText(config.getStr(TEMPLATE.XML$DATA_TEMPLATE));

		String defaultOutput = XDOCLIENT_PROGRAM_CFG_DIR + FILE_SEP + "output";
		
		if (config.getStr(TEMPLATE.XML$OUTPUT_DIR).equals("")) {
			  xmlOutDirTF.setText(defaultOutput);
		} else {
			xmlOutDirTF.setText(config.getStr(TEMPLATE.XML$OUTPUT_DIR));
		}
		
		xmlOpenCHK.setSelected(config.getBoolean(TEMPLATE.XML$OPEN_FILE));
		xmlFilePatternCOMBO.setSelectedItem(config.getStr(TEMPLATE.XML$FILENAME_PATTERN));
		xmlFileNameTF.setText(config.getStr(TEMPLATE.XML$FILENAME));
		fillParamsTF.setText(config.getStr(TEMPLATE.XML$PARAMETERS));
		toggleXMLComponents(val);
		
		val = Config.instance().getBoolean(TEMPLATE.DOC);
		generateDocCHK.setSelected(val);
		xmlDataFileTF.setText(config.getStr(TEMPLATE.DOC$DATA));
		templateFileTF.setText(config.getStr(TEMPLATE.DOC$TEMPLATE_FILE));
		templateFormatCOMBO.setSelectedItem(config.LOV(DOC_TEMPLATE_FORMAT, config.getStr(TEMPLATE.DOC$TEMPLATE_FORMAT), false));
		docOutputFormatCOMBO.setSelectedItem(config.LOV(DOC_OUTPUT_FORMAT, config.getStr(TEMPLATE.DOC$OUTPUT_FORMAT), false));
		docFilePatternCOMBO.setSelectedItem(config.getStr(TEMPLATE.DOC$FILENAME_PATTERN));
		
		if (config.getStr(TEMPLATE.DOC$OUTPUT_DIR).equals("")) {
			docOutputDirTF.setText(defaultOutput);
		} else {
			docOutputDirTF.setText(config.getStr(TEMPLATE.DOC$OUTPUT_DIR));
		}
		
		docFileNameTF.setText(config.getStr(TEMPLATE.DOC$FILENAME));
		docFileOpenCHK.setSelected(config.getBoolean(TEMPLATE.DOC$OPEN_FILE));
		keepXSLCHK.setSelected(config.getBoolean(TEMPLATE.DOC$KEEP_XML));
		keepXMLCHK.setSelected(config.getBoolean(TEMPLATE.DOC$KEEP_XSLFO));
		toggleDocComponents(val);
		
		toggleXMLFileName();
    }

	@Override
	void saveProperties() {
		config.set(TEMPLATE.XML, xmldataCHK.isSelected());
		config.set(TEMPLATE.XML$DATA_TEMPLATE, dataTemplateTF.getText());
		config.set(TEMPLATE.XML$OUTPUT_DIR, xmlOutDirTF.getText());
		config.set(TEMPLATE.XML$OPEN_FILE, xmlOpenCHK.isSelected());
		config.set(TEMPLATE.XML$FILENAME_PATTERN, (String) xmlFilePatternCOMBO.getSelectedItem());
		config.set(TEMPLATE.XML$FILENAME, xmlFileNameTF.getText());
		config.set(TEMPLATE.XML$PARAMETERS, fillParamsTF.getText());
		
		config.set(TEMPLATE.DOC, generateDocCHK.isSelected());
		config.set(TEMPLATE.DOC$DATA, xmlDataFileTF.getText());
		config.set(TEMPLATE.DOC$TEMPLATE_FILE, templateFileTF.getText());
		config.set(TEMPLATE.DOC$TEMPLATE_FORMAT, config.LOV(DOC_TEMPLATE_FORMAT, templateFormatCOMBO.getSelectedItem(), true));
		config.set(TEMPLATE.DOC$OUTPUT_FORMAT, config.LOV(DOC_OUTPUT_FORMAT, docOutputFormatCOMBO.getSelectedItem(), true));
		config.set(TEMPLATE.DOC$FILENAME_PATTERN, (String) docFilePatternCOMBO.getSelectedItem());
		config.set(TEMPLATE.DOC$FILENAME, docFileNameTF.getText());
		config.set(TEMPLATE.DOC$OUTPUT_DIR, docOutputDirTF.getText());		
		config.set(TEMPLATE.DOC$OPEN_FILE, docFileOpenCHK.isSelected());
		config.set(TEMPLATE.DOC$KEEP_XML, keepXSLCHK.isSelected());
		config.set(TEMPLATE.DOC$KEEP_XSLFO, keepXMLCHK.isSelected());
	}

	@Override
	protected boolean validateFields() {
		boolean isValid = true;
		String msg = "";
		
		if (xmldataCHK.isSelected()) {
			File file = new File(dataTemplateTF.getText());
			if (! file.exists()) {msg = config.getText("template.error_data_template"); isValid = false;}
			
			file = new File(xmlOutDirTF.getText());
			if (! file.exists()) {msg = config.getText("template.error_xml_output"); isValid = false;}
			
			if (xmlFilePatternCOMBO.getSelectedItem().equals("User-Defined") && 
				xmlFileNameTF.getText().equals("")) {msg=config.getText("template.error_xml_filename"); isValid = false;}
		}

		if (generateDocCHK.isSelected()) {
			String fileExt = config.LOV(DOC_TEMPLATE_FORMAT, templateFormatCOMBO.getSelectedItem(), true);
			if (fileExt.equals("ETEXT")) fileExt = "RTF";
			File file = new File(templateFileTF.getText());			
			if (! file.exists() || ! GUIHelper.getExtension(file).toUpperCase().equals(fileExt)) 
			  {msg = config.getText("template.error_doc_template"); isValid = false;}
			
			if (docFilePatternCOMBO.getSelectedItem().equals("User-Defined") && 
				docFileNameTF.getText().equals("")) {msg=config.getText("template.error_doc_filename"); isValid = false;}

			file = new File(docOutputDirTF.getText());
			if (! file.exists()) {msg = config.getText("template.error_doc_output"); isValid = false;}
		}
		
		if (!xmldataCHK.isSelected() && generateDocCHK.isSelected()) {
			File file = new File(xmlDataFileTF.getText());
			if (! file.exists()) {msg = config.getText("template.error_xml_data"); isValid = false;}
		}
		
		if (! config.LOV(DOC_TEMPLATE_FORMAT, templateFormatCOMBO.getSelectedItem(), true).equals("ETEXT") && 
			config.LOV(DOC_OUTPUT_FORMAT, docOutputFormatCOMBO.getSelectedItem(), true).equals("TXT")) {
			msg = config.getText("template.error_etext"); isValid = false;
		}
		
		if (!isValid) GUIHelper.showErrorDialog(this, msg);
		return isValid;
	}
}
