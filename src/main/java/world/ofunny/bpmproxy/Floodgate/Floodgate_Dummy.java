/* The copyright according to COPYRIGHT.txt applies to this file */

package world.ofunny.bpmproxy.Floodgate;

import com.velocitypowered.api.proxy.Player;

class Floodgate_Dummy implements Floodgate {

	/**
	 * Will always return false (everybody will be a Java player).
	 * Used when Floodgate is missing in solo mode (usage without Floodgate) or could not be found.
	 */
	public boolean isBedrockPlayer(Player player) {
		return false;
	}// end isBedrockPlayer 
	
}// end class Floodgate_1_0