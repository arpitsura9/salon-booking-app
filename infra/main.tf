terraform {
  required_version = ">= 1.5"
  required_providers {
    aws = { source = "hashicorp/aws", version = "~> 5.0" }
  }
}

provider "aws" {
  region = var.region
}

# ---------- Default VPC use kar rahe hain (NAT Gateway ka kharcha nahi) ----------
data "aws_vpc" "default" {
  default = true
}

data "aws_subnets" "default" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.default.id]
  }
}

data "aws_ssm_parameter" "al2023" {
  name = "/aws/service/ami-amazon-linux-latest/al2023-ami-kernel-default-x86_64"
}

# ---------- Security group: sirf tumhare IP se access ----------
resource "aws_security_group" "salon" {
  name        = "salon-practice-sg"
  description = "Salon app practice - only my IP"
  vpc_id      = data.aws_vpc.default.id

  dynamic "ingress" {
    for_each = {
      frontend = 80
      gateway  = 5000
      eureka   = 8070
      keycloak = 8080
      rabbitmq = 15672
    }
    content {
      description = ingress.key
      from_port   = ingress.value
      to_port     = ingress.value
      protocol    = "tcp"
      cidr_blocks = [var.my_ip_cidr]
    }
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# ---------- SSM role: SSH key ke bina console se terminal milega ----------
resource "aws_iam_role" "ssm" {
  name = "salon-practice-ssm-role"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect    = "Allow"
      Action    = "sts:AssumeRole"
      Principal = { Service = "ec2.amazonaws.com" }
    }]
  })
}

resource "aws_iam_role_policy_attachment" "ssm" {
  role       = aws_iam_role.ssm.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore"
}

resource "aws_iam_instance_profile" "ssm" {
  name = "salon-practice-ssm-profile"
  role = aws_iam_role.ssm.name
}

# ---------- EC2 ----------
locals {
  user_data = <<-EOT
    #!/bin/bash
    set -ex
    dnf install -y docker
    systemctl enable --now docker
    mkdir -p /usr/local/lib/docker/cli-plugins
    curl -SL https://github.com/docker/compose/releases/download/v2.29.7/docker-compose-linux-x86_64 \
      -o /usr/local/lib/docker/cli-plugins/docker-compose
    chmod +x /usr/local/lib/docker/cli-plugins/docker-compose
    mkdir -p /opt/salon
    echo "${base64encode(file("${path.module}/files/docker-compose.yml"))}" | base64 -d > /opt/salon/docker-compose.yml
    echo "${base64encode(file("${path.module}/files/init.sql"))}" | base64 -d > /opt/salon/init.sql
    cat > /opt/salon/.env <<ENVEOF
    DB_PASSWORD=${var.db_password}
    KC_ADMIN_PASSWORD=${var.keycloak_admin_password}
    ENVEOF
    cat > /opt/salon/kc-fix.sh <<'KCEOF'
    #!/bin/bash
    # Keycloak auto-setup (practice): HTTP allow, client, roles, admin user
    PASS=$(grep KC_ADMIN_PASSWORD /opt/salon/.env | cut -d= -f2)
    kc() { docker exec keycloak /opt/keycloak/bin/kcadm.sh "$@"; }

    until kc config credentials --server http://localhost:8080 --realm master --user admin --password "$PASS"; do
      sleep 10
    done

    kc update realms/master -s sslRequired=NONE

    CID=0b8d3ca2-520d-4e19-9d4b-367160644fbc
    kc create clients -r master \
      -s id=$CID \
      -s clientId=salon-booking-client \
      -s secret=kkr49jD7Hc4M22rOwJOMIP00ZU505JFlsYJOmBGrvEklm47vJ49JJdumoIwBqYcnI6plJ28Ms43MI0b0wA9Eyt \
      -s enabled=true \
      -s publicClient=false \
      -s directAccessGrantsEnabled=true \
      -s standardFlowEnabled=true \
      -s 'redirectUris=["*"]' \
      -s 'webOrigins=["*"]'

    for R in CUSTOMER ADMIN SALON_OWNER; do
      kc create clients/$CID/roles -r master -s name=$R
    done

    kc create users -r master -s username=arpit -s enabled=true -s emailVerified=true \
      -s email=arpit@example.com -s firstName=Arpit -s lastName=Admin
    kc set-password -r master --username arpit --new-password admin
    kc add-roles -r master --uusername arpit --rolename admin
    echo "Keycloak setup done"
    KCEOF
    cd /opt/salon
    docker compose up -d
    nohup bash /opt/salon/kc-fix.sh > /var/log/kc-fix.log 2>&1 &
  EOT
}

resource "aws_instance" "salon" {
  ami                         = data.aws_ssm_parameter.al2023.value
  instance_type               = var.instance_type
  subnet_id                   = data.aws_subnets.default.ids[0]
  vpc_security_group_ids      = [aws_security_group.salon.id]
  iam_instance_profile        = aws_iam_instance_profile.ssm.name
  associate_public_ip_address = true
  user_data                   = local.user_data
  user_data_replace_on_change = true

  root_block_device {
    volume_size = 30
    volume_type = "gp3"
  }

  tags = { Name = "salon-practice" }
}
