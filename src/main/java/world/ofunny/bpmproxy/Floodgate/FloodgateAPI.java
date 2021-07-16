package world.ofunny.bpmproxy.Floodgate;

import world.ofunny.bpmproxy.Utils.Logger;

public class FloodgateAPI {
	
	/*
	 * Initialisation.
	 */
	private static FloodgateAPI INSTANCE = null;
	private final  Floodgate floodgateAPI;
	
	/**
	 * Instance provider for this singleton,
	 */
	public static Floodgate get() {
		if (INSTANCE == null) {
			//synchronized block to remove overhead
			synchronized (FloodgateAPI.class) {
				INSTANCE = new FloodgateAPI();
			}// end synchronized
		}// end if instance already exists
		return INSTANCE.floodgateAPI;
	}// end Config
	
	/**
	 * constructor
	 */
	FloodgateAPI() {

		/* Floodgate 1.0 support has been dropped, supporting 2.0< only now! */
		Logger logger = Logger.get();
		// performing detection …
		if(isClass("org.geysermc.floodgate.api.FloodgateApi")) {
			// Floodgate 2.x has been found!
			logger.debugLogInfo("initialising Floodgate API version 2.x!");
			floodgateAPI = new Floodgate_2_0();
		} else {
			// Floodgate 2.0< has not been found!
			logger.logWarning("Floodgate API version 2.x or higher has not been detected – plugin running in 'solo mode' without Floodgate, all players will be treated as Java players!");
			floodgateAPI = new Floodgate_Dummy();
		}// end if Floodgate 1, 2 or none

	}//end constructor
	
	/**
	 * Tests if a given class exists and returns true or false
	 */
	public boolean isClass(String className) {
	    try  {
	        Class.forName(className);
	        return true;
	    }  catch (ClassNotFoundException e) {
	        return false;
	    }
	}// end isClass

}// end class FloodgateAPI
