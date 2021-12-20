package com.automationanywhere.botcommand.samples.Utils;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.DictionaryValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.Lists;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoogleHCActions {

    public static String getToken(String jsonPath) throws IOException, ParseException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(jsonPath))
                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
        //AccessToken token = credentials.getAccessToken();
        credentials.refreshIfExpired();
        AccessToken token = credentials.getAccessToken();
        return token.getTokenValue();
    }

    public static String analyzeEntities(String token, String medicalText, String projectId, String location) throws IOException {
        String url = "https://healthcare.googleapis.com/v1/projects/"+ projectId + "/locations/"+ location + "/services/nlp:analyzeEntities";

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("nlpService", "projects/" + projectId + "/locations/" + location + "/services/nlp");
        jsonBody.put("documentContent", medicalText);
        String response = HTTPRequest.SEND("Bearer " + token, url, "POST", jsonBody.toString());
        return response;
    }

}