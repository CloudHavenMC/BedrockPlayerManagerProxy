/* The copyright according to COPYRIGHT.txt applies to this file */

package world.ofunny.bpmproxy.config;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import world.ofunny.bpmproxy.BedrockPlayerManagerProxy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Thread safe Singelton what provides the plugins configuration.
 * (Actually self explaining).
 *
 * @author ofunny
 */
public class Config extends Data {

	/*
	 * Essential member initialisation.
	 * (Do not remove the following three unless you know what you are doing).
	 */
	private static Config INSTANCE = null;
	private Configuration configuration = null;
	private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy_MM_dd-HH_mm_ss");  
	
	/*
	 * YAML  
	 */
	private final String yamlFileName		  = "config.yml";
	
	/**
	 * Constructor of the config singleton.
	 * (Leave it private, we do not allow any instances aside of this Singleton)!
	 */
	private Config(BedrockPlayerManagerProxy bedrockPlayerManagerProxy) {
		
		/*
		 *  Save Plugin reference.
		 *  IMPORTANT: Has to be set before the first call of "loadConfiguration()"!
		 */
		setPlugin(bedrockPlayerManagerProxy);
		
		/*
		 * Loading the configuration of the plugin.
		 */
		loadConfiguration();
		
	}// end config
	
	/**
	 * Initialise the Singleton.
	 * Thread safe instantiation, even if some people don't understand the sense – trust me, synchronized has a sense here for safety reasons!
	 */
	public static void initialise(BedrockPlayerManagerProxy bedrockPlayerManagerProxy) {
		if (INSTANCE == null) {
			//synchronized block to remove overhead
			synchronized (Config.class) {
				INSTANCE = new Config(bedrockPlayerManagerProxy);
			}// end synchronized
		} // end if instance already exists
	}// end Config
	
	/**
	 * Instance provider for this singleton.
	 */
	public static Config get() { return INSTANCE; }// end get
	
	/**
	 * Check whether the configuration has been loaded successfully or not.
	 */
	public boolean hasConfiguration() {
		return configuration != null;
	}// end hasConfiguration
	
	/**
	 * Loads the config of this plugin via Spigots/Papers config API.
	 * It will only load the config once (usually at plugin activation), so a reload is not possible!
	 *
	 */
	private void loadConfiguration() {
		loadConfiguration(false);
	}// end loadConfiguration
	
	/**
	 * Reloads the config of this plugin via Spigots/Papers config API.
	 * It will reload all values from the plugins config.yml (if it does not exist, it will be generated).
	 * However, changes within the developer section of the config.yml still require a server restart!
	 * (Will only work after the plugi has been set at least once, what actually happens within the constructor anyways)
	 */
	public void reloadConfiguration() {
		loadConfiguration(true);
	}// end reloadConfiguration
	
	/**
	 * Loads the config of this plugin via Spigots/Papers config API.
	 * It will only load the config once (usually at plugin activation), so a reload is not possible!
	 */
	private void loadConfiguration(boolean reload) {

		// Create the plugins data folder if it does not exist.
		createDataFolderIfAbsent();

		// Check if the config yaml exists, otherwise create the default plugins config directory.
		File file = new File(getPlugin().getDataFolder(), yamlFileName);
		if(!file.exists()) saveDefaultConfig();// Just copying the default config from the plugins resources.

		// Loading the configuration from the file.
		configuration = loadPluginConfigFromFile();

		/*
		 * If a config file other than the current version has been found, we copy it and rename it by adding "old" to its name.
		 * (Sorry I don't have the time to write a migration method what keeps the values including all comments.
		 *  And I really don't like the API version what stripes all of them! Maybe a thing for the next update.)
		 */
		if(!configuration.getString("version").equals(getPluginVersion())) {

			// Renaming the old config and copying the default config from the plugins resources.
			file.renameTo(new File(getPlugin().getDataFolder(), dateTimeFormatter.format(LocalDateTime.now()) + "-old." + yamlFileName));

			// Copy the new default config
			saveDefaultConfig();

			// In this case we have to reload the config to be on the save side!
			configuration = loadPluginConfigFromFile();

		}// end if config is from another version
		
		// sets all config options within this class and performs post processing.
		if(this.configuration != null) setData(this.configuration);// should we set all options

	}//end loadConfiguration

	/*
	 * Creates the plugins data folder if it does not exist
	 */
	private void createDataFolderIfAbsent() {
		if(!getPlugin().getDataFolder().exists()) getPlugin().getDataFolder().mkdir();
	}// end createDataFolderIfAbsent

	/*
	 * ### The following methods are needed for the Bungee API only since they do not exist there – will deliver the functionality as given by Bukkit on default. ###
	 */


	/**
	 * Saves the plugins default configuration from the resource.
	 */
	private boolean saveDefaultConfig() {

		// Unlike Bukkit, there is no saveDefaultConfig method so we have to program it on our own.
		File file = new File(getPlugin().getDataFolder(), yamlFileName);
		try (InputStream in = getPlugin().getResourceAsStream(yamlFileName)) {
			Files.copy(in, file.toPath());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}// end try copy default config

	}//end saveDefaultConfig

	/**
	 * Getting the plugins configuration from the file.
	 * (Since there is no such method as in Bukkit, we also have to do that on our own)
	 */
	private Configuration loadPluginConfigFromFile() {
		try {
			return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getPlugin().getDataFolder(), yamlFileName));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}// end try load/reload yaml
	}// end getPluginConfig

} // end class Config
