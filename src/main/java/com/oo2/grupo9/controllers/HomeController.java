package com.oo2.grupo9.controllers;

import com.oo2.grupo9.helpers.ViewRouteHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String mostrarPaginaInicio() {
        return ViewRouteHelper.INDEX;
    }
}