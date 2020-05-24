package com.glroland.stress;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
public class DefaultServiceController {

    @RequestMapping("/")
    public String index() {
        StringBuilder response = new StringBuilder();

        response.append("Stress Me Out!!!\n");
        response.append("\n");
        response.append("Hostname: ");
        try
        {
            response.append(InetAddress.getLocalHost().getHostAddress()).append("\n");
        }
        catch(UnknownHostException e)
        {
            response.append("Caught UnknownHostException\n").append(e.toString()).append("\n");
        }

        return response.toString();
    }
}
