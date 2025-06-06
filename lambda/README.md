# AWS Lambda Functions

Este directorio contiene funciones Lambda de ejemplo para realizar tareas puntuales u operaciones programadas.

`notificationHandler.js` muestra cómo recibir eventos y procesarlos para enviar notificaciones o ejecutar auditorías en tiempo real.

Puedes desplegar la función con la AWS CLI:

```bash
zip function.zip notificationHandler.js
aws lambda create-function \
  --function-name notificationHandler \
  --runtime nodejs18.x \
  --handler notificationHandler.handler \
  --role <ARN_ROLE_LAMBDA> \
  --zip-file fileb://function.zip
```

También puedes utilizar el framework **Serverless** para automatizar la configuración y el despliegue.
