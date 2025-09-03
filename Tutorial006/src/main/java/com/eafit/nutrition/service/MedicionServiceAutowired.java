package com.eafit.nutrition.service;

import com.eafit.nutrition.model.Medicion;
import com.eafit.nutrition.model.Paciente;
import com.eafit.nutrition.repository.MedicionRepository;
import com.eafit.nutrition.repository.PacienteRepository;
import com.eafit.nutrition.repository.NutricionistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MedicionServiceAutowired {

    @Autowired
    private MedicionRepository medicionRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private NutricionistaRepository nutricionistaRepository;

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
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con id: " + pacienteId));

        var nutricionista = nutricionistaRepository.findById(nutricionistaId)
                .orElseThrow(() -> new RuntimeException("Nutricionista no encontrado con id: " + nutricionistaId));

        medicion.setPaciente(paciente);
        medicion.setNutricionista(nutricionista);

        if (medicion.getFecha() == null) {
            medicion.setFecha(LocalDate.now());
        }

        return medicionRepository.save(medicion);
    }
}