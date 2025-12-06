const API_BASE_URL = import.meta.env.VITE_API_URL || 'https://mod-ventas.onrender.com/api';

export interface AplicarDescuentoRequest {
    ventaId: string;
    dniCliente: string;
    codigoCupon?: string | null;
}

export interface AplicarDescuentoResponse {
    tipoDescuento: string;
    montoDescontado: number;
    nuevoTotalVenta: number;
    mensaje: string;
}

export const aplicarMejorDescuento = async (request: AplicarDescuentoRequest): Promise<AplicarDescuentoResponse> => {
    const response = await fetch(`${API_BASE_URL}/descuentos/aplicar-mejor-descuento`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(request),
    });

    if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || 'Error al aplicar descuento');
    }

    return response.json();
};
