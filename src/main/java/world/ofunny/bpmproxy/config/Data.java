/* The copyright according to COPYRIGHT.txt applies to this file */

package world.ofunny.bpmproxy.config;

import org.spongepowered.configurate.CommentedConfigurationNode;
import world.ofunny.bpmproxy.BedrockPlayerManagerProxy;
import world.ofunny.bpmproxy.Utils.Logger;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Data extends DataHelper {
	
	/**
	 * Dependencies 
	 */
	private final Logger logger;
	
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

	// FORCED HOSTS
	private boolean				forcedHostsEnabled;
	private String				forcedJavaServer;
	private String				forcedBedrockServer;

	// SERVER ALIAS COMMANDS
	Map<String, List<String>> 	playerTransferCommands;

	// GENERAL SETTINGS
	private boolean				debug;
	
	// OTHER STUFF
	private Path dataFolderPath;
	
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
	protected void setData(CommentedConfigurationNode configuration) {

		/*
		 * Set config params.
		 */
		// PERMISSION MODULE
		permissionModuleEnabled 			= configuration.node("permissions", "enabled").getBoolean(false);
		bedrockPermissionGroup 				= configuration.node("permissions", "bedrock_group_name").getString("bedrock_user");

		// FORCED HOSTS
		forcedHostsEnabled					= configuration.node("server", "forced_hosts", "enabled").getBoolean(false);
		forcedJavaServer					= configuration.node("server", "forced_hosts", "java").getString("");
		forcedBedrockServer					= configuration.node("server", "forced_hosts", "bedrock").getString("");

		// SERVER ALIAS COMMANDS
		playerTransferCommands 				= getMapOfStringLists(configuration, "server", "playerTransferCommands");
		
		// GENERAL SETTINGS
		debug 								= configuration.node("settings", "debug").getBoolean(false);
		
		// OTHER STUFF
		dataFolderPath 						= plugin.getDataDirectory().resolve("data");
		
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

	// FORCED HOSTS
	public boolean isForcedHostsEnabled()							{ return forcedHostsEnabled; }
	public String getForcedJavaServer()								{ return forcedJavaServer; }
	public String getForcedBedrockServer()							{ return forcedBedrockServer; }

	// SERVER ALIAS COMMANDS
	public Map<String, List<String>> getPlayerTransferCommands()	{ return playerTransferCommands; }

	// GENERAL SETTINGS
	public boolean isDebug() 										{ return debug; }
	
	// OTHER STUFF
	public Path getDataFolderPath() 								{ return dataFolderPath; }

}// end Data
