package com.glroland.stress;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultServiceController {

    @RequestMapping(path = "/", produces = "text/plain")
    public String index() {
        StringBuilder response = new StringBuilder();

        response.append("Stress Me Out!!!\n");
        response.append("\n");
        response.append("Hostname: ").append(ServiceUtils.getHostIp()).append("\n");

        return response.toString();
    }
}
