# Price Application

## Descripción
Este proyecto es una aplicación para la gestión de precios. Utiliza **Spring Boot**, **H2 Database** y sigue el enfoque de **Arquitectura Hexagonal** (Puertos y Adaptadores). El objetivo principal es proporcionar un servicio REST para crear, actualizar, leer y eliminar precios.

### Arquitectura
El proyecto sigue una arquitectura hexagonal, donde la lógica de negocio está desacoplada de los adaptadores de entrada (como controladores HTTP) y adaptadores de salida (como repositorios JPA).

### Características:
- CRUD de precios.
- Validación de datos a través de anotaciones.
- Base de datos en memoria H2.
- Cobertura de pruebas unitarias e integradas.

## Tecnologías Utilizadas
- **Java 17**
- **Spring Boot 3.x**
- **H2 Database**
- **JPA (Java Persistence API)**
- **Jakarta Validation**
- **JUnit 5** y **Spring MockMvc** para pruebas

## Instalación y Ejecución

### Requisitos Previos
- **JDK 17+**
- **Maven 3+**

### Estructura del Proyecto
```bash
src
├── main
│   ├── java
│   │   └── com/miempresa/priceapplication
│   │       ├── controller      # Controladores REST (adaptadores de entrada)
│   │       ├── service         # Lógica de negocio (núcleo)
│   │       ├── repository      # Repositorios JPA (adaptadores de salida)
│   │       └── model           # Entidades y modelos de datos
│   └── resources
│       ├── application.properties  # Configuración de la base de datos H2
│       └── data.sql               # Datos de ejemplo para la base de datos
└── test
└── java/com/miempresa/priceapplication # Pruebas
```

### Clonar el Repositorio
```bash
git clone https://github.com/CesarAugustusGroB/PriceApp.git 
```

### Swagger
Puedes consultar la API en: http://localhost:8080/swagger-ui/index.html
