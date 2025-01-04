package com.polar_moviechart.edgeservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class TestController {

    @GetMapping("/")
    public String getTestMessage() {
        return "hello polar moviechart - argocd-test";
    }
}
