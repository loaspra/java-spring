#!/usr/bin/env python3
import pymssql
import sys

SERVER = 'sql-fitness819862.database.windows.net'
DATABASE = 'FitnessAppDB'
USERNAME = 'sqladmin'
PASSWORD = 'FitnessApp2024!Secure'

def execute_sql_file(cursor, filename):
    print(f"\n{'='*60}")
    print(f"Ejecutando: {filename}")
    print('='*60)
    
    with open(filename, 'r', encoding='utf-8') as f:
        sql_content = f.read()
    
    statements = []
    if 'GO' in sql_content:
        statements = [s.strip() for s in sql_content.split('GO') if s.strip()]
    else:
        lines = []
        current_statement = []
        for line in sql_content.split('\n'):
            line = line.strip()
            if line and not line.startswith('--'):
                current_statement.append(line)
                if line.endswith(';'):
                    statements.append(' '.join(current_statement))
                    current_statement = []
        if current_statement:
            statements.append(' '.join(current_statement))
    
    for i, statement in enumerate(statements, 1):
        if statement.strip():
            try:
                print(f"\n[{i}/{len(statements)}] Ejecutando statement...")
                cursor.execute(statement)
                print(f"✓ Statement {i} ejecutado exitosamente")
            except Exception as e:
                error_msg = str(e).lower()
                print(f"✗ Error en statement {i}: {e}")
                if "already exists" not in error_msg and "cannot insert duplicate" not in error_msg:
                    raise

def main():
    try:
        print("Conectando a Azure SQL Database...")
        conn = pymssql.connect(
            server=SERVER,
            user=USERNAME,
            password=PASSWORD,
            database=DATABASE,
            as_dict=False
        )
        print("✓ Conexión exitosa!\n")
        
        cursor = conn.cursor()
        
        execute_sql_file(cursor, 'schema.sql')
        conn.commit()
        print("\n✓ Schema creado exitosamente!")
        
        execute_sql_file(cursor, 'seed.sql')
        conn.commit()
        print("\n✓ Datos de prueba insertados exitosamente!")
        
        cursor.execute("SELECT COUNT(*) FROM Clientes")
        count = cursor.fetchone()[0]
        print(f"\n✓ Total de clientes en la base de datos: {count}")
        
        cursor.close()
        conn.close()
        
        print("\n" + "="*60)
        print("¡Scripts SQL ejecutados exitosamente!")
        print("="*60)
        
    except Exception as e:
        print(f"\n✗ Error: {e}")
        sys.exit(1)

if __name__ == "__main__":
    main()
