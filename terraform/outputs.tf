output "resource_group_name" {
  description = "Resource Group name"
  value       = azurerm_resource_group.fitness.name
}

output "sql_server_name" {
  description = "SQL Server name"
  value       = azurerm_mssql_server.fitness.name
}

output "sql_server_fqdn" {
  description = "SQL Server FQDN"
  value       = azurerm_mssql_server.fitness.fully_qualified_domain_name
}

output "database_name" {
  description = "Database name"
  value       = azurerm_mssql_database.fitness.name
}

output "sql_admin_username" {
  description = "SQL Server admin username"
  value       = var.sql_admin_username
  sensitive   = true
}

output "jdbc_connection_string" {
  description = "JDBC connection string for Spring Boot"
  value       = "jdbc:sqlserver://${azurerm_mssql_server.fitness.fully_qualified_domain_name}:1433;database=${azurerm_mssql_database.fitness.name};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;"
  sensitive   = true
}
