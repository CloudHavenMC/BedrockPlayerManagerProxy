package world.ofunny.bpmproxy.config;

import net.md_5.bungee.config.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataHelper {

    /*
     * Simply gets a key collection of the given yaml section and convert that keys including values into a HashMap of Lists (String lists).
     * (Of course, this will only suit, if the value of each key should be just a list of strings!)
     */
    protected Map<String, List<String>> getMapOfStringLists(Configuration configuration, String path) {

        Map<String, List<String>> resultMap =  new HashMap<>();
        Configuration section = configuration.getSection(path);
        section.getKeys().forEach((String key) -> {
            resultMap.put(key, section.getStringList(key));
        });

        return resultMap;
    }//end getMapOfStrings

}// end class DataHelper
