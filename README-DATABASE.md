# FitnessApp - Guía de Configuración de Base de Datos

## ✅ Infraestructura Creada

**Resource Group:** rg-fitnessapp-oct2025  
**Location:** West US 2  
**SQL Server:** sql-fitness819862.database.windows.net  
**Database:** FitnessAppDB  
**Usuario Admin:** sqladmin  
**Contraseña:** FitnessApp2024!Secure  

## 📋 Pasos para Ejecutar Scripts SQL

### Opción 1: Azure Portal (Query Editor) - MÁS FÁCIL

1. Ir a https://portal.azure.com
2. Navegar a **SQL databases** → **FitnessAppDB**
3. En el menú izquierdo, seleccionar **Query editor (preview)**
4. Autenticarse con:
   - **Login:** sqladmin
   - **Password:** FitnessApp2024!Secure
5. Copiar y pegar el contenido de `schema.sql` y ejecutar
6. Luego copiar y pegar el contenido de `seed.sql` y ejecutar

### Opción 2: Azure Data Studio (Recomendado para desarrollo)

1. Descargar e instalar Azure Data Studio: https://aka.ms/azuredatastudio
2. Conectar con estos datos:
   - **Server:** sql-fitness819862.database.windows.net
   - **Authentication type:** SQL Login
   - **User name:** sqladmin
   - **Password:** FitnessApp2024!Secure
   - **Database:** FitnessAppDB
   - **Encrypt:** True
3. Abrir `schema.sql` y ejecutar
4. Abrir `seed.sql` y ejecutar

### Opción 3: sqlcmd (línea de comandos)

```bash
# Instalar sqlcmd (si no está instalado)
# En Arch Linux:
yay -S mssql-tools

# Ejecutar schema
sqlcmd -S sql-fitness819862.database.windows.net -d FitnessAppDB -U sqladmin -P 'FitnessApp2024!Secure' -i schema.sql

# Ejecutar seed data
sqlcmd -S sql-fitness819862.database.windows.net -d FitnessAppDB -U sqladmin -P 'FitnessApp2024!Secure' -i seed.sql
```

## 🔐 Información de Clientes de Prueba

Todos los clientes tienen la contraseña: **fitness123**

| Email | RUT | Nombre |
|-------|-----|--------|
| juan.perez@email.com | 12345678-9 | Juan Pérez |
| maria.lopez@email.com | 98765432-1 | María López |
| pedro.rodriguez@email.com | 11223344-5 | Pedro Rodríguez |
| ana.martinez@email.com | 55667788-9 | Ana Martínez |
| luis.sanchez@email.com | 22334455-6 | Luis Sánchez |
| carolina.fernandez@email.com | 33445566-7 | Carolina Fernández |
| roberto.gonzalez@email.com | 44556677-8 | Roberto González |
| daniela.herrera@email.com | 66778899-0 | Daniela Herrera |

**Nota:** El hash BCrypt almacenado es: `$2b$10$7IQ9Z.siMWTs/HQafefLRO7SHcQq6Dn/YB4FyMTUQUkIeIggGcSqi`

## 📦 Próximos Pasos

1. Ejecutar los scripts SQL en el orden: `schema.sql` → `seed.sql`
2. Crear proyecto Spring Boot con las dependencias:
   - Spring Web
   - Spring Data JPA
   - Thymeleaf
   - SQL Server Driver (com.microsoft.sqlserver:mssql-jdbc)
   - Spring Security
   - Validation
3. Configurar `application.properties` con la conexión a Azure SQL
4. Implementar CRUD de Clientes
