package com.fitnessapp.service;

import com.fitnessapp.dto.ClienteDTO;
import com.fitnessapp.model.Cliente;
import com.fitnessapp.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClienteService {
    
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    
    public List<ClienteDTO> findAll() {
        log.debug("Obteniendo todos los clientes");
        return clienteRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<ClienteDTO> findByEstado(Boolean estado) {
        log.debug("Buscando clientes con estado: {}", estado);
        return clienteRepository.findByEstado(estado).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public Optional<ClienteDTO> findById(Integer id) {
        log.debug("Buscando cliente con id: {}", id);
        return clienteRepository.findById(id)
            .map(this::convertToDTO);
    }
    
    public Optional<ClienteDTO> findByEmail(String email) {
        log.debug("Buscando cliente con email: {}", email);
        return clienteRepository.findByEmail(email)
            .map(this::convertToDTO);
    }
    
    public Optional<ClienteDTO> findByRut(String rut) {
        log.debug("Buscando cliente con RUT: {}", rut);
        return clienteRepository.findByRut(rut)
            .map(this::convertToDTO);
    }
    
    public List<ClienteDTO> searchByNombre(String searchTerm) {
        log.debug("Buscando clientes con término: {}", searchTerm);
        return clienteRepository.findByNombreContainingIgnoreCaseOrApellidosContainingIgnoreCase(
            searchTerm, searchTerm
        ).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public ClienteDTO save(ClienteDTO clienteDTO) {
        log.debug("Guardando cliente: {}", clienteDTO.getEmail());
        
        if (clienteDTO.getIdCliente() == null) {
            if (clienteRepository.existsByEmail(clienteDTO.getEmail())) {
                throw new IllegalArgumentException("Ya existe un cliente con ese email");
            }
            if (clienteRepository.existsByRut(clienteDTO.getRut())) {
                throw new IllegalArgumentException("Ya existe un cliente con ese RUT");
            }
        } else {
            if (clienteRepository.existsByEmailAndIdClienteNot(
                    clienteDTO.getEmail(), clienteDTO.getIdCliente())) {
                throw new IllegalArgumentException("Ya existe otro cliente con ese email");
            }
            if (clienteRepository.existsByRutAndIdClienteNot(
                    clienteDTO.getRut(), clienteDTO.getIdCliente())) {
                throw new IllegalArgumentException("Ya existe otro cliente con ese RUT");
            }
        }
        
        Cliente cliente = convertToEntity(clienteDTO);
        
        if (clienteDTO.getPassword() != null && !clienteDTO.getPassword().isEmpty()) {
            cliente.setPasswordHash(passwordEncoder.encode(clienteDTO.getPassword()));
        } else if (clienteDTO.getIdCliente() != null) {
            Cliente existing = clienteRepository.findById(clienteDTO.getIdCliente())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
            cliente.setPasswordHash(existing.getPasswordHash());
        }
        
        Cliente saved = clienteRepository.save(cliente);
        log.info("Cliente guardado exitosamente: {} (ID: {})", saved.getEmail(), saved.getIdCliente());
        
        return convertToDTO(saved);
    }
    
    public void deleteById(Integer id) {
        log.debug("Eliminando cliente con id: {}", id);
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));
        
        clienteRepository.delete(cliente);
        log.info("Cliente eliminado: {} (ID: {})", cliente.getEmail(), id);
    }
    
    public void toggleEstado(Integer id) {
        log.debug("Cambiando estado del cliente con id: {}", id);
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));
        
        cliente.setEstado(!cliente.getEstado());
        clienteRepository.save(cliente);
        log.info("Estado del cliente {} cambiado a: {}", id, cliente.getEstado());
    }
    
    public long countAll() {
        log.debug("Contando todos los clientes");
        return clienteRepository.count();
    }
    
    public long countByEstado(Boolean estado) {
        log.debug("Contando clientes con estado: {}", estado);
        return clienteRepository.findByEstado(estado).size();
    }
    
    private ClienteDTO convertToDTO(Cliente cliente) {
        return ClienteDTO.builder()
            .idCliente(cliente.getIdCliente())
            .rut(cliente.getRut())
            .nombre(cliente.getNombre())
            .apellidos(cliente.getApellidos())
            .email(cliente.getEmail())
            .telefono(cliente.getTelefono())
            .direccion(cliente.getDireccion())
            .fechaNacimiento(cliente.getFechaNacimiento())
            .fechaRegistro(cliente.getFechaRegistro())
            .estado(cliente.getEstado())
            .nombreCompleto(cliente.getNombreCompleto())
            .build();
    }
    
    private Cliente convertToEntity(ClienteDTO dto) {
        return Cliente.builder()
            .idCliente(dto.getIdCliente())
            .rut(dto.getRut())
            .nombre(dto.getNombre())
            .apellidos(dto.getApellidos())
            .email(dto.getEmail())
            .telefono(dto.getTelefono())
            .direccion(dto.getDireccion())
            .fechaNacimiento(dto.getFechaNacimiento())
            .estado(dto.getEstado() != null ? dto.getEstado() : true)
            .build();
    }
}
