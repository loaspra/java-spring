package com.fitnessapp.repository;

import com.fitnessapp.model.Clase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaseRepository extends JpaRepository<Clase, Integer> {
    
    List<Clase> findByEstado(Boolean estado);
    
    List<Clase> findByNombreContainingIgnoreCase(String nombre);
    
    List<Clase> findByNivel(String nivel);
    
    List<Clase> findByNivelAndEstado(String nivel, Boolean estado);
}
