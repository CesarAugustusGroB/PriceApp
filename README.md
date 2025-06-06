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

### Observabilidad y Monitoreo
Para vigilar el estado y el rendimiento de los servicios se pueden usar **Prometheus** y **Grafana**. Con Spring Boot Actuator se exponen métricas que Prometheus recolecta y Grafana visualiza en paneles.

Asimismo, la trazabilidad distribuida se puede habilitar añadiendo **Spring Cloud Sleuth** y **Zipkin**. De esta forma es posible seguir el recorrido de las peticiones entre microservicios y detectar cuellos de botella con rapidez.

### Despliegue en Contenedores y Kubernetes
Cada microservicio incluye un `Dockerfile` para generar su imagen. Tras compilar el proyecto con Maven se pueden construir las imágenes:

```bash
mvn clean package -DskipTests
# Ejemplo para price-service
docker build -t myrepo/price-service:latest price-service
```

Los manifiestos de Kubernetes se encuentran en la carpeta [`k8s`](k8s/). Una vez publicadas las imágenes en su registro ejecute:

```bash
kubectl apply -f k8s/
```

Esto desplegará los servicios y expondrá el `gateway-service` como punto de entrada.
