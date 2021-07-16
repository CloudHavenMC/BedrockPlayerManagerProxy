package world.ofunny.bpmproxy.Floodgate;

import net.md_5.bungee.api.connection.ProxiedPlayer;

class Floodgate_Dummy implements Floodgate {

	/**
	 * Will always return false (everybody will be a Java player).
	 * Used when Floodgate is missing in solo mode (usage without Floodgate) or could not be found.
	 */
	public boolean isBedrockPlayer(ProxiedPlayer player) {
		return false;
	}// end isBedrockPlayer 
	
}// end class Floodgate_1_0