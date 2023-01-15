/******************************************************************************
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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

public class TemplateParamsDialog  extends JDialog implements ActionListener, CONSTANTS{
	private static final long serialVersionUID = 4598786204302294939L;
	private Config config = Config.instance();
	private XPathFactory xpfac = XPathFactory.instance();
    private JTable paramTable    = new JTable();
	private String parameterVals;
	private JButton saveButton   = new JButton(config.getText("template.save"));	
	private JButton cancelButton = new JButton(config.getText("template.cancel"));
	private boolean updateVal    = false;
	
	public TemplateParamsDialog(JFrame parent, String title) {
		
        super(parent, title, true);
        //this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setSize(500, 300);
        BoxLayout boxLayout = new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS);
        this.setLayout(boxLayout);
        JPanel panel = new JPanel();
    	JScrollPane scrollPane = new JScrollPane();
    	scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    	scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    	panel.add(scrollPane);
		this.add(panel);
        
		String template = config.getStr(TEMPLATE.XML$DATA_TEMPLATE);
		String paraVals = config.getStr(TEMPLATE.XML$PARAMETERS);
		Hashtable<String, String> ht = Config.templateParamVal(paraVals);

		TableModel tableModel = new ParamTableModel();
        SAXBuilder builder = new SAXBuilder();
		try {
			Document doc = builder.build(new File(template));
			
	        XPathExpression<Element> xp  = xpfac.compile("/dataTemplate/parameters/parameter", Filters.element()); 
	        List<Element> eles = xp.evaluate(doc);
	        for (int i=0;i<eles.size();i++) {
	          String name = eles.get(i).getAttributeValue("name");
	          String dataType = eles.get(i).getAttributeValue("dataType");
	          String defaultVal = eles.get(i).getAttributeValue("defaultValue");
	          if (defaultVal==null) defaultVal="";
	          tableModel.setValueAt(name, i, 0);
	          tableModel.setValueAt(dataType, i, 1);
	          tableModel.setValueAt(ht.get(name), i, 2);
	          tableModel.setValueAt(defaultVal, i, 3);
	          //tableModel.addRow(name, dataType, "", defaultVal);
	        }
		} catch (JDOMException e) {
			Logger.logError(e);
		} catch (IOException e) {
			Logger.logError(e);
		}
		
		//paramTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        paramTable.setModel(tableModel);		
		paramTable.setPreferredScrollableViewportSize(new Dimension(600, 200));
		paramTable.setAutoCreateRowSorter(true);
		paramTable.setFillsViewportHeight(true);

    	scrollPane.setViewportView(paramTable);
    	
    	JPanel buttonPanel = new JPanel();
    	cancelButton.addActionListener(this);
    	buttonPanel.add(cancelButton);
		saveButton.addActionListener(this);
		buttonPanel.add(saveButton);
		buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(buttonPanel);
		this.pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
        	parameterVals = createparameterValues();
        	updateVal = true;
            this.setVisible(false);	
        }
        if (e.getSource() == cancelButton) {
        	updateVal = false;
        	this.setVisible(false);
        }
	}
	
	public String createparameterValues() {
		String rtnVal = "";
		int rowCount = paramTable.getRowCount();
		for (int i=0;i<rowCount;i++) {
		  String name = (String) paramTable.getValueAt(i, 0);
		  String val =  (String) paramTable.getValueAt(i, 2);
		  /*
		  if (val.equals("")) {
			  val = (String) paramTable.getValueAt(i, 3);
		  }
		  */
		  rtnVal = rtnVal + name + ":" + val;
		  
		  if (i!=rowCount-1) {
			  rtnVal = rtnVal + "|";
		  }
		}
		Logger.log(LOG_DEBUG, "Parameter Values=" + rtnVal);
		return rtnVal;
	}

	public boolean isUpdateVals() {
		return updateVal;
	}
	
	public String getParameterVals() {
		return parameterVals;
	}

	public void setParameterVals(String parameterVals) {
		this.parameterVals = parameterVals;
	}

}
