/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package push.simplevkdownloader.gson;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import push.simplevkdownloader.model.VkAudioRecording;
import push.simplevkdownloader.model.VkWallData;

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
        JsonArray attachments = wallContent.getAsJsonObject().getAsJsonArray("attachments");
        
        // add deserializer for VkAudioRecording class
        Gson gson = new GsonBuilder().registerTypeAdapter(
                    VkAudioRecording.class, new VkAudioRecordingDeserializer()).create();
        
        // read all necessary data from JSON objects, put them in order ...
        String ownerName = ownerInfo.get("screen_name").getAsString();
        long creationDate = wallContent.get("date").getAsLong() * 1000;  // adduce to ms.
        String postText = wallContent.get("text").getAsString()
                .replaceAll(";amp", "&").replaceAll("<br>", "\n");
        ArrayList<String> photoUrlList = new ArrayList<>();
        ArrayList<VkAudioRecording> audioRecordingList = new ArrayList<>();
        for (JsonElement attach : attachments) {
            JsonObject attachObj = attach.getAsJsonObject();
            String attachType = attachObj.get("type").getAsString();
            if (attachType.equals("photo")) {
                photoUrlList.add(attachObj.getAsJsonObject("photo").get("src_big").getAsString());
            } else if (attachType.equals("audio")) {
                VkAudioRecording audio = gson.fromJson(attachObj.getAsJsonObject("audio"), 
                        VkAudioRecording.class);
                audioRecordingList.add(audio);
            }
        }
                
        // ... and build vkWallDataObjects
        VkWallData.Builder vkWallDataBuilder = new VkWallData.Builder()
                .ownerName(ownerName)
                .creationDate(creationDate)
                .postText(postText)
                .photoUrlList(photoUrlList)
                .audioRecordingList(audioRecordingList);
        
        return vkWallDataBuilder.build();
    }
    
}
