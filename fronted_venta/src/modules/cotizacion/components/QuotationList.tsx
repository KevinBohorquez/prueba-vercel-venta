import { Download, Mail, CheckCircle, ShoppingCart } from 'lucide-react';
import type { Quotation } from '../types/quotation.types';

interface QuotationListProps {
  quotations: Quotation[];
  onDownloadPdf: (id: number) => void;
  onSendEmail: (quotation: Quotation) => void;
  onOpenAcceptDialog: (quotation: Quotation) => void;
  onConvertToSale: (id: number) => void;
  onViewDetail: (id: number) => void;
  isLoading?: boolean;
  isConverting?: boolean;
  // Pagination props
  page?: number;
  totalPages?: number;
  onPageChange?: (page: number) => void;
}

export function QuotationList({
  quotations,
  onDownloadPdf,
  onSendEmail,
  onOpenAcceptDialog,
  onConvertToSale,
  onViewDetail,
  isLoading = false,
  isConverting = false,
  page = 0,
  totalPages = 0,
  onPageChange,
}: QuotationListProps) {
  const getStatusColor = (status: string) => {
    switch (status) {
      case 'BORRADOR':
        return 'bg-gray-100 text-gray-700';
      case 'ENVIADA':
        return 'bg-blue-100 text-blue-700';
      case 'ACEPTADA':
        return 'bg-green-100 text-green-700';
      case 'RECHAZADA':
        return 'bg-red-100 text-red-700';
      default:
        return 'bg-gray-100 text-gray-700';
    }
  };

  return (
    <div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden flex flex-col h-full">
      {/* Table - Set flex-1 to occupy available space and allow scrolling */}
      <div className="overflow-auto flex-1">
        <table className="w-full text-left relative border-separate border-spacing-0">
          <thead className="bg-gray-50 text-gray-500 uppercase text-xs font-semibold sticky top-0 z-10 shadow-sm">
            <tr>
              <th className="px-6 py-3 bg-gray-50 border-b border-gray-200">N° Cotización</th>
              <th className="px-6 py-3 bg-gray-50 border-b border-gray-200">Cliente</th>
              <th className="px-6 py-3 bg-gray-50 border-b border-gray-200">Fecha Creación</th>
              <th className="px-6 py-3 bg-gray-50 border-b border-gray-200">Fecha Expiración</th>
              <th className="px-6 py-3 bg-gray-50 border-b border-gray-200">Total</th>
              <th className="px-6 py-3 bg-gray-50 border-b border-gray-200">Estado</th>
              <th className="px-6 py-3 bg-gray-50 border-b border-gray-200 text-right">Acciones</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-200 bg-white">
            {isLoading ? (
              <tr>
                <td colSpan={7} className="px-6 py-20 text-center text-gray-500">
                  <div className="flex flex-col items-center justify-center gap-3">
                    <svg
                      className="animate-spin h-8 w-8 text-blue-500"
                      xmlns="http://www.w3.org/2000/svg"
                      fill="none"
                      viewBox="0 0 24 24"
                    >
                      <circle
                        className="opacity-25"
                        cx="12"
                        cy="12"
                        r="10"
                        stroke="currentColor"
                        strokeWidth="4"
                      ></circle>
                      <path
                        className="opacity-75"
                        fill="currentColor"
                        d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                      ></path>
                    </svg>
                    <span>Cargando cotizaciones...</span>
                  </div>
                </td>
              </tr>
            ) : quotations.length > 0 ? (
              quotations.map((q) => (
                <tr
                  key={q.id}
                  onClick={() => onViewDetail(q.id)}
                  className="hover:bg-blue-50/50 transition-colors cursor-pointer group"
                >
                  <td className="px-6 py-4 font-medium text-gray-900">{q.numCotizacion}</td>
                  <td className="px-6 py-4 text-gray-700">{q.clienteNombre}</td>
                  <td className="px-6 py-4 text-gray-500">
                    {new Date(q.fechaCotizacion).toLocaleDateString('es-PE')}
                  </td>
                  <td className="px-6 py-4 text-gray-500">
                    {q.fechaExpiracion
                      ? new Date(q.fechaExpiracion).toLocaleDateString('es-PE')
                      : 'N/A'}
                  </td>
                  <td className="px-6 py-4 font-bold text-gray-900">
                    S/ {q.totalCotizado.toFixed(2)}
                  </td>
                  <td className="px-6 py-4">
                    <span
                      className={`px-3 py-1 rounded-full text-xs font-semibold ${getStatusColor(
                        q.estado
                      )}`}
                    >
                      {q.estado}
                    </span>
                  </td>
                  <td className="px-6 py-4">
                    <div className="flex items-center justify-end gap-2 opacity-90 group-hover:opacity-100 transition-opacity">
                      <button
                        onClick={(e) => {
                          e.stopPropagation();
                          onDownloadPdf(q.id);
                        }}
                        className="p-1.5 text-gray-600 hover:text-blue-600 hover:bg-blue-50 rounded transition-colors"
                        title="Descargar PDF"
                      >
                        <Download size={18} />
                      </button>
                      <button
                        onClick={(e) => {
                          e.stopPropagation();
                          onSendEmail(q);
                        }}
                        className="p-1.5 text-gray-600 hover:text-blue-600 hover:bg-blue-50 rounded transition-colors"
                        title="Enviar por Correo"
                      >
                        <Mail size={18} />
                      </button>
                      {q.estado !== 'ACEPTADA' ? (
                        <button
                          onClick={(e) => {
                            e.stopPropagation();
                            onOpenAcceptDialog(q);
                          }}
                          className="w-24 px-3 py-1.5 text-white bg-green-600 hover:bg-green-700 rounded-md transition-colors text-xs font-medium flex items-center justify-center gap-1"
                          title="Marcar como Aceptada"
                        >
                          <CheckCircle size={14} />
                          Aceptar
                        </button>
                      ) : (
                        <button
                          onClick={(e) => {
                            e.stopPropagation();
                            onConvertToSale(q.id);
                          }}
                          disabled={isConverting}
                          className="w-24 px-3 py-1.5 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors text-xs font-medium flex items-center justify-center gap-1 disabled:opacity-50"
                          title="Generar Venta"
                        >
                          <ShoppingCart size={14} />
                          Venta
                        </button>
                      )}
                    </div>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan={7} className="px-6 py-20 text-center text-gray-500">
                  <div className="flex flex-col items-center gap-2">
                    <ShoppingCart size={32} className="opacity-20" />
                    <p>No se encontraron cotizaciones</p>
                  </div>
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {/* Pagination Footer */}
      {totalPages > 1 && onPageChange && (
        <div className="px-6 py-4 border-t border-gray-200 bg-gray-50 flex items-center justify-between sticky bottom-0 z-10">
          <div className="text-sm text-gray-500">
            Página <span className="font-medium text-gray-900">{page + 1}</span> de{' '}
            <span className="font-medium text-gray-900">{totalPages}</span>
          </div>
          <div className="flex gap-2">
            <button
              onClick={() => onPageChange(page - 1)}
              disabled={page === 0}
              className="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
            >
              Anterior
            </button>
            <button
              onClick={() => onPageChange(page + 1)}
              disabled={page >= totalPages - 1}
              className="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
            >
              Siguiente
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
