package com.github.nathandelane.simple.ws.webapp.rest;

import spark.Request;
import spark.Response;
import spark.Route;

public class HelloResource implements WebResource {
  
  @Override
  public String getHttpMethod() {
    return "GET";
  }

  @Override
  public String getResourcePath() {
    return "/hello";
  }

  @Override
  public Route getResourceRoute() {
    return new Route() {

      public Object handle(final Request request, final Response response) throws Exception {
        return "Hello, World!";
      }
      
    };
  }

}
