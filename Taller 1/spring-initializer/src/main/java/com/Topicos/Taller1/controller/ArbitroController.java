package com.Topicos.Taller1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Topicos.Taller1.model.Arbitro;
import com.Topicos.Taller1.service.ArbitroService;

@Controller
@RequestMapping("/arbitros")
public class ArbitroController {
    private final ArbitroService service;

    public ArbitroController(ArbitroService service) {
        this.service = service;
    }

    // Actividad 1: Vista inicial
    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    // Actividad 2: Formulario creación
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("arbitro", new Arbitro());
        return "arbitro-form";
    }

    // Actividad 3: Inserción objeto
    @PostMapping("/guardar")
    public String guardarArbitro(@ModelAttribute Arbitro arbitro, Model model) {
        if (arbitro.getEspecialidad() == null || arbitro.getEspecialidad().isEmpty()
                || arbitro.getEscalafon() == null || arbitro.getEscalafon().isEmpty()) {
            model.addAttribute("error", "Todos los campos son obligatorios.");
            return "arbitro-form";
        }
        service.save(arbitro);
        model.addAttribute("mensaje", "Elemento creado satisfactoriamente");
        return "index";
    }

    // Actividad 4: Listar objetos
    @GetMapping("/listar")
    public String listarArbitros(Model model) {
        model.addAttribute("arbitros", service.findAll());
        return "arbitro-list";
    }

    // Actividad 5: Ver un objeto
    @GetMapping("/{id}")
    public String verArbitro(@PathVariable Long id, Model model) {
        var arbitro = service.findById(id).orElse(null);
        model.addAttribute("arbitro", arbitro);
        return "arbitro-view";
    }

    // Editar árbitro (mostrar formulario)
    @GetMapping("/{id}/editar")
    public String editarArbitroForm(@PathVariable Long id, Model model) {
        var arbitro = service.findById(id).orElse(null);
        if (arbitro == null) {
            return "redirect:/arbitros/listar";
        }
        model.addAttribute("arbitro", arbitro);
        model.addAttribute("editMode", true);
        return "arbitro-form";
    }

    // Editar árbitro (procesar formulario)
    @PostMapping("/{id}/editar")
    public String editarArbitro(@PathVariable Long id, @ModelAttribute Arbitro arbitro, Model model) {
        if (arbitro.getEspecialidad() == null || arbitro.getEspecialidad().isEmpty()
                || arbitro.getEscalafon() == null || arbitro.getEscalafon().isEmpty()) {
            model.addAttribute("error", "Todos los campos son obligatorios.");
            model.addAttribute("editMode", true);
            return "arbitro-form";
        }
        arbitro.setId(id);
        service.save(arbitro);
        model.addAttribute("mensaje", "Elemento editado satisfactoriamente");
        return "index";
    }

    // Actividad 6: Borrar objeto
    @PostMapping("/{id}/borrar")
    public String borrarArbitro(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/arbitros/listar";
    }
}
