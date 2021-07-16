/* The copyright according to COPYRIGHT.txt applies to this file */

package world.ofunny.bpmproxy;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import world.ofunny.bpmproxy.Events.EventListener;
import world.ofunny.bpmproxy.Utils.Logger;
import world.ofunny.bpmproxy.config.Config;

public class BedrockPlayerManagerProxy extends Plugin {

    @Override
    public void onEnable() {


        /*
         * Holds a reference of the PluginManager.
         */
        PluginManager pluginManager = this.getProxy().getPluginManager();

        /*
         * Local dependencies
         */
        Logger logger = Logger.get();

        /*
         * Load the plugins configuration from the plugin.yml.
         */
        Config.initialise(this);
        Config config = Config.get();

        /*
         * Check if the configuration did load successfully.
         */
        if(!config.hasConfiguration()) {
            logger.logError("Configuration object is null – aborting now!");
            //pluginManager.disablePlugin(this);
            return;
        }// end if configuration

        /*
         * Register EventListener.
         */
        pluginManager.registerListener(this, new EventListener());

    }// onEnable

}// end BedrockPlayerManagerProxy
