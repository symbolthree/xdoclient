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

import oracle.apps.fnd.ext.jdbc.datasource.AppsDataSource;
import oracle.jdbc.driver.OracleDriver;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection implements CONSTANTS {

	private static DBConnection instance = null;
   private Connection          connection;
   private static Config       config = Config.instance();

   protected DBConnection(String url, String username, String password, boolean checkResp) throws SQLException {
       DriverManager.registerDriver(new OracleDriver());
       connection = DriverManager.getConnection(url, username, password);
       connection.setAutoCommit(false);
       appsSessionContext(checkResp);
   }

   protected DBConnection(String appsUser, String appsPassword, File dbcFile, boolean checkResp) throws SQLException {
       AppsDataSource ads = new AppsDataSource();

       ads.setDescription("SYMPLiK XDO Client");
       ads.setUser(appsUser);
       ads.setPassword(appsPassword);
       ads.setDbcFile(dbcFile.getAbsolutePath());
       connection = ads.getConnection();
       connection.setAutoCommit(false);
       appsSessionContext(checkResp);
   }

   public static DBConnection getInstance(String url, String username, String password, boolean checkResp) throws SQLException {
       if ((instance == null) || instance.getConnection().isClosed()) {
           instance = new DBConnection(url, username, password, checkResp);
       }

       return instance;
   }

   public static DBConnection getInstance(String appsUser, String appsPwd, File dbcFile, boolean checkResp) throws SQLException {
       if ((instance == null) || instance.getConnection().isClosed()) {
           instance = new DBConnection(appsUser, appsPwd, dbcFile, checkResp);
       }

       return instance;
   }    
   
   public static DBConnection getInstance() throws SQLException {
       if (instance == null) {
           throw new SQLException("DBConnection is not instantiated.");
       }

       return instance;
   }

   public Connection getConnection() {
       return connection;
   }

   private void appsSessionContext(boolean checkResp) throws SQLException {

       String    nlsLang = config.getStr(CONNECTION.LANGUAGE);
       Statement stmt    = connection.createStatement();

       stmt.execute("alter session set NLS_LANGUAGE='" + nlsLang + "'");
       Logger.log(this, LOG_DEBUG, "alter session set NLS_LANGUAGE='" + nlsLang + "'");

       String appsRunAsUser = config.getStr(CONNECTION.EBS_USER$USER);
       String appsRespKey   = config.getStr(CONNECTION.EBS_USER$RESP_KEY);
       String appsRespApp   = config.getStr(CONNECTION.EBS_USER$RESP_APP);
       boolean checkCredential = config.getBoolean(SETTINGS.VALIDATE_EBS_USER);
       boolean runAsEBSUser = config.getBoolean(CONNECTION.EBS_USER);

       if (runAsEBSUser && checkCredential) {
         checkRunAsCredentials();
       }
       
       if (runAsEBSUser && checkResp) {
           String sql = "select a.user_id || ',' ||" +
		                "       b.responsibility_id  || ',' ||" +
		            	"       c.application_id" +
		            	"  from fnd_user a" +
		            	"     , fnd_user_resp_groups b" +
		            	"     , fnd_responsibility c" +
		            	"     , fnd_application d" +
		            	" where 1=1" +
		            	"   and a.user_name=?" +
		            	"   and B.user_id=to_char(a.user_id)" +    
		            	"   and c.responsibility_key=?" +
		            	"   and b.responsibility_id=c.responsibility_id" +
		            	"   and b.responsibility_application_id=c.application_id" +
		            	"   and d.application_id=c.application_id" +
		            	"   and d.application_short_name=?";
           		
           PreparedStatement prepStmt = connection.prepareStatement(sql);

           prepStmt.setString(1, appsRunAsUser.toUpperCase());
           prepStmt.setString(2, appsRespKey.toUpperCase());
           prepStmt.setString(3, appsRespApp.toUpperCase());

           ResultSet rs         = prepStmt.executeQuery();
           String    sessionCtx = null;

           
           while (rs.next()) {
               sessionCtx = rs.getString(1);
           }
           rs.close();
           prepStmt.close();

           if (sessionCtx != null) {
               sql = "BEGIN fnd_global.apps_initialize(" + sessionCtx + "); END;";
               Logger.log(this, LOG_DEBUG, sql);               
               stmt.execute(sql);

               sql = "SELECT RELEASE_NAME FROM FND_PRODUCT_GROUPS";
               rs = connection.createStatement().executeQuery(sql);
               rs.next();
               String releaseName = rs.getString(1);
               rs.close();
               if (releaseName.startsWith("12.")) {
                 sql = "BEGIN MO_GLOBAL.INIT('" + appsRespApp + "'); EXCEPTION WHEN OTHERS THEN NULL; END;";
                 stmt.execute(sql);
                 Logger.log(LOG_DEBUG, sql);
               }
               
           } else {
        	   Logger.log(LOG_ERROR, "Invalid EBS username and Responsibility combination.");
               throw new SQLException();
           }
       }
   }

   private void checkRunAsCredentials() throws SQLException {
     // check fnd username and password combination
     String appsRunAsUser = config.getStr(CONNECTION.EBS_USER$USER);
     String appsRunAsPwd  = config.getPassword(CONNECTION.EBS_USER$PASSWORD);
     String sql = "select fnd_web_sec.validate_login(?,?) from dual";
     PreparedStatement prepStmt = connection.prepareStatement(sql);
     prepStmt.setString(1, appsRunAsUser);
     prepStmt.setString(2, appsRunAsPwd);
     ResultSet rs         = prepStmt.executeQuery();
     rs.next();
     String isValidCredential = rs.getString(1);
     rs.close();
     if (! isValidCredential.equals("Y")) {
       throw new SQLException("Invalid EBS credentials");
     }
   }
   
   static public void reset() {
       instance = null;
   }
   
   public static void instantiateConnection(boolean reset, boolean checkResp) throws SQLException {

       if (reset) reset();

       // DIRECT Connection mode
       if (config.getBoolean(CONNECTION.DIRECT)) {
            getInstance(config.getStr(CONNECTION.DIRECT$JDBC),
                        config.getStr(CONNECTION.DIRECT$USER),
            		    config.getPassword(CONNECTION.DIRECT$PASSWORD),
            		    checkResp
            		    );

       // APPS Connection mode
       } else if (config.getBoolean(CONNECTION.APPS_DATASOURCE)) {
           File dbcFile = new File(config.getStr(CONNECTION.APPS_DATASOURCE$DBC));

           getInstance(config.getStr(CONNECTION.APPS_DATASOURCE$USER),
        		       config.getStr(CONNECTION.APPS_DATASOURCE$PASSWORD), 
                       dbcFile,
                       checkResp);
       }
   }   
}
