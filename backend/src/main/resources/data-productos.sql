-- Script para insertar productos de prueba en la base de datos
-- Ejecutar este script en la base de datos db_ventas

-- Limpiar datos existentes (opcional)
-- DELETE FROM combo_productos;
-- DELETE FROM productos;

-- Insertar productos individuales
INSERT INTO productos (codigo, nombre, tipo, precio_base, precio_final, descuento_total, informacion_adicional, imagen_url, activo, stock, created_at, updated_at, created_by)
VALUES
('PROD-0001', 'Smartphone X20', 'EQUIPO_MOVIL', 999.99, 999.99, 0.00, '128GB, Color Negro', 'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=400', TRUE, 50, NOW(), NOW(), 'admin'),
('PROD-0002', 'iPhone 14 Pro', 'EQUIPO_MOVIL', 899.99, 899.99, 0.00, '256GB, Color Blanco', 'https://images.unsplash.com/photo-1678685888221-cda5f6997548?w=400', TRUE, 30, NOW(), NOW(), 'admin'),
('PROD-0003', 'Samsung Galaxy S23', 'EQUIPO_MOVIL', 799.99, 799.99, 0.00, '128GB, Color Azul', 'https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?w=400', TRUE, 40, NOW(), NOW(), 'admin'),
('PROD-0004', 'Internet 100Mbps', 'SERVICIO_HOGAR', 49.99, 49.99, 0.00, 'Velocidad de descarga 100Mbps', 'https://images.unsplash.com/photo-1544197150-b99a580bb7a8?w=400', TRUE, 100, NOW(), NOW(), 'admin'),
('PROD-0005', 'Internet 300Mbps', 'SERVICIO_HOGAR', 79.99, 79.99, 0.00, 'Velocidad de descarga 300Mbps', 'https://images.unsplash.com/photo-1606904825846-647eb07f5be2?w=400', TRUE, 100, NOW(), NOW(), 'admin'),
('PROD-0006', 'TV Cable Premium', 'SERVICIO_HOGAR', 39.99, 39.99, 0.00, 'Más de 200 canales HD', 'https://images.unsplash.com/photo-1593359677879-a4bb92f829d1?w=400', TRUE, 100, NOW(), NOW(), 'admin'),
('PROD-0007', 'Plan Postpago Ilimitado', 'SERVICIO_MOVIL', 29.99, 29.99, 0.00, 'Llamadas y SMS ilimitados, 20GB datos', 'https://images.unsplash.com/photo-1604537529428-15bcbeecfe4d?w=400', TRUE, 100, NOW(), NOW(), 'admin'),
('PROD-0008', 'Plan Max 20GB', 'SERVICIO_MOVIL', 19.99, 19.99, 0.00, 'Llamadas ilimitadas, 20GB datos', 'https://images.unsplash.com/photo-1604537466158-719b1972feb8?w=400', TRUE, 100, NOW(), NOW(), 'admin'),
('PROD-0009', 'Plan Familia 50GB', 'SERVICIO_MOVIL', 49.99, 49.99, 0.00, 'Para toda la familia, 50GB compartidos', 'https://images.unsplash.com/photo-1604537466573-5e94508fd243?w=400', TRUE, 100, NOW(), NOW(), 'admin');

-- Insertar un combo de ejemplo
INSERT INTO productos (codigo, nombre, tipo, precio_base, precio_final, descuento_total, informacion_adicional, imagen_url, activo, stock, created_at, updated_at, created_by)
VALUES
('COMBO-0001', 'Pack Conectividad Total', 'COMBO', 119.97, 99.99, 19.98, 'Internet + TV Cable + Plan Móvil', 'https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400', TRUE, 20, NOW(), NOW(), 'admin');

-- Insertar relaciones del combo (usar los IDs correctos según tu base de datos)
-- Nota: Ajusta los IDs según los generados en tu base de datos
INSERT INTO combo_productos (combo_id, producto_id, orden, precio_individual, descuento_aplicado, created_at)
SELECT
    (SELECT id FROM productos WHERE codigo = 'COMBO-0001'),
    p.id,
    ROW_NUMBER() OVER (ORDER BY p.id),
    p.precio_final,
    0.00,
    NOW()
FROM productos p
WHERE p.codigo IN ('PROD-0003', 'PROD-0004', 'PROD-0005');

-- Verificar los datos insertados
-- SELECT * FROM productos;
-- SELECT * FROM combo_productos;

