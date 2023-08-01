package com.pnc.marketplace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pnc.marketplace.model.join.AppJoin;
import com.pnc.marketplace.service.join.JoinService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/join")
public class JoinController {

    @Autowired
    private JoinService joinService;

    @GetMapping("/info")
    ResponseEntity<?> getInformation(){

        AppJoin response = this.joinService.getInfo();

        if(response!=null)
            return ResponseEntity.status(201).body(response);
        
        log.error("Error in fetching info");
        return ResponseEntity.status(404).body(null);

    }

    
}
