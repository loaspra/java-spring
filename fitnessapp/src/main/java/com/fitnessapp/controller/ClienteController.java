package com.fitnessapp.controller;

import com.fitnessapp.dto.ClienteDTO;
import com.fitnessapp.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClienteController {
    
    private final ClienteService clienteService;
    
    @GetMapping
    public String list(@RequestParam(required = false) String search, Model model) {
        log.debug("Listando clientes con búsqueda: {}", search);
        
        if (search != null && !search.trim().isEmpty()) {
            model.addAttribute("clientes", clienteService.searchByNombre(search));
            model.addAttribute("search", search);
        } else {
            model.addAttribute("clientes", clienteService.findAll());
        }
        
        return "clientes/list";
    }
    
    @GetMapping("/nuevo")
    public String showCreateForm(Model model) {
        log.debug("Mostrando formulario de creación de cliente");
        model.addAttribute("clienteDTO", new ClienteDTO());
        model.addAttribute("isEdit", false);
        return "clientes/form";
    }
    
    @GetMapping("/editar/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        log.debug("Mostrando formulario de edición para cliente: {}", id);
        
        return clienteService.findById(id)
            .map(cliente -> {
                model.addAttribute("clienteDTO", cliente);
                model.addAttribute("isEdit", true);
                return "clientes/form";
            })
            .orElseGet(() -> {
                redirectAttributes.addFlashAttribute("error", "Cliente no encontrado");
                return "redirect:/clientes";
            });
    }
    
    @GetMapping("/ver/{id}")
    public String view(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        log.debug("Mostrando detalles del cliente: {}", id);
        
        return clienteService.findById(id)
            .map(cliente -> {
                model.addAttribute("cliente", cliente);
                return "clientes/view";
            })
            .orElseGet(() -> {
                redirectAttributes.addFlashAttribute("error", "Cliente no encontrado");
                return "redirect:/clientes";
            });
    }
    
    @PostMapping("/guardar")
    public String save(@Valid @ModelAttribute("clienteDTO") ClienteDTO clienteDTO,
                      BindingResult bindingResult,
                      Model model,
                      RedirectAttributes redirectAttributes) {
        
        log.debug("Guardando cliente: {}", clienteDTO.getEmail());
        
        if (bindingResult.hasErrors()) {
            log.warn("Errores de validación al guardar cliente");
            model.addAttribute("isEdit", clienteDTO.getIdCliente() != null);
            return "clientes/form";
        }
        
        try {
            ClienteDTO saved = clienteService.save(clienteDTO);
            String message = clienteDTO.getIdCliente() == null 
                ? "Cliente creado exitosamente" 
                : "Cliente actualizado exitosamente";
            redirectAttributes.addFlashAttribute("success", message);
            return "redirect:/clientes/ver/" + saved.getIdCliente();
        } catch (IllegalArgumentException e) {
            log.error("Error al guardar cliente: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("isEdit", clienteDTO.getIdCliente() != null);
            return "clientes/form";
        }
    }
    
    @PostMapping("/eliminar/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        log.debug("Eliminando cliente: {}", id);
        
        try {
            clienteService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Cliente eliminado exitosamente");
        } catch (IllegalArgumentException e) {
            log.error("Error al eliminar cliente: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al eliminar cliente: " + e.getMessage());
        }
        
        return "redirect:/clientes";
    }
    
    @PostMapping("/toggle-estado/{id}")
    public String toggleEstado(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        log.debug("Cambiando estado del cliente: {}", id);
        
        try {
            clienteService.toggleEstado(id);
            redirectAttributes.addFlashAttribute("success", "Estado del cliente actualizado");
        } catch (IllegalArgumentException e) {
            log.error("Error al cambiar estado: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        
        return "redirect:/clientes";
    }
}
