# AI Chat

Una aplicación Android para chatear con inteligencia artificial que permite mantener conversaciones con un asistente de IA y guardar un historial de conversaciones.

## 📱 Características principales

- Chat en tiempo real con IA de OpenAI
- Historial de conversaciones
- Generación automática de títulos para conversaciones mediante IA
- Interfaz de usuario intuitiva basada en Material 3
- Soporte para eliminación de conversaciones
- Diseño adaptado para internacionalización (i18n)

## 🏗️ Arquitectura

La aplicación está construida siguiendo una arquitectura limpia con separación de capas:

```
com.teseostudios.aichat/
├── data/                  # Capa de datos
│   ├── local/             # Persistencia local (Room)
│   │   ├── dao/           # Objetos de acceso a datos
│   │   └── entity/        # Entidades de base de datos
│   ├── mapper/            # Mappers para convertir entre entidades y modelos
│   ├── network/           # Comunicación con APIs externas
│   └── repository/        # Implementaciones de repositorios
├── di/                    # Inyección de dependencias (Hilt)
├── domain/                # Capa de dominio
│   ├── model/             # Modelos de dominio
│   └── repository/        # Interfaces de repositorios
└── ui/                    # Capa de presentación
    ├── components/        # Componentes UI reutilizables
    ├── screens/           # Pantallas de la aplicación
    │   └── chat/          # Pantalla de chat y componentes relacionados
    ├── theme/             # Estilos y temas de la aplicación
    └── viewmodel/         # ViewModels (MVVM)
```

### Patrones de diseño

- **MVVM** (Model-View-ViewModel) para la separación entre la UI y la lógica de negocio
- **Repository Pattern** para abstraer las fuentes de datos
- **Clean Architecture** para la separación de capas y responsabilidades
- **Dependency Injection** para la gestión de dependencias

## 🛠️ Tecnologías utilizadas

- **Kotlin** como lenguaje de programación principal
- **Jetpack Compose** para la interfaz de usuario declarativa
- **Material 3** como sistema de diseño
- **Hilt** para la inyección de dependencias
- **Room** para la persistencia local de datos
- **Kotlin Coroutines & Flow** para operaciones asíncronas y programación reactiva
- **ViewModel** de Architecture Components
- **OpenAI API** para la funcionalidad de IA
- **Ktor** como cliente HTTP para las comunicaciones con la API de OpenAI

## 🧩 Características técnicas destacables

### Persistencia
- **Base de datos Room** para almacenar conversaciones y mensajes
- **Relaciones entre entidades** (Conversación → Mensajes)
- **Flujos reactivos** para observar cambios en los datos

### Interacción con IA
- Integración con la API de OpenAI para generar respuestas
- Uso del contexto de conversación para mantener coherencia
- Generación de títulos de conversación utilizando la misma IA

### UI/UX
- Diseño con Material 3 para una apariencia moderna
- Animaciones y transiciones suaves
- Menú lateral para navegar entre conversaciones
- Indicadores de carga durante las comunicaciones con la API

## 🔄 Flujo de trabajo de desarrollo

Esta aplicación ha sido desarrollada utilizando un flujo de trabajo híbrido que combina:

- **Android Studio** para el diseño inicial, configuración del proyecto y pruebas en dispositivos
- **Cursor** como IDE para la implementación de código, utilizando su asistencia de IA para mejorar la productividad

Este enfoque dual permitió aprovechar las fortalezas de ambas herramientas: las capacidades específicas de Android de Android Studio y la asistencia inteligente de Cursor para la escritura de código.

## 🚀 Configuración del proyecto

### Requisitos
- Android Studio Iguana o superior
- JDK 11 o superior
- API Key de OpenAI

### Configuración
1. Clona el repositorio
2. Crea un archivo `local.properties` en la raíz del proyecto y añade tu API key:
   ```
   open.api.key="tu_api_key_de_openai"
   ```
3. Sincroniza el proyecto con Gradle
4. Ejecuta la aplicación en un emulador o dispositivo

## 📝 Notas adicionales

- La aplicación está preparada para la internacionalización, con todos los textos definidos en recursos de strings.
- Se utiliza KSP (Kotlin Symbol Processing) en lugar de KAPT para mejorar los tiempos de compilación.
- El código está estructurado para facilitar futuras ampliaciones como la adición de nuevos modelos de IA o funcionalidades.

## 🙏 Agradecimientos

Gracias a **Antonio Leiva** y su canal DevExpert por enseñar esta metodología de trabajo que combina las capacidades de **Cursor** y **Android Studio**, lo cual ha permitido aumentar significativamente la productividad en el desarrollo. La integración de la asistencia de IA de Cursor con las herramientas específicas de Android Studio ha sido clave para implementar rápidamente funcionalidades complejas, todo esto aprendido a través de su excelente taller:

- [Taller de Android de Antonio Leiva en DevExpert](https://www.youtube.com/watch?v=VGLXRna1i3U&t=2853s&ab_channel=DevExpert-Programaci%C3%B3nAndroidyKotlin)
