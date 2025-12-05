# Conexión Frontend-Backend de Productos

## Descripción General

El módulo de productos está completamente conectado entre el frontend (React + TypeScript) y el backend (Spring Boot + PostgreSQL).

## Arquitectura

### Frontend
- **Ubicación**: `fronted_venta/src/modules/producto/`
- **Servicio API**: `services/producto.service.ts`
- **Tipos**: `types/product.types.ts`
- **Páginas**:
  - `pages/PaginaGestionProductos.tsx` - Administración de combos
  - `pages/PaginaCatalogoVendedor.tsx` - Catálogo para vendedores
- **Componentes**:
  - `ProductCatalogModal.tsx` - Modal para seleccionar productos

### Backend
- **Ubicación**: `backend/src/main/java/com/venta/backend/producto/`
- **Controller**: `controller/ProductoController.java`
- **Servicios**: 
  - `service/ProductoDBService.java` - Lógica de negocio para productos y combos
- **DTOs**:
  - `dto/ProductoDBDTO.java` - DTO para productos y combos (usa Long como ID)
  - `dto/ProductoDisponibleDTO.java` - DTO simplificado para lista de productos
  - `dto/ComboDBRequest.java` - Request para crear combos
- **Entidades**:
  - `entity/Producto.java` - Entidad principal
  - `entity/ComboProducto.java` - Relación muchos a muchos entre combos y productos

## Endpoints Disponibles

### 1. Obtener productos disponibles (Vendedor)
```
GET /api/productos/disponibles
```
**Respuesta**: Lista de productos activos con stock
```json
[
  {
    "id": 1,
    "codigo": "PROD-0001",
    "nombre": "iPhone 15 Pro",
    "tipo": "EQUIPO_MOVIL",
    "precio": 4999.00,
    "imagenUrl": "/images/productos/iphone-15-pro.jpg"
  }
]
```

### 2. Obtener productos individuales (Admin)
```
GET /api/productos/admin/disponibles
```
**Respuesta**: Lista de productos individuales (no combos) para crear combos
```json
[
  {
    "id": 1,
    "codigo": "PROD-0001",
    "nombre": "iPhone 15 Pro",
    "tipo": "EQUIPO_MOVIL",
    "precioBase": 4999.00,
    "precioFinal": 4999.00,
    "informacionAdicional": "256GB, Color Titanio Natural",
    "imagenUrl": "/images/productos/iphone-15-pro.jpg"
  }
]
```

### 3. Crear combo
```
POST /api/productos/combos
```
**Request**:
```json
{
  "nombre": "Pack Móvil Total",
  "productosIds": [1, 7, 8]
}
```
**Respuesta**: Combo creado con descuentos aplicados
```json
{
  "id": 10,
  "codigo": null,
  "nombre": "Pack Móvil Total",
  "tipo": "COMBO",
  "precioBase": 5128.80,
  "precioFinal": 4479.12,
  "descuentoTotal": 649.68,
  "componentes": [
    {
      "id": 1,
      "nombre": "iPhone 15 Pro",
      "tipo": "EQUIPO_MOVIL",
      "precioBase": 4999.00,
      "precioFinal": 4999.00
    },
    {
      "id": 7,
      "nombre": "Plan Postpago Ilimitado Plus",
      "tipo": "SERVICIO_MOVIL",
      "precioBase": 79.90,
      "precioFinal": 79.90
    }
  ]
}
```

**Descuentos aplicados automáticamente**:
- Equipos Móviles: 15% de descuento
- Servicios (Hogar y Móvil): 10% de descuento

### 4. Listar combos
```
GET /api/productos/combos
```
**Respuesta**: Lista de todos los combos creados

### 5. Detalle de combo
```
GET /api/productos/combos/{id}
```
**Respuesta**: Combo con sus productos componentes

## Uso en el Frontend

### ProductCatalogModal (Vendedor)
```typescript
import { ProductoService } from '../modules/producto/services/producto.service';

// Cargar productos disponibles
const productos = await ProductoService.obtenerProductosDisponibles();
```

### PaginaGestionProductos (Admin)
```typescript
import ProductoService from '../services/producto.service';

// Obtener productos para crear combo
const productos = await ProductoService.obtenerProductosDisponiblesAdmin();

// Crear combo
const request = {
  nombre: "Mi Combo",
  productosIds: [1, 2, 3]
};
const combo = await ProductoService.crearCombo(request);

// Listar combos
const combos = await ProductoService.obtenerListaCombos();

// Ver detalle
const detalle = await ProductoService.obtenerDetalleCombo(1);
```

## Base de Datos

### Tabla: productos
```sql
CREATE TABLE productos (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(50) UNIQUE NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    precio_base DECIMAL(10,2) NOT NULL,
    precio_final DECIMAL(10,2) NOT NULL,
    descuento_total DECIMAL(10,2),
    informacion_adicional TEXT,
    imagen_url VARCHAR(500),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    stock INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(100)
);
```

### Tabla: combo_productos
```sql
CREATE TABLE combo_productos (
    id BIGSERIAL PRIMARY KEY,
    combo_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    orden INTEGER,
    precio_individual DECIMAL(10,2),
    descuento_aplicado DECIMAL(10,2),
    created_at TIMESTAMP,
    FOREIGN KEY (combo_id) REFERENCES productos(id),
    FOREIGN KEY (producto_id) REFERENCES productos(id)
);
```

## Datos de Prueba

Ejecutar el script: `backend/src/main/resources/data-productos.sql`

Este script inserta:
- 12 productos individuales (equipos móviles, servicios de hogar, servicios móviles)
- 1 combo de ejemplo

## Configuración

### Frontend
**Archivo**: `fronted_venta/src/config/api.config.ts`
```typescript
export const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';
```

### Backend
**Archivo**: `backend/src/main/resources/application.properties`
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/db_ventas
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```

## Probar la Conexión

1. **Iniciar Backend**:
```bash
cd backend
./gradlew bootRun
```

2. **Iniciar Frontend**:
```bash
cd fronted_venta
pnpm install
pnpm run dev
```

3. **Probar endpoints**:
```bash
# Listar productos disponibles
curl http://localhost:8080/api/productos/disponibles

# Crear combo
curl -X POST http://localhost:8080/api/productos/combos \
  -H "Content-Type: application/json" \
  -d '{"nombre": "Mi Combo", "productosIds": [1, 2, 3]}'
```

## Flujo de Trabajo

### Administrador (Crear Combo)
1. Acceder a página de gestión de productos
2. Ver lista de productos disponibles
3. Seleccionar productos para el combo
4. Asignar nombre al combo
5. Crear combo (descuentos se aplican automáticamente)
6. Ver lista de combos creados
7. Ver detalle de cualquier combo

### Vendedor (Vender Productos)
1. Acceder al catálogo de productos
2. Ver todos los productos y combos disponibles
3. Filtrar por tipo (Equipo, Servicio, Combo)
4. Filtrar por precio
5. Buscar por nombre o código
6. Agregar productos a la venta

## Tipos en TypeScript

```typescript
export type TipoProducto = 'EQUIPO_MOVIL' | 'SERVICIO_HOGAR' | 'SERVICIO_MOVIL' | 'COMBO';

export interface ProductoDTO {
  id: number;
  codigo?: string;
  nombre: string;
  tipo: TipoProducto;
  precioBase: number;
  precioFinal: number;
  informacionAdicional: string;
  descuentoTotal?: number;
  componentes?: ProductoDTO[];
  imagenUrl?: string;
}

export interface ComboRequest {
  nombre: string;
  productosIds: number[];
}
```

## Estado Actual

✅ Frontend conectado al backend
✅ Servicio API creado y funcionando
✅ DTOs actualizados para usar Long (number)
✅ ProductCatalogModal consume datos reales
✅ PaginaGestionProductos consume datos reales
✅ Backend actualizado para usar base de datos
✅ Endpoints REST implementados y documentados
✅ Lógica de descuentos implementada
✅ Gestión de combos completa

## Próximos Pasos

- [ ] Agregar autenticación y autorización
- [ ] Implementar imágenes reales de productos
- [ ] Agregar paginación en listados grandes
- [ ] Implementar búsqueda avanzada
- [ ] Agregar validaciones más robustas
- [ ] Implementar caché en frontend
- [ ] Agregar tests unitarios e integración
