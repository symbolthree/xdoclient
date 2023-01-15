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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

public class Logger implements CONSTANTS {
	
    final static private SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMdd.HHmmss.SSS");
    private static FileWriter             logWriter;
    private static Config config   = Config.instance();
    
    public static void initializeLogging() throws IOException {

        // default logging is: log level = INFO; log output = SYSTEM.OUT
        String val = config.getStr(SETTINGS.LOG_OUTPUT);

        if (val.equals(LOG_OUTPUT_FILE)) {
            //File logFile = new File(System.getProperty("user.dir"), LOG_OUTPUT_FILENAME);
        	File logFile = new File(XDOCLIENT_PROGRAM_CFG_DIR, LOG_OUTPUT_FILENAME);

            if (logFile.exists()) {
                logFile.delete();
            }

            logWriter = new FileWriter(logFile);
        }
    }	
	
    private static String logLevelStr(int logLevel) {
        if (logLevel == LOG_DEBUG) {
            return "DEBUG";
        }

        if (logLevel == LOG_INFO) {
            return "INFO";
        }

        if (logLevel == LOG_WARN) {
            return "WARN";
        }

        if (logLevel == LOG_ERROR) {
            return "ERROR";
        }

        return String.valueOf("DEBUG");
    }

    private static int logLevelInt(String logLevel) {
        if (logLevel.equals("DEBUG")) {
            return LOG_DEBUG;
        }

        if (logLevel.equals("INFO")) {
            return LOG_INFO;
        }

        if (logLevel.equals("WARN")) {
            return LOG_WARN;
        }

        if (logLevel.equals("ERROR")) {
            return LOG_ERROR;
        }

        return LOG_ERROR;
    }
    
	public static void logError(Throwable t) {
        if (config.getStr(SETTINGS.LOG_LEVEL).equals(logLevelStr(LOG_DEBUG))) {
            log(LOG_ERROR, getStackTrace(t));
        } else {
            log(LOG_ERROR, t.getMessage());
        }
    }

    private static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter  pw = new PrintWriter(sw, true);

        t.printStackTrace(pw);
        pw.flush();
        sw.flush();

        return sw.toString();
    }	
	
    public static void log(Object c, int logLevel, String logMsg) {
    	String className = c.getClass().getSimpleName();
    	if (className.equals("Class")) {
    		className = c.toString();
    		className = className.substring(className.lastIndexOf(".")+1); 
    	}
        String logMsg2 = className + " - " + logMsg;

        log(logLevel, logMsg2);
    }
    
    public static void log(int logLevel, String logMsg) {
        int    logThreshold = logLevelInt(config.getStr(SETTINGS.LOG_LEVEL));
        String logOutput    = config.getStr(SETTINGS.LOG_OUTPUT);
        String logLine      = timeFormat.format(new java.util.Date()) + 
        		              " - [" + logLevelStr(logLevel) + "] - " + 
        		              logMsg;

        if ((logLevel >= logThreshold) || (logLevel == LOG_ERROR)) {
            if (logOutput.equals(LOG_OUTPUT_SYSTEM_OUT)) {
                System.out.println(logLine);
            } else if (logOutput.equals(LOG_OUTPUT_FILE)) {
                try {
                    logWriter.write(logLine + LINE_SEP);
                    logWriter.flush();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }
}
