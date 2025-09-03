package com.Topicos.Taller1.service;

import com.Topicos.Taller1.model.Arbitro;
import com.Topicos.Taller1.repository.ArbitroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArbitroService {
    private final ArbitroRepository repo;

    public ArbitroService(ArbitroRepository repo) {
        this.repo = repo;
    }

    public Arbitro save(Arbitro arbitro) {
        return repo.save(arbitro);
    }

    public List<Arbitro> findAll() {
        return repo.findAll();
    }

    public Optional<Arbitro> findById(Long id) {
        return repo.findById(id);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
