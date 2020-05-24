package com.glroland.stresssvc;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultServiceController {

    @RequestMapping("/")
    public String index() {
        return "Stress Me Out!";
    }
}
