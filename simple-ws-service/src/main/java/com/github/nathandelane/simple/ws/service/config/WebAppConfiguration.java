package com.github.nathandelane.simple.ws.service.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WebAppConfiguration {
  
  private static final String WEB_APP_PORT_NUMBER_CONFIG_KEY = "webapp.portNumber";
  
  private static final String WEB_APP_THREAD_POOL_MIN_CONFIG_KEY = "webapp.threadPool.min";
  
  private static final String WEB_APP_THREAD_POOL_MAX_CONFIG_KEY = "webapp.threadPool.max";
  
  private static final String WEB_APP_THREAD_POOL_TIMEOUT = "webapp.threadPool.timeout";
  
  private static final Logger LOGGER = LogManager.getLogger(WebAppConfiguration.class);
  
  private final Properties properties;
  
  public WebAppConfiguration() {
    this.properties = loadProperties();
  }
  
  public int getWebappPortNumber() {
    final int portNumber = tryToGetIntWithMinimum(WEB_APP_PORT_NUMBER_CONFIG_KEY, 9000);
    
    return portNumber;
  }
  
  public int getMinimumThreadPoolSize() {
    final int minimumThreadPoolSize = tryToGetIntWithMinimum(WEB_APP_THREAD_POOL_MIN_CONFIG_KEY, 10);
    
    return minimumThreadPoolSize;
  }
  
  public int getMaximumThreadPoolSize() {
    final int maximumThreadPoolSize = tryToGetIntWithMaximum(WEB_APP_THREAD_POOL_MAX_CONFIG_KEY, 200);
    
    return maximumThreadPoolSize;
  }
  
  public int getThreadPoolTimeoutInMillis() {
    final int maximumThreadPoolSize = tryToGetIntWithMinimum(WEB_APP_THREAD_POOL_TIMEOUT, 9000);
    
    return maximumThreadPoolSize;
  }
  
  private int tryToGetIntWithMinimum(final String configurationKey, final int minimumRequired) {
    final String propertyValue = properties.getProperty(configurationKey);
    
    int invVal = minimumRequired;
    
    if (!(propertyValue == null || propertyValue.equals(""))) {
      try {
        int propertyValueAsInt = Integer.parseInt(propertyValue);
        
        if (propertyValueAsInt >= minimumRequired) {
          invVal = propertyValueAsInt;
        }
        else {
          LOGGER.warn(String.format("Value %d is too low - must be greater than, or equal to %d.", propertyValueAsInt, minimumRequired));
        }
      }
      catch (NumberFormatException e) {
        LOGGER.warn(String.format("Could not get int value from configuration value: %s", propertyValue), e);
      }
    }
    
    return invVal;
  }
  
  private int tryToGetIntWithMaximum(final String configurationKey, final int maximumAllowed) {
    final String propertyValue = properties.getProperty(configurationKey);
    
    int invVal = maximumAllowed;
    
    if (!(propertyValue == null || propertyValue.equals(""))) {
      try {
        int propertyValueAsInt = Integer.parseInt(propertyValue);
        
        if (propertyValueAsInt <= maximumAllowed) {
          invVal = propertyValueAsInt;
        }
        else {
          LOGGER.warn(String.format("Value %d is too high - must be less than, or equal to %d.", propertyValueAsInt, maximumAllowed));
        }
      }
      catch (NumberFormatException e) {
        LOGGER.warn(String.format("Could not get int value from configuration value: %s", propertyValue), e);
      }
    }
    
    return invVal;
  }
  
  private Properties loadProperties() {
    final Properties internalProperties = new Properties();
    final URL url = getClass().getClassLoader().getResource("application.properties");
    
    try (InputStream in = new FileInputStream(new File(url.toURI()))) {
      internalProperties.load(in);
    }
    catch (IOException e) {
      LOGGER.error("Could not load application.properties as resource.", e);
    }
    catch (URISyntaxException e1) {
      LOGGER.error("COuld not convert URL to URI.");
    }
    
    return new Properties(internalProperties);
  }

}
