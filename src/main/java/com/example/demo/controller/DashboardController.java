package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DashboardController {

    @RequestMapping(value = "/clinic", method = RequestMethod.GET)
    public String home(Model model) {
        System.out.println("home");
        return "index";
    }

    @RequestMapping(value = "/tran", method = RequestMethod.GET)
    public String transaction(Model model) {
        return "transaction/index";
    }

    @RequestMapping(value = "/tran-a-day", method = RequestMethod.GET)
    public String tranADay(Model model) {
        return "transaction/tran-a-day";
    }
}
