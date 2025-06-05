# Price Application

## Descripción
Este proyecto es ahora un conjunto de microservicios para la gestión de clientes y precios. Cada servicio es una aplicación Spring Boot independiente con su propia API REST.

### Arquitectura
Se dividen varios microservicios: **price-service**, **customer-service**, **config-server** y **discovery-server**. Los dos primeros contienen la lógica de negocio mientras que los nuevos servicios proporcionan configuración centralizada y discovery con Eureka.

### Características:
- CRUD de precios.
- Validación de datos a través de anotaciones.
- Base de datos en memoria H2.
- Cobertura de pruebas unitarias e integradas.
- Comunicación asíncrona mediante RabbitMQ.

## Tecnologías Utilizadas
- **Java 17**
- **Spring Boot 3.x**
- **Spring WebFlux** para manejo reactivo de peticiones
- **H2 Database**
- **JPA (Java Persistence API)**
- **Jakarta Validation**
- **JUnit 5** y **Spring MockMvc** para pruebas
- **RabbitMQ** para mensajería asíncrona

## Instalación y Ejecución

### Requisitos Previos
- **JDK 17+**
- **Maven 3+**

### Estructura del Proyecto
```bash
price-service/        # Microservicio para la gestión de precios
customer-service/     # Microservicio para la gestión de clientes
config-server/        # Centraliza la configuración de los servicios
discovery-server/     # Servidor Eureka para descubrir servicios
```

### Clonar el Repositorio
```bash
git clone https://github.com/CesarAugustusGroB/PriceApp.git 
```

### Swagger
La documentación interactiva se encuentra disponible una vez arranque la aplicación.
Abre tu navegador y navega a:

```
http://localhost:8080/swagger-ui.html
```

### Mensajería Asíncrona
Se integró RabbitMQ como sistema de colas para propagar eventos de forma desacoplada.
Los servicios publican mensajes en `price.events` y `customer.events` cuando se
crean o actualizan registros, permitiendo que otros sistemas reaccionen sin
bloquear la operación principal.

### Config Server y Eureka
Primero inicia **discovery-server** y **config-server**:
```bash
mvn -pl discovery-server spring-boot:run
mvn -pl config-server spring-boot:run
```
Luego ejecuta los microservicios normalmente. Ellos obtendrán su configuración desde `config-server` y se registrarán en Eureka.
