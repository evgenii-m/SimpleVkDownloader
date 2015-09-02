/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package push.mysimplevkdownloader.vkapi;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.http.HTTPException;

/**
 *
 * @author push
 */
public class VkApiController {
    
    private static final String SERVER_API_URL = "https://api.vk.com/";
    private static final String DEFAULT_EMPTY_RESPONSE = "{\"response\":[]}";
    
    
    public VkApiController() { }
    
    public JsonElement getWallDataById(String wallId, boolean extended) 
            throws IOException, HTTPException 
    {
        String requestMethodParameters = "posts=" + wallId;
        if (extended) 
            requestMethodParameters += "&extended=1";
        String response = vkApiRequest("method/wall.getById?" + requestMethodParameters);
        JsonElement wallData = packResponseToJsonObject(response);
        return wallData;
    }

    
    
    private String vkApiRequest(String requestUrlString) throws IOException, HTTPException {
        String response = null;
        URL requestUrl = new URL(SERVER_API_URL + requestUrlString);
        HttpURLConnection vkApiHttpConnection = (HttpURLConnection) requestUrl.openConnection();
        int responseCode = makeHttpRequest(vkApiHttpConnection, "POST");
        if (responseCode == HttpURLConnection.HTTP_OK) {
            response = readHttpResponse(vkApiHttpConnection);
        } else {
            throw new HTTPException(responseCode);
        }
        return response;
    }
    
    private int makeHttpRequest(HttpURLConnection httpConnection, String requestMethod) 
            throws IOException 
    {
        httpConnection.setRequestMethod(requestMethod);
        httpConnection.setDoOutput(true);
        try (OutputStream httpOutputStream = httpConnection.getOutputStream()) {
            httpOutputStream.flush();
        }
        int responseCode = httpConnection.getResponseCode();
        return responseCode;
    }
    
    private String readHttpResponse(HttpURLConnection httpConnection) throws IOException 
    {
        String response;
        try (BufferedReader httpBufferedReader = new BufferedReader(
                new InputStreamReader(httpConnection.getInputStream()))) 
        {
            String inputLine;
            StringBuilder responseStringBuilder = new StringBuilder();
            while ((inputLine = httpBufferedReader.readLine()) != null) {
                responseStringBuilder.append(inputLine);
            }
            response = responseStringBuilder.toString();
        }
        
        return response;
    }
        
    private JsonElement packResponseToJsonObject(String responseString) {    
        Gson gson = new Gson();
        JsonElement responseJson;
        try {
            responseJson = gson.fromJson(responseString, JsonObject.class).get("response");
        } catch (JsonParseException ex) {
            Logger.getLogger(VkApiController.class.getName()).log(Level.SEVERE, null, ex);
            responseJson = gson.fromJson(DEFAULT_EMPTY_RESPONSE, JsonElement.class);
        }
        return responseJson;
    }
    
}
