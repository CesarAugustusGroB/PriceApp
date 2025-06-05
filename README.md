# Price Application

## Descripción
Este proyecto es ahora un conjunto de microservicios para la gestión de clientes y precios. Cada servicio es una aplicación Spring Boot independiente con su propia API REST.

### Arquitectura
Se dividen dos microservicios: **price-service** y **customer-service**. Ambos mantienen la filosofía hexagonal, separando la lógica de negocio de los adaptadores de entrada y salida.

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
