package org.activiti;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.MessageEntity;


public class AsyncScriptTaskListener implements TaskListener {
  
  private static final long serialVersionUID = 1L;
  
  protected String script;
  
  public AsyncScriptTaskListener(String script) {
    this.script = script;
  }
  
  public void notify(DelegateTask delegateTask) {
    // Create job to execute script
    MessageEntity message = new MessageEntity();
    message.setJobHandlerConfiguration(script);
    message.setJobHandlerType(ExecuteScriptJobHandler.TYPE);
    Context.getCommandContext().getJobEntityManager().send(message);    
  }

}
