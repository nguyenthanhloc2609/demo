package com.example.demo.controller;

import javax.websocket.server.PathParam;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {
    @RequestMapping(value = "/clinic", method = RequestMethod.GET)
    public String home(Model model) {
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

    @RequestMapping(value = "/customer", method = RequestMethod.GET)
    public String listCustomer(Model model) {
        return "customer/index";
    }

    @RequestMapping(value = "/customer/{id}", method = RequestMethod.GET)
    public String editCustomer(Model model, @PathVariable String id) {
        model.addAttribute("id", id);
        return "customer/edit-customer/index";
    }

    @RequestMapping(value = "/add-customer", method = RequestMethod.GET)
    public String addCustomer(Model model) {
        return "customer/add-customer/index";
    }

    @RequestMapping(value = "/procedure", method = RequestMethod.GET)
    public String listProcedure(Model model) {
        return "procedure/index";
    }

    @RequestMapping(value = "/add-tran", method = RequestMethod.GET)
    public String addTransaction(Model model) {
        return "transaction/create/index";
    }

    @RequestMapping(value = "/excel", method = RequestMethod.GET)
    public String exportExcel(Model model) {
        return "export/index";
    }
}
