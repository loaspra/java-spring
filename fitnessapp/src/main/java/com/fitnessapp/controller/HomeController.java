package com.fitnessapp.controller;

import com.fitnessapp.service.ClienteService;
import com.fitnessapp.service.ClaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    
    private final ClienteService clienteService;
    private final ClaseService claseService;
    
    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }
    
    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("totalClientes", clienteService.countAll());
        model.addAttribute("clientesActivos", clienteService.countByEstado(true));
        model.addAttribute("totalClases", claseService.countAll());
        model.addAttribute("clasesHoy", 0);
        model.addAttribute("reservas", 0);
        return "home";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
