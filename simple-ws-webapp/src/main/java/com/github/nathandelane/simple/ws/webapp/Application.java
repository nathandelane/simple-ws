package com.github.nathandelane.simple.ws.webapp;

import static spark.Spark.awaitInitialization;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.threadPool;

import java.util.*;

import com.github.nathandelane.simple.ws.service.config.WebAppConfiguration;
import com.github.nathandelane.simple.ws.webapp.rest.HelloResource;
import com.github.nathandelane.simple.ws.webapp.rest.WebResource;

import spark.Spark;

public class Application {
  
  private final List<WebResource> webResources;
  
  private final WebAppConfiguration webAppConfig;
  
  Application() {
    webResources = new ArrayList<>();
    webResources.add(new HelloResource());
    
    webAppConfig = new WebAppConfiguration();
  }
  
  void initializeWebApplication() {
    port(webAppConfig.getWebappPortNumber());
    threadPool(webAppConfig.getMinimumThreadPoolSize(), webAppConfig.getMaximumThreadPoolSize(), webAppConfig.getThreadPoolTimeoutInMillis());
    
    for (final WebResource nextResource : webResources) {
      if (nextResource.getHttpMethod().equalsIgnoreCase("GET")) {
        get(nextResource.getResourcePath(), nextResource.getResourceRoute());
      }
    }
    
    Runtime.getRuntime().addShutdownHook(new Thread() {
      
      public void run() {
        Spark.stop();
      }
      
    });
  }
  
  void tearDownApplication() {
    
  }
  
  public static void main(final String args[]) {
    final Application app = new Application();
    app.initializeWebApplication();
    
    awaitInitialization();
  }

}
