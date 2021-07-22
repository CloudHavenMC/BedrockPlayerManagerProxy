/* The copyright according to COPYRIGHT.txt applies to this file */

package world.ofunny.bpmproxy.config;

import net.md_5.bungee.config.Configuration;
import world.ofunny.bpmproxy.BedrockPlayerManagerProxy;
import world.ofunny.bpmproxy.Utils.Logger;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Data extends DataHelper {
	
	/**
	 * Dependencies 
	 */
	private Logger logger;
	
	/**
	 * Constructor
	 */
	public Data() {	
		
		// Dependencies 
		logger = Logger.get(); 
	
		// Initialisation
		setProperties();
	
	}// end Data
	
	/**
	 * General attributes
	 */
	// PLUGIN REFERNCE
	private BedrockPlayerManagerProxy plugin = null;
	
	/**
	 * Project properties
	 */
	private String pluginVersion;
	
	/**
	 * Config attributes.
	 */
	// PERMISSION MODULE
	private boolean				permissionModuleEnabled;
	private String				bedrockPermissionGroup;

	// SERVER ALIAS COMMANDS
	Map<String, List<String>> 	playerTransferCommands;

	// GENERAL SETTINGS
	private boolean				debug;
	
	// OTHER STUFF
	private String				dataFolderPath;
	
	/**
	 * Sets the current plugins reference.
	 */
	protected void setPlugin(BedrockPlayerManagerProxy bedrockPlayerManagerProxy) {
		plugin = bedrockPlayerManagerProxy;
	}// end setPlugin
	
	
	/**
	 * Requesting the project.properties file from the plugins resource folder what
	 * contains selected properties directly from the maven pom.xml 
	 * (so we do not save version and other stuff multiple times).
	 */
	private void setProperties() {
		
		// Getting a property object.
		Properties prop = new Properties();
		
		// Trying to load our property file from resources.
		try { 
			prop.load(this.getClass().getResourceAsStream("/project.properties"));
		} catch (IOException e) { 
			logger.logError("Error: 'project.properties' resource file could not be loaded correctly!"); 
		}// end try loading project properties
		
		// Setting the desired properties or a default value if not available.
		pluginVersion = prop.getProperty("version", "error");
		
		// Creating a debug log entry with the found version information.
		logger.debugLogInfo("Setting plugin version from 'project.properties' resource file – found version string: " + pluginVersion);
		
	}// end setProperties
	
	/**
	 * Will take all defined option from the config.yml and sets them within this singleton.
	 * Performs possible post processing afterwards.
	 * 
	 * We copy all config option by value in our instance for good reasons (some preperation in case we do async stuff in the future)
	 * (Natives [including strings] get passed by value and getStringList will already return a clone, so no need to do redundant work here).
	 */
	protected void setData(Configuration configuration) {

		/*
		 * Set config params.
		 */
		// PERMISSION MODULE
		permissionModuleEnabled 			= configuration.getBoolean("permissions.enabled", false);
		bedrockPermissionGroup 				= configuration.getString("permissions.bedrock_group_name", "bedrock_user");

		// SERVER ALIAS COMMANDS
		playerTransferCommands 				= getMapOfStringLists(configuration, "playerTransferCommands");
		
		// GENERAL SETTINGS
		debug 								= configuration.getBoolean("settings.debug", false);
		
		// OTHER STUFF
		dataFolderPath 						= plugin.getDataFolder().getPath()+"/data/";
		
		/*
		 * Performing additional post processing each time the config gets (re-)loaded. 
		 */
		// Set debugging within the Logger according to the plugin config.
		logger.setDebug(debug);
			
	}// end config
	
	/**
	 * Get the Plugin instance for schedulers and similar stuff.
	 * @return returns the plugins instance.
	 */
	public BedrockPlayerManagerProxy getPlugin() 					{ return plugin; }// end of plugin
	
	// PROJECT PROPERTIES
	public String getPluginVersion() 								{ return pluginVersion; }

	// PERMISSION MODULE
	public boolean isPermissionModuleEnabled() 						{ return permissionModuleEnabled; }
	public String getBedrockPermissionGroup() 						{ return bedrockPermissionGroup; }

	// SERVER ALIAS COMMANDS
	public Map<String, List<String>> getPlayerTransferCommands()	{ return playerTransferCommands; }

	// GENERAL SETTINGS
	public boolean isDebug() 										{ return debug; }
	
	// OTHER STUFF
	public String getDataFolderPath() 								{ return dataFolderPath; }

}// end Data
