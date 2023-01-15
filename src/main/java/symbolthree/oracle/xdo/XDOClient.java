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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.SplashScreen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class XDOClient extends JFrame implements ActionListener, ChangeListener, CONSTANTS {
	private static final long serialVersionUID = 5962430543470956137L;
	private JMenuBar    mainMenu     = new JMenuBar();	
	private JMenu       actionMenu   = new JMenu();
	private JMenuItem   generateItem = new JMenuItem();
	private JMenuItem   saveItem     = new JMenuItem();
	private JMenuItem   saveAsItem   = new JMenuItem();
	private JMenuItem   loadItem     = new JMenuItem();
	private JMenuItem   clearLogItem = new JMenuItem();
	private JMenuItem   exitItem     = new JMenuItem();
	private JMenu       helpMenu     = new JMenu();
	private JMenuItem   helpItem     = new JMenuItem();
	private JMenuItem   aboutItem    = new JMenuItem();
	private JMenuItem   refreshItem  = new JMenuItem();
	private JTabbedPane tabbedPane   = new JTabbedPane();
	private JTextArea   logTextArea  = new JTextArea(5,50);
	
	private TemplatePane   templatePane   = new TemplatePane();
	private ConnectionPane connectionPane = new ConnectionPane();
	private SettingPane    settingPane    = new SettingPane();
	private static Config  config         = Config.instance(); 
	
    public XDOClient() {
    	showSplashScreen();
	}
	
    public static void main(String[] args) {
    	try {
    	  Logger.initializeLogging();
    	} catch (Exception e) {
    		System.out.println("Unable to initialize logging. System abort.");
    		System.exit(1);
    	}
    	config.setFontDir();
    	config.setLNF();
    	XDOClient xdoClient = new XDOClient();
    	xdoClient.redirectSystemStreams();
    	// check argument 1 (xdoc file)
    	if (args.length==1) {
    		Logger.log(LOG_INFO, "Input Argument: " + args[0]);
    		File file = new File(args[0]);
    		if (file.exists() && file.isFile()) {
    	    	xdoClient.start(file);
    	    	return;
    		}
    	}
    	xdoClient.start();

    }
    
    private void start(File cfgFile) {
    	config.loadConfig(cfgFile);
    	start();
    }
    
    private void start() {
    	Logger.log(this, LOG_DEBUG, "Java Home: " + System.getProperty("java.home"));
    	Logger.log(this, LOG_DEBUG, "Java Runtime: " + System.getProperty("java.runtime.version"));
    	
    	this.setIconImage(new ImageIcon(getClass().getResource(ICON_PATH + "icon.gif")).getImage());
    	this.addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent winEvt) {
  			settingPane.saveProperties();
            config.saveProgamCfgFile();
            System.exit(0);
          }
        });
    	setProgramTitle();
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.createMenu();
		
    	Container mainFrameContentPane = this.getContentPane();
    	mainFrameContentPane.setLayout(new GridBagLayout());
    	GridBagConstraints GC = new GridBagConstraints();
    	
    	JPanel mainPane = new JPanel();
    	mainPane.setLayout(new BorderLayout());

    	tabbedPane.addChangeListener(this);
		Logger.log(this, LOG_DEBUG, "Start creating template panel...");
    	templatePane.initComponents();
    	tabbedPane.addTab(config.getText("template.tab"), GUIHelper.createImageIcon("focus_active.gif"), templatePane);    	
		Logger.log(this, LOG_DEBUG, "Start creating connection panel...");    	
    	connectionPane.initComponents();
    	tabbedPane.addTab(config.getText("connection.tab"), GUIHelper.createImageIcon("focus_inactive.gif"), connectionPane);    	
		Logger.log(this, LOG_DEBUG, "Start creating setting panel...");    	
    	settingPane.initComponents();
    	tabbedPane.addTab(config.getText("settings.tab"), GUIHelper.createImageIcon("focus_inactive.gif"), settingPane);
    	
    	GC.gridx = 0;
    	GC.gridy = 0;
    	//mainPane.add(tabbedPane, BorderLayout.CENTER);    	
    	//mainFrameContentPane.add(mainPane, GC);
    	mainFrameContentPane.add(tabbedPane, GC);    	
    	
		Logger.log(this, LOG_DEBUG, "start adding log textarea...");
    	JScrollPane scrollPane = new JScrollPane();
    	scrollPane.setPreferredSize(new Dimension(100, 150));
    	logTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN,  12));
    	logTextArea.setForeground(Color.DARK_GRAY);
    	logTextArea.setEditable(false);
    	logTextArea.setLineWrap(false);
		scrollPane.setViewportView(logTextArea);

    	GC.gridx = 0;
    	GC.gridy = 1;
    	GC.fill = GridBagConstraints.HORIZONTAL;
		mainFrameContentPane.add(scrollPane, GC);
		
		this.pack();
		this.setLocationRelativeTo(this.getOwner());
		this.setResizable(false);
		this.setVisible(true);
		Logger.log(this, LOG_DEBUG, "GUI rendering done.");
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == exitItem) {
			settingPane.saveProperties();
			config.saveProgamCfgFile();
			System.exit(0);
		}
		if (e.getSource() == clearLogItem) {
            SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                	logTextArea.setText(null);
               }
            });
		}
		if (e.getSource() == generateItem) {
			if (saveProperties()) {
			  config.saveProgamCfgFile();				
			  this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));			
   			  XDOAction action = new XDOAction();
   			  action.start();
	          this.setCursor(Cursor.getDefaultCursor());

	          if (config.getBoolean(TEMPLATE.XML) && config.getBoolean(TEMPLATE.XML$OPEN_FILE)) {
	        	  GUIHelper.openFile(action.getXmlDataFile().getAbsolutePath());
	          }
	        
	          if (config.getBoolean(TEMPLATE.DOC) && config.getBoolean(TEMPLATE.DOC$OPEN_FILE)) {
	        	  GUIHelper.openFile(action.getPublishDocFile().getAbsolutePath());
	          }
			}
		}
		
		if (e.getSource() == saveItem) {
			if (saveProperties()) {
	    	  config.saveFile();
	    	  Logger.log(LOG_INFO, "XDOClient file saved successfully.");
			}
		}
		
		if (e.getSource() == saveAsItem) {
			if (saveProperties()) {
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(
						new FileNameExtensionFilter("XDOClient Config file (*." + XDOCLIENT_USER_CFG_EXT + ")", XDOCLIENT_USER_CFG_EXT));
				/** set default directory as directory for loaded file **/
				String currentFile = System.getProperty(XDOCLIENT_USER_CFG);
				if (currentFile != null && ! currentFile.equals("")) {
					fc.setSelectedFile(new File(currentFile, "new1." + XDOCLIENT_USER_CFG_EXT));
				} else {
					fc.setSelectedFile(new File(System.getProperty("user.dir"), "new1." + XDOCLIENT_USER_CFG_EXT));					
				}

				int rtnVal = fc.showSaveDialog(XDOClient.this);
				if (rtnVal == JFileChooser.APPROVE_OPTION) {
					String cfgFile = fc.getSelectedFile().getAbsolutePath();
					if (!cfgFile.endsWith("." + XDOCLIENT_USER_CFG_EXT)) cfgFile=cfgFile + "." + XDOCLIENT_USER_CFG_EXT;
					System.setProperty(XDOCLIENT_USER_CFG, cfgFile);
		    	    config.saveFile();
		    	    System.setProperty(XDOCLIENT_USER_CFG, cfgFile);
		    	    setProgramTitle();		    	    
		    	    saveItem.setEnabled(true);
					Logger.log(LOG_INFO, fc.getSelectedFile().getName());
				}				
			}
		}
		
		if (e.getSource()==loadItem) {
			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(
					new FileNameExtensionFilter("XDOClient file (*." + XDOCLIENT_USER_CFG_EXT + ")", XDOCLIENT_USER_CFG_EXT));
			fc.setSelectedFile(new File(System.getProperty("user.dir")));
			int rtnVal = fc.showOpenDialog(XDOClient.this);
			if (rtnVal == JFileChooser.APPROVE_OPTION) {
				if (config.loadConfig(fc.getSelectedFile())) {
				  Logger.log(LOG_INFO, "xdoc file " + fc.getSelectedFile().getName() + " loaded");
				  templatePane.initComponents();
				  connectionPane.initComponents();
				  //settingPane.initComponents();
				  //this.setTitle("[" + fc.getSelectedFile().getAbsolutePath() + "] - " + PROGRAM_TITLE);
		    	  setProgramTitle();				  
				  saveItem.setEnabled(true);
				}
			}
		}
		
		if (e.getSource()==refreshItem || e.getSource()==tabbedPane) {
			refreshScreen();
		}
		
		if (e.getSource()==aboutItem) {
            AboutDialog f =  new AboutDialog();
            f.setLocationRelativeTo(this);
            f.setVisible(true);
		}
		
		if (e.getSource()==helpItem) {
		  try {
			Desktop.getDesktop().browse(new URI(config.getText("menu.help_link")));
          } catch (Exception ex) {
		  }
		}
	}
	
	private void setProgramTitle() {
		String cfgFile = System.getProperty(XDOCLIENT_USER_CFG);
		if (cfgFile != null && !cfgFile.equals("")) {
	      this.setTitle("[" + cfgFile + "] - " + PROGRAM_TITLE);
		} else {
		  this.setTitle(PROGRAM_TITLE);	
		}
	}
	
	private void refreshScreen() {
	    SwingUtilities.updateComponentTreeUI(this);
		this.repaint();	    
		this.pack();
	}
	
	private boolean saveProperties() {
		boolean valid = templatePane.validateFields() &&
				        connectionPane.validateFields() &&
				        settingPane.validateFields(); 
		if (valid) {
    	   templatePane.saveProperties();
    	   connectionPane.saveProperties();
    	   settingPane.saveProperties();
		}
		return valid;
	}
	
    private void updateTextArea(final String str) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	logTextArea.append(str);
            }
        });
    }

    protected void redirectSystemStreams() {
        OutputStream out = new OutputStream() {
            public void write(int b) throws IOException {
                updateTextArea(String.valueOf((char) b));
            }
            public void write(byte[] b, int off, int len) throws IOException {
                updateTextArea(new String(b, off, len));
            }
            public void write(byte[] b) throws IOException {
                write(b, 0, b.length);
            }
        };

        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }	

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource()==tabbedPane) {
			int selected = tabbedPane.getSelectedIndex();
			for (int i=0;i<tabbedPane.getTabCount();i++) {
				if (i!=selected) {
					tabbedPane.setIconAt(i, GUIHelper.createImageIcon("focus_inactive.gif"));
				} else {
					tabbedPane.setIconAt(i, GUIHelper.createImageIcon("focus_active.gif"));
				}
			}
		}
	}
	
	private void createMenu() {
		Logger.log(this, LOG_DEBUG, "Start creating Menu...");
		
		actionMenu.setText(config.getText("menu.actions"));
		generateItem.setText(config.getText("menu.generate"));
		generateItem.setMnemonic('G');
		generateItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
		generateItem.setIcon(GUIHelper.createImageIcon("yes.gif"));		
		generateItem.addActionListener(this);
		actionMenu.add(generateItem);
		
		clearLogItem.setText(config.getText("menu.clear_log"));
		clearLogItem.setMnemonic('L');
		clearLogItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		clearLogItem.addActionListener(this);
		clearLogItem.setIcon(GUIHelper.createImageIcon("clear_log.gif"));
		actionMenu.add(clearLogItem);

		actionMenu.addSeparator();
		
		loadItem.setText(config.getText("menu.load_config"));
		loadItem.setMnemonic('L');
		loadItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		loadItem.addActionListener(this);
		loadItem.setIcon(GUIHelper.createImageIcon("open.gif"));
		actionMenu.add(loadItem);

		saveItem.setText(config.getText("menu.save_config"));
		saveItem.setMnemonic('S');
		saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		saveItem.setIcon(GUIHelper.createImageIcon("save.gif"));
		saveItem.addActionListener(this);
		if (System.getProperty(XDOCLIENT_USER_CFG)==null) saveItem.setEnabled(false);
		actionMenu.add(saveItem);
		
		saveAsItem.setText(config.getText("menu.save_config_as"));
		saveAsItem.setMnemonic('A');
		saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		saveAsItem.setIcon(GUIHelper.createImageIcon("saveas.gif"));
		saveAsItem.addActionListener(this);
		actionMenu.add(saveAsItem);
		
		actionMenu.addSeparator();
		
		exitItem.setText(config.getText("menu.exit"));
		exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
		exitItem.setIcon(GUIHelper.createImageIcon("no.gif"));
		exitItem.addActionListener(this);
		actionMenu.add(exitItem);
		mainMenu.add(actionMenu);
		Logger.log(this, LOG_DEBUG, "Action Menu done");
		
		helpMenu.setText(config.getText("menu.help"));
		
		helpItem.setText(config.getText("menu.help_content"));
		helpItem.addActionListener(this);
		helpItem.setIcon(GUIHelper.createImageIcon("help.gif"));
		helpMenu.add(helpItem);
		
		helpMenu.addSeparator();
		
		refreshItem.setText(config.getText("menu.refresh_window"));
		refreshItem.addActionListener(this);
		refreshItem.setIcon(GUIHelper.createImageIcon("refresh.gif"));
		helpMenu.add(refreshItem);
		
		helpMenu.addSeparator();
		
		aboutItem.setText(config.getText("menu.about"));
		aboutItem.addActionListener(this);
		aboutItem.setIcon(GUIHelper.createImageIcon("info.gif"));
		helpMenu.add(aboutItem);

		mainMenu.add(helpMenu);
		Logger.log(this, LOG_DEBUG, "Help Menu done");		
		
	    this.setJMenuBar(mainMenu);
	}
    
    private void showSplashScreen() {
        SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash == null) return;
        Graphics2D g = splash.createGraphics();
        if (g == null) return;
        try {
            Thread.sleep(500);
        }
        catch(InterruptedException e) {}       
    } 	
}
