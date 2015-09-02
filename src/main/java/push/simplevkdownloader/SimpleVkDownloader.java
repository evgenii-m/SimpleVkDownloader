/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package push.simplevkdownloader;

import push.simplevkdownloader.vkapi.VkApiController;

/**
 *
 * @author push
 */
public class SimpleVkDownloader {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        VkApiController vkApiController = new VkApiController();
        VkDownloadManager vkDownloadManager = new VkDownloadManager(vkApiController);
        for (String arg : args) {
            vkDownloadManager.downloadPost(arg);
        }
    }
    
}
