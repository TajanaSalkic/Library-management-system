package org.web.projekat.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StartController {
    
    @GetMapping("/start")
    public String showStart() {
        return "admin_page";
    }
}