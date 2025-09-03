package com.eafit.nutrition.service;

import com.eafit.nutrition.model.Medicion;
import com.eafit.nutrition.model.Paciente;
import com.eafit.nutrition.repository.MedicionRepository;
import com.eafit.nutrition.repository.PacienteRepository;
import com.eafit.nutrition.repository.NutricionistaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MedicionServiceConstructor {

    private final MedicionRepository medicionRepository;
    private final PacienteRepository pacienteRepository;
    private final NutricionistaRepository nutricionistaRepository;

    // Constructor que recibe los repositorios necesarios
    public MedicionServiceConstructor(
            MedicionRepository medicionRepository,
            PacienteRepository pacienteRepository,
            NutricionistaRepository nutricionistaRepository) {
        this.medicionRepository = medicionRepository;
        this.pacienteRepository = pacienteRepository;
        this.nutricionistaRepository = nutricionistaRepository;
    }

    @Transactional(readOnly = true)
    public List<Medicion> findAll() {
        return medicionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Medicion> findById(Long id) {
        return medicionRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Medicion> findLastMedicionByPacienteId(Long pacienteId) {
        return medicionRepository.findFirstByPacienteIdOrderByFechaDesc(pacienteId);
    }

    @Transactional
    public Medicion createMedicion(Long pacienteId, Long nutricionistaId, Medicion medicion) {
        // Obtenemos el paciente y el nutricionista usando sus respectivos repositorios
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con id: " + pacienteId));

        var nutricionista = nutricionistaRepository.findById(nutricionistaId)
                .orElseThrow(() -> new RuntimeException("Nutricionista no encontrado con id: " + nutricionistaId));

        // Asignamos el paciente y el nutricionista a la medición
        medicion.setPaciente(paciente);
        medicion.setNutricionista(nutricionista);

        // Si no se especificó una fecha, usamos la fecha actual
        if (medicion.getFecha() == null) {
            medicion.setFecha(LocalDate.now());
        }

        // Guardamos la medición
        return medicionRepository.save(medicion);
    }
}