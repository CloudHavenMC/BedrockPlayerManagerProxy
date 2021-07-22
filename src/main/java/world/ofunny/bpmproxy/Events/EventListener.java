/* The copyright according to COPYRIGHT.txt applies to this file */

package world.ofunny.bpmproxy.Events;

import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import world.ofunny.bpmproxy.Module.ForcedHostsModule;
import world.ofunny.bpmproxy.Module.LuckPermsModule;
import world.ofunny.bpmproxy.config.Config;

public class EventListener implements Listener {

	/*
	 * Initialisation
	 */
	private final Config config;
	private final LuckPermsModule luckPermsModule;
	private final ForcedHostsModule forcedHostsModule;

	/**
	 * Constructor
	 */
	public EventListener() {

		/*
		 * Local dependencies
		 */
		config = Config.get();
		luckPermsModule = LuckPermsModule.get();
		forcedHostsModule = ForcedHostsModule.get();

	}// end constructor

	/**
	 * Event called as soon as a connection has a ProxiedPlayer and is ready to be connected to a server.
	 */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPostLoginEvent(PostLoginEvent event) {

    	/*
    	 * Perform the group change if the permission module has been activated.
    	 */
		if(config.isPermissionModuleEnabled()) luckPermsModule.performPermissionGroupChange(event.getPlayer());
    	
    }// end onPlayerJoin

	/**
	 * Called when deciding to connect to a server. At the time when this event is called, no connection has actually been made.
	 * Cancelling the event will ensure that the connection does not proceed and can be useful to prevent certain players from accessing certain servers.
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onServerConnectEvent(ServerConnectEvent serverConnectEvent) {

		/*
		 * Perform the group change if the permission module has been activated.
		 */
		if(config.isForcedHostsEnabled()) forcedHostsModule.onServerConnectEvent(serverConnectEvent);

	}// end onPlayerJoin


}// end class EventListener
