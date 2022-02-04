package org.glamey.infra.delayserver.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;

@RestController
public class HomeController {


    @RequestMapping("/")
    public Map<String, Object> home() {
        ImmutableMap<String, Object> map = ImmutableMap.<String, Object> builder()
                .put("result", "success")
                .put("message", "this is the home page")
                .put("timestamp", System.currentTimeMillis())
                .build();
        return map;
    }
}
