#!/usr/bin/env python3
"""
Script para ejecutar SQL en Azure SQL Database
"""
import subprocess
import sys

def install_package(package):
    """Instala un paquete de Python si no está disponible"""
    subprocess.check_call([sys.executable, "-m", "pip", "install", "-q", package])

try:
    import pyodbc
except ImportError:
    print("Instalando pyodbc...")
    install_package("pyodbc")
    import pyodbc

SERVER = 'sql-fitness819862.database.windows.net'
DATABASE = 'FitnessAppDB'
USERNAME = 'sqladmin'
PASSWORD = 'FitnessApp2024!Secure'

def execute_sql_file(filename):
    """Ejecuta un archivo SQL en la base de datos"""
    connection_string = (
        f'DRIVER={{ODBC Driver 18 for SQL Server}};'
        f'SERVER={SERVER};'
        f'DATABASE={DATABASE};'
        f'UID={USERNAME};'
        f'PWD={PASSWORD};'
        f'Encrypt=yes;'
        f'TrustServerCertificate=no;'
        f'Connection Timeout=30;'
    )
    
    try:
        print(f"Conectando a {SERVER}...")
        conn = pyodbc.connect(connection_string)
        cursor = conn.cursor()
        
        print(f"Leyendo {filename}...")
        with open(filename, 'r', encoding='utf-8') as f:
            sql_content = f.read()
        
        # Dividir por GO si existe, sino por punto y coma
        if 'GO' in sql_content.upper():
            statements = [s.strip() for s in sql_content.split('GO') if s.strip()]
        else:
            statements = [s.strip() for s in sql_content.split(';') if s.strip()]
        
        print(f"Ejecutando {len(statements)} statements...")
        for i, statement in enumerate(statements, 1):
            if statement and not statement.strip().startswith('--'):
                try:
                    cursor.execute(statement)
                    conn.commit()
                    print(f"  ✓ Statement {i}/{len(statements)} ejecutado")
                except Exception as e:
                    print(f"  ✗ Error en statement {i}: {e}")
                    # Continuar con el siguiente statement
        
        cursor.close()
        conn.close()
        print(f"✓ {filename} ejecutado exitosamente")
        return True
        
    except pyodbc.Error as e:
        print(f"✗ Error de conexión: {e}")
        return False
    except FileNotFoundError:
        print(f"✗ Archivo no encontrado: {filename}")
        return False
    except Exception as e:
        print(f"✗ Error: {e}")
        return False

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python3 execute_sql.py <archivo.sql>")
        sys.exit(1)
    
    sql_file = sys.argv[1]
    success = execute_sql_file(sql_file)
    sys.exit(0 if success else 1)
