package world.ofunny.bpmproxy.Module;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.PluginManager;
import world.ofunny.bpmproxy.Utils.Logger;
import world.ofunny.bpmproxy.commands.PlayerTransferCommand;
import world.ofunny.bpmproxy.config.Config;

public enum CommandModule {

    INSTANCE;

    public static CommandModule get() {
        return INSTANCE;
    } // end getInstance*/

    /*
     * Initialisation
     */
    private final Logger logger;
    private final Config config;

    CommandModule() {

        /*
         * Local dependencies
         */
        logger = Logger.get();
        config = Config.get();

    }// end CmdInitialisation

    /**
     * Registers all commands used within this plugin.
     */
    public void registerAllCommands() {
        PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();
        config.getPlayerTransferCommands().forEach((serverName, commandAliasList) -> {
            // Iterate trough every alias name in the list of the given server key.
            commandAliasList.forEach((String commandAlias) -> {
                // Register each transfer command alias defined in the conig yaml (if the command alias is not empty or blank).
                if(commandAlias.isEmpty()) {
                    logger.logWarning("Registration of player transfer alias command for server "+serverName+" failed – a command name is blank or invalid, check your config.yml!");
                } else {
                    pluginManager.registerCommand(config.getPlugin(), new PlayerTransferCommand(serverName, commandAlias, ""));
                    logger.debugLogInfo("Registered player transfer alias command "+commandAlias+" for server "+serverName+"!");
                }// end if command not blank
            });// end for each alias in the current list of aliases
        });// end for each defined server key (server name)
    }// end registerAllCommands

}//end CommandModule