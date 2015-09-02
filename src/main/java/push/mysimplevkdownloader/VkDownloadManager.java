/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package push.mysimplevkdownloader;

import com.google.gson.*;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.xml.ws.http.HTTPException;
import push.mysimplevkdownloader.gson.VkWallDataDeserializer;
import push.mysimplevkdownloader.model.*;
import push.mysimplevkdownloader.vkapi.VkApiController;

/**
 *
 * @author push
 */
public class VkDownloadManager {    
    
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String DEFAULT_DOWNLOAD_FOLDER_PATH = System.getProperty("user.dir") + 
            FILE_SEPARATOR + "download";
    
    private final VkApiController vkApi;
    
    
    
    public VkDownloadManager(VkApiController vkApiController) { 
        vkApi = vkApiController;
    }
    
    public void downloadPost(String postUrl) {
        if (validatePostUrl(postUrl)) {
            System.out.println("Downloading wall data from [" + postUrl + "] started...");
            
            String wallId = postUrl.substring(postUrl.indexOf("wall") + 4);
            try {
                JsonElement wallDataJson = vkApi.getWallDataById(wallId, true);
                Gson gson = new GsonBuilder().registerTypeAdapter(
                    VkWallData.class, new VkWallDataDeserializer()).create();
                VkWallData wallData = gson.fromJson(wallDataJson, VkWallData.class);
                
                String destinationFolderName = createPostFolder(wallData);
                downloadPhotos(wallData, destinationFolderName);
                downloadAudioRecordings(wallData, destinationFolderName);
            } catch (IOException ex) {
                Logger.getLogger(VkDownloadManager.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("While downloading the IOException occurred, see log file for detail.");
            } catch (HTTPException ex) {
                Logger.getLogger(VkDownloadManager.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("While downloading the HTTPException occurred, see log file for detail.");
            }
        } else {
            System.out.println("Invalid post URL [" + postUrl + "]!");
        }
    }
    
    public boolean validatePostUrl(String postUrl) {
        String regExp = 
                "^("
                    + "https://vk.com/wall|"
                    + "vk.com/wall|"
                    + "https://www.vk.com/wall|"
                    + "www.vk.com/wall"
                + ")"
                + "-?[0-9]+_[0-9]+/?";
        Pattern regExpPattern = Pattern.compile(regExp);
        boolean validationResult = regExpPattern.matcher(postUrl).matches();
        return validationResult;
    } 
    
    
    
    private String createPostFolder(VkWallData wallData) throws IOException {
        String creationDateString = new SimpleDateFormat("yyyy.MM.dd HH-mm")
                .format(new Date(wallData.getCreationDate()));
        String folderName = wallData.getOwnerName() + " ("+ creationDateString + ")";
        folderName = folderName.replaceAll("[<>:\"\\\\/\\|\\?\\*]", " "); // replace forbidden symbols for Windows OS
        folderName = DEFAULT_DOWNLOAD_FOLDER_PATH + FILE_SEPARATOR + folderName;
        Path folderPath = Paths.get(folderName);
        if (Files.notExists(folderPath)) {
            Files.createDirectory(folderPath);
        }
        return folderName;
    }
    
    private void downloadPhotos(VkWallData wallData, String destinationFolderName) 
            throws IOException 
    {
        for (int i = 0; i < wallData.getPhotosCount(); ++i) {
            URL sourceUrl = new URL(wallData.getPhotoUrl(i));
            String destinationFileName = destinationFolderName + FILE_SEPARATOR +
                    String.format("%02d.jpg", i+1);
            FileDownloader fileDownloader = new FileDownloader(sourceUrl, destinationFileName);
        }
    }

    private void downloadAudioRecordings(VkWallData wallData, String destinationFolderName) 
            throws IOException
    {
        for (int i = 0; i < wallData.getAudioRecordingsCount(); ++i) {
            VkAudioRecording audioRecording = wallData.getAudioRecord(i);
            String audioFileName = String.format("%02d. %s - %s.mp3", 
                    i+1, audioRecording.getPerformer(), audioRecording.getTitle());
            audioFileName = audioFileName.replaceAll("[<>:\"\\\\/\\|\\?\\*]", " "); // replace forbidden symbols for Windows OS
            String destinationFileName = destinationFolderName + FILE_SEPARATOR + audioFileName;
            URL sourceUrl = new URL(audioRecording.getSourceUrl());
            FileDownloader fileDownloader = new FileDownloader(sourceUrl, destinationFileName);
        }
    }
    
}
