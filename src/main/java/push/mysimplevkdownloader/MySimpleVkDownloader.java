/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package push.mysimplevkdownloader;

import com.google.gson.*;
import push.mysimplevkdownloader.vkapi.VkApiController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.http.HTTPException;
import push.mysimplevkdownloader.gson.VkWallDataDeserializer;
import push.mysimplevkdownloader.model.VkWallData;

/**
 *
 * @author push
 */
public class MySimpleVkDownloader {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        VkApiController vkApiController = new VkApiController();
        try {
            JsonElement vkWallDataJson = vkApiController.getWallDataById("-31608969_17718", true);
            Gson gson = new GsonBuilder().registerTypeAdapter(
                    VkWallData.class, new VkWallDataDeserializer()).create();
            VkWallData vkWallData = gson.fromJson(vkWallDataJson, VkWallData.class);
            System.out.println("Great!");
        } catch (IOException ex) {
            Logger.getLogger(MySimpleVkDownloader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HTTPException ex) {
            Logger.getLogger(MySimpleVkDownloader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
