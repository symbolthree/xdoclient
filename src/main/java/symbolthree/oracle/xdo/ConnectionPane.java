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

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

import com.jgoodies.forms.factories.DefaultComponentFactory;


public class ConnectionPane extends XDOClientPane implements ActionListener {
	private static final long serialVersionUID = 8951619413844023624L;

	private DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
	private Config         config            = Config.instance();
	
	private JCheckBox      directConnCHK     = new JCheckBox();
	private JComponent     directConnSEP     = compFactory.createSeparator(config.getText("connection.direct_connect"));
	private JLabel         directConnUserLBL = new JLabel();
	private MSTextField    directConnUserTF  = new MSTextField("", 10);
	private JLabel         directConnPwdLBL  = new JLabel();
	private JPasswordField directConnPwdTF   = new JPasswordField("", 10);
	private JLabel         directConnUrlLBL  = new JLabel();
	private MSTextField    directConnUrlTF   = new MSTextField("", 50);
	private JCheckBox      ebsConnCHK        = new JCheckBox();
	private JComponent     ebsConnSEP        = compFactory.createSeparator(config.getText("connection.ebs_connect"));
	private JLabel         proxyUserLBL      = new JLabel();
	private MSTextField    proxyUserTF       = new MSTextField("", 10);
	private JLabel         proxyPwdLBL       = new JLabel();
	private JPasswordField proxyPwdTF        = new JPasswordField("", 10);
	private JLabel         dbcFileLBL        = new JLabel();
	private MSTextField    dbcFileTF         = new MSTextField("", 50);
	private JButton        dbcFileBTN        = new JButton();
	private JCheckBox      ebsUserCHK        = new JCheckBox();
	private JComponent     ebsUserSEP        = compFactory.createSeparator(config.getText("connection.runas_ebs"));
	private JLabel         ebsUserLBL        = new JLabel();
	private MSTextField    ebsUserTF         = new MSTextField("", 10);
	private JLabel         ebsPwdLBL         = new JLabel();
	private JPasswordField ebsPwdTF          = new JPasswordField("", 10);
	private JLabel         ebsRespLBL        = new JLabel();
	private MSTextField    ebsRespTF         = new MSTextField("", 50);
	private JButton        ebsRespBTN        = new JButton();
	private JLabel         langLBL           = new JLabel();
	private JComboBox      langCOMBO         = new JComboBox(APPS_LANGUAGES);
	private JLabel         statusLBL         = new JLabel();
	private JLabel         statusResultLBL   = new JLabel();
	private JButton        testStatusBTN     = new JButton();
	private JComponent     langSEP           = compFactory.createSeparator("");
	private JComponent     statusSEP         = compFactory.createSeparator("");
	private JComponent     connSEP           = compFactory.createSeparator("");
	
	public ConnectionPane() {
		this.setLayout(new GridBagLayout());
    	GridBagConstraints GC = new GridBagConstraints();		
		GC.fill=GridBagConstraints.HORIZONTAL;
    	GC.anchor=GridBagConstraints.EAST;
    	GC.insets = new Insets(2,2,2,2);

    	GC.gridx=1;
    	GC.gridy=1;
    	directConnCHK.addActionListener(this);
		this.add(directConnCHK, GC);
		GC.gridx=2;
		GC.gridwidth=5;
    	GC.anchor=GridBagConstraints.WEST;
		this.add(directConnSEP, GC);
		GC.gridwidth=1;
    	GC.anchor=GridBagConstraints.EAST;
    	
		//---- directConn----
		directConnUserLBL.setText(config.getText("connection.username"));
    	GC.gridx=2;
    	GC.gridy=2;
		this.add(directConnUserLBL, GC);
		GC.gridx=3;
		this.add(directConnUserTF, GC);

		directConnPwdLBL.setText(config.getText("connection.password"));
		GC.gridx=4;
		this.add(directConnPwdLBL, GC);
		GC.gridx=5;
		GC.anchor=GridBagConstraints.WEST;
		this.add(directConnPwdTF, GC);
		GC.anchor=GridBagConstraints.EAST;

		//---- directConnURL ----
    	GC.gridx=2;
    	GC.gridy=3;
		directConnUrlLBL.setText(config.getText("connection.jdbc_url"));
		this.add(directConnUrlLBL, GC);
		GC.gridx=3;
		GC.gridwidth=3;
		this.add(directConnUrlTF, GC);
		GC.gridwidth=1;
		
		// EBS DataSource
    	GC.gridx=1;
    	GC.gridy=4;
    	ebsConnCHK.addActionListener(this);
		this.add(ebsConnCHK, GC);
		GC.gridx=2;
		GC.gridwidth=5;
    	GC.anchor=GridBagConstraints.WEST;		
		this.add(ebsConnSEP, GC);
		GC.gridwidth=1;
    	GC.anchor=GridBagConstraints.EAST;		
		
		//---- proxyUserLBL ----
    	GC.gridx=2;
    	GC.gridy=5;
		proxyUserLBL.setText(config.getText("connection.ebs_proxy_name"));
		this.add(proxyUserLBL, GC);
		GC.gridx=3;
		this.add(proxyUserTF, GC);
		proxyPwdLBL.setText(config.getText("connection.ebs_proxy_password"));
		GC.gridx=4;
		this.add(proxyPwdLBL, GC);
		GC.gridx=5;
		GC.anchor=GridBagConstraints.WEST;
		this.add(proxyPwdTF, GC);
		GC.anchor=GridBagConstraints.EAST;
		
		//---- dbcFileLBL ----
    	GC.gridx=2;
    	GC.gridy=6;		
		dbcFileLBL.setText(config.getText("connection.dbc_file"));
		this.add(dbcFileLBL, GC);
		GC.gridx=3;
		GC.gridwidth=3;
		this.add(dbcFileTF, GC);
		GC.gridwidth=1;
		
		//---- dbcFileBTN ----
		dbcFileBTN = GUIHelper.createButton(config.getText("connection.select"), "open.gif");
		GC.gridx=6;		
		this.add(dbcFileBTN, GC);
		
    	GC.gridx=1;
    	GC.gridy=7;
    	ebsUserCHK.addActionListener(this);
		this.add(ebsUserCHK, GC);
		GC.gridx=2;
		GC.gridwidth=5;
    	GC.anchor=GridBagConstraints.WEST;
		this.add(ebsUserSEP, GC);
		GC.gridwidth=1;
		GC.anchor=GridBagConstraints.EAST;
		
		//---- ebsUserLBL ----
    	GC.gridx=2;
    	GC.gridy=8;
		ebsUserLBL.setText(config.getText("connection.ebs_username"));
		this.add(ebsUserLBL, GC);
		GC.gridx=3;
		this.add(ebsUserTF, GC);

		//---- ebsPwdLBL ----
		ebsPwdLBL.setText(config.getText("connection.ebs_password"));
		GC.gridx=4;
		this.add(ebsPwdLBL, GC);
		GC.gridx=5;
		GC.anchor=GridBagConstraints.WEST;		
		this.add(ebsPwdTF, GC);
		GC.anchor=GridBagConstraints.EAST;
		
    	GC.gridx=2;
    	GC.gridy=9;
		ebsRespLBL.setText(config.getText("connection.responsibility"));
		this.add(ebsRespLBL, GC);
		GC.gridx=3;
		GC.gridwidth=3;
		this.add(ebsRespTF, GC);
		GC.gridwidth=1;

		//---- ebsRespBTN ----
		ebsRespBTN = GUIHelper.createButton(config.getText("connection.select"), "open.gif");
		ebsRespBTN.addActionListener(this);
		GC.gridx=6;
		this.add(ebsRespBTN, GC);
		
		// ---- langSep ----
    	GC.gridx=1;
    	GC.gridy=10;
    	GC.gridwidth=6;
		this.add(langSEP, GC);
		GC.gridwidth=1;
		
		//---- langLBL ----
    	GC.gridx=2;
    	GC.gridy=11;
		langLBL.setText(config.getText("connection.language"));
		this.add(langLBL, GC);
		GC.gridx=3;
		GC.gridwidth=2;
    	GC.anchor=GridBagConstraints.WEST;		
		this.add(langCOMBO, GC);
		GC.gridwidth=1;
    	GC.anchor=GridBagConstraints.EAST;
    	
		// ---- StatusSep ----
    	GC.gridx=1;
    	GC.gridy=12;
    	GC.gridwidth=6;
		this.add(statusSEP, GC);
		GC.gridwidth=1;
		
		//---- statusLBL ----
    	GC.gridx=2;
    	GC.gridy=13;
		statusLBL.setText(config.getText("connection.status"));
		this.add(statusLBL, GC);

		//---- statusResultLBL ----
    	GC.gridx=3;
    	GC.gridy=13;
		statusResultLBL.setText(config.getText("connection.untested"));
		this.add(statusResultLBL, GC);

		//---- testStatusBTN ----
    	GC.gridx=6;
    	GC.gridy=13;
		testStatusBTN = GUIHelper.createButton(config.getText("connection.test"), "info.gif");
		testStatusBTN.addActionListener(this);
		this.add(testStatusBTN, GC);

		GC.gridx=1;
    	GC.gridy=14;	
    	GC.gridwidth=6;
		this.add(connSEP, GC);
	}

	@Override
	void initComponents() {
		boolean val;
		val = config.getBoolean(CONNECTION.DIRECT);
		directConnCHK.setSelected(val);
		directConnUserTF.setText(config.getStr(CONNECTION.DIRECT$USER));
		directConnPwdTF.setText(config.getPassword(CONNECTION.DIRECT$PASSWORD));
		directConnUrlTF.setText(config.getStr(CONNECTION.DIRECT$JDBC));			
		toggleDirectConn(val);

		val = config.getBoolean(CONNECTION.APPS_DATASOURCE);
		ebsConnCHK.setSelected(val);
		proxyUserTF.setText(config.getStr(CONNECTION.APPS_DATASOURCE$USER));
		proxyPwdTF.setText(config.getPassword(CONNECTION.APPS_DATASOURCE$PASSWORD));
		dbcFileTF.setText(config.getStr(CONNECTION.APPS_DATASOURCE$DBC));
		toggleEBSConn(val);			
		
		val = config.getBoolean(CONNECTION.EBS_USER);
		ebsUserCHK.setSelected(val);
		ebsUserTF.setText(config.getStr(CONNECTION.EBS_USER$USER));
		ebsPwdTF.setText(config.getPassword(CONNECTION.EBS_USER$PASSWORD));
		ebsRespTF.setText(config.getStr(CONNECTION.EBS_USER$RESP_NAME));
		toogleEBSUser(val);			
		
		langCOMBO.setSelectedItem(config.getStr(CONNECTION.LANGUAGE));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==directConnCHK) {
			toggleDirectConn(directConnCHK.isSelected());
		}
		if (e.getSource()==ebsConnCHK) {
			toggleEBSConn(ebsConnCHK.isSelected());
		}
		if (e.getSource()==ebsUserCHK) {
			toogleEBSUser(ebsUserCHK.isSelected());
		}		
		if (e.getSource()==ebsRespBTN) {
            try {
              if (ebsUserTF.getText().equals("")) throw new Exception(config.getText("connection.error_ebs_user"));
    		  if (config.getBoolean(SETTINGS.VALIDATE_EBS_USER) && ebsPwdTF.getPassword().length==0) {
    			throw new Exception(config.getText("connection.error_ebs_pwd"));
    		  }
    			
              this.saveProperties();
              this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));              
              DBConnection.instantiateConnection(true, false);
              RespSelectionDialog f =  new RespSelectionDialog(
						            		  (JFrame)this.getRootPane().getParent(),
						            		  config.getStr("connection.responsibility"));
              f.setLocationRelativeTo(this);
              f.setVisible(true);
              this.setCursor(Cursor.getDefaultCursor());                        
              ebsRespTF.setText(f.getResp().getName());
              
              config.set(CONNECTION.EBS_USER$RESP_KEY, f.getResp().getKey());
              config.set(CONNECTION.EBS_USER$RESP_APP, f.getResp().getApp());
              
            } catch (Exception ex) {
            	GUIHelper.showErrorDialog(this, ex.getMessage());
            	Logger.logError(ex);
            }
        }
		
		if (e.getSource()==testStatusBTN) {
			
			if (this.validateFields()) {
	            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	            this.saveProperties();
	            try {
	                DBConnection.instantiateConnection(true, true);
	                statusResultLBL.setText(config.getText("connection.valid"));
	                statusResultLBL.setForeground(Color.GREEN);
	            } catch (Exception ex) {
	            	GUIHelper.showErrorDialog(this, ex.getMessage());
	                statusResultLBL.setText(config.getText("connection.invalid"));
	                statusResultLBL.setForeground(Color.RED);
	            }
	            this.setCursor(Cursor.getDefaultCursor()); 
			}
		}
		
	}
	
	private void toggleDirectConn(boolean val) {
		directConnUserLBL.setEnabled(val);
		directConnUserTF.setEnabled(val);
		directConnPwdLBL.setEnabled(val);
		directConnPwdTF.setEnabled(val);
		directConnUrlLBL.setEnabled(val);
		directConnUrlTF.setEnabled(val);
		if (val) {
			toggleEBSConn(!val);
			ebsConnCHK.setSelected(false);
		}
	}
	
	private void toggleEBSConn(boolean val) {
		proxyUserLBL.setEnabled(val);
		proxyUserTF.setEnabled(val);
		proxyPwdLBL.setEnabled(val);
		proxyPwdTF.setEnabled(val);
		dbcFileLBL.setEnabled(val);
		dbcFileTF.setEnabled(val);
		dbcFileBTN.setEnabled(val);
		if (val) {
			toggleDirectConn(!val);
			directConnCHK.setSelected(false);
		}
	}
	
	private void toogleEBSUser(boolean val) {
		ebsUserLBL.setEnabled(val);
		ebsUserTF.setEnabled(val);
		ebsPwdLBL.setEnabled(val);
		ebsPwdTF.setEnabled(val);
		ebsRespLBL.setEnabled(val);
		ebsRespTF.setEnabled(val);
		ebsRespBTN.setEnabled(val);
		config.set(CONNECTION.EBS_USER, val);		
	}

	@Override
	protected void saveProperties() {
		config.set(CONNECTION.DIRECT, directConnCHK.isSelected());
		config.set(CONNECTION.DIRECT$USER, directConnUserTF.getText());
		config.setPwd(CONNECTION.DIRECT$PASSWORD, directConnPwdTF.getPassword());
		config.set(CONNECTION.DIRECT$JDBC, directConnUrlTF.getText());			
		
		config.set(CONNECTION.APPS_DATASOURCE, ebsConnCHK.isSelected());
		config.set(CONNECTION.APPS_DATASOURCE$USER, proxyUserTF.getText());
		config.setPwd(CONNECTION.APPS_DATASOURCE$PASSWORD, proxyPwdTF.getPassword());
		config.set(CONNECTION.APPS_DATASOURCE$DBC, dbcFileTF.getText());
		
		config.set(CONNECTION.EBS_USER, ebsUserCHK.isSelected());
		config.set(CONNECTION.EBS_USER$USER, ebsUserTF.getText());
		config.setPwd(CONNECTION.EBS_USER$PASSWORD, ebsPwdTF.getPassword());
		config.set(CONNECTION.EBS_USER$RESP_NAME, ebsRespTF.getText());
		
		config.set(CONNECTION.LANGUAGE, (String)langCOMBO.getSelectedItem());		
	}

	@Override
	protected boolean validateFields() {
		boolean valid = true;
		String msg = "";
		
		if (config.getBoolean(TEMPLATE.XML) && 
			! directConnCHK.isSelected() && !ebsConnCHK.isSelected()) {
			valid = false;
			msg = config.getText("connection.error_select_conn");
		}
		
		if (directConnCHK.isSelected()) {
			if (directConnUserTF.getText().equals("") || 
				directConnPwdTF.getPassword().length==0 ||
				directConnUrlTF.getText().equals("")) {
				valid = false;
				msg   = config.getText("connection.error_direct_conn");
			}
		}
		
		if (ebsConnCHK.isSelected()) {
			if (proxyUserTF.getText().equals("") || 
				proxyPwdTF.equals("") || 
				dbcFileTF.getText().equals("")) {
				valid = false;
				msg   = config.getText("connection.error_ebs_conn");
			}
		}
		
		if (ebsUserCHK.isSelected()) {
			if (ebsUserTF.getText().equals("")) {valid=false; msg=config.getText("connection.error_ebs_user");}
			if (ebsRespTF.getText().equals("")) {valid=false; msg=config.getText("connection.error_ebs_resp");}
			if (config.getBoolean(SETTINGS.VALIDATE_EBS_USER) && ebsPwdTF.getPassword().length==0) {
				valid=false; msg=config.getText("connection.error_ebs_pwd");
			}
		}
		
		if (!valid) {GUIHelper.showErrorDialog(this, msg);}
		
		return valid;
	}
}
 