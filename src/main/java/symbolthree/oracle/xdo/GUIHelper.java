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
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/*
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
*/

public class GUIHelper implements CONSTANTS {

	private static Config config = Config.instance();
	
    protected static JButton createButton(String buttonName, String iconFile) {
        return new JButton(buttonName, createImageIcon(iconFile));
    }

    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = GUIHelper.class.getResource(ICON_PATH + path);

        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            Logger.log(LOG_ERROR, "Couldn't find file: " + path);
            return null;
        }
    }

    protected static JFileChooser filterFileChooser(final String ext, String file) {
		JFileChooser fc = new JFileChooser();
		if (file.equals("")) file = XDOCLIENT_PROGRAM_CFG_DIR;
    	Logger.log(GUIHelper.class, LOG_DEBUG, "Create File Dialog with filter of " + ext + " and file of " + file);
    	File f = new File(file);
    	if (f.isDirectory()) {
    		fc.setCurrentDirectory(f);
    	} else if (f.isFile()) {
    		fc.setSelectedFile(f);
    	}
		if (! ext.equals("")) {
		  fc.setFileFilter(new FileNameExtensionFilter(ext.toUpperCase() + " Files", ext));
		}
        return fc;
	}
	
	protected static JFileChooser dirChooser(String dir) {
		JFileChooser fc = new JFileChooser();
		if (dir.equals("")) dir = System.getProperty("user.dir");
    	Logger.log(GUIHelper.class, LOG_DEBUG, "Create Directory Dialog with dir of " + dir);		
		fc.setCurrentDirectory(new File(dir));
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        return fc;
	}

    protected static void showErrorDialog(Component component, String errMsg) {
        JOptionPane.showMessageDialog(component, errMsg, "Error", JOptionPane.ERROR_MESSAGE);
    }	
	
    protected static String getExtension(File f) {
        try {
            if (f != null) {
                String ext = null;
                String s   = f.getName();
                int    i   = s.lastIndexOf('.');

                if ((i >= 0) && (i < s.length() - 1)) {
                    ext = s.substring(i + 1).toLowerCase();
                }

                return ext;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    protected static void openFile(final String filename) {
        Logger.log(GUIHelper.class, LOG_DEBUG, "Open file " + filename);    	
        
        if (filename==null)  return;

        File file = new File(filename);
        if (!file.exists() || ! file.isFile()) return;
        
        String mimeType        = getExtension(file).toLowerCase();
        String programFilePath = config.getStr("settings.file_open." + mimeType);
        File program           = new File(programFilePath);
        
        try {
        // use system's extension-program mapping for Windows platform, if no mimetype defined
          if (System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") >= 0) {
              if (programFilePath.equals("")) {
           	  
            	  Logger.log(LOG_DEBUG, "Windows open file " + file.getAbsolutePath());
                  ExecutorService executorService = Executors.newSingleThreadExecutor();
                  executorService.submit(new Runnable() {
                      @Override
                      public void run() {
                        try {
                      	  ProcessBuilder builder = new ProcessBuilder();
                       	  builder.command("cmd.exe", "/c", "start", filename);
                          builder.start();
                        } catch (IOException e) {
                            e.printStackTrace();  
                        }
                      }
                  });
                  
              } else {
                openFileByProgram(filename, program);
              }
          }
          
          // non-windows platform
          if (System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") < 0) {
              if (programFilePath != null && program.exists() && program.isFile()) {
                  openFileByProgram(filename, program);              
              } else {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(file);
              }
          }
        } catch (Exception e) {
        	Logger.logError(e);
        }
    }    
    
    private static void openFileByProgram(final String filename, final File program) {
        Logger.log(GUIHelper.class, LOG_DEBUG, "Open file " + filename + " by " + program.getAbsolutePath());    	
        if (program.isFile() && program.exists()) {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                  try {
                	  ProcessBuilder builder = new ProcessBuilder();
                 	  builder.command(program.getAbsolutePath(), filename);
                      builder.start();
                  } catch (IOException e) {
                      e.printStackTrace();  
                  }
                }
            });
        } else {
        	Logger.log(LOG_ERROR, "Invalid program to open file " + filename);
        }
      }
    
    
}
