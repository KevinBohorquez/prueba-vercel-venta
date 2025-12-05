import { SellerType, type SellerToolbarProps } from "../types/seller.types";
import { Package } from 'lucide-react';

const SellerToolbar = ({onNewSellerClick, onCreateComboClick, isAdmin = false} : SellerToolbarProps) => (
  <div className="flex flex-col md:flex-row justify-between items-center space-y-4 md:space-y-0 mb-6">
    {/* Barra de Búsqueda */}
    <div className="w-full md:w-1/3">
      <input
        type="text"
        placeholder="Buscar por nombre, DNI o código..."
        className="w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
      />
    </div>

    {/* Filtros y Botones */}
    <div className="flex flex-col md:flex-row items-center space-y-4 md:space-y-0 md:space-x-4">
      <select className="w-full md:w-auto px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none">
        <option value="">Todo Tipo</option>
        <option value={SellerType.Interno}>Interno</option>
        <option value={SellerType.Externo}>Externo</option>
      </select>
      
      <select className="w-full md:w-auto px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none">
        <option value="">Toda Sede</option>
        <option value="call-center">Call Center (Piso 4)</option>
        <option value="real-plaza">Módulo Real Plaza - 1er Piso</option>
      </select>

      {/* Botón Crear Combo - Solo para Administradores */}
      {isAdmin && onCreateComboClick && (
        <button 
          onClick={onCreateComboClick}
          className="w-full md:w-auto bg-gradient-to-r from-indigo-600 to-purple-600 text-white px-4 py-2 rounded-lg shadow-md hover:from-indigo-700 hover:to-purple-700 transition duration-300 flex items-center gap-2"
        >
          <Package size={18} />
          Crear Combo
        </button>
      )}

      {/* Botón Crear Vendedor */}
      <button 
        onClick={onNewSellerClick}
        className="w-full md:w-auto bg-blue-600 text-white px-4 py-2 rounded-lg shadow-md hover:bg-blue-700 transition duration-300">
        Nuevo Vendedor
      </button>
    </div>
  </div>
);


export default SellerToolbar;