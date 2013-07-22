package org.activiti;

import java.io.IOException;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Router;


public class TestApplication extends Application {
  
    @Override
    public synchronized Restlet createInboundRoot() {
      Router router = new Router(getContext());
      router.attach("/echo", EchoResource.class);
      router.attach("/scripts/task-complete.js", TaskCompleteScriptResource.class);
      return router;
    }
    
    public static class EchoResource extends ServerResource {
      
      @Post
      public void echo(Representation entity) {
        try {
          System.out.println("[ECHO SERVICE] " + entity.getText());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      
    }
    
    public static class TaskCompleteScriptResource extends ServerResource {
      
      @Get
      public String getScript() {
        String script = "";
        script = script + "importPackage(java.net);";
        script = script + "importPackage(java.io);";
        script = script + "var url = new URL('http://localhost:8182/echo');";
        script = script + "var connection = url.openConnection();";
        script = script + "connection.setRequestMethod('POST');";
        script = script + "connection.setDoOutput(true);";
        script = script + "var outputStream = new BufferedOutputStream(connection.getOutputStream());";
        script = script + "outputStream.write(new java.lang.String(\"{'eventType':'task-complete'}\").bytes);";
        script = script + "outputStream.flush();";
        script = script + "connection.connect();";
        script = script + "var respCode = connection.getResponseCode();";
        script = script + "if (respCode != 200) ";
        script = script + "println('Response code : ' + respCode);";
        script = script + "outputStream.close();";
        script = script + "connection.disconnect();";
        return script;
      }
      
    }

}

