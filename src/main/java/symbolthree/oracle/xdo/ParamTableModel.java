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

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class ParamTableModel extends AbstractTableModel implements CONSTANTS {

	private static final long serialVersionUID = -2712513233518576275L;
	private String[] columnNames = {"Name", "DataType", "Value", "Default"};
	private ArrayList<DataTemplateParam> params = new ArrayList<DataTemplateParam>(); 
	
	@Override
	public String getColumnName(int col) {
        return columnNames[col].toString();
    }
	
	@Override
	public boolean isCellEditable(int row, int column) {
	     return column == 2;
	}

	@Override
	public int getRowCount() {
		return params.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		DataTemplateParam param = params.get(rowIndex);
		switch (columnIndex) {
		  case 0: return param.getName();
		  case 1: return param.getDataType();
		  case 2: return param.getValue();
		  case 3: return param.getDefaultValue();
		  default: return "XXXX";
		}
	}

   @Override	
   public void setValueAt(Object value, int row, int column) {
	    boolean rowExist = false;
	    DataTemplateParam param;
	    Logger.log(this, LOG_DEBUG, "setValueAt: " + value + "," + row + ","+ column);
	    if (params.size() <= row)  {
		  param = new DataTemplateParam();
	    } else {
	      rowExist = true;
	      param = params.get(row);
	    }
	    String v = value==null?"":value.toString();
		switch (column) {
		  case 0: param.setName(v);break;
		  case 1: param.setDataType(v);break;
		  case 2: param.setValue(v);break;
		  case 3: param.setDefaultValue(v);break;
		}
		if (!rowExist) params.add(param);
        fireTableCellUpdated(row, column);
    }	
	
	public void addRow(String _name, String _type, String _value, String _default) {
		DataTemplateParam param = new DataTemplateParam();
		param.setName(_name);
		param.setDataType(_type);
		param.setValue(_value);
		param.setDefaultValue(_default);
	}
}
