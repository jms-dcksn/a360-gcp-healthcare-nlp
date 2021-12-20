/*
 * Copyright (c) 2020 Automation Anywhere.
 * All rights reserved.
 *
 * This software is the proprietary information of Automation Anywhere.
 * You shall use it only in accordance with the terms of the license agreement
 * you entered into with Automation Anywhere.
 */

/**
 * @author James Dickson
 */
package com.automationanywhere.botcommand.samples.commands.basic;

import static com.automationanywhere.commandsdk.model.AttributeType.TEXT;
import static com.automationanywhere.commandsdk.model.DataType.STRING;

import com.automationanywhere.botcommand.exception.BotCommandException;

import java.io.IOException;
import java.util.Map;

import com.automationanywhere.botcommand.samples.Utils.GoogleHC;
import com.automationanywhere.botcommand.samples.Utils.GoogleHCActions;
import com.automationanywhere.commandsdk.annotations.BotCommand;
import com.automationanywhere.commandsdk.annotations.CommandPkg;
import com.automationanywhere.commandsdk.annotations.Execute;
import com.automationanywhere.commandsdk.annotations.Idx;
import com.automationanywhere.commandsdk.annotations.Pkg;
import com.automationanywhere.commandsdk.annotations.Sessions;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import org.json.simple.parser.ParseException;

/**
 * @author James Dickson
 */
@BotCommand
@CommandPkg(label = "Start Session",
		description = "Starts new session",
		icon = "healthcare_nlp_api.svg",
		name = "startHCSession",
		node_label = "Start Session {{sessionName}}",
		comment = true
		)
public class A_StartSession {

	@Sessions
	private Map<String, Object> sessionMap;

	@Execute
	public void execute(
			@Idx(index = "1", type = TEXT)
			@Pkg(label = "Start Session", default_value_type = STRING, default_value = "Default")
			@NotEmpty String sessionName,
			@Idx(index = "2", type = AttributeType.FILE)  @Pkg(label = "Google API Authentication File (JSON)" , default_value_type = DataType.FILE) @NotEmpty  String jsonPath,
			@Idx(index = "3", type = TEXT) @Pkg(label = "Project ID",  default_value_type = STRING) @NotEmpty String projectID,
			@Idx(index = "4", type = AttributeType.SELECT, options = {
					@Idx.Option(index = "4.1", pkg = @Pkg(label = "US Central1", value = "us-central1"))})
			@Pkg(label = "Location", default_value = "us-central1", default_value_type = STRING) @NotEmpty String location
	) throws IOException, ParseException {

		if (sessionMap.containsKey(sessionName)) {
			throw new BotCommandException("Session name in use");
		}
		String token = GoogleHCActions.getToken(jsonPath);
		GoogleHC authItems = new GoogleHC(location, token, projectID);
		sessionMap.put(sessionName, authItems);
	}
	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}
}
