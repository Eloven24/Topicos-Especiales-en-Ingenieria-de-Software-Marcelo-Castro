package com.eafit.nutrition.controller;

import com.eafit.nutrition.model.Medicion;
import com.eafit.nutrition.service.MedicionServiceConstructor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/mediciones")
public class MedicionController {

    private final MedicionServiceConstructor medicionService;

    @Autowired
    public MedicionController(MedicionServiceConstructor medicionService) {
        this.medicionService = medicionService;
    }

    @PostMapping
    public ResponseEntity<Medicion> createMedicion(
            @RequestParam @Positive(message = "El ID del paciente debe ser mayor que 0") Long pacienteId,
            @RequestParam @Positive(message = "El ID del nutricionista debe ser mayor que 0") Long nutricionistaId,
            @Valid @RequestBody Medicion medicion) {
        try {
            Medicion nuevaMedicion = medicionService.createMedicion(pacienteId, nutricionistaId, medicion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMedicion);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/pacientes/{pacienteId}/ultima")
    public ResponseEntity<Medicion> getUltimaMedicion(
            @PathVariable @Positive(message = "El ID del paciente debe ser mayor que 0") Long pacienteId) {
        Optional<Medicion> medicion = medicionService.findLastMedicionByPacienteId(pacienteId);
        return medicion.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/pacientes/{pacienteId}/comparacion")
    public ResponseEntity<Map<String, Object>> compararMediciones(
            @PathVariable @Positive(message = "El ID del paciente debe ser mayor que 0") Long pacienteId) {
        List<Medicion> mediciones = medicionService.findAll()
                .stream()
                .filter(m -> m.getPaciente().getId().equals(pacienteId))
                .sorted((m1, m2) -> m2.getFecha().compareTo(m1.getFecha()))
                .toList();

        if (mediciones.size() < 2) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No hay suficientes mediciones para comparar"));
        }

        Medicion ultima = mediciones.get(0);
        Medicion anterior = mediciones.get(1);

        Map<String, Object> comparacion = new HashMap<>();
        comparacion.put("ultimaMedicion", ultima);
        comparacion.put("anteriorMedicion", anterior);
        comparacion.put("diferenciaPeso", ultima.getPeso() - anterior.getPeso());
        comparacion.put("diferenciaIMC", ultima.calcularIMC() - anterior.calcularIMC());
        if (ultima.getCircunferenciaCintura() != null && anterior.getCircunferenciaCintura() != null) {
            comparacion.put("diferenciaCintura", ultima.getCircunferenciaCintura() - anterior.getCircunferenciaCintura());
        }
        if (ultima.getCircunferenciaCadera() != null && anterior.getCircunferenciaCadera() != null) {
            comparacion.put("diferenciaCadera", ultima.getCircunferenciaCadera() - anterior.getCircunferenciaCadera());
        }
        if (ultima.getPorcentajeGrasaCorporal() != null && anterior.getPorcentajeGrasaCorporal() != null) {
            comparacion.put("diferenciaGrasa", ultima.getPorcentajeGrasaCorporal() - anterior.getPorcentajeGrasaCorporal());
        }

        return ResponseEntity.ok(comparacion);
    }
}