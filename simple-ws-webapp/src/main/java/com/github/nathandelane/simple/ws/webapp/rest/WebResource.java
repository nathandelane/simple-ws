package com.github.nathandelane.simple.ws.webapp.rest;

import spark.Route;

public interface WebResource {
  
  String getHttpMethod();
  
  String getResourcePath();
  
  Route getResourceRoute();

}
