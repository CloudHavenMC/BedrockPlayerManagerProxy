package world.ofunny.bpmproxy.config;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataHelper {

    /*
     * Simply gets a key collection of the given yaml section and convert that keys including values into a HashMap of Lists (String lists).
     * (Of course, this will only suit, if the value of each key should be just a list of strings!)
     */
    protected Map<String, List<String>> getMapOfStringLists(CommentedConfigurationNode configuration, Object... path) {

        Map<String, List<String>> resultMap =  new HashMap<>();
        CommentedConfigurationNode subNode = configuration.node(path);
        subNode.childrenMap().forEach((k, v) -> {
            try {
                resultMap.put(k.toString(), v.getList(String.class));
            } catch (SerializationException e) {
                throw new RuntimeException(e);
            }
        });

        return resultMap;
    }//end getMapOfStrings

}// end class DataHelper
