package world.ofunny.bpmproxy.Events;

import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import world.ofunny.bpmproxy.Module.LuckPermsModule;
import world.ofunny.bpmproxy.config.Config;

public class EventListener implements Listener {

	/*
	 * Initialisation
	 */
	private final Config config;
	private final LuckPermsModule luckPermsModule;

	/**
	 * Constructor
	 */
	public EventListener() {

		/*
		 * Local dependencies
		 */
		config = Config.get();
		luckPermsModule = LuckPermsModule.get();

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


}// end class EventListener
