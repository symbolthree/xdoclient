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

/* String lookup for Connection Panel */
public interface CONNECTION {
	
	public static String DIRECT                    = "connection.direct";
	public static String DIRECT$USER               = "connection.direct.user";
	public static String DIRECT$PASSWORD           = "connection.direct.password";
	public static String DIRECT$JDBC               = "connection.direct.jdbc";
	
	public static String APPS_DATASOURCE           = "connection.apps_datasource";
	public static String APPS_DATASOURCE$USER      = "connection.apps_datasource.user";
	public static String APPS_DATASOURCE$PASSWORD  = "connection.apps_datasource.password";
	public static String APPS_DATASOURCE$DBC       = "connection.apps_datasource.dbc";
	
	public static String EBS_USER                  = "connection.ebs_user";
	public static String EBS_USER$USER             = "connection.ebs_user.user"; 
	public static String EBS_USER$PASSWORD         = "connection.ebs_user.password";
	public static String EBS_USER$RESP_NAME        = "connection.ebs_user.resp_name";
	public static String EBS_USER$RESP_KEY         = "connection.ebs_user.resp_key";
	public static String EBS_USER$RESP_APP         = "connection.ebs_user.resp_app";
	
	public static String LANGUAGE                  = "connection.language";
	
}
