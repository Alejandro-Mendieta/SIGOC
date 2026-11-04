/* Autor : Alejandro Mendieta - Julian Andres Mesa
Date : 31 - 10 - 2026
Description : SIGOC - Presentador MVP.
 */

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class Present {

    private IoManager objectIoManager;
    private Model objectModel;

    public Present() {
        objectIoManager = new IoManager();
        objectModel = new Model();
    }

    public void init() {
        int opcion;
        do {
            objectIoManager.showInfo("\n============================================================\n");
            objectIoManager.showInfo("  SIGOC - Sistema de Gestion de Conciertos\n");
            objectIoManager.showInfo("============================================================\n");
            objectIoManager.showInfo("  1. Fase Preventa y Taquilla\n");
            objectIoManager.showInfo("  2. Fase Alimentos y Bebidas\n");
            objectIoManager.showInfo("  3. Fase Mercancia Oficial\n");
            objectIoManager.showInfo("  4. Fase Flujo de Caja\n");
            objectIoManager.showInfo("  5. Fase Operacion de Personal\n");
            objectIoManager.showInfo("  6. Reporte Consolidado\n");
            objectIoManager.showInfo("  0. Salir\n");
            opcion = requestInt("Seleccione una opcion: ", 0, 6);

            switch (opcion) {
                case 1: menuTaquilla(); break;
                case 2: menuAlimentos(); break;
                case 3: menuMerchandising(); break;
                case 4: menuFlujoCaja(); break;
                case 5: menuPersonal(); break;
                case 6: generarReporte(); break;
                case 0: objectIoManager.showInfo("\nSaliendo de SIGOC...\n"); break;
            }
        } while (opcion != 0);
    }

    private void menuTaquilla() {
        int opcion;
        do {
            objectIoManager.showInfo("\n============================================================\n");
            objectIoManager.showInfo("  FASE 1 - Preventa y Taquilla\n");
            objectIoManager.showInfo("============================================================\n");
            objectIoManager.showInfo("  1. Registrar cliente\n");
            objectIoManager.showInfo("  2. Buscar cliente por documento\n");
            objectIoManager.showInfo("  3. Buscar cliente por nombre\n");
            objectIoManager.showInfo("  4. Actualizar cliente\n");
            objectIoManager.showInfo("  5. Eliminar cliente\n");
            objectIoManager.showInfo("  6. Registrar concierto\n");
            objectIoManager.showInfo("  7. Ver conciertos\n");
            objectIoManager.showInfo("  8. Vender ticket\n");
            objectIoManager.showInfo("  9. Consultar seccion\n");
            objectIoManager.showInfo(" 10. Consultar aforo\n");
            objectIoManager.showInfo("  0. Volver\n");
            opcion = requestInt("Opcion: ", 0, 10);

            switch (opcion) {
                case 1: registrarClienteUI(); break;
                case 2: buscarClienteDocUI(); break;
                case 3: buscarClienteNombreUI(); break;
                case 4: actualizarClienteUI(); break;
                case 5: eliminarClienteUI(); break;
                case 6: registrarConciertoUI(); break;
                case 7: verConciertosUI(); break;
                case 8: venderTicketUI(); break;
                case 9: consultarSeccionUI(); break;
                case 10: consultarAforoUI(); break;
            }
        } while (opcion != 0);
    }

    private void registrarClienteUI() {
        objectIoManager.showInfo("\n--- Registrar Cliente ---\n");
        String documento = objectIoManager.requestInfo("Documento: ");
        String nombre = objectIoManager.requestInfo("Nombre: ");
        String telefono = objectIoManager.requestInfo("Telefono: ");
        String fechaStr = objectIoManager.requestInfo("Fecha nacimiento (year-month-day): ");
        try {
            LocalDate fecha = LocalDate.parse(fechaStr, DateTimeFormatter.ISO_LOCAL_DATE);
            Cliente cliente = new Cliente(documento, nombre, telefono, fecha);
            if (objectModel.registrarCliente(cliente)) {
                objectIoManager.showInfo("Cliente registrado. " + cliente + "\n");
            } else {
                objectIoManager.showInfo("No se pudo registrar (documento duplicado o cupo lleno).\n");
            }
        } catch (Exception e) {
            objectIoManager.showInfo("Fecha invalida. Use formato year-month-day.\n");
        }
    }

    private void buscarClienteDocUI() {
        objectIoManager.showInfo("\n--- Buscar Cliente por Documento ---\n");
        String documento = objectIoManager.requestInfo("Documento: ");
        Cliente cliente = objectModel.buscarClientePorDocumento(documento);
        if (cliente != null) objectIoManager.showInfo(cliente + "\n");
        else objectIoManager.showInfo("Cliente no encontrado.\n");
    }

    private void buscarClienteNombreUI() {
        objectIoManager.showInfo("\n--- Buscar Cliente por Nombre ---\n");
        String nombre = objectIoManager.requestInfo("Nombre parcial: ");
        Cliente[] resultados = objectModel.buscarClientePorNombre(nombre);
        if (resultados.length == 0) {
            objectIoManager.showInfo("Sin resultados.\n");
        } else {
            for (Cliente cliente : resultados) objectIoManager.showInfo("  - " + cliente + "\n");
        }
    }

    private void actualizarClienteUI() {
        objectIoManager.showInfo("\n--- Actualizar Cliente ---\n");
        String documento = objectIoManager.requestInfo("Documento: ");
        String nombre = objectIoManager.requestInfo("Nuevo nombre (Enter para conservar): ");
        String telefono = objectIoManager.requestInfo("Nuevo telefono (Enter para conservar): ");
        if (objectModel.actualizarCliente(documento, nombre, telefono)) objectIoManager.showInfo("Cliente actualizado.\n");
        else objectIoManager.showInfo("Cliente no encontrado.\n");
    }

    private void eliminarClienteUI() {
        objectIoManager.showInfo("\n--- Eliminar Cliente ---\n");
        String documento = objectIoManager.requestInfo("Documento: ");
        if (objectModel.eliminarCliente(documento)) objectIoManager.showInfo("Cliente eliminado.\n");
        else objectIoManager.showInfo("Cliente no encontrado.\n");
    }

    private void registrarConciertoUI() {
        objectIoManager.showInfo("\n--- Registrar Concierto ---\n");
        String codigo = objectIoManager.requestInfo("Codigo: ");
        String nombre = objectIoManager.requestInfo("Nombre: ");
        String fechaStr = objectIoManager.requestInfo("Fecha (yyyy-mm-dd): ");
        double precio = requestDouble("Precio base: ");
        int capacidad = requestInt("Capacidad (1-100): ", 1, 100);
        String menoresStr = objectIoManager.requestInfo("Permite menores? (S/N): ");
        boolean menores = menoresStr.trim().equalsIgnoreCase("S");
        try {
            LocalDate fecha = LocalDate.parse(fechaStr, DateTimeFormatter.ISO_LOCAL_DATE);
            Concierto concierto = new Concierto(codigo, nombre, fecha, precio, capacidad, menores);
            if (objectModel.registrarConcierto(concierto)) objectIoManager.showInfo("Concierto registrado.\n");
            else objectIoManager.showInfo("No se pudo registrar.\n");
        } catch (Exception e) {
            objectIoManager.showInfo("Fecha invalida.\n");
        }
    }

    private void verConciertosUI() {
        objectIoManager.showInfo("\n--- Conciertos Registrados ---\n");
        Concierto[] conciertos = objectModel.getConciertos();
        if (conciertos.length == 0) objectIoManager.showInfo("(ninguno)\n");
        else for (Concierto concierto : conciertos) objectIoManager.showInfo("  - " + concierto + "\n");
    }

    private void venderTicketUI() {
        objectIoManager.showInfo("\n--- Vender Ticket ---\n");
        String documento = objectIoManager.requestInfo("Documento del cliente: ");
        Cliente cliente = objectModel.buscarClientePorDocumento(documento);
        if (cliente == null) { objectIoManager.showInfo("Cliente no existe.\n"); return; }

        String codigo = objectIoManager.requestInfo("Codigo del concierto: ");
        Concierto concierto = objectModel.buscarConcierto(codigo);
        if (concierto == null) { objectIoManager.showInfo("Concierto no existe.\n"); return; }

        char letra = objectIoManager.requestInfo("Seccion (A-D): ").toUpperCase().charAt(0);
        int numero = requestInt("Numero de silla (1-25): ", 1, 25);
        String tipo = objectIoManager.requestInfo("Tipo venta (FISICA/DIGITAL): ").toUpperCase();

        Ticket ticket = objectModel.venderTicket(cliente, concierto, letra, numero, tipo);
        if (ticket != null) {
            objectIoManager.showInfo("Venta exitosa: " + ticket + "\n");
            if (objectModel.alertaAforo()) {
                objectIoManager.showInfo("ALERTA: Aforo supera el 90% (" +
                        String.format("%.2f", objectModel.porcentajeAforo()) + "%)\n");
            }
        } else {
            objectIoManager.showInfo("No se pudo vender (silla ocupada, no existe o restriccion de edad).\n");
        }
    }

    private void consultarSeccionUI() {
        objectIoManager.showInfo("\n--- Consultar Seccion ---\n");
        char letra = objectIoManager.requestInfo("Letra seccion (A-D): ").toUpperCase().charAt(0);
        Seccion seccion = objectModel.getSeccion(letra);
        if (seccion == null) { objectIoManager.showInfo("Seccion no existe.\n"); return; }

        objectIoManager.showInfo("Seccion " + seccion.getLetra() + " - Ocupadas: " +
                seccion.contarOcupadas() + " | Libres: " + seccion.contarLibres() + "\n");
        Silla[] sillas = seccion.getSillas();
        for (int i = 0; i < sillas.length; i++) {
            objectIoManager.showInfo("  " + sillas[i] + "\n");
        }
    }

    private void consultarAforoUI() {
        objectIoManager.showInfo("\n--- Estado de Aforo ---\n");
        int ocupadas = objectModel.totalOcupadas();
        int total = objectModel.totalSillas();
        double porcentaje = objectModel.porcentajeAforo();
        objectIoManager.showInfo("Ocupadas: " + ocupadas + " / " + total + "\n");
        objectIoManager.showInfo("Porcentaje: " + String.format("%.2f", porcentaje) + "%\n");
        if (objectModel.alertaAforo()) objectIoManager.showInfo("ALERTA: supera el 90%\n");
        else objectIoManager.showInfo("Aforo dentro del rango permitido.\n");
    }

    private void menuAlimentos() {
        int opcion;
        do {
            objectIoManager.showInfo("\n============================================================\n");
            objectIoManager.showInfo("  FASE 2 - Alimentos y Bebidas\n");
            objectIoManager.showInfo("============================================================\n");
            objectIoManager.showInfo("  1. Registrar/abastecer producto\n");
            objectIoManager.showInfo("  2. Vender producto\n");
            objectIoManager.showInfo("  3. Consultar stock\n");
            objectIoManager.showInfo("  4. Inventario sugerido\n");
            objectIoManager.showInfo("  0. Volver\n");
            opcion = requestInt("Opcion: ", 0, 4);

            switch (opcion) {
                case 1: registrarProductoUI(); break;
                case 2: venderProductoUI(); break;
                case 3: consultarStockUI(); break;
                case 4: inventarioSugeridoUI(); break;
            }
        } while (opcion != 0);
    }

    private void registrarProductoUI() {
        objectIoManager.showInfo("\n--- Registrar Producto ---\n");
        String nombre = objectIoManager.requestInfo("Nombre del producto: ");
        int cantidad = requestInt("Cantidad a ingresar: ", 1, 10000);
        if (objectModel.registrarProducto(nombre, cantidad)) objectIoManager.showInfo("Producto registrado.\n");
        else objectIoManager.showInfo("No se pudo registrar (limite alcanzado).\n");
    }

    private void venderProductoUI() {
        objectIoManager.showInfo("\n--- Vender Producto ---\n");
        String nombre = objectIoManager.requestInfo("Nombre del producto: ");
        int cantidad = requestInt("Cantidad a vender: ", 1, 10000);
        if (objectModel.venderProducto(nombre, cantidad)) objectIoManager.showInfo("Venta registrada.\n");
        else objectIoManager.showInfo("No se pudo vender (producto inexistente o stock insuficiente).\n");
    }

    private void consultarStockUI() {
        objectIoManager.showInfo("\n--- Consultar Stock ---\n");
        String[] productos = objectModel.getProductos();
        if (productos.length == 0) {
            objectIoManager.showInfo("(sin productos registrados)\n");
            return;
        }
        for (String producto : productos) {
            objectIoManager.showInfo("  - " + producto + " stock: " + objectModel.getStock(producto) + "\n");
        }
    }

    private void inventarioSugeridoUI() {
        objectIoManager.showInfo("\n--- Inventario Sugerido ---\n");
        String nombre = objectIoManager.requestInfo("Producto: ");
        int sugerido = objectModel.calcularInventarioSugerido(nombre, objectModel.porcentajeAforo());
        objectIoManager.showInfo("Cantidad sugerida para reabastecer '" + nombre + "': " + sugerido + " unidades\n");
    }

    private void menuMerchandising() {
        int opcion;
        do {
            objectIoManager.showInfo("\n============================================================\n");
            objectIoManager.showInfo("  FASE 3 - Mercancia Oficial\n");
            objectIoManager.showInfo("============================================================\n");
            objectIoManager.showInfo("  1. Registrar producto de merchandising\n");
            objectIoManager.showInfo("  2. Registrar venta\n");
            objectIoManager.showInfo("  3. Ver productos y ventas\n");
            objectIoManager.showInfo("  4. Total ingresos merchandising\n");
            objectIoManager.showInfo("  0. Volver\n");
            opcion = requestInt("Opcion: ", 0, 4);

            switch (opcion) {
                case 1: registrarMerchUI(); break;
                case 2: venderMerchUI(); break;
                case 3: verMerchUI(); break;
                case 4: totalMerchUI(); break;
            }
        } while (opcion != 0);
    }

    private void registrarMerchUI() {
        objectIoManager.showInfo("\n--- Registrar Producto de Merchandising ---\n");
        String nombre = objectIoManager.requestInfo("Nombre del producto: ");
        double precio = requestDouble("Precio unitario: ");
        if (objectModel.registrarMerch(nombre, precio)) objectIoManager.showInfo("Producto registrado.\n");
        else objectIoManager.showInfo("Limite de productos alcanzado.\n");
    }

    private void venderMerchUI() {
        objectIoManager.showInfo("\n--- Registrar Venta de Merchandising ---\n");
        String nombre = objectIoManager.requestInfo("Producto: ");
        int cantidad = requestInt("Cantidad: ", 1, 10000);
        if (objectModel.venderMerch(nombre, cantidad)) objectIoManager.showInfo("Venta registrada.\n");
        else objectIoManager.showInfo("Producto no encontrado.\n");
    }

    private void verMerchUI() {
        objectIoManager.showInfo("\n--- Productos de Merchandising ---\n");
        String[] productos = objectModel.getMerchProductos();
        if (productos.length == 0) {
            objectIoManager.showInfo("(sin productos registrados)\n");
            return;
        }
        for (int i = 0; i < productos.length; i++) {
            objectIoManager.showInfo("  - " + productos[i] +
                    " | Precio: $" + String.format("%.2f", objectModel.getMerchPrecio(i)) +
                    " | Vendidos: " + objectModel.getMerchVendidos(i) + "\n");
        }
    }

    private void totalMerchUI() {
        objectIoManager.showInfo("\n--- Total Ingresos Merchandising ---\n");
        objectIoManager.showInfo("Total: $" + String.format("%.2f", objectModel.totalIngresosMerch()) + "\n");
    }

    private void menuFlujoCaja() {
        int opcion;
        do {
            objectIoManager.showInfo("\n============================================================\n");
            objectIoManager.showInfo("  FASE 4 - Flujo de Caja\n");
            objectIoManager.showInfo("============================================================\n");
            objectIoManager.showInfo("  1. Registrar costo de alquiler\n");
            objectIoManager.showInfo("  2. Registrar costo de personal\n");
            objectIoManager.showInfo("  3. Registrar costo de logistica\n");
            objectIoManager.showInfo("  4. Ver costos y total\n");
            objectIoManager.showInfo("  0. Volver\n");
            opcion = requestInt("Opcion: ", 0, 4);

            switch (opcion) {
                case 1: costoAlquilerUI(); break;
                case 2: costoPersonalUI(); break;
                case 3: costoLogisticaUI(); break;
                case 4: verCostosUI(); break;
            }
        } while (opcion != 0);
    }

    private void costoAlquilerUI() {
        objectIoManager.showInfo("\n--- Costo de Alquiler ---\n");
        double valor = requestDouble("Valor: ");
        objectModel.setCostoAlquiler(valor);
        objectIoManager.showInfo("Costo de alquiler registrado.\n");
    }

    private void costoPersonalUI() {
        objectIoManager.showInfo("\n--- Costo de Personal ---\n");
        double valor = requestDouble("Valor: ");
        objectModel.setCostoPersonal(valor);
        objectIoManager.showInfo("Costo de personal registrado.\n");
    }

    private void costoLogisticaUI() {
        objectIoManager.showInfo("\n--- Costo de Logistica ---\n");
        double valor = requestDouble("Valor: ");
        objectModel.setCostoLogistica(valor);
        objectIoManager.showInfo("Costo de logistica registrado.\n");
    }

    private void verCostosUI() {
        objectIoManager.showInfo("\n--- Costos Operativos ---\n");
        objectIoManager.showInfo("Alquiler:   $" + String.format("%.2f", objectModel.getCostoAlquiler()) + "\n");
        objectIoManager.showInfo("Personal:   $" + String.format("%.2f", objectModel.getCostoPersonal()) + "\n");
        objectIoManager.showInfo("Logistica:  $" + String.format("%.2f", objectModel.getCostoLogistica()) + "\n");
        objectIoManager.showInfo("-------------------------------\n");
        objectIoManager.showInfo("TOTAL:      $" + String.format("%.2f", objectModel.totalCostos()) + "\n");
    }

    private void menuPersonal() {
        int opcion;
        do {
            objectIoManager.showInfo("\n============================================================\n");
            objectIoManager.showInfo("  FASE 5 - Operacion de Personal\n");
            objectIoManager.showInfo("============================================================\n");
            objectIoManager.showInfo("  1. Registrar personal\n");
            objectIoManager.showInfo("  2. Ver personal y turnos\n");
            objectIoManager.showInfo("  0. Volver\n");
            opcion = requestInt("Opcion: ", 0, 2);

            switch (opcion) {
                case 1: registrarPersonalUI(); break;
                case 2: verPersonalUI(); break;
            }
        } while (opcion != 0);
    }

    private void registrarPersonalUI() {
        objectIoManager.showInfo("\n--- Registrar Personal ---\n");
        String nombre = objectIoManager.requestInfo("Nombre: ");
        String rol = objectIoManager.requestInfo("Rol (CAJERO/BARISTA/LOGISTICA): ").toUpperCase();
        String turno = objectIoManager.requestInfo("Turno (MANANA/TARDE/NOCHE): ").toUpperCase();
        if (objectModel.registrarPersonal(nombre, rol, turno)) objectIoManager.showInfo("Personal registrado.\n");
        else objectIoManager.showInfo("Limite de personal alcanzado.\n");
    }

    private void verPersonalUI() {
        objectIoManager.showInfo("\n--- Personal Registrado ---\n");
        String[] nombres = objectModel.getPersonalNombre();
        if (nombres.length == 0) {
            objectIoManager.showInfo("(sin personal registrado)\n");
            return;
        }
        String[] roles = objectModel.getPersonalRol();
        String[] turnos = objectModel.getPersonalTurno();
        for (int i = 0; i < nombres.length; i++) {
            objectIoManager.showInfo("  - " + nombres[i] +
                    " | Rol: " + roles[i] +
                    " | Turno: " + turnos[i] + "\n");
        }
    }

    private void generarReporte() {
        objectIoManager.showInfo("\n============================================================\n");
        objectIoManager.showInfo("  REPORTE CONSOLIDADO DE LA JORNADA\n");
        objectIoManager.showInfo("============================================================\n");

        double ingresoBoleteria = objectModel.totalIngresosBoleteria();
        int ticketsVendidos = objectModel.getTickets().length;
        objectIoManager.showInfo("FASE 1 - Boleteria\n");
        objectIoManager.showInfo("   Tickets vendidos: " + ticketsVendidos + "\n");
        objectIoManager.showInfo("   Ingresos boleteria: $" + String.format("%.2f", ingresoBoleteria) + "\n");
        objectIoManager.showInfo("   Aforo: " + String.format("%.2f", objectModel.porcentajeAforo()) + "%\n\n");

        objectIoManager.showInfo("FASE 2 - Alimentos y Bebidas\n");
        String[] productos = objectModel.getProductos();
        if (productos.length == 0) objectIoManager.showInfo("   (sin productos)\n");
        else for (String producto : productos) {
            objectIoManager.showInfo("   - " + producto + " stock final: " + objectModel.getStock(producto) + "\n");
        }
        objectIoManager.showInfo("\n");

        objectIoManager.showInfo("FASE 3 - Merchandising\n");
        objectIoManager.showInfo("   Ingresos totales: $" + String.format("%.2f", objectModel.totalIngresosMerch()) + "\n\n");

        objectIoManager.showInfo("FASE 4 - Flujo de Caja\n");
        objectIoManager.showInfo("   Costos totales: $" + String.format("%.2f", objectModel.totalCostos()) + "\n\n");

        objectIoManager.showInfo("FASE 5 - Personal\n");
        objectIoManager.showInfo("   Total registrados: " + objectModel.getPersonalNombre().length + "\n\n");

        double ingresosTotales = ingresoBoleteria + objectModel.totalIngresosMerch();
        double balance = ingresosTotales - objectModel.totalCostos();

        objectIoManager.showInfo("============================================================\n");
        objectIoManager.showInfo("INGRESOS TOTALES:  $" + String.format("%.2f", ingresosTotales) + "\n");
        objectIoManager.showInfo("COSTOS TOTALES:    $" + String.format("%.2f", objectModel.totalCostos()) + "\n");
        objectIoManager.showInfo("BALANCE NETO:      $" + String.format("%.2f", balance) + "\n");
        objectIoManager.showInfo("============================================================\n");

        if (balance >= 0) objectIoManager.showInfo("La jornada genero GANANCIA.\n");
        else objectIoManager.showInfo("La jornada genero PERDIDA.\n");
    }

    private int requestInt(String message, int min, int max) {
        int valor;
        while (true) {
            try {
                valor = Integer.parseInt(objectIoManager.requestInfo(message).trim());
                if (valor >= min && valor <= max) return valor;
                objectIoManager.showInfo("Valor fuera de rango [" + min + " - " + max + "]. Intente de nuevo.\n");
            } catch (NumberFormatException e) {
                objectIoManager.showInfo("Debe ingresar un numero entero valido.\n");
            }
        }
    }

    private double requestDouble(String message) {
        double valor;
        while (true) {
            try {
                valor = Double.parseDouble(objectIoManager.requestInfo(message).trim());
                if (valor > 0) return valor;
                objectIoManager.showInfo("El valor debe ser estrictamente positivo.\n");
            } catch (NumberFormatException e) {
                objectIoManager.showInfo("Debe ingresar un numero decimal valido.\n");
            }
        }
    }
}