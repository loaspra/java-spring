package com.fitnessapp.controller;

import com.fitnessapp.dto.ClaseDTO;
import com.fitnessapp.service.ClaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clases")
@RequiredArgsConstructor
@Slf4j
public class ClaseController {
    
    private final ClaseService claseService;
    
    @GetMapping
    public String list(@RequestParam(required = false) String search, Model model) {
        log.debug("Listando clases con búsqueda: {}", search);
        
        if (search != null && !search.trim().isEmpty()) {
            model.addAttribute("clases", claseService.searchByNombre(search));
            model.addAttribute("search", search);
        } else {
            model.addAttribute("clases", claseService.findAll());
        }
        
        return "clases/list";
    }
    
    @GetMapping("/nuevo")
    public String showCreateForm(Model model) {
        log.debug("Mostrando formulario de creación de clase");
        model.addAttribute("claseDTO", new ClaseDTO());
        model.addAttribute("isEdit", false);
        model.addAttribute("niveles", new String[]{"Principiante", "Intermedio", "Avanzado"});
        return "clases/form";
    }
    
    @GetMapping("/editar/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        log.debug("Mostrando formulario de edición para clase: {}", id);
        
        return claseService.findById(id)
            .map(clase -> {
                model.addAttribute("claseDTO", clase);
                model.addAttribute("isEdit", true);
                model.addAttribute("niveles", new String[]{"Principiante", "Intermedio", "Avanzado"});
                return "clases/form";
            })
            .orElseGet(() -> {
                redirectAttributes.addFlashAttribute("error", "Clase no encontrada");
                return "redirect:/clases";
            });
    }
    
    @GetMapping("/ver/{id}")
    public String view(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        log.debug("Mostrando detalles de la clase: {}", id);
        
        return claseService.findById(id)
            .map(clase -> {
                model.addAttribute("clase", clase);
                return "clases/view";
            })
            .orElseGet(() -> {
                redirectAttributes.addFlashAttribute("error", "Clase no encontrada");
                return "redirect:/clases";
            });
    }
    
    @PostMapping("/guardar")
    public String save(@Valid @ModelAttribute("claseDTO") ClaseDTO claseDTO,
                      BindingResult bindingResult,
                      Model model,
                      RedirectAttributes redirectAttributes) {
        
        log.debug("Guardando clase: {}", claseDTO.getNombre());
        
        if (bindingResult.hasErrors()) {
            log.warn("Errores de validación al guardar clase");
            model.addAttribute("isEdit", claseDTO.getIdClase() != null);
            model.addAttribute("niveles", new String[]{"Principiante", "Intermedio", "Avanzado"});
            return "clases/form";
        }
        
        try {
            ClaseDTO saved = claseService.save(claseDTO);
            String message = claseDTO.getIdClase() == null 
                ? "Clase creada exitosamente" 
                : "Clase actualizada exitosamente";
            redirectAttributes.addFlashAttribute("success", message);
            return "redirect:/clases/ver/" + saved.getIdClase();
        } catch (IllegalArgumentException e) {
            log.error("Error al guardar clase: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("isEdit", claseDTO.getIdClase() != null);
            model.addAttribute("niveles", new String[]{"Principiante", "Intermedio", "Avanzado"});
            return "clases/form";
        }
    }
    
    @PostMapping("/eliminar/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        log.debug("Eliminando clase: {}", id);
        
        try {
            claseService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Clase eliminada exitosamente");
        } catch (IllegalArgumentException e) {
            log.error("Error al eliminar clase: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al eliminar clase: " + e.getMessage());
        }
        
        return "redirect:/clases";
    }
    
    @PostMapping("/toggle-estado/{id}")
    public String toggleEstado(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        log.debug("Cambiando estado de la clase: {}", id);
        
        try {
            claseService.toggleEstado(id);
            redirectAttributes.addFlashAttribute("success", "Estado de la clase actualizado");
        } catch (IllegalArgumentException e) {
            log.error("Error al cambiar estado: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        
        return "redirect:/clases";
    }
}
