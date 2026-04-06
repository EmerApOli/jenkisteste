package com.example.appone;

import java.net.InetAddress;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/app-one")
public class AppOneController {

    @Value("${server.port}")
    private String port;

    @GetMapping("/hello")
    public Map<String, Object> hello() throws Exception {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("application", "app-one");
        response.put("message", "Olá da app-one");
        response.put("host", InetAddress.getLocalHost().getHostName());
        response.put("timestamp", OffsetDateTime.now().toString());
        return response;
    }

    @GetMapping("/info")
    public Map<String, Object> info() throws Exception {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("application", "app-one");
        response.put("version", "1.0.0");
        response.put("port", port);
        response.put("host", InetAddress.getLocalHost().getHostName());
        return response;
    }
}
