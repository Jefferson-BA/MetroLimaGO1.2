# 🗺️ Google Maps Setup - MetroLima GO

## 📋 Configuración Completa

### 1. **API Key de Google Cloud Console**
1. Ve a [Google Cloud Console](https://console.cloud.google.com/)
2. Crea un nuevo proyecto o selecciona uno existente
3. Habilita las siguientes APIs:
   - **Maps SDK for Android**
   - **Directions API**
   - **Places API**
   - **Geocoding API** (opcional)
4. **Genera UNA SOLA API Key** (sirve para todas las APIs)

### 2. **Configurar Variables de Entorno**

#### Opción A: Archivo .env (Recomendado)
```bash
# Copia el archivo de ejemplo
cp env.example .env

# Edita el archivo .env con tu API key
GOOGLE_MAPS_API_KEY=tu_api_key_aqui
```

#### Opción B: Variables de Sistema
```bash
export GOOGLE_MAPS_API_KEY="tu_api_key_aqui"
```

### 3. **Configurar API Key en AndroidManifest.xml**
El archivo ya está configurado para usar variables de entorno:
```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="${GOOGLE_MAPS_API_KEY}" />
```

### 4. **Dependencias Agregadas**
```kotlin
// Google Maps
implementation("com.google.maps.android:maps-compose:4.3.0")
implementation("com.google.android.gms:play-services-maps:18.2.0")
implementation("com.google.android.gms:play-services-location:21.0.1")
```

### 5. **Permisos Configurados**
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

## 🚇 **Datos del Metro de Lima**

### **Estaciones Implementadas:**
- **Línea 1**: Villa El Salvador → San Juan de Lurigancho (10 estaciones)
- **Línea 2**: Ate Vitarte → Callao (10 estaciones)  
- **Línea 3**: Comas → Chorrillos (10 estaciones)

### **Coordenadas Reales:**
- Todas las estaciones tienen coordenadas GPS reales de Lima
- Centro del mapa: Lima (-12.0464, -77.0428)
- Zoom por defecto: 12

## 🛠️ **Próximos Pasos**

### **1. Obtener API Keys:**
```bash
# 1. Ve a Google Cloud Console
# 2. Crea un proyecto
# 3. Habilita las APIs necesarias
# 4. Genera API Keys
# 5. Configura restricciones (Android apps)
```

### **2. Configurar Variables:**
```bash
# Copia el archivo de ejemplo
cp env.example .env

# Edita con tu API key real
nano .env
```

### **3. Probar la Implementación:**
```bash
# Sincroniza el proyecto
./gradlew clean build

# Ejecuta la app
./gradlew installDebug
```

## 🔒 **Seguridad**

### **Archivos Protegidos:**
- `.env` - Variables de entorno
- `app/.env` - Configuración local
- `**/secrets.properties` - Archivos de secretos
- `**/api_keys.properties` - API Keys

### **Gitignore Configurado:**
```gitignore
# Archivos de configuración sensibles
.env
*.env
app/.env
**/secrets.properties
**/api_keys.properties
```

## 📱 **Funcionalidades Implementadas**

### **PlanificadorRutaScreen:**
- ✅ Dropdowns funcionales para selección de estaciones
- ✅ Validación automática del botón "Calcular Ruta"
- ✅ Tema dinámico (oscuro/claro)
- ✅ Navegación integrada

### **MockStations:**
- ✅ 30 estaciones con coordenadas reales
- ✅ 3 líneas del Metro de Lima
- ✅ Funciones helper para obtener datos por línea
- ✅ Coordenadas de Lima para centrar el mapa

### **ConfigManager:**
- ✅ Manejo seguro de API Keys
- ✅ Configuración desde archivos properties
- ✅ Variables de entorno
- ✅ Configuración de coordenadas de Lima

## 🚀 **Listo para Implementar Google Maps**

El proyecto está completamente configurado para:
1. **Mostrar mapas** de Lima con Google Maps
2. **Marcar estaciones** del Metro de Lima
3. **Calcular rutas** entre estaciones
4. **Mostrar información** de estaciones
5. **Navegación** paso a paso

**Siguiente paso:** Implementar el composable GoogleMap en PlanificadorRutaScreen.
