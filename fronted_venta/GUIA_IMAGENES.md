# Guía para Agregar Imágenes a Productos

## Opción 1: Imágenes Locales (Recomendado para desarrollo)

### Paso 1: Coloca tus imágenes
Guarda tus imágenes en la carpeta:
```
fronted_venta/public/images/productos/
```

Ejemplo de estructura:
```
public/
  images/
    productos/
      iphone-15.jpg
      samsung-s24.png
      plan-hogar.jpg
      internet-fibra.webp
```

### Paso 2: Configura el backend
En tu backend Spring Boot, cuando retornes productos, usa rutas relativas:

```java
@Entity
public class Producto {
    private Long id;
    private String codigo;
    private String nombre;
    private String imagenUrl; // Guarda: "/images/productos/iphone-15.jpg"
    // ...
}
```

Ejemplo de respuesta JSON:
```json
{
  "id": 1,
  "codigo": "PROD-0001",
  "nombre": "iPhone 15 Pro",
  "tipo": "EQUIPO_MOVIL",
  "precio": 999.99,
  "imagenUrl": "/images/productos/iphone-15.jpg"
}
```

### Paso 3: Ejecuta el programa
Las imágenes se mostrarán automáticamente. Vite sirve todo el contenido de `public/` en la raíz.

---

## Opción 2: URLs Externas (CDN o servidor remoto)

Si las imágenes están en un servidor externo o CDN:

```json
{
  "id": 2,
  "codigo": "PROD-0002",
  "nombre": "Samsung Galaxy S24",
  "imagenUrl": "https://tu-cdn.com/productos/samsung-s24.jpg"
}
```

El frontend detecta automáticamente URLs completas (que empiezan con `http://` o `https://`).

---

## Opción 3: Base de datos directa (No recomendado)

Si guardas las imágenes como BLOB en MySQL:

1. Convierte el BLOB a Base64 en el backend
2. Retorna: `"data:image/jpeg;base64,/9j/4AAQSkZJRg..."`

---

## Formato de Imágenes Recomendado

- **Formatos**: JPG, PNG, WebP
- **Tamaño**: Máximo 500KB por imagen
- **Dimensiones**: 800x600px o 4:3 ratio
- **Optimización**: Usa herramientas como TinyPNG antes de subirlas

---

## Solución de Problemas

### Imagen no se muestra
1. Verifica que el archivo existe en `public/images/productos/`
2. Verifica que el nombre coincide exactamente (case-sensitive)
3. Revisa la consola del navegador (F12) para errores 404

### Fallback automático
Si una imagen falla al cargar, el sistema muestra automáticamente un placeholder gris con el texto "Sin Imagen".

---

## Ejemplo Completo

### Archivo: `iphone-15.jpg`
```
fronted_venta/public/images/productos/iphone-15.jpg
```

### Backend Response:
```json
{
  "id": 1,
  "codigo": "PROD-0001",
  "nombre": "iPhone 15 Pro Max 256GB",
  "tipo": "EQUIPO_MOVIL",
  "precioBase": 1299.99,
  "precioFinal": 1104.99,
  "imagenUrl": "/images/productos/iphone-15.jpg"
}
```

### Resultado:
✅ La imagen se muestra en CreateComboModal
✅ La imagen se muestra en ProductCatalogModal
✅ Si falla, muestra placeholder automático
