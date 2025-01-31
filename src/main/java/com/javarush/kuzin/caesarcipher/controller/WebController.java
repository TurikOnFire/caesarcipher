package com.javarush.kuzin.caesarcipher.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class WebController {
    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }
}