package com.fitnessapp.repository;

import com.fitnessapp.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    
    Optional<Cliente> findByEmail(String email);
    
    Optional<Cliente> findByRut(String rut);
    
    List<Cliente> findByEstado(Boolean estado);
    
    List<Cliente> findByNombreContainingIgnoreCaseOrApellidosContainingIgnoreCase(
        String nombre, String apellidos
    );
    
    boolean existsByEmail(String email);
    
    boolean existsByRut(String rut);
    
    boolean existsByEmailAndIdClienteNot(String email, Integer idCliente);
    
    boolean existsByRutAndIdClienteNot(String rut, Integer idCliente);
}
