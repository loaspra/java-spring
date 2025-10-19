terraform {
  required_version = ">= 1.0"
  
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~> 3.0"
    }
    random = {
      source  = "hashicorp/random"
      version = "~> 3.0"
    }
  }
}

provider "azurerm" {
  subscription_id = "a9582bdc-7a61-4877-845f-12d80041a7c8"
  tenant_id       = "542d0381-2f3c-4694-8f70-6f864a6db8e6"
  
  features {
    resource_group {
      prevent_deletion_if_contains_resources = false
    }
  }
}

resource "random_string" "suffix" {
  length  = 6
  special = false
  upper   = false
}

resource "azurerm_resource_group" "fitness" {
  name     = "rg-fitnessapp-${var.environment}"
  location = var.location
  
  tags = {
    Environment = var.environment
    Project     = "FitnessApp"
    ManagedBy   = "Terraform"
  }
}

resource "azurerm_mssql_server" "fitness" {
  name                         = "sql-fitnessapp-${var.environment}-${random_string.suffix.result}"
  resource_group_name          = azurerm_resource_group.fitness.name
  location                     = azurerm_resource_group.fitness.location
  version                      = "12.0"
  administrator_login          = var.sql_admin_username
  administrator_login_password = var.sql_admin_password
  
  minimum_tls_version = "1.2"
  
  tags = {
    Environment = var.environment
    Project     = "FitnessApp"
  }
}

resource "azurerm_mssql_database" "fitness" {
  name           = "FitnessAppDB"
  server_id      = azurerm_mssql_server.fitness.id
  collation      = "SQL_Latin1_General_CP1_CI_AS"
  license_type   = "LicenseIncluded"
  sku_name       = "Basic"
  max_size_gb    = 2
  zone_redundant = false
  
  tags = {
    Environment = var.environment
    Project     = "FitnessApp"
  }
}

resource "azurerm_mssql_firewall_rule" "allow_azure_services" {
  name             = "AllowAzureServices"
  server_id        = azurerm_mssql_server.fitness.id
  start_ip_address = "0.0.0.0"
  end_ip_address   = "0.0.0.0"
}

resource "azurerm_mssql_firewall_rule" "allow_all" {
  name             = "AllowAll"
  server_id        = azurerm_mssql_server.fitness.id
  start_ip_address = "0.0.0.0"
  end_ip_address   = "255.255.255.255"
}
