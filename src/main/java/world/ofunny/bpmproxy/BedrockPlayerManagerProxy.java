/* The copyright according to COPYRIGHT.txt applies to this file */

package world.ofunny.bpmproxy;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginManager;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import net.luckperms.api.LuckPermsProvider;
import world.ofunny.bpmproxy.Events.EventListener;
import world.ofunny.bpmproxy.Module.CommandModule;
import world.ofunny.bpmproxy.Module.ForcedHostsModule;
import world.ofunny.bpmproxy.Module.LuckPermsModule;
import world.ofunny.bpmproxy.Utils.Logger;
import world.ofunny.bpmproxy.config.Config;

import java.nio.file.Path;

@Plugin(
        id = "bedrockplayermanager",
        name = "BedrockPlayerManager",
        version = "1.0",
        dependencies = {@Dependency(id = "luckperms"), @Dependency(id = "floodgate", optional = true), @Dependency(id = "geyser", optional = true)}
)
public class BedrockPlayerManagerProxy {

    @Getter
    private static BedrockPlayerManagerProxy instance;
    @Inject @Getter
    private java.util.logging.Logger serverLogger;
    @Inject @Getter
    private ProxyServer proxyServer;
    @DataDirectory @Inject @Getter
    private Path dataDirectory;

    @Subscribe
    public void onInitialize(ProxyInitializeEvent event) {
        instance = this;

        /*
         * Local dependencies
         */
        Logger.get().initialize(this);

        /*
         * Load the plugins configuration from the bungee.yml.
         */
        Config.initialise(this);
        Config config = Config.get();

        /*
         * Check if the configuration did load successfully.
         */
        if(!config.hasConfiguration()) {
            Logger.get().logError("Configuration object is null – aborting now!");
            //pluginManager.disablePlugin(this);
            return;
        }// end if configuration

        LuckPermsModule.get().initialize(this);
        ForcedHostsModule.get().initialize(this);

        /*
         * Register EventListener.
         */
        proxyServer.getEventManager().register(this, new EventListener());

        /*
         * Register Commands.
         */
        CommandModule.get().registerAllCommands();
    }

}// end BedrockPlayerManagerProxy
