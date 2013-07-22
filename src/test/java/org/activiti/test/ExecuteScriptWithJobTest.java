package org.activiti.test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.activiti.TestApplication;
import org.activiti.engine.ManagementService;
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

public class ExecuteScriptWithJobTest {
	
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
	@Deployment(resources = {"org/activiti/test/my-process-with-job.bpmn20.xml"})
	public void test() throws Exception{
		ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
		assertNotNull(processInstance);
		
		// There shouldn't be any variables (that sometimes happens when usng scripting)
		assertEquals(0, activitiRule.getRuntimeService().getVariables(processInstance.getId()).size());
		
		assertEquals(0, activitiRule.getManagementService().createJobQuery().count());
		
		Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
		activitiRule.getTaskService().complete(task.getId());
		
		ManagementService managementService = activitiRule.getManagementService();
		assertEquals(1, managementService.createJobQuery().count());
		
		// Execute the job
		System.out.println("Executing Job");
		managementService.executeJob(managementService.createJobQuery().singleResult().getId());
		
	}
	
}
