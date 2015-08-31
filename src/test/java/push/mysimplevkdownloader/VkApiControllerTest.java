/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package push.mysimplevkdownloader;

import push.mysimplevkdownloader.vkapi.VkApiController;
import com.google.gson.JsonObject;
import static org.testng.Assert.*;
import org.testng.annotations.*;

/**
 *
 * @author push
 */
public class VkApiControllerTest {
    // resources initialized in setUpMethod()
    private VkApiController vkApiController;
    
    
    
    @DataProvider
    public Object[][] wallIdDataSet() {
       return new Object[][] {
            { "-31608969_17718" }
       };
    }
    
    @DataProvider
    public Object[][] incorrectWallIdDataSet() {
        return new Object[][] {
            { null },
            { "abcde" },
            { "91048429_31619" }    // this post doesn't available without access token
        };
    }
    
    
    
    @Test(dataProvider = "wallIdDataSet")
    public void shouldReturnWallDataByWallId(String wallId) throws Exception {
        JsonObject wallData = vkApiController.getWallDataById(wallId, true).getAsJsonObject();

        assertTrue(wallData.has("wall"), "Response hasn't \"wall\" field!");
        assertTrue(wallData.has("profiles"), "Response hasn't \"profiles\" field!");
        assertTrue(wallData.has("groups"), "Response hasn't \"groups\" field!");
                
        JsonObject wallContent = wallData.getAsJsonArray("wall").get(0).getAsJsonObject();
        String expectedId = wallId.substring(wallId.indexOf('_') + 1);
        String expectedFromId = wallId.substring(0, wallId.indexOf('_'));
        assertEquals(wallContent.get("id").toString(), expectedId);
        assertEquals(wallContent.get("from_id").toString(), expectedFromId);
    }
    
    @Test(dataProvider = "incorrectWallIdDataSet")
    public void shouldReturnEmptyResponseByIncorrectWallId(String incorrectWallId) throws Exception {
        JsonObject wallData = vkApiController.getWallDataById(incorrectWallId, true).getAsJsonObject();
        
        assertTrue((wallData.getAsJsonArray("wall").size() == 0) && 
                (wallData.getAsJsonArray("profiles").size() == 0) &&
                (wallData.getAsJsonArray("groups").size() == 0),
                "Response has non-empty field!");
    }
    
    
    
    
    
    public VkApiControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
        vkApiController = new VkApiController();
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }
    
}
