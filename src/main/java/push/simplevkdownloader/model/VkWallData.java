/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package push.simplevkdownloader.model;

import java.util.ArrayList;

/**
 *
 * @author push
 */
public class VkWallData {
    
    private final String ownerName;
    private final long creationDate;
    private final ArrayList<String> photoUrlList;
    private final ArrayList<VkAudioRecording> audioRecordingList;
    private final String postText;
    
    
    
    public static class Builder {
        private String ownerName;
        private long creationDate;
        private ArrayList<String> photoUrlList;
        private ArrayList<VkAudioRecording> audioRecordingList;
        private String postText;
        
        public Builder() { 
            ownerName = "unknown";
            creationDate = 0;
            photoUrlList = new ArrayList<>();
            audioRecordingList = new ArrayList<>();
            postText = "empty";
        }
        
        public Builder ownerName(String val) {
            ownerName = val;
            return this;
        }
        
        public Builder creationDate(long val) {
            creationDate = val;
            return this;
        }
        
        public Builder postText(String val) {
            postText = val;
            return this;
        }
        
        public Builder photoUrlList(ArrayList<String> val) {
            photoUrlList = val;
            return this;
        }
        
        public Builder audioRecordingList(ArrayList<VkAudioRecording> val) {
            audioRecordingList = val;
            return this;
        }
        
        public VkWallData build() {
            return new VkWallData(this);
        }
    }
    
    
    
    private VkWallData(Builder builder) {
        ownerName = builder.ownerName;
        creationDate = builder.creationDate;
        postText = builder.postText;
        photoUrlList = builder.photoUrlList;
        audioRecordingList = builder.audioRecordingList;
    }
    
    public String getOwnerName() {
        return ownerName;
    }
    
    public long getCreationDate() {
        return creationDate;
    }
    
    public String getPostText() {
        return postText;
    }
    
    public int getPhotosCount() {
        return photoUrlList.size();
    }
    
    public String getPhotoUrl(int index) {
        return photoUrlList.get(index);
    }
    
    public int getAudioRecordingsCount() {
        return audioRecordingList.size();
    }
    
    public VkAudioRecording getAudioRecord(int index) {
        return audioRecordingList.get(index);
    }
    
}
