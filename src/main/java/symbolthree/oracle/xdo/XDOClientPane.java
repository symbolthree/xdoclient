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

import javax.swing.JPanel;

/* Parent class for tabbedpane */
public abstract class XDOClientPane extends JPanel implements CONSTANTS {

	private static final long serialVersionUID = 6386952920722428209L;
	
    abstract void initComponents();
    
    abstract void saveProperties();
    
    abstract boolean validateFields();
  
}
