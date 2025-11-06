# Configuración de Firebase AI Logic con Gemini

Esta guía te ayudará a configurar Firebase AI Logic con Gemini API para el chatbot de MetroLima GO.

## Pasos de Configuración

### 1. Crear/Configurar Proyecto en Firebase

1. Ve a la [Consola de Firebase](https://console.firebase.google.com/)
2. Crea un nuevo proyecto o selecciona uno existente
3. Si es un proyecto nuevo, sigue el asistente de configuración

### 2. Configurar Firebase AI Logic

1. En la consola de Firebase, navega a **Firebase AI Logic** (o busca "AI Logic" en el menú)
2. Haz clic en **Comenzar** para iniciar el flujo de configuración
3. Selecciona el proveedor de API:
   - **Gemini Developer API** (Recomendado para empezar)
     - Facturación opcional
     - Disponible en el plan Spark (gratis)
     - Puedes actualizar a facturación más adelante
   - **Vertex AI Gemini API** (Requiere facturación)
     - Requiere plan Blaze (pago por uso)

### 3. Obtener google-services.json

1. En la consola de Firebase, ve a **Configuración del proyecto** (ícono de engranaje)
2. En la sección "Tus apps", haz clic en el ícono de Android
3. Ingresa el nombre del paquete de tu app: `com.tecsup.metrolimago1`
4. Descarga el archivo `google-services.json`
5. Coloca el archivo en: `app/google-services.json`

**Nota:** El archivo `google-services.json` es necesario para que Firebase funcione correctamente. Sin este archivo, la app no podrá conectarse a Firebase.

### 4. Verificar Configuración

Una vez que hayas agregado el archivo `google-services.json`, la app debería poder:
- Conectarse a Firebase
- Usar Gemini API a través de Firebase AI Logic
- Generar respuestas en el chatbot

## Estructura del Código

### Servicio de Gemini

El servicio `GeminiService` está ubicado en:
```
app/src/main/java/com/tecsup/metrolimago1/data/service/GeminiService.kt
```

Este servicio:
- Usa Firebase AI Logic SDK
- Se conecta a Gemini Developer API
- Maneja errores y reintentos
- Soporta historial de conversación

### Integración en ChatScreen

El `ChatScreen` ha sido actualizado para usar `GeminiService` en lugar de `AIService`. El chatbot ahora:
- Usa Gemini 2.5 Flash como modelo
- Mantiene contexto de conversación
- Maneja errores de manera elegante

## Modelos Disponibles

Puedes cambiar el modelo en `GeminiService.kt`:

```kotlin
private val model = Firebase.ai(backend = GenerativeBackend.googleAI())
    .generativeModel("gemini-2.5-flash")
```

Modelos disponibles:
- `gemini-2.5-flash` - Rápido y eficiente (recomendado)
- `gemini-2.5-pro` - Más potente pero más lento
- `gemini-1.5-flash` - Versión anterior, más económica
- `gemini-1.5-pro` - Versión anterior, más potente

## Solución de Problemas

### Error: "API key not found"
- Verifica que hayas configurado Firebase AI Logic en la consola
- Asegúrate de que el archivo `google-services.json` esté en la ubicación correcta
- Verifica que hayas seleccionado Gemini Developer API en la configuración

### Error: "Network error"
- Verifica tu conexión a internet
- Asegúrate de que la app tenga permisos de internet en el AndroidManifest.xml

### Error: "Quota exceeded"
- Verifica tu plan de Firebase
- Revisa los límites de uso en la consola de Firebase
- Considera actualizar a un plan con más cuota

## Próximos Pasos

1. **Agregar google-services.json** - Descarga y coloca el archivo en `app/google-services.json`
2. **Probar el chatbot** - Ejecuta la app y prueba el chatbot
3. **Personalizar el prompt** - Ajusta el prompt del sistema en `GeminiService.kt` según tus necesidades
4. **Configurar App Check** (Opcional) - Para mayor seguridad en producción

## Recursos

- [Documentación de Firebase AI Logic](https://firebase.google.com/docs/ai-logic)
- [Documentación de Gemini API](https://ai.google.dev/docs)
- [Consola de Firebase](https://console.firebase.google.com/)

