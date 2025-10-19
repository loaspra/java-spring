# Evaluación 2 — FitnessApp (Java Spring, Thymeleaf, Bootstrap, SQL Server/Azure)
**Entorno sugerido:** Fedora + VS Code + Spring Boot + Thymeleaf + Bootstrap + SQL Server (local en contenedor) o Azure SQL + Azure Data Studio (ADS)  
**Flujo exigido:** Login → Home (plantilla Bootstrap) → Menú lateral con "Gestión de Usuarios" → ABM/CRUD de usuarios (listar, crear, editar, eliminar) bajo arquitectura MVC.  
**Fecha de preparación del documento:** 2025-10-18 18:13 UTC

---

## ACLARACIONES DEL PROYECTO (Actualizado)

1. **Base de datos**: Azure SQL (infraestructura provisionada vía Terraform)
2. **Plantilla Bootstrap**: Tabler - Admin Template (HTML puro, Bootstrap 5, open source)
3. **Alcance funcional**: 
   - La base de datos COMPLETA debe crearse (todas las tablas: Cliente, Entrenador, Sede, Clase, Reserva, etc.)
   - La aplicación implementará ÚNICAMENTE el CRUD de Usuarios (tabla Cliente)
   - Login y Home también están incluidos
4. **Tipo de eliminación**: Borrado físico (DELETE)
5. **Versiones de tecnología**: Spring Boot 3.3.x (última estable), Java 17
6. **Roles**: Sin diferenciación de roles - todos los usuarios pueden gestionar usuarios (simple)

---

## 1) Objetivo del proyecto
- Entregar una aplicación web en Java con Spring que:
  - Permita **autenticación (login)** con usuarios almacenados en SQL Server/Azure SQL.
  - Tras el login, muestre un **Home/Dashboard** con una **plantilla Bootstrap** distinta a la usada en clase.
  - Incorpore en el **sidebar** un ítem “**Gestión de Usuarios**”.
  - En “Gestión de Usuarios”, muestre una **tabla** con los usuarios y ofrezca **crear, editar y eliminar**.
  - Respete la **arquitectura MVC**, use **Thymeleaf** para vistas y **JPA** para el acceso a datos.
  - Entregue **scripts SQL** de las tablas involucradas.

---

## 2) Requisitos de entorno (checklist)
1. **Java 17 (JDK) y Maven** instalados en Fedora.  
2. **VS Code** con extensiones:
   - Extension Pack for Java (o equivalentes de Java, Maven, Test Runner).
   - Spring Boot Extension Pack.
   - Spring Initializr Java Support.
   - SQL Server (mssql) —opcional si no usas ADS.
3. **Azure Data Studio (ADS)** para conectarte a Azure SQL.  
4. **Terraform** instalado para provisionar infraestructura en Azure.
5. **Acceso a Azure**:
   - Servidor lógico creado vía Terraform.
   - Base de datos creada.
   - Regla de firewall para tu IP pública habilitada.

> **DECISIÓN TOMADA**: Usaremos Azure SQL provisionado con Terraform.

---

## 3) Base de datos y modelo (con mejoras según comentarios del profesor)
- Base: **FitnessAppDB**.  
- **IMPORTANTE**: Se debe crear la base de datos COMPLETA con todas las tablas (Cliente, Entrenador, Sede, Clase, Reserva, etc.)
- **ALCANCE DE LA APLICACIÓN**: Solo se implementará el CRUD de la tabla **Cliente** (Gestión de Usuarios)
- Esquema base (evaluación 1) incluido más abajo, **extendido** con:
  - **DisponibilidadEntrenador**: franjas de disponibilidad por día/horario.
  - **DisponibilidadSede**: franjas de disponibilidad de parques/locales, con capacidad utilizable.
  - **ParametroSistema**: clave–valor para reglas generales (por ejemplo, **DesinfeccionMinutos = 30**).
  - **PublicacionSemana**: registro de la publicación semanal de clases (se hace los viernes).
  - **InformeSesion**: informe redactado por el trainer al finalizar una clase (incidentes, observaciones).
- Observación: La regla "30 minutos de desinfección" puede **validarse en la aplicación** usando el parámetro
  `DesinfeccionMinutos`. Si deseas validación a nivel de BD, añade un **trigger** o proceso de verificación
  que impida crear clases en una misma `Sede` cuya hora de inicio esté dentro del bloque de desinfección de la clase anterior.

**Contraseñas:** la columna `Cliente.ClaveHash` debe almacenar **hashes BCrypt**.  
Para poblar usuarios de prueba, genera hashes y úsalos en los INSERTs.

---

## 4) Estructura del proyecto (alto nivel, sin código)
- `controller/` → Controladores MVC (Login, Home, Usuario).
- `entity/` → Entidades JPA (Cliente, y futuras: Entrenador, etc.).
- `repository/` → Repositorios JPA.
- `service/` → Capa de negocio, validaciones y transacciones.
- `security/` → Configuración de Spring Security (login, rutas protegidas, BCrypt).
- `resources/templates/` → Vistas Thymeleaf: `login.html`, `home.html`, `usuarios/lista.html`, `usuarios/form.html`, fragmentos (`_layout.html`, `_sidebar.html`, etc.).
- `resources/static/` → Recursos estáticos (CSS/JS/IMG/vendors de la plantilla Bootstrap).
- `application.properties` → datasource (local/Azure), JPA, Thymeleaf.

---

## 5) Dependencias mínimas (Spring Initializr)
- Spring Web  
- Thymeleaf  
- Spring Data JPA  
- Spring Security  
- Validation  
- SQL Server Driver  
*(Java 17, Spring Boot 3.3.x - última versión estable)*

---

## 6) Configuración de conexión
- **Azure SQL (SELECCIONADO)**: `jdbc:sqlserver://<servidor>.database.windows.net:1433;database=FitnessAppDB;encrypt=true`  
- Credenciales provistas por Terraform outputs
- `spring.jpa.hibernate.ddl-auto=none` (usamos script SQL).  
- Desactivar cache de Thymeleaf en desarrollo.

**Prueba:** Conectar vía ADS y ejecutar un `SELECT` para verificar conectividad.

---

## 7) Seguridad y autenticación (directrices)
- **Login** personalizado en `/login` (email + contraseña).
- **UserDetails** basado en tabla `Cliente` (`Email` como username y `ClaveHash` BCrypt).
- **Sin roles diferenciados**: Todos los usuarios logueados tienen acceso completo.
- **Rutas protegidas**: `/home` y `/usuarios/**`.
- **Recursos estáticos** permitidos sin login.
- **Redirecciones**: post-login a `/home`, logout a `/login?logout`.
- **CSRF**: incluir token en formularios Thymeleaf (POST/PUT/DELETE).

---

## 8) Integración de plantilla Bootstrap (no React/Next)
1. **Plantilla seleccionada**: Tabler (https://tabler.io) - Open source, Bootstrap 5, HTML puro
2. Copiar CSS/JS/vendors a `static/…`, y vistas base a `templates/`.  
3. Crear **fragmentos Thymeleaf** (header, sidebar, footer).  
4. Agregar en el **sidebar** el ítem **"Gestión de Usuarios"** hacia `/usuarios`.  
5. Asegurar rutas relativas (sin romper referencias a estáticos).  
6. Verificar responsive, breadcrumbs y componentes de UI (tabla/paginación).

---

## 9) Casos de uso mínimos (pantallas)
- **Login**: formulario, mensajes de error, redirección a Home si OK.
- **Home**: dashboard de la plantilla Tabler.
- **Gestión de Usuarios**:
  - **Listar**: tabla con ID/Nombre/Apellido/Email/Estado/Acciones.
  - **Crear**: validaciones, hash BCrypt al guardar, estado activo por defecto.
  - **Editar**: actualizar datos y, opcionalmente, contraseña (si no vacía, re-hash).
  - **Eliminar**: confirmación (modal); **borrado físico (DELETE)**.
  - **Mensajes**: alertas de éxito/error.

---

## 10) Validaciones y mensajes
- **Servidor**: Bean Validation (obligatorios, tamaños, formato email, unicidad de email).
- **Cliente**: validaciones HTML5/Bootstrap.
- **Mensajes** claros para duplicado de email, contraseñas débiles, etc.

---

## 11) Pruebas funcionales (checklist)
- Login correcto/incorrecto/inactivo.
- Acceso restringido a `/home` y `/usuarios/**`.
- CRUD de usuarios (crear/editar/eliminar) con validaciones y mensajes.
- CSRF presente en formularios.
- Pruebas en Chrome/Firefox y móvil.

---

## 12) Entregables
- Código fuente del proyecto.
- Plantilla Bootstrap integrada: **Tabler** (https://tabler.io).
- **Script SQL completo** (abajo) - base de datos completa creada.
- **Archivos Terraform** para provisionar Azure SQL.
- README con instrucciones de ejecución y conexión Azure SQL.
- Capturas: login, home, lista de usuarios, formularios, confirmación de eliminación.

---

# Script SQL — FitnessAppDB (extendido)
> Incluye la base del enunciado y **mejoras** por comentarios del profesor: disponibilidad de entrenador/sede, parámetro de desinfección, publicación de semana, e informe de sesión.

```sql
-- ==============================
-- 0) Preparación
-- ==============================
USE master;
GO
IF EXISTS(SELECT * FROM sys.databases WHERE name='FitnessAppDB')
BEGIN
    ALTER DATABASE FitnessAppDB SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE FitnessAppDB;
END
GO

-- ==============================
-- 1) Creación de base
-- ==============================
CREATE DATABASE FitnessAppDB;
GO
USE FitnessAppDB;
GO

-- ==============================
-- 2) Tablas núcleo
-- ==============================

CREATE TABLE Cliente (
    IdCliente INT IDENTITY PRIMARY KEY,
    Nombre NVARCHAR(100) NOT NULL,
    Apellido NVARCHAR(100) NOT NULL,
    Email NVARCHAR(150) UNIQUE NOT NULL,
    Telefono NVARCHAR(20),
    FechaRegistro DATETIME DEFAULT GETDATE(),
    Estado BIT DEFAULT 1,
    ClaveHash NVARCHAR(256) NOT NULL -- BCrypt
);

CREATE TABLE Entrenador (
    IdEntrenador INT IDENTITY PRIMARY KEY,
    Nombre NVARCHAR(100) NOT NULL,
    Apellido NVARCHAR(100) NOT NULL,
    Especialidad NVARCHAR(100),
    Estado BIT DEFAULT 1
);

CREATE TABLE Planes (
    IdPlan INT IDENTITY PRIMARY KEY,
    Nombre NVARCHAR(100) NOT NULL,
    Categoria NVARCHAR(50),
    DuracionMeses INT NOT NULL,
    SesionesGrupales INT NOT NULL,
    SesionesPersonales INT NOT NULL,
    Precio DECIMAL(10,2) NOT NULL,
    DiasAvisoRenovacion INT DEFAULT 7 -- notificación comercial previa al vencimiento
);

CREATE TABLE Membresia (
    IdMembresia INT IDENTITY PRIMARY KEY,
    IdCliente INT NOT NULL,
    IdPlan INT NOT NULL,
    FechaInicio DATE NOT NULL,
    FechaFin DATE NOT NULL,
    Activa BIT DEFAULT 1,
    CONSTRAINT FK_Membresia_Cliente FOREIGN KEY (IdCliente) REFERENCES Cliente(IdCliente),
    CONSTRAINT FK_Membresia_Planes FOREIGN KEY (IdPlan) REFERENCES Planes(IdPlan)
);

CREATE TABLE Sede (
    IdSede INT IDENTITY PRIMARY KEY,
    Tipo NVARCHAR(50) CHECK (Tipo IN ('Local','Parque')),
    Nombre NVARCHAR(100) NOT NULL,
    Direccion NVARCHAR(200),
    Capacidad INT -- capacidad máxima física del ambiente
);

-- Publicación semanal de la grilla (operaciones la publica los viernes)
CREATE TABLE PublicacionSemana (
    IdPublicacion INT IDENTITY PRIMARY KEY,
    SemanaInicio DATE NOT NULL,
    SemanaFin DATE NOT NULL,
    FechaPublicacion DATETIME NOT NULL DEFAULT GETDATE(),
    Observaciones NVARCHAR(200)
);

-- Disponibilidades
CREATE TABLE DisponibilidadEntrenador (
    IdDisponibilidad INT IDENTITY PRIMARY KEY,
    IdEntrenador INT NOT NULL,
    DiaSemana TINYINT NOT NULL CHECK (DiaSemana BETWEEN 1 AND 7), -- 1=lunes ... 7=domingo
    HoraInicio TIME NOT NULL,
    HoraFin TIME NOT NULL,
    Estado BIT NOT NULL DEFAULT 1, -- 1=Disponible 0=No disponible
    CONSTRAINT FK_DispEnt_Entrenador FOREIGN KEY (IdEntrenador) REFERENCES Entrenador(IdEntrenador),
    CONSTRAINT CK_DispEnt_Rango CHECK (HoraFin > HoraInicio)
);

CREATE TABLE DisponibilidadSede (
    IdDisponibilidad INT IDENTITY PRIMARY KEY,
    IdSede INT NOT NULL,
    Fecha DATE NOT NULL,                   -- disponibilidad por fecha (parques pueden variar)
    HoraInicio TIME NOT NULL,
    HoraFin TIME NOT NULL,
    CapacidadDisponible INT NULL,          -- opcional si difiere de la capacidad total
    Estado BIT NOT NULL DEFAULT 1,         -- 1=Disponible 0=No disponible
    CONSTRAINT FK_DispSede_Sede FOREIGN KEY (IdSede) REFERENCES Sede(IdSede),
    CONSTRAINT CK_DispSede_Rango CHECK (HoraFin > HoraInicio)
);

-- Parámetros del sistema (reglas generales, p.ej., desinfección 30m)
CREATE TABLE ParametroSistema (
    Clave NVARCHAR(100) NOT NULL PRIMARY KEY,
    Valor NVARCHAR(200) NOT NULL,
    Descripcion NVARCHAR(200) NULL
);

-- Clases/entrenamientos
CREATE TABLE Clase (
    IdClase INT IDENTITY PRIMARY KEY,
    Tipo NVARCHAR(50) CHECK (Tipo IN ('Grupal','Personal')),
    IdEntrenador INT NOT NULL,
    IdSede INT NOT NULL,
    Fecha DATE NOT NULL,
    HoraInicio TIME NOT NULL,
    HoraFin TIME NOT NULL,
    CupoMaximo INT,
    IdPublicacion INT NULL, -- opcional, referencia a la grilla publicada
    CONSTRAINT FK_Clase_Entrenador FOREIGN KEY (IdEntrenador) REFERENCES Entrenador(IdEntrenador),
    CONSTRAINT FK_Clase_Sede FOREIGN KEY (IdSede) REFERENCES Sede(IdSede),
    CONSTRAINT FK_Clase_Publicacion FOREIGN KEY (IdPublicacion) REFERENCES PublicacionSemana(IdPublicacion),
    CONSTRAINT CK_Clase_Rango CHECK (HoraFin > HoraInicio)
);

CREATE TABLE Reserva (
    IdReserva INT IDENTITY PRIMARY KEY,
    IdCliente INT NOT NULL,
    IdClase INT NOT NULL,
    Estado NVARCHAR(20) CHECK (Estado IN ('Reservado','Cancelado','Asistió','Falta')),
    FechaReserva DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_Reserva_Cliente FOREIGN KEY (IdCliente) REFERENCES Cliente(IdCliente),
    CONSTRAINT FK_Reserva_Clase FOREIGN KEY (IdClase) REFERENCES Clase(IdClase),
    CONSTRAINT UQ_Reserva_Unica UNIQUE (IdCliente, IdClase) -- evita reservas duplicadas
);

-- Lista de espera cuando cupo está lleno
CREATE TABLE ListaEspera (
    IdEspera INT IDENTITY PRIMARY KEY,
    IdCliente INT NOT NULL,
    IdClase INT NOT NULL,
    FechaRegistro DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_Espera_Cliente FOREIGN KEY (IdCliente) REFERENCES Cliente(IdCliente),
    CONSTRAINT FK_Espera_Clase FOREIGN KEY (IdClase) REFERENCES Clase(IdClase),
    CONSTRAINT UQ_Espera_Unica UNIQUE (IdCliente, IdClase)
);

-- Asistencia vinculada a reserva
CREATE TABLE Asistencia (
    IdAsistencia INT IDENTITY PRIMARY KEY,
    IdReserva INT NOT NULL,
    Estado NVARCHAR(20) CHECK (Estado IN ('Asistió','Falta','Tarde')),
    CONSTRAINT FK_Asistencia_Reserva FOREIGN KEY (IdReserva) REFERENCES Reserva(IdReserva)
);

-- Sanciones por faltas
CREATE TABLE Sancion (
    IdSancion INT IDENTITY PRIMARY KEY,
    IdCliente INT NOT NULL,
    FechaInicio DATE NOT NULL,
    FechaFin DATE NOT NULL,
    Motivo NVARCHAR(200),
    CONSTRAINT FK_Sancion_Cliente FOREIGN KEY (IdCliente) REFERENCES Cliente(IdCliente)
);

-- Pagos de membresía
CREATE TABLE Pago (
    IdPago INT IDENTITY PRIMARY KEY,
    IdMembresia INT NOT NULL,
    Monto DECIMAL(10,2) NOT NULL,
    FechaPago DATETIME DEFAULT GETDATE(),
    Estado NVARCHAR(20) CHECK (Estado IN ('Pendiente','Confirmado','Fallido')),
    CONSTRAINT FK_Pago_Membresia FOREIGN KEY (IdMembresia) REFERENCES Membresia(IdMembresia)
);

-- Reclamos
CREATE TABLE Reclamo (
    IdReclamo INT IDENTITY PRIMARY KEY,
    IdCliente INT NOT NULL,
    Fecha DATETIME DEFAULT GETDATE(),
    Descripcion NVARCHAR(500),
    Estado NVARCHAR(20) CHECK (Estado IN ('Abierto','En Proceso','Cerrado')),
    CONSTRAINT FK_Reclamo_Cliente FOREIGN KEY (IdCliente) REFERENCES Cliente(IdCliente)
);

-- Informe del entrenador sobre la sesión (incidentes, resumen)
CREATE TABLE InformeSesion (
    IdInforme INT IDENTITY PRIMARY KEY,
    IdClase INT NOT NULL,
    IdEntrenador INT NOT NULL,
    FechaCreacion DATETIME DEFAULT GETDATE(),
    Resumen NVARCHAR(1000) NULL,
    Incidentes NVARCHAR(1000) NULL,
    CONSTRAINT FK_Informe_Clase FOREIGN KEY (IdClase) REFERENCES Clase(IdClase),
    CONSTRAINT FK_Informe_Entrenador FOREIGN KEY (IdEntrenador) REFERENCES Entrenador(IdEntrenador),
    CONSTRAINT UQ_Informe_Clase UNIQUE (IdClase) -- un informe por clase
);

-- ==============================
-- 3) Índices útiles
-- ==============================
CREATE INDEX IX_Cliente_Email ON Cliente(Email);
CREATE INDEX IX_Clase_Sede_Fecha ON Clase(IdSede, Fecha, HoraInicio);
CREATE INDEX IX_Reserva_Clase ON Reserva(IdClase);
CREATE INDEX IX_DispEnt_DiaHora ON DisponibilidadEntrenador(IdEntrenador, DiaSemana, HoraInicio);
CREATE INDEX IX_DispSede_FechaHora ON DisponibilidadSede(IdSede, Fecha, HoraInicio);

-- ==============================
-- 4) Parámetros iniciales
-- ==============================
INSERT INTO ParametroSistema (Clave, Valor, Descripcion)
VALUES ('DesinfeccionMinutos', '30', 'Tiempo mínimo de desinfección entre sesiones');

-- ==============================
-- 5) Datos de ejemplo (clientes con hashes ficticios)
-- Reemplaza los "hash" por valores BCrypt reales antes de usar en producción
-- ==============================
INSERT INTO Cliente (Nombre, Apellido, Email, Telefono, ClaveHash)
VALUES
('Juan','Lopez','juan.lopez@example.com','987654321','$2a$10$hashJuan'),
('Maria','Gomez','maria.gomez@example.com','987654322','$2a$10$hashMaria'),
('Carlos','Perez','carlos.perez@example.com','987654323','$2a$10$hashCarlos'),
('Luisa','Fernandez','luisa.fernandez@example.com','987654324','$2a$10$hashLuisa'),
('Andres','Ramirez','andres.ramirez@example.com','987654325','$2a$10$hashAndres'),
('Sofia','Torres','sofia.torres@example.com','987654326','$2a$10$hashSofia'),
('Diego','Vargas','diego.vargas@example.com','987654327','$2a$10$hashDiego'),
('Valeria','Rojas','valeria.rojas@example.com','987654328','$2a$10$hashValeria'),
('Miguel','Castillo','miguel.castillo@example.com','987654329','$2a$10$hashMiguel'),
('Camila','Martinez','camila.martinez@example.com','987654330','$2a$10$hashCamila');
GO

-- Entrenadores, sedes y disponibilidades de ejemplo (mínimos)
INSERT INTO Entrenador (Nombre, Apellido, Especialidad) VALUES
('Ana','Salas','Funcional'), ('Pedro','Ibarra','Crossfit');

INSERT INTO Sede (Tipo, Nombre, Direccion, Capacidad) VALUES
('Parque','Parque Central','Av. Siempre Viva 123', 40),
('Local','Local Miraflores','Calle Lima 456', 25);

-- Disponibilidad: Lunes (1) 08:00-12:00 para Ana; Martes (2) 16:00-20:00 para Pedro
INSERT INTO DisponibilidadEntrenador (IdEntrenador, DiaSemana, HoraInicio, HoraFin) VALUES
(1, 1, '08:00', '12:00'),
(2, 2, '16:00', '20:00');

-- Disponibilidad de sedes (por fecha)
DECLARE @hoy DATE = CAST(GETDATE() AS DATE);
INSERT INTO DisponibilidadSede (IdSede, Fecha, HoraInicio, HoraFin, CapacidadDisponible) VALUES
(1, @hoy, '06:00', '18:00', 35),
(2, @hoy, '07:00', '22:00', 25);

-- ==============================
-- 6) Consultas de verificación
-- ==============================
SELECT name FROM sys.tables ORDER BY name;
SELECT TOP 5 * FROM Cliente ORDER BY IdCliente;
```
---

## 13) Riesgos comunes y mitigaciones
- **Elegir plantilla React/Next**: no compatible con Thymeleaf. Preferir plantillas HTML/Bootstrap puras.
- **Rutas de estáticos rotas**: revisar referencias a CSS/JS en `static`.
- **Contraseñas en texto plano**: usar **BCrypt**.
- **CSRF ausente**: incluir tokens en formularios Thymeleaf.
- **Email duplicado**: validar y mostrar mensaje claro.
- **Desinfección 30m**: documentar control con `ParametroSistema` y validar en lógica de asignación de clases.

---

## 14) Plan de trabajo sugerido
1. **Infraestructura Azure** (Terraform para Azure SQL).
2. **Base de datos completa** (script, ADS, pruebas de conectividad).
3. Proyecto y dependencias (Spring Initializr).
4. Seguridad (login, rutas protegidas).
5. Plantilla Bootstrap Tabler (layout + sidebar "Gestión de Usuarios").
6. Modelo/Repositorio/Servicio (Cliente).
7. CRUD Usuarios (lista, crear, editar, eliminar físico).
8. Pulido UI (alertas, validaciones).
9. Pruebas (funcionales, CSRF, navegadores).
10. README y capturas.
