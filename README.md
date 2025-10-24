# MetroLima GO

Aplicación móvil para la planificación de viajes en el Metro de Lima, desarrollada en Android con Jetpack Compose.

## 🎯 Objetivo

Brindar a los ciudadanos y visitantes una forma rápida y visual de planificar sus viajes en el Metro de Lima y rutas integradas (alimentadores, corredores y transporte complementario).

## 🚀 Características Principales

### 📱 Módulos Implementados

- **🏠 Home**: Pantalla principal con acceso a todas las funcionalidades
- **🚇 Estaciones**: Lista y detalles de estaciones del Metro de Lima
- **🗺️ Rutas**: Planificador de rutas con origen y destino
- **📍 Mapas**: Visualización de mapas con Google Maps
- **⚡ En Vivo**: Estado del servicio en tiempo real
- **💬 Chat**: Asistente IA con respuestas predefinidas
- **⚙️ Configuración**: Temas, idiomas y información de la app

### 🎨 Diseño y UX

- **Material 3**: Diseño moderno con Material Design 3
- **Temas**: Modo claro y oscuro
- **Idiomas**: Soporte para español e inglés
- **Navegación**: Bottom Navigation con 5 secciones principales
- **Colores**: Esquema de colores característico del Metro de Lima (naranja)

## 🛠️ Tecnologías Utilizadas

- **Android**: Kotlin + Jetpack Compose
- **Navegación**: Navigation Compose
- **Base de Datos**: Room (SQLite)
- **Mapas**: Google Maps Compose
- **Red**: Retrofit + OkHttp
- **Corrutinas**: Kotlin Coroutines
- **UI**: Material 3 Components
- **Localización**: Sistema de traducciones personalizado

## 📁 Estructura del Proyecto

```
app/src/main/java/com/tecsup/metrolimago1/
├── data/
│   ├── local/          # Room Database
│   └── remote/         # API Services
├── domain/
│   └── models/         # Data Models
├── ui/
│   ├── screens/        # Pantallas principales
│   ├── components/     # Componentes reutilizables
│   └── theme/          # Temas y colores
├── navigation/         # Navegación entre pantallas
└── utils/             # Utilidades (traducciones, locales)
```

## 🎯 Funcionalidades por Módulo

### 🏠 Home Screen
- Acceso rápido a todas las funcionalidades
- Búsqueda de estaciones
- Próximas llegadas
- Notificaciones del servicio

### 🚇 Estaciones
- Lista completa de estaciones
- Búsqueda por nombre o línea
- Detalles individuales con mapas
- Información de horarios y servicios

### 🗺️ Planificador de Rutas
- Selección de origen y destino
- Cálculo de tiempo estimado
- Visualización de pasos del recorrido
- Integración con mapas

### 📍 Mapas
- Google Maps integrado
- Marcadores de estaciones
- Rutas visuales
- Filtros por líneas

### ⚡ Estado en Vivo
- Estado operacional del servicio
- Alertas y notificaciones
- Horarios actualizados
- Información de mantenimiento

### 💬 Chat IA
- Asistente con respuestas predefinidas
- Preguntas frecuentes
- Soporte en español e inglés
- Información del Metro de Lima

### ⚙️ Configuración
- Cambio de tema (claro/oscuro)
- Selección de idioma
- Información de la aplicación
- Datos del desarrollador

## 🎨 Sistema de Temas

### Colores Característicos
- **Naranja Metro**: Color principal del Metro de Lima
- **Verde Línea 1**: Identificación visual
- **Azul Línea 2**: Identificación visual
- **Amarillo Línea 3**: Identificación visual

### Modo Oscuro/Claro
- Adaptación automática del sistema
- Colores optimizados para cada modo
- Iconos y textos con contraste adecuado

## 🌐 Sistema de Localización

- **Español**: Idioma por defecto
- **Inglés**: Traducción completa
- **Persistencia**: Guardado de preferencias
- **Cambio dinámico**: Sin reinicio de aplicación

## 📱 Pantallas Principales

1. **Splash Screen**: Pantalla de carga con logo
2. **Home**: Dashboard principal
3. **Estaciones**: Lista y búsqueda
4. **Detalle Estación**: Información completa
5. **Planificador**: Selección de rutas
6. **Mapas**: Visualización geográfica
7. **En Vivo**: Estado del servicio
8. **Chat**: Asistente IA
9. **Configuración**: Ajustes de la app

## 🚀 Instalación y Uso

1. Clonar el repositorio
2. Abrir en Android Studio
3. Sincronizar dependencias
4. Ejecutar en dispositivo o emulador
5. Configurar API Key de Google Maps (opcional)

## 📋 Requisitos

- Android Studio Arctic Fox o superior
- Android SDK 24+
- Kotlin 1.8+
- Google Maps API Key (para funcionalidad completa)

## 👥 Desarrollo

**Docente**: Juan León S.  
**Proyecto**: MetroLima GO  
**Tecnología**: Android + Jetpack Compose  

## 📄 Licencia

Proyecto académico desarrollado para fines educativos.

---

*MetroLima GO - Tu compañero ideal para navegar por el sistema de Metro de Lima*