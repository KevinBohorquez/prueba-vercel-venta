# 🖼️ Guía de Imágenes para Productos en el Modal de Combos

## ✅ Implementación Actual

Se han agregado imágenes referenciales a cada producto en el modal de creación de combos con las siguientes características:

### Características Visuales:
- **Aspecto ratio 4:3** para mantener consistencia
- **Hover effect** con zoom suave en la imagen
- **Overlay semi-transparente** cuando el producto está seleccionado
- **Badges flotantes** sobre la imagen (tipo de producto y descuento)
- **Checkbox integrado** en la esquina superior izquierda
- **Animaciones suaves** en transiciones

## 🎨 Opciones para Personalizar las Imágenes

### **Opción 1: Imágenes Placeholder (Actual)**

Actualmente usa placeholders que se diferencian por color según el tipo:

```typescript
const getProductImage = (tipo: TipoProducto): string => {
  switch (tipo) {
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

### **Opción 2: Agregar Campo de Imagen en el Backend**

Modifica el tipo `ProductoDTO` para incluir una URL de imagen:

```typescript
// En product.types.ts
export interface ProductoDTO {
  id: string;
  nombre: string;
  tipo: TipoProducto;
  precioBase: number;
  precioFinal: number;
  informacionAdicional: string;
  descuentoTotal?: number;
  componentes?: ProductoDTO[];
  imagenUrl?: string; // ⭐ Nueva propiedad
}
```

Luego actualiza la función:

```typescript
const getProductImage = (producto: ProductoDTO): string => {
  // Si el producto tiene imagen propia, úsala
  if (producto.imagenUrl) {
    return producto.imagenUrl;
  }
  
  // Caso contrario, usa imagen por defecto según tipo
  switch (producto.tipo) {
    case 'EQUIPO_MOVIL':
      return 'https://via.placeholder.com/300x200/4F46E5/ffffff?text=Smartphone';
    case 'SERVICIO_HOGAR':
      return 'https://via.placeholder.com/300x200/10B981/ffffff?text=Internet+Hogar';
    case 'SERVICIO_MOVIL':
      return 'https://via.placeholder.com/300x200/10B981/ffffff?text=Plan+Movil';
    default:
      return '/assets/images/default-product.jpg';
  }
};
```

Y en el JSX:

```tsx
<img 
  src={getProductImage(producto)} // Pasa el objeto completo
  alt={producto.nombre}
  className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
/>
```

### **Opción 3: Usar un CDN o Carpeta de Assets Local**

Crea una carpeta de imágenes en tu proyecto:

```
src/
  assets/
    images/
      productos/
        smartphone-default.jpg
        internet-hogar-default.jpg
        plan-movil-default.jpg
```

Importa y usa:

```typescript
import smartphoneImg from '../assets/images/productos/smartphone-default.jpg';
import internetHogarImg from '../assets/images/productos/internet-hogar-default.jpg';
import planMovilImg from '../assets/images/productos/plan-movil-default.jpg';

const getProductImage = (tipo: TipoProducto): string => {
  switch (tipo) {
    case 'EQUIPO_MOVIL':
      return smartphoneImg;
    case 'SERVICIO_HOGAR':
      return internetHogarImg;
    case 'SERVICIO_MOVIL':
      return planMovilImg;
    default:
      return '/default-product.jpg';
  }
};
```

### **Opción 4: Unsplash o API de Imágenes Gratuitas**

Para imágenes de alta calidad sin costo:

```typescript
const getProductImage = (tipo: TipoProducto, productId: string): string => {
  const seed = productId; // Usa el ID para que siempre sea la misma imagen
  
  switch (tipo) {
    case 'EQUIPO_MOVIL':
      return `https://source.unsplash.com/300x200/?smartphone,mobile&sig=${seed}`;
    case 'SERVICIO_HOGAR':
      return `https://source.unsplash.com/300x200/?internet,wifi,home&sig=${seed}`;
    case 'SERVICIO_MOVIL':
      return `https://source.unsplash.com/300x200/?mobile,data,plan&sig=${seed}`;
    default:
      return `https://source.unsplash.com/300x200/?technology&sig=${seed}`;
  }
};
```

Uso en el componente:

```tsx
<img 
  src={getProductImage(producto.tipo, producto.id)}
  alt={producto.nombre}
  className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
/>
```

### **Opción 5: Iconos en lugar de Imágenes (Lightweight)**

Si prefieres un enfoque más minimalista:

```tsx
import { Smartphone, Wifi, Signal, Package } from 'lucide-react';

const ProductIcon = ({ tipo }: { tipo: TipoProducto }) => {
  const iconClass = "w-16 h-16";
  
  switch (tipo) {
    case 'EQUIPO_MOVIL':
      return <Smartphone className={iconClass} />;
    case 'SERVICIO_HOGAR':
      return <Wifi className={iconClass} />;
    case 'SERVICIO_MOVIL':
      return <Signal className={iconClass} />;
    default:
      return <Package className={iconClass} />;
  }
};

// En la tarjeta:
<div className="relative aspect-[4/3] w-full flex items-center justify-center bg-gradient-to-br from-blue-100 to-purple-100">
  <ProductIcon tipo={producto.tipo} />
</div>
```

## 🔧 Mejoras Adicionales para Imágenes

### 1. **Lazy Loading**
```tsx
<img 
  src={getProductImage(producto.tipo)}
  alt={producto.nombre}
  loading="lazy" // ⭐ Carga diferida
  className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
/>
```

### 2. **Placeholder mientras carga**
```tsx
<img 
  src={getProductImage(producto.tipo)}
  alt={producto.nombre}
  onError={(e) => {
    e.currentTarget.src = '/fallback-image.jpg'; // Imagen de respaldo
  }}
  className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
/>
```

### 3. **Estado de carga**
```tsx
const [imageLoaded, setImageLoaded] = useState(false);

<div className="relative aspect-[4/3] w-full overflow-hidden bg-gray-200">
  {!imageLoaded && (
    <div className="absolute inset-0 flex items-center justify-center">
      <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
    </div>
  )}
  <img 
    src={getProductImage(producto.tipo)}
    alt={producto.nombre}
    onLoad={() => setImageLoaded(true)}
    className={`w-full h-full object-cover transition-opacity ${imageLoaded ? 'opacity-100' : 'opacity-0'}`}
  />
</div>
```

## 📝 Recomendaciones

1. **Para Desarrollo**: Usa placeholders o Unsplash (Opción 1 o 4)
2. **Para Producción**: Implementa URLs en el backend (Opción 2)
3. **Para Performance**: Usa lazy loading y optimiza las imágenes
4. **Tamaño Recomendado**: 300x200px o 600x400px (retina)
5. **Formato**: WebP para mejor compresión, con fallback a JPG/PNG

## 🎯 Implementación Recomendada Paso a Paso

### Backend:
1. Agrega campo `imagenUrl` a la entidad Producto
2. Guarda las imágenes en un servidor de archivos o CDN
3. Retorna la URL completa en la API

### Frontend:
1. Actualiza el tipo `ProductoDTO` con `imagenUrl?: string`
2. Modifica `getProductImage` para usar la URL del backend
3. Implementa fallback a imágenes por defecto
4. Agrega lazy loading y error handling

¡La implementación actual ya está lista para recibir imágenes reales! Solo necesitas agregar el campo `imagenUrl` en el backend y el frontend lo mostrará automáticamente.
