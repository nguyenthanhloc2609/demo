package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class DashboardController {

    @RequestMapping(value = "/clinic", method = RequestMethod.GET)
    public String home(Model model) {
        return "components/calendar/basic";
    }
}
