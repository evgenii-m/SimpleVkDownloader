/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package push.mysimplevkdownloader.gson;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.Date;
import push.mysimplevkdownloader.model.VkWallData;

/**
 *
 * @author push
 */
public class VkWallDataDeserializer implements JsonDeserializer<VkWallData> {

    @Override
    public VkWallData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
            throws JsonParseException 
    {
        JsonObject wallContent = json.getAsJsonObject().getAsJsonArray("wall").get(0).getAsJsonObject();
        JsonObject ownerInfo = json.getAsJsonObject().getAsJsonArray("groups").get(0).getAsJsonObject();
        
        // read all necessary data from JSON objects, put them in order ...
        String ownerName = ownerInfo.get("screen_name").getAsString();
        Date creationDate = new Date(wallContent.get("date").getAsLong() * 1000);  // adduce to ms.
        String postText = wallContent.get("text").getAsString()
                .replaceAll(";amp", "&").replaceAll("<br>", "\n");
                
        // ... and build vkWallDataObjects
        VkWallData.Builder vkWallDataBuilder = new VkWallData.Builder()
                .ownerName(ownerName)
                .creationDate(creationDate)
                .postText(postText);
        
        return vkWallDataBuilder.build();
    }
    
}
