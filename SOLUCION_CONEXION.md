# 🔧 Solución al Problema de Conexión API

## ⚠️ Problema Identificado

El error `ERR_CONNECTION_TIMED_OUT` al acceder a `10.0.2.2:8000` desde el navegador es **NORMAL** porque:

- `10.0.2.2` es una IP especial que **SOLO funciona desde dentro del emulador Android**
- **NO funciona** desde el navegador de tu computadora
- Es el alias que el emulador usa para referirse a `localhost` de tu máquina

## ✅ Soluciones

### 1. Para Probar desde el Navegador (tu computadora)

Usa estas URLs en tu navegador:

```
http://127.0.0.1:8000/api/stations/
http://localhost:8000/api/stations/
http://172.20.10.4:8000/api/stations/
```

### 2. Para que Funcione en el Emulador Android

Asegúrate de que Django esté corriendo con:

```bash
python manage.py runserver 0.0.0.0:8000
```

**IMPORTANTE:** El `0.0.0.0` permite que Django acepte conexiones desde cualquier IP, incluyendo el emulador.

### 3. Verificar Firewall

Si sigue sin funcionar, verifica el firewall de Windows:

1. Abre "Firewall de Windows Defender"
2. Permite Python a través del firewall
3. O temporalmente desactiva el firewall para probar

### 4. Configuración Correcta según tu Caso

#### Si usas EMULADOR Android:
- En `StationService.kt` debe estar: `http://10.0.2.2:8000/api/`
- Django debe correr: `python manage.py runserver 0.0.0.0:8000`
- Para probar desde navegador usa: `http://127.0.0.1:8000/api/stations/`

#### Si usas DISPOSITIVO FÍSICO:
- En `StationService.kt` debe estar: `http://172.20.10.4:8000/api/`
- Django debe correr: `python manage.py runserver 0.0.0.0:8000`
- Dispositivo y computadora deben estar en la misma red WiFi
- Para probar desde navegador usa: `http://172.20.10.4:8000/api/stations/`

## 🧪 Pasos para Verificar

1. **Inicia Django correctamente:**
   ```bash
   python manage.py runserver 0.0.0.0:8000
   ```

2. **Prueba desde el navegador (tu PC):**
   ```
   http://127.0.0.1:8000/api/stations/
   ```
   Debe funcionar y mostrar el JSON.

3. **Recompila la app Android** con la URL correcta

4. **Revisa los logs en Logcat:**
   - Busca `StationService` para ver qué URL está usando
   - Busca errores de conexión

5. **Si sigue fallando:**
   - Verifica que el emulador tenga conexión a internet
   - Verifica el firewall de Windows
   - Prueba con dispositivo físico en lugar de emulador

## 📝 Resumen

- `10.0.2.2` = Solo funciona desde emulador Android
- `127.0.0.1` o `localhost` = Solo funciona desde tu PC
- `172.20.10.4` = Funciona desde cualquier dispositivo en tu red local
- `0.0.0.0:8000` = Django acepta conexiones desde cualquier origen

