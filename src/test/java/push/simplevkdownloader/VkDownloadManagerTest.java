/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package push.simplevkdownloader;

import push.simplevkdownloader.VkDownloadManager;
import com.google.gson.*;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;
import org.testng.annotations.*;
import push.simplevkdownloader.vkapi.VkApiController;

/**
 *
 * @author push
 */
public class VkDownloadManagerTest {
    
    VkDownloadManager vkDownloadManager;
    
    
    
    @DataProvider
    public Object[][] correctPostUrlDataSet() {
       return new Object[][] {
            { "https://vk.com/wall91048429_31619" },
            { "vk.com/wall91048429_31619" },
            { "https://www.vk.com/wall91048429_31619" },
            { "www.vk.com/wall91048429_31619" },
            { "https://vk.com/wall-91048429_31619" },
            { "https://vk.com/wall-91048429_31619/" }
       };
    }
    
    @DataProvider
    public Object[][] incorrectPostUrlDataSet() {
       return new Object[][] {
            { "-31608969_17718" },
            { "https://translate.google.ru/wall-31608969_17718" },
            { "https://vk.com/91048429_31619" },
            { "https://vk.com/wall9104842931619" },
            { "https://vk.com/wall910484293_1619ab" }
       };
    }
    
    @DataProvider
    public Object[][] mockWallDataSet() {
        return new Object[][] {
            { "-31608969_17718", "baudiozapis (2015.08.16 19-18)" }
        };
    }
    
    
    
    @Test (dataProvider = "correctPostUrlDataSet")
    public void shouldValidateCorrectUrl(String postUrl) {
        VkApiController vkApi = mock(VkApiController.class);
        vkDownloadManager = new VkDownloadManager(vkApi);
        boolean validationResult = vkDownloadManager.validatePostUrl(postUrl);
        assertTrue(validationResult, postUrl + "recognized as incorrect!");
    }
    
    @Test (dataProvider = "incorrectPostUrlDataSet")
    public void shouldValidateIncorrectUrl(String postUrl) {
        VkApiController vkApi = mock(VkApiController.class);
        vkDownloadManager = new VkDownloadManager(vkApi);
        boolean validationResult = vkDownloadManager.validatePostUrl(postUrl);        
        assertFalse(validationResult, postUrl + "recognized as correct!");
    }
    
    @Test (dataProvider = "mockWallDataSet")
    public void shouldCreateFolderForWallData(String wallId, String expectedFolderName) 
            throws Exception 
    {
        // prepare mock object
//        VkApiController vkApi = mock(VkApiController.class);
//        // before testing make sure that the JSON files exists and valid
//        String jsonDataFileName = System.getProperty("user.dir") + "/src/test/resources/wall" + 
//                wallId + ".json";
//        JsonObject json = new Gson().fromJson(new FileReader(jsonDataFileName), JsonObject.class);
//        when(vkApi.getWallDataById(wallId, true)).thenReturn(json);
//        vkDownloadManager = new VkDownloadManager(vkApi);
//        
//        Path destinationFolder = Paths.get(
//                VkDownloadManager.DEFAULT_DOWNLOAD_FOLDER_PATH + expectedFolderName);
//        Files.deleteIfExists(destinationFolder); // expected folder shouldn't be guaranteed to
//        
//        vkDownloadManager.downloadPost("https://vk.com/wall" + wallId);
//        assertTrue(Files.exists(destinationFolder), "Folder [" + expectedFolderName + "] not found.");
    }
    
    
    
    public VkDownloadManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }
    
}
