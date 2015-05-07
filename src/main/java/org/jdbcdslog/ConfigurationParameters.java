package org.jdbcdslog;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationParameters {
	
	static Logger logger = LoggerFactory.getLogger(ConfigurationParameters.class);
	
	static long slowQueryThreshold = Long.MAX_VALUE;
	
	static boolean printStackTrace = true;
	
	static boolean logText = false;
	
	static boolean noCommit = false;
	
	static boolean rollbackOnClose = false;
	
	static boolean logTime = true;
	
	static boolean printMethodName = true;
	
	static boolean logSelect = true;
	
	static {
		ClassLoader loader = ConfigurationParameters.class.getClassLoader();
		InputStream in = null;
		try {
			in = loader.getResourceAsStream("jdbcdslog.properties");
			Properties props = new Properties(System.getProperties());
			if(in != null)
				props.load(in);
			String sSlowQueryThreshold = props.getProperty("jdbcdslog.slowQueryThreshold");
			if(sSlowQueryThreshold != null && isLong(sSlowQueryThreshold))
				slowQueryThreshold = Long.parseLong(sSlowQueryThreshold);
			if(slowQueryThreshold == -1)
				slowQueryThreshold = Long.MAX_VALUE;
			String sLogText = props.getProperty("jdbcdslog.logText");
			if("true".equalsIgnoreCase(sLogText))
				logText = true;
			String sprintStackTrace = props.getProperty("jdbcdslog.printStackTrace");
			if("false".equalsIgnoreCase(sprintStackTrace))
				printStackTrace = false;
			String snoCommit = props.getProperty("jdbcdslog.noCommit");
			if("true".equalsIgnoreCase(snoCommit))
				noCommit = true;
			String srollbackOnClose = props.getProperty("jdbcdslog.rollbackOnClose");
			if("true".equalsIgnoreCase(srollbackOnClose))
				rollbackOnClose = true;
			String slogTime = props.getProperty("jdbcdslog.logTime");
			if("false".equalsIgnoreCase(slogTime))
				logTime = false;
			String sprintMethodName = props.getProperty("jdbcdslog.printMethodName");
			if("false".equalsIgnoreCase(sprintMethodName))
				printMethodName = false;
			String slogSelect = props.getProperty("jdbcdslog.logSelect");
			if("false".equalsIgnoreCase(slogSelect))
				logSelect = false;
		} catch(
				Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if(in != null)
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
		}
	}
	
	public static void setLogText(boolean alogText) {
		logText = alogText;
	}

	private static boolean isLong(String sSlowQueryThreshold) {
		try {
			Long.parseLong(sSlowQueryThreshold);
			return true;
		} catch(Exception e) {
			return false;
		}
	}
}
