package org.activiti.test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.activiti.TestApplication;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.restlet.Component;
import org.restlet.data.Protocol;

public class ExecuteScriptInProcessTest {
	
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();
	
	protected static Component component;
	
	@BeforeClass
	public static void setupServer() throws Exception{
	  component = new Component();  
    component.getServers().add(Protocol.HTTP, 8182);   
    component.getDefaultHost().attach(new TestApplication());
    component.start();
	}
	
	@AfterClass
	public static void closeServer() throws Exception {
	  if (component != null) {
	    component.stop();
	  }
	}
	
	@Test
	@Deployment(resources = {"org/activiti/test/my-process.bpmn20.xml"})
	public void test() throws Exception{
		ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
		assertNotNull(processInstance);
		
		// There shouldn't be any variables (that is something that could happen when usng scripts when it's not configged correctly)
		assertEquals(0, activitiRule.getRuntimeService().getVariables(processInstance.getId()).size());
		
		Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
		activitiRule.getTaskService().complete(task.getId());
	}
	
}
