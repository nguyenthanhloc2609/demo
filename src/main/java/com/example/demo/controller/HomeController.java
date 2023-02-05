package com.example.demo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(Model model) {
        return "dashboard/index";
    }

    @RequestMapping(value = "/tran", method = RequestMethod.GET)
    public String transaction(Model model) {
        return "transaction/index";
    }

    @RequestMapping(value = "/tran/{id}", method = RequestMethod.GET)
    public String editTransaction(Model model, @PathVariable String id) {
        model.addAttribute("tranId", id);
        return "transaction/edit/index";
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

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(Model model) {
        return "metronic/index";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView begin(Model model) {
        return new ModelAndView("redirect:/home");
        // return "login/index";
        // return "metronic/custom/pages/user/login-1";
    }

    @RequestMapping(value = "/user/change-password", method = RequestMethod.GET)
    public String changePassword(Model model) {
        return "user/change-password/index";
    }

    @RequestMapping(value = "/user/new-user", method = RequestMethod.GET)
    public String newUser(Model model) {
        return "user/new-user/index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        return "login/index";
        // return "metronic/custom/pages/user/login-1";
    }
}
