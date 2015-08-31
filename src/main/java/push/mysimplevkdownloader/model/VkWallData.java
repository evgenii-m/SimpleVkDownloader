/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package push.mysimplevkdownloader.model;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author push
 */
public class VkWallData {
    
    private final String ownerName;
    private final Date creationDate;
//    private final String creationDateString;
//    private final String folderTitle; 
//    private final ArrayList<String> photoUrlList;
//    private final ArrayList<AudioTrack> audioTrackList;
    private final String postText;
    
    
    
    public static class Builder {
        private String ownerName;
        private Date creationDate;
//        private ArrayList<String> photoUrlList;
//        private final ArrayList<AudioTrack> audioTrackList;
        private String postText;
        
        public Builder() { }
        
        public Builder ownerName(String val) {
            ownerName = val;
            return this;
        }
        
        public Builder creationDate(Date val) {
            creationDate = val;
            return this;
        }
        
        public Builder postText(String val) {
            postText = val;
            return this;
        }
        
        public VkWallData build() {
            return new VkWallData(this);
        }
    }
    
    
    
    VkWallData(Builder builder) {
        ownerName = builder.ownerName;
        creationDate = builder.creationDate;
        postText = builder.postText;
    }
    
}
