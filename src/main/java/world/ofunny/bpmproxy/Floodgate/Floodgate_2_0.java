package world.ofunny.bpmproxy.Floodgate;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.geysermc.floodgate.api.FloodgateApi;

class Floodgate_2_0 implements Floodgate {
	
	/*
	 * Members
	 */
	private final FloodgateApi floodgateApi;
	
	/**
	 * Constructor
	 */
	public Floodgate_2_0() {
		
		// Just holding the refernce to save the lookup each call.
		floodgateApi = FloodgateApi.getInstance();
		
	}// end Floodgate_2_0
	
	/**
	 * Check if user is coming from Floodgate
	 */
	public boolean isBedrockPlayer(ProxiedPlayer player) {
		return floodgateApi.isFloodgatePlayer(player.getUniqueId());
	}// end isBedrockPlayer 
	
}// end class Floodgate_2_0