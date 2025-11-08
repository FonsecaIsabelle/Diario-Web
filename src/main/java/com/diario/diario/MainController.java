package com.diario.diario;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String index(){
	    return "index";
    }

    @GetMapping("/cadastro")
    public String cadastro(){
	    return "cadastro";
    }

    @GetMapping("/login")
    public String login(){
	    return "login";
    }

    @GetMapping("/diario")
    public String diario(){
	    return "diario";
    }
}