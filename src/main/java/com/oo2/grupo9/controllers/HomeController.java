package com.oo2.grupo9.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oo2.grupo9.helpers.ViewRouteHelper;

import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/")
public class HomeController {

        @GetMapping("")
        public String index() {
            return ViewRouteHelper.INDEX;
        }
}
