package com.example.demo.controller;

import com.example.demo.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HelloController {

    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @GetMapping("/hello")
    public String sayHello() {
        logger.info("sayHello был вызван");
        return "Привет, мир!";
    }

    @PostMapping("/echo")
    public Message echo(@RequestBody Message input) {
        logger.info("echo был вызван");
        return input;
    }
}
