package com.veeva.vault.util;


public class Environment {

   public static String getLogLevel() {
      String logLevel = System.getenv("LOG_LEVEL");
      if(logLevel == null) {
         logLevel = "DEBUG";
      }

      return logLevel.toLowerCase();
   }
}
