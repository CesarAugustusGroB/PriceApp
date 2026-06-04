# Price Application

Plataforma de **microservicios** para la gestión de precios y clientes. Cada servicio es una
aplicación Spring Boot independiente con su propia API REST, asegurada con JWT y registrada en
Eureka, con configuración centralizada y mensajería asíncrona vía RabbitMQ.

## Features

- **CRUD de precios y clientes** con validación de datos mediante anotaciones (Jakarta Validation).
- **Seguridad con JWT**: autenticación en `gateway-service` y validación de tokens como
  *OAuth2 Resource Server* en los microservicios de negocio.
- **API Gateway** (`gateway-service`) como punto de entrada único, con enrutamiento (Spring Cloud
  Gateway), *rate limiting* (bucket4j) y logging de peticiones.
- **Service discovery** con Eureka (`discovery-server`) y **configuración centralizada**
  (`config-server`, Spring Cloud Config).
- **Mensajería asíncrona** con RabbitMQ: los servicios publican eventos al crear/actualizar registros.
- **Predicción de precios** (`/api/prices/predict`) mediante un modelo de regresión lineal simple
  sobre el histórico.
- **Documentación interactiva** con Swagger UI (springdoc OpenAPI).
- **Pruebas** unitarias e integradas con JUnit 5, MockMvc y Mockito.
- **Despliegue**: `Dockerfile` por servicio, manifiestos de Kubernetes (`k8s/`), IaC con
  Terraform/Ansible (`iac/`), pipeline `Jenkinsfile` y un ejemplo de AWS Lambda (`lambda/`).

## Tech Stack

- **Java 17**, **Spring Boot 3.x**, **Maven** (proyecto multi-módulo)
- **Spring Cloud**: Gateway, Netflix Eureka, Config
- **Spring Security** + **OAuth2 Resource Server** + **JJWT** (tokens JWT)
- **Spring WebFlux** (gateway y endpoints reactivos de price-service) y **Spring MVC**
- **Spring Data JPA** + **H2** (base de datos en memoria)
- **Spring AMQP / RabbitMQ** para eventos
- **springdoc-openapi** (Swagger UI), **Lombok**, **Jakarta Validation**
- **JUnit 5**, **MockMvc**, **Mockito**

## Getting Started

### Requisitos Previos

- **JDK 17+**
- **Maven 3+**
- **RabbitMQ** en ejecución (para la mensajería de eventos)

### Clonar el repositorio

```bash
git clone https://github.com/CesarAugustusGroB/trade-price-app.git
cd trade-price-app
```

### Compilar

```bash
mvn clean package
```

### Ejecutar

Inicia primero la infraestructura (discovery + config) y luego los servicios de negocio:

```bash
mvn -pl discovery-server spring-boot:run
mvn -pl config-server spring-boot:run
mvn -pl gateway-service spring-boot:run
mvn -pl price-service spring-boot:run
mvn -pl customer-service spring-boot:run
```

Los microservicios obtienen su configuración desde `config-server` y se registran en Eureka.

### Documentación de la API

Cada microservicio expone Swagger UI una vez en ejecución:

```
http://localhost:<puerto>/swagger-ui.html
```

También puedes importar la especificación OpenAPI en Postman desde `http://localhost:<puerto>/api-docs`,
o usar la colección de ejemplo en [`docs/PriceApp.postman_collection.json`](docs/PriceApp.postman_collection.json).

### Despliegue en contenedores y Kubernetes

```bash
mvn clean package -DskipTests
docker build -t myrepo/price-service:latest price-service   # ejemplo por servicio
kubectl apply -f k8s/                                        # despliega y expone gateway-service
```

Para infraestructura como código, consulta [`iac/terraform`](iac/terraform/) y
[`iac/ansible`](iac/ansible/). El `Jenkinsfile` define un pipeline que compila, prueba, construye
imágenes Docker, despliega en Kubernetes y realiza *rollback* ante fallos.

## Project Structure

```
trade-price-app/
├── price-service/        # CRUD de precios, eventos y predicción de precios
├── customer-service/     # CRUD de clientes y autenticación
├── gateway-service/      # API Gateway: enrutamiento, JWT login y rate limiting
├── config-server/        # Configuración centralizada (Spring Cloud Config)
├── discovery-server/     # Servidor Eureka (service discovery)
├── k8s/                  # Manifiestos de Kubernetes
├── iac/                  # Terraform y Ansible
├── lambda/               # Ejemplo de función AWS Lambda
├── docs/                 # Colección Postman y documentación
├── Jenkinsfile           # Pipeline CI/CD de ejemplo
└── pom.xml               # POM padre multi-módulo
```
