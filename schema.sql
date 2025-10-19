-- FitnessApp Database Schema
-- Base de datos para sistema de gestión de gimnasio

-- ========================================
-- TABLA: Cliente
-- ========================================
CREATE TABLE Cliente (
    id_cliente INT PRIMARY KEY IDENTITY(1,1),
    rut VARCHAR(12) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    direccion VARCHAR(255),
    fecha_nacimiento DATE,
    fecha_registro DATETIME NOT NULL DEFAULT GETDATE(),
    estado BIT NOT NULL DEFAULT 1,
    password_hash VARCHAR(255) NOT NULL,
    CONSTRAINT chk_email_cliente CHECK (email LIKE '%@%.%')
);

-- ========================================
-- TABLA: Membresia
-- ========================================
CREATE TABLE Membresia (
    id_membresia INT PRIMARY KEY IDENTITY(1,1),
    id_cliente INT NOT NULL,
    tipo_membresia VARCHAR(50) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'Activa',
    monto DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_membresia_cliente FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente) ON DELETE CASCADE,
    CONSTRAINT chk_fecha_membresia CHECK (fecha_fin > fecha_inicio),
    CONSTRAINT chk_estado_membresia CHECK (estado IN ('Activa', 'Suspendida', 'Vencida', 'Cancelada')),
    CONSTRAINT chk_tipo_membresia CHECK (tipo_membresia IN ('Mensual', 'Trimestral', 'Semestral', 'Anual'))
);

-- ========================================
-- TABLA: Clase
-- ========================================
CREATE TABLE Clase (
    id_clase INT PRIMARY KEY IDENTITY(1,1),
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    duracion_minutos INT NOT NULL,
    capacidad_maxima INT NOT NULL,
    nivel VARCHAR(20) NOT NULL,
    estado BIT NOT NULL DEFAULT 1,
    CONSTRAINT chk_duracion CHECK (duracion_minutos > 0),
    CONSTRAINT chk_capacidad CHECK (capacidad_maxima > 0),
    CONSTRAINT chk_nivel CHECK (nivel IN ('Principiante', 'Intermedio', 'Avanzado'))
);

-- ========================================
-- TABLA: Instructor
-- ========================================
CREATE TABLE Instructor (
    id_instructor INT PRIMARY KEY IDENTITY(1,1),
    rut VARCHAR(12) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    especialidad VARCHAR(100),
    fecha_contratacion DATE NOT NULL,
    estado BIT NOT NULL DEFAULT 1,
    CONSTRAINT chk_email_instructor CHECK (email LIKE '%@%.%')
);

-- ========================================
-- TABLA: Horario
-- ========================================
CREATE TABLE Horario (
    id_horario INT PRIMARY KEY IDENTITY(1,1),
    id_clase INT NOT NULL,
    id_instructor INT NOT NULL,
    dia_semana VARCHAR(15) NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    sala VARCHAR(50),
    estado BIT NOT NULL DEFAULT 1,
    CONSTRAINT fk_horario_clase FOREIGN KEY (id_clase) REFERENCES Clase(id_clase) ON DELETE CASCADE,
    CONSTRAINT fk_horario_instructor FOREIGN KEY (id_instructor) REFERENCES Instructor(id_instructor),
    CONSTRAINT chk_hora_horario CHECK (hora_fin > hora_inicio),
    CONSTRAINT chk_dia_semana CHECK (dia_semana IN ('Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado', 'Domingo'))
);

-- ========================================
-- TABLA: Reserva
-- ========================================
CREATE TABLE Reserva (
    id_reserva INT PRIMARY KEY IDENTITY(1,1),
    id_cliente INT NOT NULL,
    id_horario INT NOT NULL,
    fecha_reserva DATETIME NOT NULL DEFAULT GETDATE(),
    estado VARCHAR(20) NOT NULL DEFAULT 'Confirmada',
    fecha_clase DATE NOT NULL,
    CONSTRAINT fk_reserva_cliente FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente) ON DELETE CASCADE,
    CONSTRAINT fk_reserva_horario FOREIGN KEY (id_horario) REFERENCES Horario(id_horario) ON DELETE CASCADE,
    CONSTRAINT chk_estado_reserva CHECK (estado IN ('Confirmada', 'Cancelada', 'Completada', 'No Asistió'))
);

-- ========================================
-- TABLA: Asistencia
-- ========================================
CREATE TABLE Asistencia (
    id_asistencia INT PRIMARY KEY IDENTITY(1,1),
    id_cliente INT NOT NULL,
    id_horario INT NOT NULL,
    fecha_asistencia DATE NOT NULL,
    hora_entrada TIME NOT NULL,
    hora_salida TIME,
    CONSTRAINT fk_asistencia_cliente FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente) ON DELETE CASCADE,
    CONSTRAINT fk_asistencia_horario FOREIGN KEY (id_horario) REFERENCES Horario(id_horario)
);

-- ========================================
-- TABLA: Pago
-- ========================================
CREATE TABLE Pago (
    id_pago INT PRIMARY KEY IDENTITY(1,1),
    id_cliente INT NOT NULL,
    id_membresia INT NOT NULL,
    fecha_pago DATETIME NOT NULL DEFAULT GETDATE(),
    monto DECIMAL(10,2) NOT NULL,
    metodo_pago VARCHAR(50) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'Completado',
    comprobante VARCHAR(100),
    CONSTRAINT fk_pago_cliente FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente) ON DELETE CASCADE,
    CONSTRAINT fk_pago_membresia FOREIGN KEY (id_membresia) REFERENCES Membresia(id_membresia),
    CONSTRAINT chk_monto CHECK (monto > 0),
    CONSTRAINT chk_metodo_pago CHECK (metodo_pago IN ('Efectivo', 'Tarjeta Débito', 'Tarjeta Crédito', 'Transferencia', 'PayPal')),
    CONSTRAINT chk_estado_pago CHECK (estado IN ('Pendiente', 'Completado', 'Rechazado', 'Reembolsado'))
);

-- ========================================
-- TABLA: Equipamiento
-- ========================================
CREATE TABLE Equipamiento (
    id_equipamiento INT PRIMARY KEY IDENTITY(1,1),
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    categoria VARCHAR(50) NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    estado VARCHAR(20) NOT NULL DEFAULT 'Disponible',
    fecha_adquisicion DATE,
    fecha_ultimo_mantenimiento DATE,
    CONSTRAINT chk_cantidad CHECK (cantidad >= 0),
    CONSTRAINT chk_estado_equipamiento CHECK (estado IN ('Disponible', 'En Uso', 'Mantenimiento', 'Fuera de Servicio'))
);

-- ========================================
-- ÍNDICES para mejorar performance
-- ========================================
CREATE INDEX idx_cliente_email ON Cliente(email);
CREATE INDEX idx_cliente_rut ON Cliente(rut);
CREATE INDEX idx_membresia_cliente ON Membresia(id_cliente);
CREATE INDEX idx_membresia_estado ON Membresia(estado);
CREATE INDEX idx_reserva_cliente ON Reserva(id_cliente);
CREATE INDEX idx_reserva_fecha ON Reserva(fecha_clase);
CREATE INDEX idx_asistencia_fecha ON Asistencia(fecha_asistencia);
CREATE INDEX idx_pago_cliente ON Pago(id_cliente);
CREATE INDEX idx_horario_clase ON Horario(id_clase);
