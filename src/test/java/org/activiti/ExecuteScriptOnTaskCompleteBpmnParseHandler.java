package org.activiti;

import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.AbstractBpmnParseHandler;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.task.TaskDefinition;

public class ExecuteScriptOnTaskCompleteBpmnParseHandler extends AbstractBpmnParseHandler<UserTask> {

  protected String scriptingLanguage;

  protected String userTaskCompleteScript;

  protected boolean executeScriptInJob;

  public ExecuteScriptOnTaskCompleteBpmnParseHandler(String scriptingLanguage) {
    this.scriptingLanguage = scriptingLanguage;
  }

  @Override
  protected Class< ? extends BaseElement> getHandledType() {
    return UserTask.class;
  }

  @Override
  protected void executeParse(BpmnParse bpmnParse, UserTask element) {
    if (userTaskCompleteScript != null) {
      
      String taskDefinitionKey = element.getId();
      TaskDefinition taskDefinition = ((ProcessDefinitionEntity) bpmnParse.getCurrentScope()
              .getProcessDefinition()).getTaskDefinitions().get(taskDefinitionKey);
      
      if (executeScriptInJob) {
        taskDefinition.addTaskListener(TaskListener.EVENTNAME_COMPLETE, new AsyncScriptTaskListener(userTaskCompleteScript));
      } else {
        taskDefinition.addTaskListener(TaskListener.EVENTNAME_COMPLETE, new ScriptTaskListener(scriptingLanguage, userTaskCompleteScript));
      }
    }
  }

  public String getUserTaskCompleteScript() {
    return userTaskCompleteScript;
  }

  public void setUserTaskCompleteScript(String userTaskCompleteScript) {
    this.userTaskCompleteScript = userTaskCompleteScript;
  }

  public boolean isExecuteScriptInJob() {
    return executeScriptInJob;
  }

  public void setExecuteScriptInJob(boolean executeScriptInJob) {
    this.executeScriptInJob = executeScriptInJob;
  }

}
