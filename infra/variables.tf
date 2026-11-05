variable "region" {
  type    = string
  default = "ap-south-1"
}

# Free plan account pe kuch instance types blocked ho sakte hain.
# Error aaye to m7i-flex.large, c7i-flex.large ya t3.large try karo.
variable "instance_type" {
  type    = string
  default = "m7i-flex.large"
}

# Tumhara public IP, format: "1.2.3.4/32"  (google pe "what is my ip")
variable "my_ip_cidr" {
  type = string
}

variable "db_password" {
  type      = string
  sensitive = true
}

variable "keycloak_admin_password" {
  type      = string
  sensitive = true
}
