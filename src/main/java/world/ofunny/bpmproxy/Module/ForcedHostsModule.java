package world.ofunny.bpmproxy.Module;

import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import world.ofunny.bpmproxy.BedrockPlayerManagerProxy;
import world.ofunny.bpmproxy.Floodgate.Floodgate;
import world.ofunny.bpmproxy.Floodgate.FloodgateAPI;
import world.ofunny.bpmproxy.Utils.Logger;
import world.ofunny.bpmproxy.config.Config;

public enum ForcedHostsModule {

    INSTANCE;

    public static ForcedHostsModule get() {
        return INSTANCE;
    } // end getInstance*/

    /*
     * Initialisation
     */
    private ProxyServer proxyServer;
    private Floodgate floodgate;
    private Logger logger;
    private Config config;

    public void initialize(BedrockPlayerManagerProxy plugin) {
        proxyServer = plugin.getProxyServer();
        floodgate = FloodgateAPI.get();
        logger = Logger.get();
        config = Config.get();
    }


    /**
     * Called when deciding to connect to a server. At the time when this event is called, no connection has actually been made.
     * If the module has been activated in the config an a forced host has been defined, the target server will be overwritten here.
     */
    public void onServerConnectEvent(ServerPreConnectEvent serverConnectEvent) {

        /*
         * We just perform forced connection on proxy join!
         */
        if (serverConnectEvent.getPreviousServer() == null)
            return;

        /*
         * Performing the server assignment depending on the client the player is using.
         */
        if(floodgate.isBedrockPlayer(serverConnectEvent.getPlayer())) {

            // If a forced Bedrock host has been activated
            if(!config.getForcedBedrockServer().isEmpty()) {
                proxyServer.getServer(config.getForcedBedrockServer()).ifPresentOrElse(server -> {
                    serverConnectEvent.setResult(ServerPreConnectEvent.ServerResult.allowed(server));
                    logger.debugLogInfo("The Bedrock user has been forced to join " + config.getForcedBedrockServer()+"!");
                }, () -> logger.debugLogWarning("The Bedrock user has failed to join " + config.getForcedBedrockServer() + " because the server couldn't be found."));
            } else {
                logger.debugLogInfo("Skipping force join for the Bedrock user, because the Bedrock server is blank in the config!");
            }// end if forced Bedrock host

        } else {

            // If a forced Java host has been activated
            if(!config.getForcedJavaServer().isEmpty()) {
                proxyServer.getServer(config.getForcedJavaServer()).ifPresentOrElse(server -> {
                    serverConnectEvent.setResult(ServerPreConnectEvent.ServerResult.allowed(server));
                    logger.debugLogInfo("The Java user has been forced to join " + config.getForcedJavaServer()+"!");
                }, () -> logger.debugLogWarning("The Java user has failed to join " + config.getForcedJavaServer() + " because the server couldn't be found."));
            } else {
                logger.debugLogInfo("Skipping force join for the Java user, because the Java server is blank in the config!");
            }// end if forced java host

        }// end if Bedrock noob

    }// end onServerConnectEvent


}// end enum class ForcedHostsModule