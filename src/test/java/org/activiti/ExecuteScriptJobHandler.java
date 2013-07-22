package org.activiti;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.jobexecutor.JobHandler;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.JobEntity;


public class ExecuteScriptJobHandler implements JobHandler {

  public static final String TYPE = "execute-script";
  
  public String getType() {
    return TYPE;
  }

  public void execute(JobEntity job, String configuration, ExecutionEntity execution, CommandContext commandContext) {
    ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
    ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");

    Reader scriptReader = null;
    try {
      URL url = new URL(configuration);
      scriptReader = new BufferedReader(new InputStreamReader(url.openStream())); 
      scriptEngine.eval(scriptReader);
    } catch (Exception e) {
      throw new ActivitiException("problem evaluating script: " + e.getMessage(), e);
    } finally {
      if (scriptReader != null) {
        try {
          scriptReader.close();
        } catch (IOException e) {
          throw new ActivitiException("Could not close script reader: " + e.getMessage(), e);
        }
      }
    }
  }


}
