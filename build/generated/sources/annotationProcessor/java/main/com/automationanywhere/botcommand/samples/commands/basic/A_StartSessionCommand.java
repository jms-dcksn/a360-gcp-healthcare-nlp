package com.automationanywhere.botcommand.samples.commands.basic;

import com.automationanywhere.bot.service.GlobalSessionContext;
import com.automationanywhere.botcommand.BotCommand;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.i18n.Messages;
import com.automationanywhere.commandsdk.i18n.MessagesFactory;
import java.lang.ClassCastException;
import java.lang.Deprecated;
import java.lang.Object;
import java.lang.String;
import java.lang.Throwable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class A_StartSessionCommand implements BotCommand {
  private static final Logger logger = LogManager.getLogger(A_StartSessionCommand.class);

  private static final Messages MESSAGES_GENERIC = MessagesFactory.getMessages("com.automationanywhere.commandsdk.generic.messages");

  @Deprecated
  public Optional<Value> execute(Map<String, Value> parameters, Map<String, Object> sessionMap) {
    return execute(null, parameters, sessionMap);
  }

  public Optional<Value> execute(GlobalSessionContext globalSessionContext,
      Map<String, Value> parameters, Map<String, Object> sessionMap) {
    logger.traceEntry(() -> parameters != null ? parameters.entrySet().stream().filter(en -> !Arrays.asList( new String[] {}).contains(en.getKey()) && en.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).toString() : null, ()-> sessionMap != null ?sessionMap.toString() : null);
    A_StartSession command = new A_StartSession();
    HashMap<String, Object> convertedParameters = new HashMap<String, Object>();
    if(parameters.containsKey("sessionName") && parameters.get("sessionName") != null && parameters.get("sessionName").get() != null) {
      convertedParameters.put("sessionName", parameters.get("sessionName").get());
      if(convertedParameters.get("sessionName") !=null && !(convertedParameters.get("sessionName") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","sessionName", "String", parameters.get("sessionName").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("sessionName") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","sessionName"));
    }

    if(parameters.containsKey("jsonPath") && parameters.get("jsonPath") != null && parameters.get("jsonPath").get() != null) {
      convertedParameters.put("jsonPath", parameters.get("jsonPath").get());
      if(convertedParameters.get("jsonPath") !=null && !(convertedParameters.get("jsonPath") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","jsonPath", "String", parameters.get("jsonPath").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("jsonPath") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","jsonPath"));
    }

    if(parameters.containsKey("projectID") && parameters.get("projectID") != null && parameters.get("projectID").get() != null) {
      convertedParameters.put("projectID", parameters.get("projectID").get());
      if(convertedParameters.get("projectID") !=null && !(convertedParameters.get("projectID") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","projectID", "String", parameters.get("projectID").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("projectID") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","projectID"));
    }

    if(parameters.containsKey("location") && parameters.get("location") != null && parameters.get("location").get() != null) {
      convertedParameters.put("location", parameters.get("location").get());
      if(convertedParameters.get("location") !=null && !(convertedParameters.get("location") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","location", "String", parameters.get("location").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("location") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","location"));
    }
    if(convertedParameters.get("location") != null) {
      switch((String)convertedParameters.get("location")) {
        case "us-central1" : {

        } break;
        default : throw new BotCommandException(MESSAGES_GENERIC.getString("generic.InvalidOption","location"));
      }
    }

    command.setSessionMap(sessionMap);
    try {
      command.execute((String)convertedParameters.get("sessionName"),(String)convertedParameters.get("jsonPath"),(String)convertedParameters.get("projectID"),(String)convertedParameters.get("location"));Optional<Value> result = Optional.empty();
      return logger.traceExit(result);
    }
    catch (ClassCastException e) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.IllegalParameters","execute"));
    }
    catch (BotCommandException e) {
      logger.fatal(e.getMessage(),e);
      throw e;
    }
    catch (Throwable e) {
      logger.fatal(e.getMessage(),e);
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.NotBotCommandException",e.getMessage()),e);
    }
  }
}
