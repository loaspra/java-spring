package com.fitnessapp.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaseDTO {
    
    private Integer idClase;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;
    
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String descripcion;
    
    @NotNull(message = "La duración es obligatoria")
    @Min(value = 1, message = "La duración debe ser mayor a 0")
    private Integer duracionMinutos;
    
    @NotNull(message = "La capacidad máxima es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser mayor a 0")
    private Integer capacidadMaxima;
    
    @NotBlank(message = "El nivel es obligatorio")
    @Pattern(regexp = "^(Principiante|Intermedio|Avanzado)$", message = "El nivel debe ser Principiante, Intermedio o Avanzado")
    private String nivel;
    
    private Boolean estado;
}
