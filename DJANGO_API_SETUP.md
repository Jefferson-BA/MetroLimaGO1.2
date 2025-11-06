# 🐍 Guía para Crear la API Django

## 📁 Estructura Recomendada

```
tu-proyecto/
├── MetroLimaGO_Nuevo/          (tu proyecto Android actual)
│   └── app/
│
└── metrolima_api/               (NUEVO - Proyecto Django)
    ├── manage.py
    ├── metrolima_api/
    │   ├── __init__.py
    │   ├── settings.py
    │   ├── urls.py
    │   ├── wsgi.py
    │   └── asgi.py
    ├── stations/                (app Django para estaciones)
    │   ├── __init__.py
    │   ├── admin.py
    │   ├── apps.py
    │   ├── models.py
    │   ├── serializers.py
    │   ├── views.py
    │   ├── urls.py
    │   └── migrations/
    ├── requirements.txt
    └── .env                      (configuración secreta)
```

## 🚀 Pasos para Crear el Proyecto Django

### 1. Crear el Proyecto Django

```bash
# Crear carpeta para el proyecto Django (fuera de MetroLimaGO_Nuevo)
mkdir metrolima_api
cd metrolima_api

# Crear entorno virtual
python -m venv venv

# Activar entorno virtual
# Windows:
venv\Scripts\activate
# Linux/Mac:
source venv/bin/activate

# Instalar Django y dependencias
pip install django djangorestframework django-cors-headers pillow

# Crear proyecto Django
django-admin startproject metrolima_api .

# Crear app para estaciones
python manage.py startapp stations
```

### 2. Configurar settings.py

```python
# metrolima_api/settings.py

INSTALLED_APPS = [
    'django.contrib.admin',
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.sessions',
    'django.contrib.messages',
    'django.contrib.staticfiles',
    
    # Third party apps
    'rest_framework',
    'corsheaders',  # Para permitir CORS desde Android
    
    # Local apps
    'stations',
]

MIDDLEWARE = [
    'django.middleware.security.SecurityMiddleware',
    'django.contrib.sessions.middleware.SessionMiddleware',
    'corsheaders.middleware.CorsMiddleware',  # Agregar aquí
    'django.middleware.common.CommonMiddleware',
    'django.middleware.csrf.CsrfViewMiddleware',
    'django.contrib.auth.middleware.AuthenticationMiddleware',
    'django.contrib.messages.middleware.MessageMiddleware',
    'django.middleware.clickjacking.XFrameOptionsMiddleware',
]

# Configuración CORS para permitir acceso desde Android
CORS_ALLOWED_ORIGINS = [
    "http://localhost:8000",
    "http://127.0.0.1:8000",
    # Agregar tu IP local para desarrollo: "http://192.168.1.XXX:8000"
]

CORS_ALLOW_ALL_ORIGINS = True  # Solo para desarrollo, cambiar en producción

# Configuración de archivos multimedia (para imágenes)
MEDIA_URL = '/media/'
MEDIA_ROOT = BASE_DIR / 'media'
```

### 3. Crear el Modelo Station

```python
# stations/models.py

from django.db import models

class Station(models.Model):
    STATUS_CHOICES = [
        ('OPERATIONAL', 'Operativa'),
        ('MAINTENANCE', 'Mantenimiento'),
        ('CONSTRUCTION', 'Construcción'),
        ('CLOSED', 'Cerrada'),
    ]
    
    id = models.CharField(primary_key=True, max_length=50)  # "LIM-01"
    name = models.CharField(max_length=200)
    line = models.CharField(max_length=50)  # "Línea 1", "Línea 2", etc.
    address = models.TextField()
    latitude = models.DecimalField(max_digits=9, decimal_places=6)
    longitude = models.DecimalField(max_digits=9, decimal_places=6)
    description = models.TextField(blank=True)  # Descripción detallada
    opening_time = models.CharField(max_length=5, default="05:00")
    closing_time = models.CharField(max_length=5, default="23:00")
    status = models.CharField(
        max_length=20, 
        choices=STATUS_CHOICES,
        default='OPERATIONAL'
    )
    image_url = models.URLField(blank=True, null=True)  # URL de imagen externa
    image = models.ImageField(upload_to='stations/', blank=True, null=True)  # Imagen local
    updated_at = models.DateTimeField(auto_now=True)
    created_at = models.DateTimeField(auto_now_add=True)
    
    class Meta:
        ordering = ['line', 'name']
        verbose_name = 'Estación'
        verbose_name_plural = 'Estaciones'
    
    def __str__(self):
        return f"{self.name} ({self.line})"
```

### 4. Crear Serializer

```python
# stations/serializers.py

from rest_framework import serializers
from .models import Station

class StationSerializer(serializers.ModelSerializer):
    image_url = serializers.SerializerMethodField()
    
    class Meta:
        model = Station
        fields = [
            'id', 'name', 'line', 'address', 'latitude', 'longitude',
            'description', 'opening_time', 'closing_time', 'status',
            'image_url', 'updated_at'
        ]
        read_only_fields = ['updated_at', 'created_at']
    
    def get_image_url(self, obj):
        # Si tiene imagen local, devolver URL completa
        if obj.image:
            request = self.context.get('request')
            if request:
                return request.build_absolute_uri(obj.image.url)
        # Si no, usar image_url directo
        return obj.image_url or ''
    
    def to_representation(self, instance):
        # Convertir Decimal a String para la API
        representation = super().to_representation(instance)
        representation['latitude'] = str(instance.latitude)
        representation['longitude'] = str(instance.longitude)
        return representation
```

### 5. Crear ViewSet

```python
# stations/views.py

from rest_framework import viewsets
from rest_framework.decorators import action
from rest_framework.response import Response
from .models import Station
from .serializers import StationSerializer

class StationViewSet(viewsets.ReadOnlyModelViewSet):
    """
    ViewSet para estaciones.
    ReadOnly porque Android solo necesita leer datos (por ahora).
    """
    queryset = Station.objects.all()
    serializer_class = StationSerializer
    
    def get_serializer_context(self):
        # Pasar request al serializer para generar URLs completas
        return {'request': self.request}
    
    @action(detail=False, methods=['get'], url_path='by_line')
    def by_line(self, request):
        """
        GET /api/stations/by_line/?line=Línea 1
        Obtiene estaciones filtradas por línea
        """
        line = request.query_params.get('line', None)
        if line:
            stations = Station.objects.filter(line=line)
            serializer = self.get_serializer(stations, many=True)
            return Response(serializer.data)
        return Response([])
```

### 6. Configurar URLs

```python
# stations/urls.py

from django.urls import path, include
from rest_framework.routers import DefaultRouter
from .views import StationViewSet

router = DefaultRouter()
router.register(r'stations', StationViewSet, basename='station')

urlpatterns = [
    path('', include(router.urls)),
]
```

```python
# metrolima_api/urls.py

from django.contrib import admin
from django.urls import path, include
from django.conf import settings
from django.conf.urls.static import static

urlpatterns = [
    path('admin/', admin.site.urls),
    path('api/', include('stations.urls')),
]

# Para servir archivos de media en desarrollo
if settings.DEBUG:
    urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)
```

### 7. Configurar Admin (opcional pero útil)

```python
# stations/admin.py

from django.contrib import admin
from .models import Station

@admin.register(Station)
class StationAdmin(admin.ModelAdmin):
    list_display = ['id', 'name', 'line', 'status']
    list_filter = ['line', 'status']
    search_fields = ['name', 'address', 'description']
```

### 8. Migraciones y Crear Superusuario

```bash
# Crear migraciones
python manage.py makemigrations

# Aplicar migraciones
python manage.py migrate

# Crear superusuario para acceder al admin
python manage.py createsuperuser
```

### 9. Ejecutar el Servidor

```bash
# Ejecutar servidor de desarrollo
python manage.py runserver 0.0.0.0:8000

# Ahora puedes acceder:
# - API: http://localhost:8000/api/stations/
# - Admin: http://localhost:8000/admin/
```

### 10. Configurar la URL en Android

En Android, necesitas configurar la URL base de tu API Django:

**Opción 1: En `ConfigManager.kt`**
```kotlin
fun getMetroLimaBaseUrl(): String {
    return properties?.getProperty("METRO_LIMA_BASE_URL", "http://192.168.1.XXX:8000/api/") 
        ?: "http://192.168.1.XXX:8000/api/"
}
```

**Opción 2: Cambiar directamente en `StationApiService.kt`**
```kotlin
private const val DEFAULT_BASE_URL = "http://TU_IP_LOCAL:8000/api/"
```

Para encontrar tu IP local:
- Windows: `ipconfig` en CMD, buscar "IPv4"
- Linux/Mac: `ifconfig` o `ip addr`

## 📝 Ejemplo de Uso

### Agregar una Estación desde el Admin Django

1. Accede a `http://localhost:8000/admin/`
2. Inicia sesión con tu superusuario
3. Ve a "Stations" > "Add Station"
4. Completa los campos:
   - ID: `LIM-01`
   - Name: `Villa El Salvador`
   - Line: `Línea 1`
   - Address: `Av. Villa El Salvador`
   - Latitude: `-12.1939`
   - Longitude: `-76.9399`
   - Description: `Terminal sur de la Línea 1`
   - Status: `Operativa`
   - Image: Sube una imagen o usa `image_url`

### Consumir desde Android

La app Android ahora:
1. Intenta sincronizar desde la API al iniciar
2. Si no hay conexión, usa datos de Room (offline)
3. Muestra imágenes desde URLs o archivos del servidor

## 🔧 Próximos Pasos (Opcional)

- Agregar autenticación (si necesitas datos privados)
- Agregar paginación para muchas estaciones
- Agregar filtros avanzados
- Agregar servicios cercanos como modelo separado
- Agregar sistema de versionado de API

## 📚 Recursos

- [Django REST Framework Docs](https://www.django-rest-framework.org/)
- [Django CORS Headers](https://pypi.org/project/django-cors-headers/)
- [Django Admin Docs](https://docs.djangoproject.com/en/stable/ref/contrib/admin/)

