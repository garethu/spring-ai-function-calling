package com.yootiful.functioncalling.controller;

import com.yootiful.functioncalling.model.Question;
import com.yootiful.functioncalling.service.AdviceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdviceController {
    private final AdviceService adviceService;

    public AdviceController(AdviceService adviceService) {
        this.adviceService = adviceService;
    }
    @PostMapping("/advice/question")
    public ResponseEntity<String> generate(@RequestBody Question question) {
        return ResponseEntity.ok(adviceService.answer(question.text()));
    }
}

