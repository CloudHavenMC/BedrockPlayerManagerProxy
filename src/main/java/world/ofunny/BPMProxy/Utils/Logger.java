/* The copyright according to COPYRIGHT.txt applies to this file */

package world.ofunny.BPMProxy.Utils;

import net.md_5.bungee.api.ProxyServer;
import java.util.logging.Level;

/**
 * Enum singelton what provides the plugins Logger.
 * (Actually self explaining).
 *
 * @author ofunny
 */
public enum Logger {

	INSTANCE;
	public static Logger get() {
		return INSTANCE;
	} // end getInstance
	
	/*
	 * Initialise
	 */
	private java.util.logging.Logger logger;
	private boolean debug = false;
	private final String logMessagePrefix = "[BedrockPlayerManager]: ";
	
	/**
	 * Defualt logger constructor.
	 */
	private Logger() {
		logger = ProxyServer.getInstance().getLogger();
	}// end Utils

	/**
	 * Toggles debug output (if used in the plugin).
	 * @param debug true to activate, false to deactivate debugging.
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}// end debug
	
	/**
	 * Get default logger instance.
	 */
	private java.util.logging.Logger getLogger() {
		return logger;
	}// get Logger

	/**
	 * Create a error log entry with the given description.
	 * @param Message a text note to describe/explain the error.
	 */
	public void logError(String Message) {
		getLogger().log(Level.SEVERE, logMessagePrefix+Message);
	}// end log

	/**
	 * Create a warning log entry with the given description.
	 * @param Message a text note to describe/explain the warning.
	 */
	public void logWarning(String Message) {
		getLogger().log(Level.WARNING, logMessagePrefix+Message);
	}// end log

	/**
	 * Create a info log entry with the given description.
	 * @param Message a text note to describe/explain the entry.
	 */
	public void logInfo(String Message) {
		getLogger().log(Level.INFO, logMessagePrefix+Message);
	}// end log


	/**
	 * Create a debug error log entry with the given description.
	 * @param Message a text note to describe/explain the error.
	 */
	public void debugLogError(String Message) {
		if(debug) getLogger().log(Level.SEVERE, logMessagePrefix+Message);
	}// end log

	/**
	 * Create a debug warning log entry with the given description.
	 * @param Message a text note to describe/explain the warning.
	 */
	public void debugLogWarning(String Message) {
		if(debug) getLogger().log(Level.WARNING, logMessagePrefix+Message);
	}// end log

	/**
	 * Create a debug info log entry with the given description.
	 * @param Message a text note to describe/explain the entry.
	 */
	public void debugLogInfo(String Message) {
		if(debug) getLogger().log(Level.INFO, logMessagePrefix+Message);
	}// end log

} // end class(enum) Utils