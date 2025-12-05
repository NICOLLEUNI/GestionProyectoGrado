# GestionProyectoGrado
Proyecto para la gestion de proyectos de grado con microservicios
Este repositorio contiene una plataforma orientada a la gestión integral de proyectos de grado, desarrollada bajo una arquitectura basada en microservicios. Esta estructura favorece la modularidad, escalabilidad y mantenibilidad del sistema.

La solución integra tanto componentes backend como frontend, permitiendo la interacción del usuario y el soporte de procesos académicos relacionados con el ciclo de vida del proyecto.

#Estructura del Repositorio
Frontend
Aplicación cliente encargada de la interfaz de usuario, mediante la cual se accede e interactúa con los servicios del sistema.
Este componente también participa en la comunicación con otros módulos a través de RabbitMQ.

##Microservicio - Ejecución de Proyecto
Módulo responsable del seguimiento, control y administración del desarrollo del proyecto de grado.
Expone sus servicios mediante documentación interactiva utilizando Swagger y participa en el intercambio de mensajes a través de RabbitMQ.

##Microservicio - Evaluación
Servicio responsable de gestionar evaluaciones, revisiones y decisiones académicas vinculadas al proyecto.
Participa en la comunicación asíncrona mediante RabbitMQ.

##Microservicio - Ingreso
Componente encargado de la autenticación, registro y validación de usuarios, basado en el uso de tokens.
Además, expone sus servicios mediante Swagger y se integra con otros módulos por medio de RabbitMQ.

##Microservicio - Notificación
Servicio dedicado al envío de alertas y mensajes relacionados con el proceso de gestión del proyecto.
Utiliza RabbitMQ para la distribución de notificaciones hacia otros componentes.

##Microservicio - Submission
Módulo encargado de la carga, recepción y gestión de documentos asociados al proyecto.
Participa en la comunicación asíncrona mediante RabbitMQ.

3#Archivos Adicionales
El repositorio incluye archivos de configuración, documentación técnica y licencias necesarias para el despliegue y la operación adecuada de la plataforma.


