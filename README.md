# MetroLima GO 🚇

**Aplicación móvil para planificación de viajes en el Metro de Lima**

Una aplicación Android desarrollada con Jetpack Compose que brinda a los ciudadanos y visitantes una forma rápida y visual de planificar sus viajes en el Metro de Lima y rutas integradas.

> **Proyecto desarrollado por estudiantes de Tecsup** bajo la supervisión del docente Juan León S.

## 📱 Características Principales

### 🏠 Módulo de Inicio (Home)
- **Pantalla principal** con acceso a todas las funcionalidades
- **Búsqueda inteligente** con autocompletado y filtrado
- **Próximas llegadas** con información en tiempo real
- **Notificaciones** y alertas del sistema
- **Chat con IA** para consultas sobre el metro
- **Pruebas de rutas** con ejemplos prácticos

### 🚉 Módulo de Estaciones
- **Lista completa** de estaciones del Metro de Lima (Línea 1 y 2)
- **Búsqueda avanzada** por nombre o línea
- **Filtros dinámicos** por línea del metro
- **Detalles individuales** de cada estación
- **Información de ubicación** con coordenadas GPS
- **Integración con mapas** para visualización geográfica

### 🗺️ Módulo de Rutas
- **Planificador de rutas** con selección de origen y destino
- **Cálculo automático** de tiempo estimado de viaje
- **Pasos detallados** del recorrido con estaciones intermedias
- **Soporte para transferencias** entre líneas
- **Ubicación actual** del usuario para facilitar la planificación
- **Visualización en mapa** con rutas marcadas

### 📊 Módulo de Datos Externos
- **Consumo de APIs** mediante Retrofit
- **Datos actualizados** de horarios y alertas
- **Notificaciones push** para alertas en tiempo real
- **Información de mantenimiento** y avisos del sistema

### ⚙️ Módulo de Configuración
- **Tema claro/oscuro** con Material 3
- **Cambio de idioma** (Español/Inglés) con persistencia
- **Información de la app** y créditos del proyecto
- **Configuración de preferencias** del usuario

### 🗺️ Módulo de Mapas
- **Mapa completo** del sistema de Metro de Lima
- **Integración con Google Maps** para visualización detallada
- **Marcadores de estaciones** con información contextual
- **Filtros por líneas** para mejor visualización
- **Vista ampliada** del mapa del metro

### 💬 Módulo de Chat IA
- **Asistente virtual** para consultas sobre el metro
- **Respuestas inteligentes** sobre horarios, rutas y estaciones
- **Interfaz de chat** moderna y fluida

### 📺 Módulo En Vivo
- **Estado del servicio** en tiempo real
- **Horarios de operación** por día de la semana
- **Alertas y notificaciones** del sistema
- **Información de mantenimiento** programado

## 🛠️ Tecnologías Implementadas

### Arquitectura y Patrones
- **MVVM (Model-View-ViewModel)** con Jetpack Compose
- **Navigation Compose** para navegación entre pantallas
- **Repository Pattern** para manejo de datos
- **Dependency Injection** con KSP

### Base de Datos
- **Room Database** para almacenamiento local
- **Entidades**: `Station`, `FavoriteStation`
- **DAOs** para operaciones CRUD
- **Migraciones** de base de datos

### Red y APIs
- **Retrofit** para consumo de APIs REST
- **Gson** para serialización JSON
- **OkHttp** con logging interceptor
- **Coroutines** para operaciones asíncronas

### UI/UX
- **Jetpack Compose** con Material 3
- **Temas personalizados** con modo claro/oscuro
- **Animaciones fluidas** y transiciones
- **Diseño responsivo** para diferentes tamaños de pantalla
- **Efectos visuales** como Liquid Glass

### Mapas y Ubicación
- **Google Maps** con Compose
- **Play Services Location** para GPS
- **Marcadores personalizados** para estaciones
- **Rutas visuales** en el mapa

### Internacionalización
- **Soporte multiidioma** (Español/Inglés)
- **LocalizationManager** personalizado
- **Persistencia de idioma** seleccionado
- **Recursos localizados** completos

## 📁 Estructura del Proyecto

```
app/src/main/java/com/tecsup/metrolimago1/
├── components/           # Componentes reutilizables
├── data/                # Capa de datos
│   ├── local/           # Base de datos local (Room)
│   └── service/         # Servicios de red
├── domain/              # Lógica de negocio
│   ├── models/          # Modelos de dominio
│   └── services/        # Servicios de dominio
├── navigation/          # Navegación de la app
├── ui/                  # Interfaz de usuario
│   ├── screens/         # Pantallas principales
│   └── theme/           # Temas y estilos
└── utils/               # Utilidades y helpers
```

## 📸 Capturas de Pantalla

## 🚀 Instalación y Configuración

### Requisitos
- **Android Studio** Arctic Fox o superior
- **JDK 11** o superior
- **Android SDK** API 24+
- **Google Maps API Key**

### Pasos de Instalación

1. **Clonar el repositorio**
```bash
git clone https://github.com/Jefferson-BA/MetroLimaGO1.2.git
cd MetroLimaGO1.2
```

2. **Configurar API Key de Google Maps**
   - Obtener una API Key de Google Maps
   - Agregar la clave en `app/build.gradle.kts`:
   ```kotlin
   buildConfigField("String", "GOOGLE_MAPS_API_KEY", "\"TU_API_KEY_AQUI\"")
   ```

3. **Sincronizar dependencias**
```bash
./gradlew build
```

4. **Ejecutar la aplicación**
   - Conectar dispositivo Android o usar emulador
   - Ejecutar desde Android Studio o usar:
   ```bash
   ./gradlew installDebug
   ```

## 📱 Pantallas Implementadas

### 1. Splash Screen
- Pantalla de carga inicial con logo del metro
- Animación de fade-in
- Redirección automática a la pantalla de introducción

### 2. Introducción (Glass Intro)
- Pantalla de bienvenida con efecto Liquid Glass
- Diseño moderno y atractivo
- Botón de continuar hacia la pantalla principal

### 3. Home Screen
- Pantalla principal con acceso a todas las funcionalidades
- Barra de búsqueda inteligente
- Sección de próximas llegadas
- Notificaciones y alertas
- Chat con IA
- Pruebas de rutas

### 4. Lista de Estaciones
- Lista completa de estaciones del metro
- Búsqueda por nombre
- Filtros por línea (Línea 1, Línea 2)
- Navegación a detalles de estación

### 5. Detalle de Estación
- Información completa de cada estación
- Ubicación en mapa
- Horarios de operación
- Conexiones disponibles

### 6. Planificador de Rutas
- Selección de estación origen y destino
- Cálculo automático de tiempo estimado
- Pasos detallados del recorrido
- Visualización en mapa

### 7. Mapa del Metro
- Vista completa del sistema de metro
- Marcadores de todas las estaciones
- Filtros por líneas
- Integración con Google Maps

### 8. Configuración
- Cambio de tema (claro/oscuro)
- Selección de idioma (Español/Inglés)
- Información de la aplicación
- Créditos del proyecto

### 9. Chat IA
- Interfaz de chat con asistente virtual
- Respuestas sobre información del metro
- Diseño moderno y fluido

### 10. En Vivo
- Estado actual del servicio
- Horarios de operación
- Alertas y notificaciones
- Información de mantenimiento

## 🎨 Diseño y UX

### Temas
- **Material 3** con colores personalizados del Metro de Lima
- **Modo claro y oscuro** con transiciones suaves
- **Paleta de colores** basada en la identidad visual del metro

### Componentes Reutilizables
- **GlobalBottomNavBar**: Navegación inferior global
- **SearchBar**: Barra de búsqueda personalizada
- **MenuCard**: Tarjetas de menú interactivas
- **LiquidGlassCard**: Tarjetas con efecto glass

### Animaciones
- **Transiciones fluidas** entre pantallas
- **Animaciones de carga** y estados
- **Efectos visuales** como Liquid Glass
- **Microinteracciones** para mejor UX

## 📊 Datos y Estaciones

### Estaciones Implementadas
- **Línea 1**: 10 estaciones desde Villa El Salvador hasta San Juan de Lurigancho
- **Línea 2**: 4 estaciones desde Ate Vitarte hasta Callao
- **Coordenadas GPS** reales para cada estación
- **Información detallada** de ubicación y servicios

### Algoritmo de Rutas
- **Cálculo de tiempo** basado en estaciones intermedias
- **Soporte para transferencias** entre líneas
- **Optimización de rutas** para menor tiempo de viaje
- **Pasos detallados** del recorrido

## 🌐 Internacionalización

### Idiomas Soportados
- **Español** (idioma por defecto)
- **Inglés** (traducción completa)

### Características
- **Cambio dinámico** de idioma sin reiniciar la app
- **Persistencia** del idioma seleccionado
- **Recursos localizados** para todas las pantallas
- **Soporte completo** para RTL (futuro)

## 🔧 Configuración Técnica

### Dependencias Principales
```kotlin
// Compose
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")

// Navigation
implementation("androidx.navigation:navigation-compose")

// Room
implementation("androidx.room:room-runtime")
implementation("androidx.room:room-ktx")

// Retrofit
implementation("com.squareup.retrofit2:retrofit")
implementation("com.squareup.retrofit2:converter-gson")

// Google Maps
implementation("com.google.maps.android:maps-compose")
implementation("com.google.android.gms:play-services-maps")
```

### Configuración de Build
- **Target SDK**: 34
- **Min SDK**: 24
- **Compile SDK**: 34
- **Java Version**: 11

## 🤝 Contribuciones del Equipo

### **Jefferson-BA** - Líder Técnico
- **Arquitectura**: Diseño de la estructura MVVM y patrones de desarrollo
- **Navegación**: Implementación completa del sistema de navegación con Compose
- **APIs**: Integración de Google Maps y servicios externos
- **UI Principal**: Desarrollo de HomeScreen y componentes base
- **Coordinación**: Gestión de ramas y merges del proyecto

### **jeff** - Especialista en Rutas
- **Geolocalización**: Implementación de ubicación actual del usuario
- **Algoritmos**: Desarrollo del sistema de cálculo de rutas
- **Mapas**: Integración avanzada con Google Maps para visualización
- **Filtros**: Sistema de filtrado por líneas del metro
- **Internacionalización**: Implementación inicial del cambio de idiomas

### **ccaihuari** - Diseñador UI/UX
- **Pantallas**: Desarrollo de ConfiguracionScreen y VivoScreen
- **Componentes**: Creación de elementos reutilizables
- **Splash**: Implementación de pantalla de carga
- **Navegación**: Mejoras en el navbar global
- **Búsqueda**: Desarrollo de barras de búsqueda personalizadas

### **Jhnn** - Especialista en Datos
- **Base de Datos**: Implementación completa de Room Database
- **Modelos**: Creación de entidades y DAOs
- **Datos Mock**: Desarrollo de datos de estaciones del metro
- **Servicios**: Configuración de Retrofit y APIs
- **Persistencia**: Sistema de guardado de preferencias
- **Traducción**: Sistema completo de internacionalización

### 🎯 Commits Destacados por Desarrollador

#### **Jefferson-BA**
- `feat: crear pantallas placeholder con navegación básica`
- `feat: integrar navegación en MainActivity`
- `feat: crear HomeScreen con diseño base`
- `implementacion de busqueda`
- `implementacion de barras de busqueda`
- `implementacion del chat box sin api`
- `Implementar sistema de notificaciones push para alertas en tiempo real`
- `Implementar busqueda avanzada con autocompletado y filtrado funcional`

#### **jeff**
- `implementacion de mi ubicacion(actual) al apartado planificaion de rutas`
- `implementacion de mapa en estaciones`
- `implementacion de filtros en apartado de rutas`
- `integracion de cambio de idiomas(Es-In)`
- `implementacion de mapa con rutas`
- `implementacion de marcado de rutas`
- `feat: Agregar estaciones Línea 1 y 2 del Metro de Lima`
- `feat: Crear data class PasoRuta`
- `implementacion de la plantilla "configuracion"`
- `implementacion del navbar global que se mostrará en todas las pantallas`
- `implementacion de plantilla "en vivo"`
- `implementacion de splash (carga de pantalla)`
- `implementacion de barras de busqueda`
- `implementacion del navbar mejorada`

#### **Jhnn**
-`implementacion de colores`
-`themas oscuro y claro`
- `primer modelo de inicio con entidad Estacion, DAO y MetroDatabase`
- `implementacion de metodos de busqueda`
- `traductor funcional`
- `diseño completado` (mejoras finales de UI)
- `fondo y navbar update` (actualizaciones visuales)
-`ayuda con los Apis`
-`testeo`

## 📈 Cronología de Desarrollo

### Día 1 - Análisis y Setup
- ✅ Configuración del proyecto en Android Studio
- ✅ Dependencias básicas (Compose, Room, Retrofit, Coroutines)
- ✅ Diseño de logo e íconos
- ✅ Pantalla inicial funcional

### Día 2 - Diseño de UI
- ✅ HomeScreen con diseño moderno
- ✅ ListaEstacionesScreen con filtros
- ✅ DetalleEstacionScreen con información completa
- ✅ Navegación entre pantallas implementada

### Día 3 - Base de Datos Local
- ✅ Entidad Station, DAO y MetroDatabase
- ✅ Datos mock de estaciones insertados
- ✅ CRUD básico funcionando con Room
- ✅ Filtros por nombre y línea implementados

### Día 4 - Consumo de API
- ✅ Integración con Retrofit configurada
- ✅ Modelos de red y Repository pattern
- ✅ Servicios de IA para chat implementados
- ✅ Sistema de notificaciones push

### Día 5 - Planificador de Rutas
- ✅ Pantalla de selección origen/destino
- ✅ Cálculo de tiempo estimado simulado
- ✅ Algoritmo de pasos del recorrido
- ✅ Integración con mapas para visualización

### Día 6 - Ajustes Visuales
- ✅ Material 3 con temas personalizados
- ✅ Modo oscuro/claro implementado
- ✅ Navegación inferior global
- ✅ Animaciones y efectos visuales (Liquid Glass)

### Día 7 - Pruebas y Despliegue
- ✅ Pruebas funcionales completadas
- ✅ Sistema de internacionalización (Español/Inglés)
- ✅ Documentación completa
- ✅ Versión final lista para entrega

## 👥 Equipo de Desarrollo

**Docente**: Juan León S.

### 🧑‍💻 Desarrolladores

#### **Jefferson-BA** (Jefferson)
- **Rol**: Desarrollador Principal y Coordinador del Proyecto
- **Contribuciones**:
  - Arquitectura general de la aplicación
  - Implementación del sistema de navegación
  - Desarrollo de la pantalla principal (HomeScreen)
  - Sistema de búsqueda avanzada con autocompletado
  - Integración de Google Maps
  - Sistema de notificaciones push
  - Chat con IA
  - Configuración de dependencias y build.gradle

#### **jeff** (Jeff)
- **Rol**: Desarrollador de Funcionalidades de Rutas y Mapas
- **Contribuciones**:
  - Implementación de ubicación actual en planificación de rutas
  - Integración de mapas en estaciones
  - Sistema de filtros para rutas por líneas
  - Mejoras en el planificador de rutas
  - Implementación de mapa agrandado
  - Integración de cambio de idiomas (Español/Inglés)
  - Marcado de rutas en mapas
  - Algoritmo de cálculo de rutas

#### **ccaihuari** (Carlos)
- **Rol**: Desarrollador de UI/UX y Pantallas
- **Contribuciones**:
  - Implementación de pantallas de configuración
  - Desarrollo de plantilla "En Vivo"
  - Implementación de splash screen (pantalla de carga)
  - Mejoras en el navbar global
  - Implementación de barras de búsqueda
  - Diseño de componentes reutilizables
  - Mejoras en la interfaz de usuario

#### **Jhnn** (Jhon)
- **Rol**: Desarrollador de Base de Datos y Servicios
- **Contribuciones**:
  - Implementación de Room Database
  - Creación de entidades y DAOs
  - Sistema de datos mock para estaciones
  - Implementación de servicios de red
  - Configuración de Retrofit
  - Sistema de persistencia de datos
  - Integración de APIs externas
  - Sistema de internacionalización completo

## 📄 Licencia

Este proyecto está desarrollado como parte del curso de desarrollo móvil en Tecsup.

## 🎓 Información Académica

- **Institución**: Tecsup
- **Curso**: Desarrollo Móvil
- **Docente**: Juan León S.
- **Período**: 2024
- **Tecnología**: Android con Jetpack Compose

## 🔮 Futuras Mejoras

- [ ] Integración con APIs reales del Metro de Lima
- [ ] Notificaciones push en tiempo real
- [ ] Sistema de favoritos para estaciones
- [ ] Historial de rutas consultadas
- [ ] Modo offline con datos sincronizados
- [ ] Widgets para pantalla de inicio
- [ ] Integración con sistemas de pago
- [ ] Realidad aumentada para navegación

---

**MetroLima GO** - Tu compañero de viaje en el Metro de Lima 🚇✨