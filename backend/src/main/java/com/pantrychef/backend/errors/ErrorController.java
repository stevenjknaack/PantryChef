package com.pantrychef.backend.errors;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
    @GetMapping(path="/error") 
    public String error() {
        return "YOU MESSED UP";
    }
}
