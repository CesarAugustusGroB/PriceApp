exports.handler = async (event) => {
    console.log('Received event:', JSON.stringify(event));
    // Procesar el evento y ejecutar tareas como notificaciones o auditorías
    return {
        statusCode: 200,
        body: JSON.stringify({ message: 'Event processed' })
    };
};
