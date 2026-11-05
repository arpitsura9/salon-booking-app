output "public_ip" {
  value = aws_instance.salon.public_ip
}

output "urls" {
  value = {
    eureka   = "http://${aws_instance.salon.public_ip}:8070"
    gateway  = "http://${aws_instance.salon.public_ip}:5000"
    keycloak = "http://${aws_instance.salon.public_ip}:8080"
    rabbitmq = "http://${aws_instance.salon.public_ip}:15672"
  }
}
