// Copiar y pegar estos handlers en PaginaVentaDirecta.tsx después de la línea 237

const handleEliminarCliente = async () => {
    if (!ventaId) return;

    try {
        await desasignarClienteDeVenta(Number(ventaId));
        setClienteSeleccionado(null);
        setBusquedaCliente('');
        alert('Cliente eliminado exitosamente');
    } catch (error) {
        console.error('Error al eliminar cliente:', error);
        alert('Error al eliminar cliente de la venta');
    }
};


const handleAplicarDescuento = async () => {
    if (!ventaId) {
        alert('No hay venta activa');
        return;
    }

    if (!clienteSeleccionado) {
        alert('Debe asignar un cliente antes de aplicar descuentos');
        return;
    }

    try {
        const response = await aplicarMejorDescuento({
            ventaId: ventaId,
            dniCliente: clienteSeleccionado.dni,
            codigoCupon: codigoCupon || null
        });

        setMensajeDescuento(`✓ ${response.mensaje} - Descuento: S/ ${response.montoDescontado.toFixed(2)}`);
        await cargarTotales();
        setTimeout(() => setMensajeDescuento(null), 5000);
    } catch (error) {
        console.error('Error al aplicar descuento:', error);
        alert('Error al aplicar descuento');
    }
};

const handleClienteRegistrado = async (cliente: Cliente) => {
    if (!ventaId) return;

    try {
        await asignarClienteAVenta(Number(ventaId), cliente.clienteId);
        setClienteSeleccionado({
            id: cliente.clienteId,
            nombre: cliente.fullName,
            dni: cliente.dni,
            telefono: cliente.phoneNumber || 'N/A',
            email: cliente.email
        });
        alert('Cliente registrado y asignado exitosamente');
    } catch (error) {
        console.error('Error al asignar cliente:', error);
        alert('Error al asignar cliente a la venta');
    }
};
