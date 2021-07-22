/* The copyright according to COPYRIGHT.txt applies to this file */

package world.ofunny.bpmproxy.Module;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.messaging.MessagingService;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.InheritanceNode;
import world.ofunny.bpmproxy.Floodgate.Floodgate;
import world.ofunny.bpmproxy.Floodgate.FloodgateAPI;
import world.ofunny.bpmproxy.Utils.Logger;
import world.ofunny.bpmproxy.config.Config;

public enum LuckPermsModule {

	/*
	 * Neue Singleton-Implementierung mittels ENUM-Type.
	 * Ist zugleich automatisch Thread-sicher und serialisierbar.
	 */
	INSTANCE;
	public static LuckPermsModule get() {
		return INSTANCE;
	} // end getInstance

	/*
	 * Initialisation
	 */
	private final String pluginName = "LuckPerms";
	private final LuckPerms luckPermsApi;
	private final ProxyServer proxyServer;
	private final Floodgate floodgate;
	private final Logger logger;
	private final Config config;
	
	/*
	 * Contructor
	 */
	LuckPermsModule() {
		
		/*
		 * Local dependencies
		 */
		proxyServer = ProxyServer.getInstance();
		floodgate = FloodgateAPI.get();
		logger = Logger.get();
		config = Config.get();
		
		/*
		 * Luckperms plugin check.
		 */
		if(proxyServer.getPluginManager().getPlugin(pluginName) == null) {
			luckPermsApi = null;
			logger.logError(pluginName+" plugin not found! "+pluginName+" must be installed on your proxy server for the permissions module to work properly – otherwise deactivate the permission module in your config.yml!");
		} else {
			luckPermsApi = LuckPermsProvider.get();
		}// if Luckperms is installed
		
	}// end LuckPermsModule
	
	/*
	 * Applies or removes the permission group defined in the plugins config file.
	 */
	public void performPermissionGroupChange(ProxiedPlayer player) {
		
		/*
		 * If LuckPermsApi is given ...
		 */
		if(luckPermsApi == null) {
			logger.logWarning("LuckPerms-Api not found: aborting now!");
			return;
		}// end if luckPermsApi

		/*
		 * Obtain group object for the Bedrock group.
		 */
        Group group = luckPermsApi.getGroupManager().getGroup(config.getBedrockPermissionGroup());

        /*
         * If this group does not exist, cancel with error,
         * then this must first be created in LuckPerms.
         */
        if (group == null) {
        	logger.logWarning("Group " + config.getBedrockPermissionGroup() + " does not exist in LukePerms, create the permission group first: aborting now!");
            return;
        }// end if group
		
        /*
         * Perform permission change.
         */
        performAsyncGroupChange(player, player.getUniqueId(), group, config.getBedrockPermissionGroup());
		 
	}// end PostLoginEvent
	
	/*
	 * Perfom permission change in any case async, no matter what Luckperms might change in its API.
	 * Also Luckperms claims to be completley ascny save, an recommends to run Luckperms Api calls async only.
	 */
	private void performAsyncGroupChange(final ProxiedPlayer player, final UUID playerUUID, final Group group, final String bedrockPermissionGroup) {

		/*
		 * Run task async
		 */
		//Bukkit.getScheduler().runTaskAsynchronously(
		proxyServer.getScheduler().runAsync(config.getPlugin(), new Runnable() {
            @Override
            public void run() {
            	
            	/*
            	 * Inits
            	 */
            	String playerName = player.getName();
            	
            	/*
                 *  Load, modify & save the user in LuckPerms.
                 */
                CompletableFuture<Void> userModificationFuture = luckPermsApi.getUserManager().modifyUser(playerUUID, (User user) -> {

                    // Create a node to add to the player.
                    Node node = InheritanceNode.builder(group).build();

                    if(floodgate.isBedrockPlayer(player)) {
                    	
                    	/*
                    	 * Add the node to the user.
                    	 */
                    	logger.debugLogInfo("LuckPerms: adding permission group '"+bedrockPermissionGroup+"' for player " + playerName);
        	            user.data().add(node);
        	            
        	            
        			} else {
        				
        				/*
        				 *  Remove the node from the user.
        				 */
        				logger.debugLogInfo("LuckPerms: removing permission group '"+bedrockPermissionGroup+"' for player " + playerName);
        	            user.data().remove(node);
        	            
        	            
        			}// end if Bedrock noob

                });

        		/*
        		 *  Since we're already on an async thread, it doesn't matter how long we have to wait for the elusive Log to show up.
        		 *  The #join method will block - and wait until the Log has been supplied, and then return it.
        		 *  If for whatever reason the process to obtain a ActionLog threw an exception,
        		 *  this method will rethrow an the same exception wrapped in a CompletionException
        		 */
                try {
                	/*
                	 * Lets try to join it.
                	 */
                	userModificationFuture.join();

                } catch (CompletionException ex) {
                	logger.logError("Command execution caused an error: " + ex.getMessage());
				}// end try dispatchCommand
                
                /*
                 * Notify other servers about the permission change.
                 */
                logger.debugLogInfo("Starting messaging for " + playerUUID.toString());
            	User LuckPermsUser = luckPermsApi.getUserManager().getUser(playerUUID);
            	if(LuckPermsUser != null) {
            		
            		/*
            		 * Since Luckperms messagin service is optional,
            		 * we have to check if it is present first otherwise we risk an exception.
            		 */
            		Optional<MessagingService> luckPermMessagingService = luckPermsApi.getMessagingService();
            		
            		/*
            		 * If present, perfom messaging.
            		 */
            		if(luckPermMessagingService.isPresent()) {
            			
            			luckPermMessagingService.get().pushUserUpdate(LuckPermsUser);
                		logger.debugLogInfo("LuckPerms: informed other servers about the permission change.");
                		
            		} else {
            			
            			logger.debugLogInfo("LuckPerms: messaging service was deactivated in the Luckperms configuration, aborting messaging!");
            			
            		}// end if present
            		
            	} else {
            		logger.logWarning("Could not push message since LuckPermsUser is null");
            	}// end if LuckPermsUser

            }//end run
        });
	}// end performAsyncGroupChange
	
} // end class(enum) LuckPermsModule
