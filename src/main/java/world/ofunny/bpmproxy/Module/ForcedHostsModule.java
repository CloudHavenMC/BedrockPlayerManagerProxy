package world.ofunny.bpmproxy.Module;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.ServerConnectEvent;
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
    private final ProxyServer proxyServer;
    private final Floodgate floodgate;
    private final Logger logger;
    private final Config config;

    ForcedHostsModule() {

        /*
         * Local dependencies
         */
        proxyServer = ProxyServer.getInstance();
        floodgate = FloodgateAPI.get();
        logger = Logger.get();
        config = Config.get();

    }// end ForcedHostsModule


    /**
     * Called when deciding to connect to a server. At the time when this event is called, no connection has actually been made.
     * If the module has been activated in the config an a forced host has been defined, the target server will be overwritten here.
     */
    public void onServerConnectEvent(ServerConnectEvent serverConnectEvent) {

        /*
         * We just perform forced connection on proxy join!
         */
        if(serverConnectEvent.getReason() != ServerConnectEvent.Reason.JOIN_PROXY) return;

        /*
         * Performing the server assignment depending on the client the player is using.
         */
        if(floodgate.isBedrockPlayer(serverConnectEvent.getPlayer())) {

            // If a forced Bedrock host has been activated
            if(!config.getForcedBedrockServer().isEmpty()) {
                serverConnectEvent.setTarget(proxyServer.getServerInfo(config.getForcedBedrockServer()));
                logger.debugLogInfo("The Bedrock user has been forced to join " + config.getForcedBedrockServer()+"!");
            } else {
                logger.debugLogInfo("Skipping force join for the Bedrock user, because the Bedrock server is blank in the config!");
            }// end if forced Bedrock host

        } else {

            // If a forced Java host has been activated
            if(!config.getForcedJavaServer().isEmpty()) {
                serverConnectEvent.setTarget(proxyServer.getServerInfo(config.getForcedJavaServer()));
                logger.debugLogInfo("The Java user has been forced to join "+config.getForcedJavaServer()+"!");
            } else {
                logger.debugLogInfo("Skipping force join for the Java user, because the Java server is blank in the config!");
            }// end if forced java host

        }// end if Bedrock noob

    }// end onServerConnectEvent


}// end enum class ForcedHostsModule