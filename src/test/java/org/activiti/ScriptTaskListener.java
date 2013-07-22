package org.activiti;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.scripting.ScriptingEngines;

public class ScriptTaskListener implements TaskListener {
  
  private static final long serialVersionUID = 1L;
  
  protected String language;
  protected String script;
  
  public ScriptTaskListener(String language, String script) {
    this.language = language;
    this.script = script; 
  }
  
  public void notify(DelegateTask delegateTask) {
    // Execute script directly
    ScriptingEngines scriptingEngines = Context
            .getProcessEngineConfiguration()
            .getScriptingEngines();
    scriptingEngines.evaluate(script, language, delegateTask, false);
  }

}
