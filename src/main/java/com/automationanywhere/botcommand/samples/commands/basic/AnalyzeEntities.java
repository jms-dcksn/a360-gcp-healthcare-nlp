package com.automationanywhere.botcommand.samples.commands.basic;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.DictionaryValue;
import com.automationanywhere.botcommand.data.impl.ListValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.samples.Utils.GoogleHC;
import com.automationanywhere.botcommand.samples.Utils.GoogleHCActions;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;

import static com.automationanywhere.commandsdk.model.AttributeType.TEXT;
import static com.automationanywhere.commandsdk.model.DataType.STRING;

/**
 * @author James Dickson
 */
@BotCommand
@CommandPkg(label = "Analyze Entities",
        description = "Analyze Entities",
        icon = "healthcare_nlp_api.svg",
        name = "analyze",
        node_label = "Analyze entities in medical text",
        comment = true,
        return_type = DataType.DICTIONARY,
        return_required = true,
        return_label = "Assigned to Dictionary Variable - each key contains a List",
        return_description = "Output to dictionary with keys 'entityPairs' (List of Dictionaries each with keys 'type' & 'content' " +
                "'entityMentions' (List of JSON strings), 'entities' (List of JSON strings)" +
                ", 'relationships' (List of JSON strings) - if found. If no relationships exist, a String is returned at this key."
)

public class AnalyzeEntities {
    @Sessions
    private Map<String, Object> sessionMap;

    @Execute
    public DictionaryValue execute(
            @Idx(index = "1", type = TEXT)
            @Pkg(label = "Start Session", default_value_type = STRING, default_value = "Default")
            @NotEmpty String sessionName,
            @Idx(index = "2", type = TEXT)  @Pkg(label = "Medical Text" , default_value_type = DataType.STRING, description = "Enter the text to be" +
                    "analyzed") @NotEmpty  String text
    ) throws IOException {
        if(text.equals("")){
            throw new BotCommandException("Text input cannot be empty.");
        }
        GoogleHC authItems = (GoogleHC) sessionMap.get(sessionName);
        String projectId = authItems.getProjectId();
        String token = authItems.getToken();
        String location = authItems.getLocation();
        Map<String, Value> responseMap = new LinkedHashMap();

        String response = GoogleHCActions.analyzeEntities(token, text, projectId, location);

        try {
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
            ListValue relationshipsListOutput = new ListValue();
            if(relationships != null) {
                for (int i = 0; i < relationships.size(); i++) {
                    String relationship = String.valueOf(relationships.get(i));
                    relationshipsList.add(new StringValue(relationship));
                }
            } else {
                relationshipsList.add(new StringValue("No relationships found"));
            }
            relationshipsListOutput.set(relationshipsList);

            responseMap.put("entityPairs", entityPairsOutput);
            responseMap.put("entityMentions", entityMentionsOutput);
            responseMap.put("entities", entitiesListOutput);
            responseMap.put("relationships", relationshipsListOutput);
        }
        catch (Exception e) {throw new BotCommandException("Something went wrong parsing the response. " + e);}
        return new DictionaryValue(responseMap);
    }
    public void setSessionMap(Map<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }
}
