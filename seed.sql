-- FitnessApp - Datos de Prueba (Seed Data)

-- ========================================
-- CLIENTES (con contraseñas BCrypt)
-- password para todos: "fitness123"
-- BCrypt hash: $2b$10$7IQ9Z.siMWTs/HQafefLRO7SHcQq6Dn/YB4FyMTUQUkIeIggGcSqi
-- ========================================
INSERT INTO Cliente (rut, nombre, apellidos, email, telefono, direccion, fecha_nacimiento, password_hash, estado) VALUES
('12345678-9', 'Juan', 'Pérez González', 'juan.perez@email.com', '+56912345678', 'Av. Principal 123, Santiago', '1990-05-15', '$2b$10$7IQ9Z.siMWTs/HQafefLRO7SHcQq6Dn/YB4FyMTUQUkIeIggGcSqi', 1),
('98765432-1', 'María', 'López Silva', 'maria.lopez@email.com', '+56998765432', 'Calle Secundaria 456, Providencia', '1985-08-20', '$2b$10$7IQ9Z.siMWTs/HQafefLRO7SHcQq6Dn/YB4FyMTUQUkIeIggGcSqi', 1),
('11223344-5', 'Pedro', 'Rodríguez Muñoz', 'pedro.rodriguez@email.com', '+56911223344', 'Pasaje Los Olivos 789, Las Condes', '1992-03-10', '$2b$10$7IQ9Z.siMWTs/HQafefLRO7SHcQq6Dn/YB4FyMTUQUkIeIggGcSqi', 1),
('55667788-9', 'Ana', 'Martínez Torres', 'ana.martinez@email.com', '+56955667788', 'Calle Nueva 321, Ñuñoa', '1988-11-25', '$2b$10$7IQ9Z.siMWTs/HQafefLRO7SHcQq6Dn/YB4FyMTUQUkIeIggGcSqi', 1),
('22334455-6', 'Luis', 'Sánchez Vega', 'luis.sanchez@email.com', '+56922334455', 'Av. Libertador 654, Vitacura', '1995-07-08', '$2b$10$7IQ9Z.siMWTs/HQafefLRO7SHcQq6Dn/YB4FyMTUQUkIeIggGcSqi', 1),
('33445566-7', 'Carolina', 'Fernández Rojas', 'carolina.fernandez@email.com', '+56933445566', 'Calle Central 987, Maipú', '1993-02-14', '$2b$10$7IQ9Z.siMWTs/HQafefLRO7SHcQq6Dn/YB4FyMTUQUkIeIggGcSqi', 1),
('44556677-8', 'Roberto', 'González Castro', 'roberto.gonzalez@email.com', '+56944556677', 'Paseo Ahumada 147, Santiago Centro', '1987-09-30', '$2b$10$7IQ9Z.siMWTs/HQafefLRO7SHcQq6Dn/YB4FyMTUQUkIeIggGcSqi', 1),
('66778899-0', 'Daniela', 'Herrera Pinto', 'daniela.herrera@email.com', '+56966778899', 'Calle Los Aromos 258, La Reina', '1991-12-05', '$2b$10$7IQ9Z.siMWTs/HQafefLRO7SHcQq6Dn/YB4FyMTUQUkIeIggGcSqi', 1);

-- ========================================
-- INSTRUCTORES
-- ========================================
INSERT INTO Instructor (rut, nombre, apellidos, email, telefono, especialidad, fecha_contratacion, estado) VALUES
('10111213-1', 'Carlos', 'Ramírez Soto', 'carlos.ramirez@fitnessapp.com', '+56910111213', 'Spinning y Cardio', '2023-01-15', 1),
('14151617-2', 'Sofía', 'Vargas Díaz', 'sofia.vargas@fitnessapp.com', '+56914151617', 'Yoga y Pilates', '2023-03-20', 1),
('18192021-3', 'Miguel', 'Torres Bravo', 'miguel.torres@fitnessapp.com', '+56918192021', 'Musculación', '2022-11-10', 1),
('22232425-4', 'Valentina', 'Morales Reyes', 'valentina.morales@fitnessapp.com', '+56922232425', 'Zumba y Baile', '2023-05-01', 1),
('26272829-5', 'Diego', 'Castro Núñez', 'diego.castro@fitnessapp.com', '+56926272829', 'CrossFit', '2023-02-14', 1);

-- ========================================
-- CLASES
-- ========================================
INSERT INTO Clase (nombre, descripcion, duracion_minutos, capacidad_maxima, nivel, estado) VALUES
('Spinning Intenso', 'Clase de ciclismo indoor de alta intensidad', 45, 20, 'Avanzado', 1),
('Yoga Matinal', 'Yoga relajante para comenzar el día', 60, 15, 'Principiante', 1),
('Musculación Funcional', 'Entrenamiento con pesas y ejercicios funcionales', 60, 12, 'Intermedio', 1),
('Zumba Fitness', 'Baile aeróbico al ritmo de música latina', 50, 25, 'Principiante', 1),
('CrossFit Advanced', 'Entrenamiento funcional de alta intensidad', 60, 15, 'Avanzado', 1),
('Pilates Core', 'Fortalecimiento del core y flexibilidad', 55, 12, 'Intermedio', 1),
('Cardio Box', 'Combinación de boxeo y ejercicios cardiovasculares', 45, 18, 'Intermedio', 1),
('Yoga Flow', 'Secuencias dinámicas de yoga', 60, 15, 'Intermedio', 1);

-- ========================================
-- HORARIOS
-- ========================================
INSERT INTO Horario (id_clase, id_instructor, dia_semana, hora_inicio, hora_fin, sala, estado) VALUES
(1, 1, 'Lunes', '07:00', '07:45', 'Sala Spinning 1', 1),
(1, 1, 'Miércoles', '07:00', '07:45', 'Sala Spinning 1', 1),
(1, 1, 'Viernes', '07:00', '07:45', 'Sala Spinning 1', 1),
(2, 2, 'Lunes', '08:00', '09:00', 'Sala Yoga', 1),
(2, 2, 'Miércoles', '08:00', '09:00', 'Sala Yoga', 1),
(2, 2, 'Viernes', '08:00', '09:00', 'Sala Yoga', 1),
(3, 3, 'Martes', '18:00', '19:00', 'Sala Pesas 1', 1),
(3, 3, 'Jueves', '18:00', '19:00', 'Sala Pesas 1', 1),
(4, 4, 'Lunes', '19:00', '19:50', 'Sala Multiuso', 1),
(4, 4, 'Miércoles', '19:00', '19:50', 'Sala Multiuso', 1),
(5, 5, 'Martes', '07:00', '08:00', 'Área CrossFit', 1),
(5, 5, 'Jueves', '07:00', '08:00', 'Área CrossFit', 1),
(6, 2, 'Martes', '10:00', '10:55', 'Sala Yoga', 1),
(6, 2, 'Jueves', '10:00', '10:55', 'Sala Yoga', 1),
(7, 1, 'Miércoles', '18:30', '19:15', 'Sala Multiuso', 1),
(8, 2, 'Sábado', '09:00', '10:00', 'Sala Yoga', 1);

-- ========================================
-- MEMBRESIAS
-- ========================================
INSERT INTO Membresia (id_cliente, tipo_membresia, fecha_inicio, fecha_fin, estado, monto) VALUES
(1, 'Mensual', '2025-10-01', '2025-10-31', 'Activa', 35000.00),
(2, 'Trimestral', '2025-09-01', '2025-11-30', 'Activa', 95000.00),
(3, 'Semestral', '2025-07-01', '2025-12-31', 'Activa', 180000.00),
(4, 'Mensual', '2025-10-15', '2025-11-14', 'Activa', 35000.00),
(5, 'Anual', '2025-01-01', '2025-12-31', 'Activa', 320000.00),
(6, 'Trimestral', '2025-08-01', '2025-10-31', 'Activa', 95000.00),
(7, 'Mensual', '2025-09-20', '2025-10-19', 'Vencida', 35000.00),
(8, 'Semestral', '2025-06-01', '2025-11-30', 'Activa', 180000.00);

-- ========================================
-- RESERVAS
-- ========================================
INSERT INTO Reserva (id_cliente, id_horario, fecha_clase, estado) VALUES
(1, 1, '2025-10-21', 'Confirmada'),
(1, 4, '2025-10-21', 'Confirmada'),
(2, 2, '2025-10-23', 'Confirmada'),
(2, 10, '2025-10-23', 'Confirmada'),
(3, 7, '2025-10-22', 'Confirmada'),
(4, 9, '2025-10-21', 'Confirmada'),
(5, 11, '2025-10-22', 'Confirmada'),
(6, 13, '2025-10-22', 'Confirmada'),
(8, 16, '2025-10-19', 'Completada');

-- ========================================
-- ASISTENCIAS
-- ========================================
INSERT INTO Asistencia (id_cliente, id_horario, fecha_asistencia, hora_entrada, hora_salida) VALUES
(1, 1, '2025-10-14', '06:55', '07:50'),
(2, 2, '2025-10-16', '07:58', '09:05'),
(3, 7, '2025-10-15', '17:55', '19:10'),
(5, 11, '2025-10-15', '06:50', '08:05'),
(8, 16, '2025-10-12', '08:55', '10:05'),
(1, 3, '2025-10-16', '06:52', '07:48'),
(2, 5, '2025-10-16', '07:55', '09:02');

-- ========================================
-- PAGOS
-- ========================================
INSERT INTO Pago (id_cliente, id_membresia, fecha_pago, monto, metodo_pago, estado, comprobante) VALUES
(1, 1, '2025-10-01 10:30:00', 35000.00, 'Tarjeta Crédito', 'Completado', 'COMP-2025-001'),
(2, 2, '2025-09-01 14:15:00', 95000.00, 'Transferencia', 'Completado', 'COMP-2025-002'),
(3, 3, '2025-07-01 09:45:00', 180000.00, 'Transferencia', 'Completado', 'COMP-2025-003'),
(4, 4, '2025-10-15 16:20:00', 35000.00, 'Tarjeta Débito', 'Completado', 'COMP-2025-004'),
(5, 5, '2025-01-01 11:00:00', 320000.00, 'Tarjeta Crédito', 'Completado', 'COMP-2025-005'),
(6, 6, '2025-08-01 13:30:00', 95000.00, 'Efectivo', 'Completado', 'COMP-2025-006'),
(7, 7, '2025-09-20 10:00:00', 35000.00, 'Tarjeta Débito', 'Completado', 'COMP-2025-007'),
(8, 8, '2025-06-01 15:45:00', 180000.00, 'Transferencia', 'Completado', 'COMP-2025-008');

-- ========================================
-- EQUIPAMIENTO
-- ========================================
INSERT INTO Equipamiento (nombre, descripcion, categoria, cantidad, estado, fecha_adquisicion, fecha_ultimo_mantenimiento) VALUES
('Bicicleta Spinning', 'Bicicleta estática de alta resistencia', 'Cardio', 20, 'Disponible', '2023-01-15', '2025-09-01'),
('Colchoneta Yoga', 'Colchoneta antideslizante para yoga y pilates', 'Accesorios', 30, 'Disponible', '2023-02-01', NULL),
('Mancuernas 5kg', 'Par de mancuernas de 5 kilogramos', 'Pesas', 15, 'Disponible', '2022-12-10', NULL),
('Mancuernas 10kg', 'Par de mancuernas de 10 kilogramos', 'Pesas', 12, 'Disponible', '2022-12-10', NULL),
('Barra Olímpica', 'Barra de 20kg para levantamiento', 'Pesas', 8, 'Disponible', '2023-01-20', '2025-08-15'),
('Banco Ajustable', 'Banco ajustable para entrenamiento', 'Equipamiento', 10, 'Disponible', '2023-01-20', '2025-08-15'),
('Cinta de Correr', 'Cinta de correr profesional', 'Cardio', 6, 'Disponible', '2023-03-01', '2025-09-10'),
('Elíptica', 'Máquina elíptica profesional', 'Cardio', 5, 'Disponible', '2023-03-01', '2025-09-10'),
('Kettlebell 12kg', 'Pesa rusa de 12 kilogramos', 'Pesas', 10, 'Disponible', '2023-04-15', NULL),
('TRX', 'Sistema de entrenamiento en suspensión', 'Funcional', 8, 'Disponible', '2023-05-01', NULL),
('Cuerda para Saltar', 'Cuerda de saltar ajustable', 'Accesorios', 20, 'Disponible', '2023-02-15', NULL),
('Balón Medicinal 8kg', 'Balón medicinal para entrenamiento funcional', 'Funcional', 12, 'Disponible', '2023-04-01', NULL);
