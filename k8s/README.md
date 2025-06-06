# Kubernetes Deployment

Esta carpeta contiene manifiestos de Kubernetes para desplegar los microservicios que componen **Price Application**.

## Construir las imágenes

Ejecute Maven para generar los JAR de cada servicio y cree las imágenes Docker:

```bash
mvn clean package -DskipTests
# Ejemplo para price-service
docker build -t myrepo/price-service:latest price-service
```

Repita el comando `docker build` para cada módulo (customer-service, gateway-service, config-server y discovery-server) cambiando el nombre de la imagen.

## Despliegue en el clúster

Una vez publicadas las imágenes en su registro, aplique los manifiestos:

```bash
kubectl apply -f discovery-server.yaml
kubectl apply -f config-server.yaml
kubectl apply -f price-service.yaml
kubectl apply -f customer-service.yaml
kubectl apply -f gateway-service.yaml
```

El `gateway-service` se expone como un `LoadBalancer` y actúa de punto de entrada al sistema.
