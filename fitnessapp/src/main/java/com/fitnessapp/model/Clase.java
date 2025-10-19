package com.fitnessapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "Clase")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Clase {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clase")
    private Integer idClase;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @NotNull(message = "La duración es obligatoria")
    @Min(value = 1, message = "La duración debe ser mayor a 0")
    @Column(name = "duracion_minutos", nullable = false)
    private Integer duracionMinutos;
    
    @NotNull(message = "La capacidad máxima es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser mayor a 0")
    @Column(name = "capacidad_maxima", nullable = false)
    private Integer capacidadMaxima;
    
    @NotBlank(message = "El nivel es obligatorio")
    @Column(nullable = false, length = 20)
    private String nivel;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean estado = true;
}
