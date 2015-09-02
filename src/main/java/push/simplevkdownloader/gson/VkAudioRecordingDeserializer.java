/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package push.simplevkdownloader.gson;

import com.google.gson.*;
import java.lang.reflect.Type;
import push.simplevkdownloader.model.VkAudioRecording;

/**
 *
 * @author push
 */
public class VkAudioRecordingDeserializer implements JsonDeserializer<VkAudioRecording> {

    @Override
    public VkAudioRecording deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
            throws JsonParseException 
    {
        JsonObject audioInfo = json.getAsJsonObject();
        String performer = audioInfo.get("artist").getAsString();
        String title = audioInfo.get("title").getAsString();
        String sourceUrl = audioInfo.get("url").getAsString();
        
        VkAudioRecording.Builder vkAudioRecordingBuilder = new VkAudioRecording.Builder()
                .performer(performer)
                .title(title)
                .sourceUrl(sourceUrl);
        
        return vkAudioRecordingBuilder.build();
    }
    
}
