import { Route, Routes } from "react-router-dom"
import { PaginaVenta } from "./paginas/PaginaVenta"
import { PaginaCliente } from "./paginas/PaginaCliente"
import { PaginaCotizacion } from './modules/cotizacion/pages/PaginaCotizacion';
import { LayoutPrincipal } from './components/layout/LayoutPrincipal';
import { PaginaNoEncontrada } from "./paginas/PaginaNoEncontrada"
import Login from "./components/Login"
import { useRole } from "./contexts/RoleContext"
import { PaginaVendedor } from "./paginas/PaginaVendedor"
import { PaginaVentaDirecta } from "./paginas/PaginaVentaDirecta"
import { PaginaVentaLead } from "./paginas/PaginaVentaLead"

// --- NUEVAS IMPORTACIONES ---
import { PaginaGestionProductos } from './modules/producto/pages/PaginaGestionProductos'; 
import { PaginaCatalogoVendedor } from './modules/producto/pages/PaginaCatalogoVendedor'; 
// ---------------------------

function App() {
  const { role } = useRole();

  if (!role) return <Login />;

  // Se ha cambiado la estructura para usar rutas anidadas y controlar el rol de forma más limpia.
  return (
    <Routes>
      <Route path="/" element={<LayoutPrincipal />}>
        {role === 'vendedor' && (
          <>
            {/* Rutas Principales del Vendedor */}
            <Route index element={<PaginaVenta />} />
            <Route path="registrar-venta" element={<PaginaVentaDirecta />} />
            <Route path="registrar-venta-lead" element={<PaginaVentaLead />} />
            <Route path="pagina-cotizacion" element={<PaginaCotizacion />} />
            <Route path="pagina-cliente" element={<PaginaCliente />} />

            {/* Nueva Ruta para el Catálogo de Vendedor */}
            <Route path="catalogo-productos" element={<PaginaCatalogoVendedor />} />
          </>
        )}

        {role === 'administrador' && (
          <>
            {/* Rutas Principales del Administrador */}
            <Route index element={<PaginaVendedor />} /> {/* Página de inicio del Admin */}
            <Route path="pagina-vendedor" element={<PaginaVendedor />} />
            <Route path="pagina-cliente" element={<PaginaCliente />} />
            
            {/* Nueva Ruta para la Gestión de Productos del Administrador */}
            <Route path="gestion-productos" element={<PaginaGestionProductos />} />
          </>
        )}
        
        {/* En caso de que haya login pero no se reconozca el rol */}
        <Route path="*" element={<PaginaNoEncontrada />}></Route>
      </Route>

      <Route path="*" element={<PaginaNoEncontrada />}></Route>
    </Routes>
  );
}

export default App;