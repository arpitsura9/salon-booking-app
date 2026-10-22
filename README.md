\# Salon Booking App



!\[Backend CI](https://github.com/arpitsura9/salon-booking-app/actions/workflows/backend.yml/badge.svg)

!\[Frontend CI](https://github.com/arpitsura9/salon-booking-app/actions/workflows/frontend.yml/badge.svg)



A full-stack salon booking platform where customers can discover salons,

browse services, book appointments, pay online, receive real-time

notifications, and leave reviews. Salon owners get a dashboard to manage

services, categories, bookings, and earnings.



\## Tech Stack



\- \*\*Frontend:\*\* React, Redux, Tailwind CSS, Material UI

\- \*\*Backend:\*\* Java 21, Spring Boot 3 microservices

\- \*\*Service Discovery and Routing:\*\* Eureka Server, Spring Cloud Gateway

\- \*\*Authentication:\*\* Keycloak (OAuth2 / JWT)

\- \*\*Messaging:\*\* RabbitMQ

\- \*\*Database:\*\* MySQL

\- \*\*DevOps:\*\* Docker, Docker Compose, GitHub Actions (CI)



\## Architecture



| Service | Responsibility |

|---|---|

| `eureka-server` | Service registry |

| `gateway-server` | API gateway and security |

| `user-service` | Users and authentication (Keycloak) |

| `salon-service` | Salon details |

| `category-service` | Service categories |

| `service-offering` | Services offered by salons |

| `booking-service` | Appointment booking and reports |

| `payment-service` | Payments |

| `review` | Ratings and reviews |

| `notifications` | Real-time notifications (WebSocket) |



\## Project Structure



```

salon-booking-app/

├── frontend/              # React app

├── microservices/

│   ├── eureka-server/

│   ├── gateway-server/

│   ├── user-service/

│   ├── salon-service/

│   ├── category-service/

│   ├── service-offering/

│   ├── booking-service/

│   ├── payment-service/

│   ├── review/

│   ├── notifications/

│   └── docker-compose/

└── .github/workflows/     # CI pipelines

```



\## Prerequisites



\- Java 21 and Maven

\- Node.js 20

\- MySQL 8

\- RabbitMQ

\- Keycloak

\- Docker (optional)



\## Configuration



Secrets are not stored in this repository. Set them as environment

variables before running the services:



| Variable | Used for |

|---|---|

| `DB\_PASSWORD` | MySQL password |

| `RAZORPAY\_SECRET` | Payment gateway secret |



On Windows:



```

setx DB\_PASSWORD "your-password"

setx RAZORPAY\_SECRET "your-secret"

```



Restart your terminal or IDE after setting them.



\## Running Locally



\### Backend



Start the services in this order:



1\. `eureka-server`

2\. `gateway-server`

3\. All other services (any order)



```bash

cd microservices/<service-name>

mvn spring-boot:run

```



Check that all services are registered on the Eureka dashboard.



\### Frontend



```bash

cd frontend

npm install

npm start

```



\## CI



GitHub Actions builds the frontend and each of the 10 microservices on

every push and pull request:



\- `.github/workflows/backend.yml` - Maven build (matrix of 10 services)

\- `.github/workflows/frontend.yml` - React build



\## Author



Arpit Sura - \[GitHub](https://github.com/arpitsura9)

