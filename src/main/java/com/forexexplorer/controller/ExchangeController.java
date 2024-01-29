package com.forexexplorer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/root")
public class ExchangeController {

    @GetMapping(value = "/sample")
    public ResponseEntity<String> sample() {
        return ResponseEntity.ok("Hello World");
    }

    
}
