# 🎯 Estado de Features Extras

## 📍 Estado Actual

### ✅ Implementado

#### 1. **Notificaciones Push** - PARCIAL
- ✅ `NotificationService.kt` existe con funciones para alertas
- ✅ Canal de notificaciones configurado
- ✅ Permisos configurados en AndroidManifest.xml
- ❌ No se integra aún en la UI
- ❌ No se muestran notificaciones de forma automática

**Funciones disponibles:**
- `showServiceAlert()` - Alertas generales del servicio
- `showMaintenanceAlert()` - Alertas de mantenimiento
- `showDelayAlert()` - Alertas de retrasos
- `showCustomAlert()` - Notificaciones personalizadas

**Falta:**
- Integración en las pantallas
- Notificaciones automáticas de retrasos
- Suscripciones a líneas favoritas

---

### ❌ Pendiente de Implementar

#### 2. **Notificaciones de Retrasos**
- ❌ Sistema de detección de retrasos
- ❌ Notificaciones automáticas
- ❌ API para consultar estado del servicio en tiempo real

#### 3. **Widgets**
- ❌ Widget para home screen
- ❌ Mostrar próximas llegadas
- ❌ Estado del servicio en tiempo real
- ❌ Rutas favoritas

#### 4. **Modo de Accesibilidad**
- ❌ Textos más grandes
- ❌ Soporte TalkBack
- ❌ Alto contraste
- ❌ Navegación simplificada
- ❌ Gestos amplificados

#### 5. **Comentarios/Valoraciones de Estaciones**
- ❌ Sistema de reseñas
- ❌ Calificación de estaciones
- ❌ Comentarios de usuarios
- ❌ Base de datos para reviews

---

## 🛠️ Plan de Implementación

### Prioridad ALTA

#### 1. Completar Notificaciones Push

**Pasos:**
1. Integrar `NotificationService` en `VivoScreen`
2. Mostrar notificaciones cuando haya alertas
3. Agregar botón "Suscribirse a alertas" en detalle de líneas
4. Guardar preferencias de notificaciones en SharedPreferences

**Archivos a modificar:**
- `services/NotificationService.kt` - Mejorar funciones
- `ui/screens/vivo/VivoScreen.kt` - Integrar notificaciones
- `ui/screens/lines/LineDetailScreen.kt` - Botón de suscripción

---

### Prioridad MEDIA

#### 2. Sistema de Valoraciones

**Nuevos archivos:**
```
app/src/main/java/com/tecsup/metrolimago1/
├── data/
│   ├── local/
│   │   ├── Review.kt              # Entity para Room
│   │   └── dao/
│   │       └── ReviewDao.kt       # DAO para reviews
├── ui/
│   └── screens/
│       └── estaciones/
│           └── StationRatingScreen.kt  # Pantalla de valoraciones
└── domain/
    └── models/
        └── StationRating.kt       # Modelo de datos
```

**Funcionalidades:**
- CRUD de reviews
- Calificación con estrellas (1-5)
- Comentarios de texto
- Mostrar promedio de calificación en detalle de estación
- Lista de reviews recientes

---

#### 3. Modo de Accesibilidad

**Nuevos archivos:**
```
app/src/main/java/com/tecsup/metrolimago1/
├── ui/
│   ├── theme/
│   │   ├── AccessibilityTheme.kt     # Tema de accesibilidad
│   │   └── AccessibilitySettings.kt # Configuración
└── utils/
    └── AccessibilityUtils.kt         # Utilidades
```

**Funcionalidades:**
- Tamaños de texto configurables (Small, Medium, Large, Extra Large)
- Soporte TalkBack para lectores de pantalla
- Modo de alto contraste
- Botones más grandes
- Gestos simplificados

**Agregar a Configuración:**
- Sección "Accesibilidad"
- Slider para tamaño de texto
- Toggle para modo alto contraste
- Toggle para TalkBack

---

### Prioridad BAJA

#### 4. Widgets

**Nuevos archivos:**
```
app/src/main/java/com/tecsup/metrolimago1/
└── widget/
    ├── MetroWidget.kt               # Widget básico
    └── MetroWidgetProvider.kt      # Provider
```

**Funcionalidades:**
- Widget de estado del servicio
- Próximas llegadas
- Rutas favoritas
- Acceso rápido a búsqueda

---

## 📋 Checklist de Implementación

### Notificaciones Push
- [ ] Mejorar `NotificationService.kt` con más opciones
- [ ] Integrar notificaciones en `VivoScreen`
- [ ] Agregar botón de suscripción en `LineDetailScreen`
- [ ] Guardar preferencias de notificaciones
- [ ] Mostrar notificaciones automáticas de retrasos
- [ ] Agregar badge en icono de app

### Valoraciones
- [ ] Crear modelo `StationRating`
- [ ] Crear Entity `Review.kt`
- [ ] Crear `ReviewDao.kt`
- [ ] Actualizar `AppDatabase.kt`
- [ ] Crear pantalla `StationRatingScreen.kt`
- [ ] Integrar en `EstacionDetailScreen`
- [ ] Mostrar promedio de calificación

### Accesibilidad
- [ ] Crear `AccessibilityTheme.kt`
- [ ] Crear `AccessibilitySettings.kt`
- [ ] Agregar slider de tamaño de texto
- [ ] Agregar modo alto contraste
- [ ] Configurar TalkBack
- [ ] Agregar a pantalla de configuración

### Widgets
- [ ] Crear `MetroWidget.kt`
- [ ] Crear `MetroWidgetProvider.kt`
- [ ] Agregar layout del widget
- [ ] Configurar actualización automática
- [ ] Probar en diferentes tamaños

---

## 🎯 Orden Recomendado

1. **Completar Notificaciones Push** (2-3 horas)
   - Más fácil de implementar
   - Ya existe base del código
   - Alto impacto para usuarios

2. **Sistema de Valoraciones** (4-5 horas)
   - Agrega interacción social
   - Útil para mejorar experiencia

3. **Modo de Accesibilidad** (5-6 horas)
   - Mejora accesibilidad para todos
   - Requiere testing con TalkBack

4. **Widgets** (6-8 horas)
   - Más complejo
   - Requiere actualizaciones en background
   - Testing en diferentes dispositivos

---

## 📝 Notas

- **Notificaciones**: Ya existe la infraestructura, solo falta integrar
- **Valoraciones**: Requiere nueva tabla en Room
- **Accesibilidad**: Requiere cambios en theme y composables
- **Widgets**: Requiere configuración especial de Android

---

¿Con cuál feature quieres empezar? 🚀

