package com.automationanywhere.botcommand.samples.misc;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.DictionaryValue;
import com.automationanywhere.botcommand.data.impl.ListValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.samples.Utils.GoogleHCActions;
import com.automationanywhere.botcommand.samples.Utils.HTTPRequest;
import com.google.auth.oauth2.AccessToken;

//import com.google.auth.appengine.AppEngineCredentials;
import com.google.auth.oauth2.GoogleCredentials;
//import com.google.cloud.storage.Bucket;
//import com.google.cloud.storage.Storage;
//import com.google.cloud.storage.StorageOptions;
import com.google.common.collect.Lists;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class test {

    public static void main(String[] args) throws Exception {
        String jsonPath = "C:\\Users\\jamesdickson\\OneDrive - Automation Anywhere Software Private Limited\\Documents\\Google Partnership\\Healthcare API\\covid-19-help-275415-28a5c3390f8e.json";
        String tokenString = GoogleHCActions.getToken(jsonPath);
        String projectId = "covid-19-help-275415";
        String location = "us-central1";
        String url = "https://healthcare.googleapis.com/v1/projects/"+ projectId + "/locations/"+ location + "/services/nlp:analyzeEntities";
        String response = GoogleHCActions.analyzeEntities(tokenString, "Echinacea was given to patient to boost immune system.", projectId, location);
        Map<String, Value> responseMap = new LinkedHashMap();
            //parse response
        Object obj = new JSONParser().parse(response);
        JSONObject jsonObject = (JSONObject) obj;
        JSONArray entityMentions = (JSONArray) jsonObject.get("entityMentions");
        //iterate over entityMentions and add to list
        List<StringValue> entityMentionsList = new ArrayList<>();
        List<DictionaryValue> entityPairs = new ArrayList<>();
        for (int i = 0; i < entityMentions.size(); i++) {
            String entityMention = String.valueOf(entityMentions.get(i));
            entityMentionsList.add(new StringValue(entityMention));
            //add more logic for extracting specific pairs into a new list
            Map<String, Value> map = new LinkedHashMap();
            JSONObject mention = (JSONObject) entityMentions.get(i);
            map.put("type", new StringValue(String.valueOf(mention.get("type"))));
            JSONObject textContent = (JSONObject) mention.get("text");
            map.put("content", new StringValue(String.valueOf(textContent.get("content"))));
            entityPairs.add(new DictionaryValue(map));
        }
        ListValue entityMentionsOutput = new ListValue();
        entityMentionsOutput.set(entityMentionsList);
        ListValue entityPairsOutput = new ListValue();
        entityPairsOutput.set(entityPairs);

        JSONArray entities = (JSONArray) jsonObject.get("entities");
        //iterate over entities and add to list
        List<StringValue> entitiesList = new ArrayList<>();
        for (int i = 0; i < entities.size(); i++) {
            String entity = String.valueOf(entities.get(i));
            entitiesList.add(new StringValue(entity));
        }
        ListValue entitiesListOutput = new ListValue();
        entitiesListOutput.set(entitiesList);

        JSONArray relationships = (JSONArray) jsonObject.get("relationships");
        //iterate over ...
        List<StringValue> relationshipsList = new ArrayList<>();
        if(!(relationships ==null)) {
            for (int i = 0; i < relationships.size(); i++) {
                String relationship = String.valueOf(relationships.get(i));
                relationshipsList.add(new StringValue(relationship));
            }
            ListValue relationshipsListOutput = new ListValue();
            relationshipsListOutput.set(relationshipsList);
            responseMap.put("relationships", relationshipsListOutput);
        }

        responseMap.put("entityPairs", entityPairsOutput);
        responseMap.put("entityMentions", entityMentionsOutput);
        responseMap.put("entities", entitiesListOutput);


        System.out.println(responseMap);
// OR
        //AccessToken token = credentials.refreshAccessToken();
        //HttpResponse response = request.execute();

    }


}



