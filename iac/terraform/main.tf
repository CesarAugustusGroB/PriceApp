terraform {
  required_version = ">= 1.2"
  required_providers {
    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = "~> 2.23"
    }
  }
}

provider "kubernetes" {
  config_path = var.kubeconfig
}

variable "kubeconfig" {
  description = "Ruta al archivo kubeconfig"
  type        = string
  default     = "~/.kube/config"
}

resource "kubernetes_namespace" "priceapp" {
  metadata {
    name = "priceapp"
  }
}
