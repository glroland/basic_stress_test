package com.glroland.stress;

public class ServiceUtils {
    
    public static String getServiceUrl()
    {
        String stressUrl = System.getenv("STRESS_URL");
        if ((stressUrl == null) || (stressUrl.length() == 0))
        {
            stressUrl = "http://localhost:8080";
        }
        return stressUrl;
    }
}