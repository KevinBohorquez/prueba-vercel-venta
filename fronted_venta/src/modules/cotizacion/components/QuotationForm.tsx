import { ShoppingCartIcon } from '../../../components/Icons';
import { UserPlus } from 'lucide-react';
import type { QuotationFormData } from '../types/quotation.types';
import type { Cliente } from '../../cliente/services/clienteService';
import type { VendedorResponse } from '../../vendedor/services/vendedorService';
import type { Producto } from '../../producto/services/productoService';
import { ProductCatalogModal, type Product } from '../../../components/ProductCatalogModal';
import { SearchableSelect } from '../../../components/SearchableSelect';

interface QuotationFormProps {
  formData: QuotationFormData;
  onFormDataChange: (data: QuotationFormData) => void;
  clientes: Cliente[];
  vendedores: VendedorResponse[];
  productos: Producto[];
  selectedProduct: number | null;
  onSelectedProductChange: (productId: number | null) => void;
  onAddItem: () => void;
  onRemoveItem: (tempId: string) => void;
  onUpdateQuantity: (tempId: string, quantity: number) => void;
  totals: { subtotal: number; tax: number; total: number };
  onSave: () => void;
  onCancel: () => void;
  isSubmitting?: boolean;
  error?: string | null;
  catalogModalOpen: boolean;
  onOpenCatalog: () => void;
  onCloseCatalog: () => void;
  onAddProductFromCatalog: (product: Product) => void;
  onOpenCreateClient: () => void;
}

export function QuotationForm({
  formData,
  onFormDataChange,
  clientes,
  vendedores,
  productos,
  selectedProduct,
  onSelectedProductChange,
  onAddItem,
  onRemoveItem,
  onUpdateQuantity,
  totals,
  onSave,
  onCancel,
  isSubmitting = false,
  error = null,
  catalogModalOpen,
  onOpenCatalog,
  onCloseCatalog,
  onAddProductFromCatalog,
  onOpenCreateClient,
}: QuotationFormProps) {
  return (
    <div className="flex flex-col h-[calc(100vh-8rem)] gap-4">
      {/* Header & Meta Data Card */}
      <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-4 shrink-0">
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-lg font-bold text-gray-800">Nueva Cotización</h2>
          <button
            onClick={onCancel}
            className="text-gray-500 hover:text-gray-700 text-sm font-medium"
          >
            Cancelar
          </button>
        </div>

        {error && (
          <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg">
            <p className="text-red-800 text-sm">{error}</p>
          </div>
        )}

        <div className="grid grid-cols-1 md:grid-cols-12 gap-4">
          {/* Client Selection - 5 cols */}
          <div className="md:col-span-5">
            <label className="block text-xs font-medium text-gray-500 mb-1">Cliente</label>
            <div className="flex gap-2">
              <div className="flex-1">
                <SearchableSelect
                  options={clientes}
                  value={formData.clienteId}
                  onChange={(value) => onFormDataChange({ ...formData, clienteId: value })}
                  getOptionLabel={(cliente) =>
                    `${cliente.nombre}${cliente.documento ? ` - ${cliente.documento}` : ''}`
                  }
                  getOptionValue={(cliente) => cliente.id}
                  label=""
                  placeholder="Buscar cliente..."
                  required
                />
              </div>
              <button
                onClick={onOpenCreateClient}
                className="px-3 py-2 bg-blue-50 text-blue-600 hover:bg-blue-100 rounded-lg transition-colors"
                title="Nuevo Cliente"
              >
                <UserPlus size={18} />
              </button>
            </div>
          </div>

          {/* Seller Selection - 4 cols */}
          <div className="md:col-span-4">
            <label className="block text-xs font-medium text-gray-500 mb-1">Vendedor</label>
            <SearchableSelect
              options={vendedores}
              value={formData.vendedorId}
              onChange={(value) => onFormDataChange({ ...formData, vendedorId: value })}
              getOptionLabel={(vendedor) => `${vendedor.fullName}`}
              getOptionValue={(vendedor) => vendedor.sellerId}
              label=""
              placeholder="Vendedor..."
              required
            />
          </div>

          {/* Validity - 3 cols */}
          <div className="md:col-span-3">
            <label className="block text-xs font-medium text-gray-500 mb-1">Validez (Días)</label>
            <input
              type="number"
              min="1"
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none text-sm"
              value={formData.validezDias}
              onChange={(e) =>
                onFormDataChange({
                  ...formData,
                  validezDias: parseInt(e.target.value) || 15,
                })
              }
            />
          </div>
        </div>
      </div>

      <div className="flex-1 min-h-0 grid grid-cols-1 lg:grid-cols-4 gap-4">
        {/* Main Content: Products Table */}
        <div className="lg:col-span-3 bg-white rounded-xl shadow-sm border border-gray-200 flex flex-col min-h-0">
          {/* Toolbar */}
          <div className="p-4 border-b border-gray-200 flex gap-3 bg-gray-50/50">
            <div className="flex-1 min-w-[200px]">
              <select
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none text-sm bg-white"
                value={selectedProduct || ''}
                onChange={(e) =>
                  onSelectedProductChange(e.target.value ? Number(e.target.value) : null)
                }
              >
                <option value="">Buscar producto...</option>
                {productos.map((p) => (
                  <option key={p.id} value={p.id}>
                    {p.nombre} - S/ {p.precio.toFixed(2)}
                  </option>
                ))}
              </select>
            </div>
            <button
              onClick={onAddItem}
              disabled={!selectedProduct}
              className="px-4 py-2 bg-blue-600 hover:bg-blue-700 disabled:bg-gray-300 text-white rounded-lg font-medium text-sm transition-colors whitespace-nowrap"
            >
              Agregar
            </button>
            <button
              onClick={onOpenCatalog}
              className="px-4 py-2 bg-white border border-gray-300 hover:bg-gray-50 text-gray-700 rounded-lg font-medium text-sm transition-colors flex items-center gap-2 whitespace-nowrap"
            >
              <ShoppingCartIcon size={16} />
              Catálogo
            </button>
          </div>

          {/* Table Container */}
          <div className="flex-1 overflow-auto">
            <table className="w-full text-left border-collapse">
              <thead className="bg-gray-50 text-gray-500 text-xs uppercase font-semibold sticky top-0 z-10">
                <tr>
                  <th className="px-4 py-3 border-b border-gray-200">Producto</th>
                  <th className="px-4 py-3 border-b border-gray-200 w-24 text-center">Cant.</th>
                  <th className="px-4 py-3 border-b border-gray-200 w-32 text-right">Precio</th>
                  <th className="px-4 py-3 border-b border-gray-200 w-32 text-right">Total</th>
                  <th className="px-4 py-3 border-b border-gray-200 w-12"></th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-100">
                {formData.items.length > 0 ? (
                  formData.items.map((item) => (
                    <tr key={item.tempId} className="hover:bg-gray-50 group">
                      <td className="px-4 py-2 text-sm text-gray-800">{item.productoNombre}</td>
                      <td className="px-4 py-2 text-center">
                        <input
                          type="number"
                          min="1"
                          className="w-16 px-2 py-1 border border-gray-200 rounded text-center text-sm focus:border-blue-500 outline-none bg-gray-50 focus:bg-white transition-colors"
                          value={item.cantidad}
                          onChange={(e) =>
                            onUpdateQuantity(item.tempId, parseInt(e.target.value) || 1)
                          }
                        />
                      </td>
                      <td className="px-4 py-2 text-sm text-right text-gray-600">
                        S/ {item.precioUnitario.toFixed(2)}
                      </td>
                      <td className="px-4 py-2 text-sm text-right font-medium text-gray-900">
                        S/ {item.subtotal.toFixed(2)}
                      </td>
                      <td className="px-4 py-2 text-right">
                        <button
                          onClick={() => onRemoveItem(item.tempId)}
                          className="text-gray-400 hover:text-red-500 transition-colors bg-transparent p-1 rounded-full hover:bg-red-50"
                        >
                          &times;
                        </button>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan={5} className="px-4 py-12 text-center text-gray-400">
                      <div className="flex flex-col items-center gap-2">
                        <ShoppingCartIcon size={32} className="opacity-20" />
                        <p className="text-sm italic">No hay productos seleccionados</p>
                      </div>
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>

        {/* Sidebar: Totals & Actions */}
        <div className="lg:col-span-1 flex flex-col gap-4">
          <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-5 flex flex-col gap-4">
            <h3 className="font-semibold text-gray-800 text-sm uppercase tracking-wide">Resumen</h3>

            <div className="space-y-2 py-4 border-y border-dashed border-gray-200">
              <div className="flex justify-between text-sm">
                <span className="text-gray-500">Subtotal</span>
                <span className="font-medium text-gray-900">S/ {totals.subtotal.toFixed(2)}</span>
              </div>
              <div className="flex justify-between text-sm">
                <span className="text-gray-500">IGV (18%)</span>
                <span className="font-medium text-gray-900">S/ {totals.tax.toFixed(2)}</span>
              </div>
            </div>

            <div className="flex justify-between items-baseline mb-2">
              <span className="text-base font-bold text-gray-800">Total</span>
              <span className="text-2xl font-bold text-blue-600">S/ {totals.total.toFixed(2)}</span>
            </div>

            <button
              onClick={onSave}
              disabled={isSubmitting || formData.items.length === 0}
              className="w-full bg-blue-600 hover:bg-blue-700 text-white py-3 rounded-lg font-semibold shadow-md shadow-blue-200 transition-all disabled:opacity-50 disabled:cursor-not-allowed disabled:shadow-none flex items-center justify-center gap-2"
            >
              {isSubmitting ? 'Guardando...' : 'Guardar Cotización'}
            </button>
          </div>
        </div>
      </div>

      {/* Product Catalog Modal */}
      <ProductCatalogModal
        isOpen={catalogModalOpen}
        onClose={onCloseCatalog}
        onAddProduct={onAddProductFromCatalog}
      />
    </div>
  );
}
