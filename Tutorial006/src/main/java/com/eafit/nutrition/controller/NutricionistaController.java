package com.eafit.nutrition.controller;

import com.eafit.nutrition.model.Nutricionista;
import com.eafit.nutrition.repository.NutricionistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/nutricionistas")
public class NutricionistaController {

    @Autowired
    private NutricionistaRepository nutricionistaRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Nutricionista> getNutricionista(@PathVariable Long id) {
        Optional<Nutricionista> nutricionista = nutricionistaRepository.findById(id);
        return nutricionista.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/with-pacientes")
    public ResponseEntity<Nutricionista> getNutricionistaWithPacientes(@PathVariable Long id) {
        Optional<Nutricionista> nutricionista = nutricionistaRepository.findByIdWithPacientes(id);
        return nutricionista.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/with-pacientes-graph")
    public ResponseEntity<Nutricionista> getNutricionistaWithPacientesGraph(@PathVariable Long id) {
        Optional<Nutricionista> nutricionista = nutricionistaRepository.findByIdWithPacientesGraph(id);
        return nutricionista.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
