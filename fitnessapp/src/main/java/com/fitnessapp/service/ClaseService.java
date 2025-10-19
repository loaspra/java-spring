package com.fitnessapp.service;

import com.fitnessapp.dto.ClaseDTO;
import com.fitnessapp.model.Clase;
import com.fitnessapp.repository.ClaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClaseService {
    
    private final ClaseRepository claseRepository;
    
    public List<ClaseDTO> findAll() {
        log.debug("Obteniendo todas las clases");
        return claseRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<ClaseDTO> findByEstado(Boolean estado) {
        log.debug("Buscando clases con estado: {}", estado);
        return claseRepository.findByEstado(estado).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public Optional<ClaseDTO> findById(Integer id) {
        log.debug("Buscando clase con id: {}", id);
        return claseRepository.findById(id)
            .map(this::convertToDTO);
    }
    
    public List<ClaseDTO> searchByNombre(String searchTerm) {
        log.debug("Buscando clases con término: {}", searchTerm);
        return claseRepository.findByNombreContainingIgnoreCase(searchTerm).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<ClaseDTO> findByNivel(String nivel) {
        log.debug("Buscando clases con nivel: {}", nivel);
        return claseRepository.findByNivel(nivel).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<ClaseDTO> findByNivelAndEstado(String nivel, Boolean estado) {
        log.debug("Buscando clases con nivel: {} y estado: {}", nivel, estado);
        return claseRepository.findByNivelAndEstado(nivel, estado).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public ClaseDTO save(ClaseDTO claseDTO) {
        log.debug("Guardando clase: {}", claseDTO.getNombre());
        
        Clase clase = convertToEntity(claseDTO);
        Clase saved = claseRepository.save(clase);
        log.info("Clase guardada exitosamente: {} (ID: {})", saved.getNombre(), saved.getIdClase());
        
        return convertToDTO(saved);
    }
    
    public void deleteById(Integer id) {
        log.debug("Eliminando clase con id: {}", id);
        Clase clase = claseRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Clase no encontrada con ID: " + id));
        
        claseRepository.delete(clase);
        log.info("Clase eliminada: {} (ID: {})", clase.getNombre(), id);
    }
    
    public void toggleEstado(Integer id) {
        log.debug("Cambiando estado de la clase con id: {}", id);
        Clase clase = claseRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Clase no encontrada con ID: " + id));
        
        clase.setEstado(!clase.getEstado());
        claseRepository.save(clase);
        log.info("Estado de la clase {} cambiado a: {}", id, clase.getEstado());
    }
    
    public long countAll() {
        log.debug("Contando todas las clases");
        return claseRepository.count();
    }
    
    public long countByEstado(Boolean estado) {
        log.debug("Contando clases con estado: {}", estado);
        return claseRepository.findByEstado(estado).size();
    }
    
    private ClaseDTO convertToDTO(Clase clase) {
        return ClaseDTO.builder()
            .idClase(clase.getIdClase())
            .nombre(clase.getNombre())
            .descripcion(clase.getDescripcion())
            .duracionMinutos(clase.getDuracionMinutos())
            .capacidadMaxima(clase.getCapacidadMaxima())
            .nivel(clase.getNivel())
            .estado(clase.getEstado())
            .build();
    }
    
    private Clase convertToEntity(ClaseDTO dto) {
        return Clase.builder()
            .idClase(dto.getIdClase())
            .nombre(dto.getNombre())
            .descripcion(dto.getDescripcion())
            .duracionMinutos(dto.getDuracionMinutos())
            .capacidadMaxima(dto.getCapacidadMaxima())
            .nivel(dto.getNivel())
            .estado(dto.getEstado() != null ? dto.getEstado() : true)
            .build();
    }
}
