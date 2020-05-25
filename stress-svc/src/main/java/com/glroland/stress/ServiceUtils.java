package com.glroland.stress;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServiceUtils {
    
    public static final String HEADER_HOST_IP = "X-HTTP-Host-IP";
    public static final int DEFAULT_TIMEOUT = 5;
    
    public static String getServiceUrl()
    {
        String stressUrl = System.getenv("STRESS_URL");
        if ((stressUrl == null) || (stressUrl.length() == 0))
        {
            stressUrl = "http://localhost:8080";
        }
        return stressUrl;
    }

    public static int getTimeLimitSeconds()
    {
        String stressTimeout = System.getenv("STRESS_TIMEOUT");
        if ((stressTimeout == null) || (stressTimeout.length() == 0))
        {
            return DEFAULT_TIMEOUT;
        }
        return Integer.parseInt(stressTimeout);
    }

    public static String getHostIp()
    {
        try
        {
//            return "(" + InetAddress. getLocalHost() + " - " + InetAddress.getLocalHost().getHostAddress() + ")";
            return InetAddress. getLocalHost().toString();
}
        catch(UnknownHostException e)
        {
            return "Caught UnknownHostException - " + e.toString();
        }
    }
}
