/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package push.mysimplevkdownloader.model;

/**
 *
 * @author push
 */
public class VkAudioRecording {
    
    private final String performer;
    private final String title;
    private final String sourceUrl;

    
    
    public static class Builder {
        private String performer;
        private String title;
        private String sourceUrl;
        
        public Builder() { 
            performer = "unknown";
            title = "unknown";
            sourceUrl = "empty";
        }
        
        public Builder performer(String val) {
            performer = val;
            return this;
        }
        
        public Builder title(String val) {
            title = val;
            return this;
        } 
        
        public Builder sourceUrl(String val) {
            sourceUrl = val;
            return this;
        }
        
        public VkAudioRecording build() {
            return new VkAudioRecording(this);
        }
    }
    
    
    
    private VkAudioRecording(Builder builder) {
        performer = builder.performer;
        title = builder.title;
        sourceUrl = builder.sourceUrl;
    }
    
    public String getPerformer() {
        return performer;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getSourceUrl() {
        return sourceUrl;
    }
    
}
