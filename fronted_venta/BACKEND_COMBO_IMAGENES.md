# 📦 Sistema de Combos - Base de Datos y Campo de Imagen

## 🗄️ ¿Cómo se guardan los Combos en la Base de Datos?

### **Respuesta: SÍ, los combos se guardan en una base de datos**

Basándome en el análisis del código frontend, el sistema funciona así:

### **1. Flujo de Creación de Combos**

```
Frontend (React) → Backend API → Base de Datos
```

**Endpoints utilizados:**
- `GET /api/productos/disponibles` - Obtiene productos individuales
- `POST /api/productos/combos` - Crea un nuevo combo
- `GET /api/productos/combos` - Lista combos existentes (usado en PaginaGestionProductos)

### **2. Estructura de Datos que se Envía**

Cuando creas un combo, el frontend envía:

```json
{
  "nombre": "Súper Pack Hogar y Móvil",
  "productosIds": [
    "1495a19c-a312-42bf-96aa-9d7182d2be3f",
    "98824f7f-fad1-48ec-9a3b-23e340efa1ac"
  ]
}
```

### **3. Lo que el Backend DEBE Hacer**

El backend (Spring Boot) debe:

1. **Recibir** la lista de IDs de productos
2. **Buscar** cada producto en la base de datos
3. **Calcular** los descuentos:
   - 15% para `EQUIPO_MOVIL`
   - 10% para `SERVICIO_HOGAR` y `SERVICIO_MOVIL`
4. **Crear** un nuevo producto tipo `COMBO` con:
   - Precio base = suma de precios base de componentes
   - Precio final = suma de precios con descuentos aplicados
   - Descuento total = diferencia entre precio base y final
5. **Guardar** en la base de datos
6. **Retornar** el combo creado

### **4. Estructura Probable de la Base de Datos**

#### Tabla: `productos`
```sql
CREATE TABLE productos (
    id VARCHAR(255) PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    tipo VARCHAR(50) NOT NULL, -- 'EQUIPO_MOVIL', 'SERVICIO_HOGAR', 'SERVICIO_MOVIL', 'COMBO'
    precio_base DECIMAL(10,2) NOT NULL,
    precio_final DECIMAL(10,2) NOT NULL,
    informacion_adicional TEXT,
    descuento_total DECIMAL(10,2),
    imagen_url VARCHAR(500), -- ⭐ NUEVO CAMPO
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Tabla: `combo_productos` (relación muchos a muchos)
```sql
CREATE TABLE combo_productos (
    combo_id VARCHAR(255) NOT NULL,
    producto_id VARCHAR(255) NOT NULL,
    orden INT,
    PRIMARY KEY (combo_id, producto_id),
    FOREIGN KEY (combo_id) REFERENCES productos(id) ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE
);
```

---

## ✅ Implementación del Campo `imagenUrl` (Opción 2)

### **Cambios Realizados en el Frontend**

#### **1. Tipo de Datos Actualizado**

```typescript
// src/modules/producto/types/product.types.ts
export interface ProductoDTO {
  id: string;
  nombre: string;
  tipo: TipoProducto;
  precioBase: number;
  precioFinal: number;
  informacionAdicional: string;
  descuentoTotal?: number;
  componentes?: ProductoDTO[];
  imagenUrl?: string; // ⭐ NUEVO CAMPO
}
```

#### **2. Función Actualizada para Obtener Imagen**

```typescript
// src/components/CreateComboModal.tsx
const getProductImage = (producto: ProductoDTO): string => {
  // Prioridad 1: Imagen desde el backend
  if (producto.imagenUrl) {
    return producto.imagenUrl;
  }
  
  // Prioridad 2: Imagen por defecto según tipo
  switch (producto.tipo) {
    case 'EQUIPO_MOVIL':
      return 'https://via.placeholder.com/300x200/4F46E5/ffffff?text=Smartphone';
    case 'SERVICIO_HOGAR':
      return 'https://via.placeholder.com/300x200/10B981/ffffff?text=Internet+Hogar';
    case 'SERVICIO_MOVIL':
      return 'https://via.placeholder.com/300x200/10B981/ffffff?text=Plan+Movil';
    default:
      return 'https://via.placeholder.com/300x200/6B7280/ffffff?text=Producto';
  }
};
```

#### **3. Características Adicionales Implementadas**

- ✅ **Lazy Loading**: Carga diferida de imágenes
- ✅ **Fallback**: Imagen por defecto si falla la carga
- ✅ **Error Handling**: Manejo de errores de carga

```tsx
<img 
  src={getProductImage(producto)}
  alt={producto.nombre}
  onError={(e) => {
    e.currentTarget.src = 'https://via.placeholder.com/300x200/6B7280/ffffff?text=Sin+Imagen';
  }}
  loading="lazy"
  className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
/>
```

---

## 🔧 Implementación en el Backend (Spring Boot)

### **Paso 1: Actualizar la Entidad Producto**

```java
// src/main/java/com/empresa/ventas/model/Producto.java

@Entity
@Table(name = "productos")
public class Producto {
    
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoProducto tipo;
    
    @Column(name = "precio_base", nullable = false)
    private BigDecimal precioBase;
    
    @Column(name = "precio_final", nullable = false)
    private BigDecimal precioFinal;
    
    @Column(name = "informacion_adicional", columnDefinition = "TEXT")
    private String informacionAdicional;
    
    @Column(name = "descuento_total")
    private BigDecimal descuentoTotal;
    
    @Column(name = "imagen_url", length = 500)
    private String imagenUrl; // ⭐ NUEVO CAMPO
    
    @ManyToMany
    @JoinTable(
        name = "combo_productos",
        joinColumns = @JoinColumn(name = "combo_id"),
        inverseJoinColumns = @JoinColumn(name = "producto_id")
    )
    private Set<Producto> componentes = new HashSet<>();
    
    // Getters y Setters...
}
```

### **Paso 2: Actualizar el DTO**

```java
// src/main/java/com/empresa/ventas/dto/ProductoDTO.java

public class ProductoDTO {
    private String id;
    private String nombre;
    private TipoProducto tipo;
    private BigDecimal precioBase;
    private BigDecimal precioFinal;
    private String informacionAdicional;
    private BigDecimal descuentoTotal;
    private String imagenUrl; // ⭐ NUEVO CAMPO
    private List<ProductoDTO> componentes;
    
    // Constructor, Getters y Setters...
}
```

### **Paso 3: Actualizar el Mapper**

```java
// src/main/java/com/empresa/ventas/mapper/ProductoMapper.java

@Component
public class ProductoMapper {
    
    public ProductoDTO toDTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setTipo(producto.getTipo());
        dto.setPrecioBase(producto.getPrecioBase());
        dto.setPrecioFinal(producto.getPrecioFinal());
        dto.setInformacionAdicional(producto.getInformacionAdicional());
        dto.setDescuentoTotal(producto.getDescuentoTotal());
        dto.setImagenUrl(producto.getImagenUrl()); // ⭐ NUEVO
        
        if (producto.getComponentes() != null && !producto.getComponentes().isEmpty()) {
            dto.setComponentes(
                producto.getComponentes().stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList())
            );
        }
        
        return dto;
    }
}
```

### **Paso 4: Migración de Base de Datos**

```sql
-- V1__agregar_campo_imagen_url.sql

ALTER TABLE productos 
ADD COLUMN imagen_url VARCHAR(500);

-- Opcional: Agregar imágenes por defecto según tipo
UPDATE productos 
SET imagen_url = 'https://tu-cdn.com/imagenes/smartphone-default.jpg'
WHERE tipo = 'EQUIPO_MOVIL' AND imagen_url IS NULL;

UPDATE productos 
SET imagen_url = 'https://tu-cdn.com/imagenes/internet-hogar.jpg'
WHERE tipo = 'SERVICIO_HOGAR' AND imagen_url IS NULL;

UPDATE productos 
SET imagen_url = 'https://tu-cdn.com/imagenes/plan-movil.jpg'
WHERE tipo = 'SERVICIO_MOVIL' AND imagen_url IS NULL;
```

---

## 📤 Opciones para Almacenar Imágenes

### **Opción A: Almacenamiento Local (Desarrollo)**

```java
@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    
    @PostMapping("/{id}/imagen")
    public ResponseEntity<String> subirImagen(
        @PathVariable String id,
        @RequestParam("file") MultipartFile file
    ) {
        String fileName = fileStorageService.storeFile(file);
        String fileUrl = "/uploads/" + fileName;
        
        productoService.actualizarImagenUrl(id, fileUrl);
        
        return ResponseEntity.ok(fileUrl);
    }
}
```

### **Opción B: Almacenamiento en la Nube (Producción)**

```java
// Usando AWS S3, Google Cloud Storage, o similar

@Service
public class ImagenService {
    
    @Autowired
    private AmazonS3 s3Client;
    
    public String subirImagenS3(MultipartFile file, String productoId) {
        String keyName = "productos/" + productoId + "/" + file.getOriginalFilename();
        
        s3Client.putObject(
            new PutObjectRequest(bucketName, keyName, file.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead)
        );
        
        return s3Client.getUrl(bucketName, keyName).toString();
    }
}
```

### **Opción C: URL Externas (Más Simple)**

Simplemente guardar URLs de imágenes alojadas en:
- CDN externo
- Cloudinary
- Imgur
- Tu propio servidor de archivos

```java
@PostMapping
public ResponseEntity<ProductoDTO> crearProducto(@RequestBody CrearProductoRequest request) {
    Producto producto = new Producto();
    producto.setNombre(request.getNombre());
    producto.setTipo(request.getTipo());
    producto.setPrecioBase(request.getPrecioBase());
    producto.setImagenUrl(request.getImagenUrl()); // URL directa
    
    Producto guardado = productoRepository.save(producto);
    return ResponseEntity.ok(productoMapper.toDTO(guardado));
}
```

---

## 🧪 Ejemplo de Respuesta del Backend

```json
{
  "id": "1495a19c-a312-42bf-96aa-9d7182d2be3f",
  "nombre": "Smartphone X20",
  "tipo": "EQUIPO_MOVIL",
  "precioBase": 1200.00,
  "precioFinal": 1200.00,
  "informacionAdicional": "256GB, 8GB RAM",
  "descuentoTotal": 0.00,
  "imagenUrl": "https://tu-cdn.com/imagenes/smartphone-x20.jpg",
  "componentes": null
}
```

---

## 📋 Checklist de Implementación

### Backend:
- [ ] Agregar campo `imagenUrl` a la entidad `Producto`
- [ ] Actualizar `ProductoDTO` con el campo
- [ ] Actualizar mapper para incluir `imagenUrl`
- [ ] Crear migración de base de datos
- [ ] (Opcional) Implementar endpoint para subir imágenes
- [ ] Retornar `imagenUrl` en todas las respuestas de productos

### Frontend:
- [x] Agregar campo `imagenUrl` a `ProductoDTO` TypeScript
- [x] Actualizar función `getProductImage()` para usar URL del backend
- [x] Implementar fallback para imágenes faltantes
- [x] Agregar lazy loading y error handling
- [x] Mantener compatibilidad con productos sin imagen

### Base de Datos:
- [ ] Ejecutar migración para agregar columna
- [ ] (Opcional) Popular con imágenes por defecto
- [ ] Verificar índices si hay muchas consultas por tipo

---

## 🚀 Cómo Probar

1. **Agregar imágenes a productos existentes:**
```sql
UPDATE productos 
SET imagen_url = 'https://example.com/smartphone.jpg'
WHERE id = '1495a19c-a312-42bf-96aa-9d7182d2be3f';
```

2. **Crear nuevo producto con imagen:**
```bash
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "iPhone 15 Pro",
    "tipo": "EQUIPO_MOVIL",
    "precioBase": 4500.00,
    "imagenUrl": "https://example.com/iphone15.jpg"
  }'
```

3. **Verificar en el frontend:**
   - Abrir el modal de crear combo
   - Las imágenes deberían cargarse desde las URLs del backend
   - Si falta una imagen, mostrará el placeholder

---

## 💡 Recomendaciones Finales

1. **Usar CDN** para mejor rendimiento
2. **Optimizar imágenes** (WebP, compresión)
3. **Tamaño estándar**: 600x400px para retina display
4. **Cache**: Implementar cache en el navegador
5. **Backup**: Tener siempre imagen por defecto

¡El frontend ya está listo para recibir y mostrar las imágenes del backend!
