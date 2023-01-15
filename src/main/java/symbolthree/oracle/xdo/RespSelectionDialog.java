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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class RespSelectionDialog extends JDialog implements ActionListener, CONSTANTS {
    private static final long serialVersionUID  = 7709750436889537451L;
    private Config                    config    = Config.instance();    
    private JButton                   button    = new JButton(config.getText("connection.select"));
    private ArrayList<Resp>           allResps  = new ArrayList<Resp>();
    private JList                     allRespList;
    private Resp                      selectedResp;

    public RespSelectionDialog(JFrame parent, String title) {
        super(parent, title, true);
        setSize(450, 300);

        JPanel    panel     = new JPanel();
        BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);

        panel.setLayout(boxLayout);
        try {
          getRespNameList();
          RespListModel listModel = new RespListModel(allResps);
          allRespList = new JList(listModel);
          allRespList.setLayoutOrientation(JList.VERTICAL);
          allRespList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
          JScrollPane listPane = new JScrollPane(allRespList);
          panel.add(listPane);
          button.addActionListener(this);
          button.setAlignmentX(CENTER_ALIGNMENT);
          panel.add(button);
          this.add(panel);
        } catch (SQLException sqle) {
          showErrorDialog(sqle.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
        	setResp((Resp)allRespList.getSelectedValue());
            this.setVisible(false);
        }
    }

    private void setResp(Resp resp) {
        this.selectedResp = resp;
    }

    public Resp getResp() {
        return selectedResp;
    }

    private void getRespNameList() throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        String     sql  =
            "select tab3.responsibility_name    " +
            "     , tab2.responsibility_key     " +
            "     , tab6.application_short_name " +
            "  from fnd_user              tab1  " +
            "     , fnd_responsibility    tab2  " + 
            "     , fnd_responsibility_tl tab3  " + 
            "     , wf_local_user_roles   tab4  " +
            "     , fnd_languages         tab5  " +
            "     , fnd_application       tab6  " +
            " where tab1.user_name=tab4.user_name " +
            "   and tab2.responsibility_id=tab3.responsibility_id" +
            "   and tab4.parent_orig_system_id=tab2.responsibility_id" +
            "   and trunc(NVL(tab4.effective_end_date,to_date('31-12-9999','DD-MM-RRRR'))) > trunc(sysdate)" +
            "   and trunc(tab4.effective_start_date) <= trunc(sysdate)" +
            "   and trunc(NVL(tab4.expiration_date,to_date('31-12-9999', 'DD-MM-RRRR'))) > trunc(sysdate)" +
            "   and tab4.parent_orig_system='FND_RESP'"  +
            "   and tab3.language=tab5.language_code" +
            "   and upper(tab4.user_name)=?" +
            "   and tab5.nls_language=?" +
            "   and tab6.application_id=tab2.application_id" +
            " order by upper(responsibility_name)";

        PreparedStatement prepStmt = conn.prepareStatement(sql);

        String username = config.getStr(CONNECTION.EBS_USER$USER).toUpperCase();
        String lang     = config.getStr(CONNECTION.LANGUAGE);
        prepStmt.setString(1, username);
        prepStmt.setString(2, lang);

        Logger.log(this, LOG_DEBUG, "Start getting all responsibilities for " + username + " in " + lang);
        ResultSet rs = prepStmt.executeQuery();

        while (rs.next()) {
        	Resp resp = new Resp();
        	resp.setName(rs.getString(1));
        	resp.setKey(rs.getString(2));
        	resp.setApp(rs.getString(3));
        	Logger.log(this, LOG_DEBUG, resp.getName());
        	allResps.add(resp);
        }
        rs.close();
        prepStmt.close();
    }
    
    protected void showErrorDialog(String errMsg) {
      JOptionPane.showMessageDialog(this, errMsg, "Error", JOptionPane.ERROR_MESSAGE);
  }
    
}
