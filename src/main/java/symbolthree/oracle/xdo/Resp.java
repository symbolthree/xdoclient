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

/* Data Object to hold responsibility info*/
public class Resp {
	
	private String name;
	private String key;
	private String app;
	
	public Resp() {
	}
	
	public Resp(String name, String key, String app) {
		this.name = name;
		this.key = key;
		this.app = app;
	}

	@Override
	/* Used by JList to get display text */
	public String toString() {
		return this.name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

}
