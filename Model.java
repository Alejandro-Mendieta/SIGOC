/* Autor : Alejandro Mendieta - Julian Andres Mesa
Date : 31 - 10 - 2026
Description : SIGOC - Modelo MVP
 */

import java.time.LocalDate;
import java.time.Period;

class Model {

    private Cliente[] clientes;
    private int numClientes;

    private Concierto[] conciertos;
    private int numConciertos;

    private Zona[] zonas;
    private int numZonas;

    private Ticket[] tickets;
    private int numTickets;

    private Seccion[] secciones;
    private static final int NUM_SECCIONES = 4;
    private static final int SILLAS_POR_SECCION = 25;

    private String[] productos;
    private int[] stockProductos;
    private int numProductos;

    private String[] merchProductos;
    private double[] merchPrecios;
    private int[] merchVendidos;
    private int numMerch;

    private String[] personalNombre;
    private String[] personalRol;
    private String[] personalTurno;
    private int numPersonal;

    private double costoAlquiler;
    private double costoPersonal;
    private double costoLogistica;

    private int contadorTicket;

    public Model() {
        clientes = new Cliente[100];
        numClientes = 0;

        conciertos = new Concierto[20];
        numConciertos = 0;

        zonas = new Zona[5];
        numZonas = 0;
        zonas[numZonas++] = new Zona("VIP", 150000.0);
        zonas[numZonas++] = new Zona("GENERAL", 50000.0);

        tickets = new Ticket[500];
        numTickets = 0;

        secciones = new Seccion[NUM_SECCIONES];
        for (int i = 0; i < NUM_SECCIONES; i++) {
            secciones[i] = new Seccion((char) ('A' + i));
        }

        productos = new String[20];
        stockProductos = new int[20];
        numProductos = 0;

        merchProductos = new String[20];
        merchPrecios = new double[20];
        merchVendidos = new int[20];
        numMerch = 0;

        personalNombre = new String[50];
        personalRol = new String[50];
        personalTurno = new String[50];
        numPersonal = 0;

        costoAlquiler = 0;
        costoPersonal = 0;
        costoLogistica = 0;

        contadorTicket = 1;
    }

    public boolean registrarCliente(Cliente cliente) {
        if (numClientes >= clientes.length) return false;
        if (buscarClientePorDocumento(cliente.getDocumento()) != null) return false;
        clientes[numClientes] = cliente;
        numClientes++;
        return true;
    }

    public Cliente buscarClientePorDocumento(String documento) {
        for (int i = 0; i < numClientes; i++) {
            if (clientes[i].getDocumento().equalsIgnoreCase(documento)) return clientes[i];
        }
        return null;
    }

    public Cliente[] buscarClientePorNombre(String nombreParcial) {
        Cliente[] encontrados = new Cliente[numClientes];
        int n = 0;
        for (int i = 0; i < numClientes; i++) {
            if (clientes[i].getNombre().toLowerCase().contains(nombreParcial.toLowerCase())) {
                encontrados[n] = clientes[i];
                n++;
            }
        }
        Cliente[] resultado = new Cliente[n];
        for (int i = 0; i < n; i++) resultado[i] = encontrados[i];
        return resultado;
    }

    public boolean actualizarCliente(String documento, String nuevoNombre, String nuevoTelefono) {
        Cliente cliente = buscarClientePorDocumento(documento);
        if (cliente == null) return false;
        if (nuevoNombre != null && !nuevoNombre.isEmpty()) cliente.setNombre(nuevoNombre);
        if (nuevoTelefono != null && !nuevoTelefono.isEmpty()) cliente.setTelefono(nuevoTelefono);
        return true;
    }

    public boolean eliminarCliente(String documento) {
        for (int i = 0; i < numClientes; i++) {
            if (clientes[i].getDocumento().equalsIgnoreCase(documento)) {
                for (int j = i; j < numClientes - 1; j++) clientes[j] = clientes[j + 1];
                clientes[numClientes - 1] = null;
                numClientes--;
                return true;
            }
        }
        return false;
    }

    public Cliente[] getClientes() {
        Cliente[] resultado = new Cliente[numClientes];
        for (int i = 0; i < numClientes; i++) resultado[i] = clientes[i];
        return resultado;
    }

    public boolean registrarConcierto(Concierto concierto) {
        if (numConciertos >= conciertos.length) return false;
        conciertos[numConciertos] = concierto;
        numConciertos++;
        return true;
    }

    public Concierto buscarConcierto(String codigo) {
        for (int i = 0; i < numConciertos; i++) {
            if (conciertos[i].getCodigo().equalsIgnoreCase(codigo)) return conciertos[i];
        }
        return null;
    }

    public Concierto[] getConciertos() {
        Concierto[] resultado = new Concierto[numConciertos];
        for (int i = 0; i < numConciertos; i++) resultado[i] = conciertos[i];
        return resultado;
    }

    public Zona getZona(String nombre) {
        for (int i = 0; i < numZonas; i++) {
            if (zonas[i].getNombre().equalsIgnoreCase(nombre)) return zonas[i];
        }
        return null;
    }

    public Zona[] getZonas() {
        Zona[] resultado = new Zona[numZonas];
        for (int i = 0; i < numZonas; i++) resultado[i] = zonas[i];
        return resultado;
    }

    public double calcularPrecioFinal(Concierto concierto, Zona zona) {
        return concierto.getPrecioBase() + zona.getPrecioAdicional();
    }

    public Seccion[] getSecciones() {
        return secciones;
    }

    public Seccion getSeccion(char letra) {
        for (int i = 0; i < secciones.length; i++) {
            if (secciones[i].getLetra() == letra) return secciones[i];
        }
        return null;
    }

    public int totalSillas() {
        return NUM_SECCIONES * SILLAS_POR_SECCION;
    }

    public int totalOcupadas() {
        int total = 0;
        for (int i = 0; i < secciones.length; i++) total += secciones[i].contarOcupadas();
        return total;
    }

    public Ticket venderTicket(Cliente cliente, Concierto concierto, char letraSeccion, int numeroSilla, String tipoVenta) {
        Seccion seccion = getSeccion(letraSeccion);
        if (seccion == null) return null;

        Silla silla = seccion.getSilla(numeroSilla);
        if (silla == null || silla.estaOcupada()) return null;

        if (silla.getTipo().equals("VIP") && cliente.calcularEdad() < 18) return null;
        if (!concierto.isPermiteMenores() && cliente.calcularEdad() < 18) return null;

        Zona zona = getZona(silla.getTipo());
        double precio = calcularPrecioFinal(concierto, zona);

        Ticket ticket = new Ticket(contadorTicket, cliente, concierto, silla, precio, tipoVenta);
        contadorTicket++;
        silla.ocupar(ticket);
        tickets[numTickets] = ticket;
        numTickets++;
        return ticket;
    }

    public Ticket[] getTickets() {
        Ticket[] resultado = new Ticket[numTickets];
        for (int i = 0; i < numTickets; i++) resultado[i] = tickets[i];
        return resultado;
    }

    public double porcentajeAforo() {
        return (double) totalOcupadas() / totalSillas() * 100.0;
    }

    public boolean alertaAforo() {
        return porcentajeAforo() > 90.0;
    }

    public boolean registrarProducto(String nombre, int stockInicial) {
        if (numProductos >= productos.length) return false;
        for (int i = 0; i < numProductos; i++) {
            if (productos[i].equalsIgnoreCase(nombre)) {
                stockProductos[i] += stockInicial;
                return true;
            }
        }
        productos[numProductos] = nombre;
        stockProductos[numProductos] = stockInicial;
        numProductos++;
        return true;
    }

    public boolean venderProducto(String nombre, int cantidad) {
        for (int i = 0; i < numProductos; i++) {
            if (productos[i].equalsIgnoreCase(nombre)) {
                if (stockProductos[i] >= cantidad) {
                    stockProductos[i] -= cantidad;
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public int getStock(String nombre) {
        for (int i = 0; i < numProductos; i++) {
            if (productos[i].equalsIgnoreCase(nombre)) return stockProductos[i];
        }
        return -1;
    }

    public String[] getProductos() {
        String[] resultado = new String[numProductos];
        for (int i = 0; i < numProductos; i++) resultado[i] = productos[i];
        return resultado;
    }

    public int calcularInventarioSugerido(String producto, double porcentaje) {
        int asistentes = totalOcupadas();
        double factor = 0.20;
        if (producto.toLowerCase().contains("cerveza")) factor = 0.25;
        else if (producto.toLowerCase().contains("agua")) factor = 0.30;
        else if (producto.toLowerCase().contains("hamburguesa")) factor = 0.10;
        return (int) Math.ceil(asistentes * factor * (porcentaje / 100.0)) + 10;
    }

    public boolean registrarMerch(String nombre, double precio) {
        if (numMerch >= merchProductos.length) return false;
        merchProductos[numMerch] = nombre;
        merchPrecios[numMerch] = precio;
        merchVendidos[numMerch] = 0;
        numMerch++;
        return true;
    }

    public boolean venderMerch(String nombre, int cantidad) {
        for (int i = 0; i < numMerch; i++) {
            if (merchProductos[i].equalsIgnoreCase(nombre)) {
                merchVendidos[i] += cantidad;
                return true;
            }
        }
        return false;
    }

    public String[] getMerchProductos() {
        String[] resultado = new String[numMerch];
        for (int i = 0; i < numMerch; i++) resultado[i] = merchProductos[i];
        return resultado;
    }

    public double getMerchPrecio(int indice) {
        return merchPrecios[indice];
    }

    public int getMerchVendidos(int indice) {
        return merchVendidos[indice];
    }

    public double totalIngresosMerch() {
        double total = 0;
        for (int i = 0; i < numMerch; i++) total += merchPrecios[i] * merchVendidos[i];
        return total;
    }

    public boolean registrarPersonal(String nombre, String rol, String turno) {
        if (numPersonal >= personalNombre.length) return false;
        personalNombre[numPersonal] = nombre;
        personalRol[numPersonal] = rol;
        personalTurno[numPersonal] = turno;
        numPersonal++;
        return true;
    }

    public String[] getPersonalNombre() {
        String[] resultado = new String[numPersonal];
        for (int i = 0; i < numPersonal; i++) resultado[i] = personalNombre[i];
        return resultado;
    }

    public String[] getPersonalRol() {
        String[] resultado = new String[numPersonal];
        for (int i = 0; i < numPersonal; i++) resultado[i] = personalRol[i];
        return resultado;
    }

    public String[] getPersonalTurno() {
        String[] resultado = new String[numPersonal];
        for (int i = 0; i < numPersonal; i++) resultado[i] = personalTurno[i];
        return resultado;
    }

    public void setCostoAlquiler(double valor) {
        costoAlquiler = valor;
    }

    public void setCostoPersonal(double valor) {
        costoPersonal = valor;
    }

    public void setCostoLogistica(double valor) {
        costoLogistica = valor;
    }

    public double getCostoAlquiler() {
        return costoAlquiler;
    }

    public double getCostoPersonal() {
        return costoPersonal;
    }

    public double getCostoLogistica() {
        return costoLogistica;
    }

    public double totalCostos() {
        return costoAlquiler + costoPersonal + costoLogistica;
    }

    public double totalIngresosBoleteria() {
        double total = 0;
        for (int i = 0; i < numTickets; i++) total += tickets[i].getPrecioFinal();
        return total;
    }
}

class Cliente {

    private String documento;
    private String nombre;
    private String telefono;
    private LocalDate fechaNacimiento;

    public Cliente(String documento, String nombre, String telefono, LocalDate fechaNacimiento) {
        this.documento = documento;
        this.nombre = nombre;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int calcularEdad() {
        if (fechaNacimiento == null) return -1;
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    public String enmascararTelefono() {
        if (telefono == null || telefono.length() <= 4) return "****";
        String ultimos4 = telefono.substring(telefono.length() - 4);
        String mascara = "";
        for (int i = 0; i < telefono.length() - 4; i++) mascara = mascara + "*";
        return mascara + ultimos4;
    }

    @Override
    public String toString() {
        return "Documento: " + documento +
                " | Nombre: " + nombre +
                " | Telefono: " + enmascararTelefono() +
                " | Edad: " + calcularEdad();
    }
}

class Concierto {

    private String codigo;
    private String nombre;
    private LocalDate fecha;
    private double precioBase;
    private int capacidad;
    private boolean permiteMenores;

    public Concierto(String codigo, String nombre, LocalDate fecha, double precioBase, int capacidad, boolean permiteMenores) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.fecha = fecha;
        this.precioBase = precioBase;
        this.capacidad = capacidad;
        this.permiteMenores = permiteMenores;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public boolean isPermiteMenores() {
        return permiteMenores;
    }

    @Override
    public String toString() {
        return "Codigo: " + codigo +
                " | Nombre: " + nombre +
                " | Fecha: " + fecha +
                " | Precio base: " + precioBase +
                " | Capacidad: " + capacidad +
                " | Permite menores: " + permiteMenores;
    }
}

class Zona {

    private String nombre;
    private double precioAdicional;

    public Zona(String nombre, double precioAdicional) {
        this.nombre = nombre;
        this.precioAdicional = precioAdicional;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecioAdicional() {
        return precioAdicional;
    }

    @Override
    public String toString() {
        return "Zona: " + nombre + " | Adicional: " + precioAdicional;
    }
}

class Ticket {

    private int id;
    private Cliente cliente;
    private Concierto concierto;
    private Silla silla;
    private double precioFinal;
    private String tipoVenta;

    public Ticket(int id, Cliente cliente, Concierto concierto, Silla silla, double precioFinal, String tipoVenta) {
        this.id = id;
        this.cliente = cliente;
        this.concierto = concierto;
        this.silla = silla;
        this.precioFinal = precioFinal;
        this.tipoVenta = tipoVenta;
    }

    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Concierto getConcierto() {
        return concierto;
    }

    public Silla getSilla() {
        return silla;
    }

    public double getPrecioFinal() {
        return precioFinal;
    }

    public String getTipoVenta() {
        return tipoVenta;
    }

    @Override
    public String toString() {
        return "Ticket #" + id +
                " | Cliente: " + cliente.getNombre() +
                " | Silla: " + silla.getCodigo() +
                " | Precio: $" + String.format("%.2f", precioFinal) +
                " | Venta: " + tipoVenta;
    }
}

class Silla {

    private String codigo;
    private String tipo;
    private Ticket ticket;

    public Silla(String codigo, String tipo) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.ticket = null;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getTipo() {
        return tipo;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public boolean estaOcupada() {
        return ticket != null;
    }

    public void ocupar(Ticket ticket) {
        this.ticket = ticket;
    }

    public void liberar() {
        this.ticket = null;
    }

    @Override
    public String toString() {
        return codigo + " [" + tipo + "] " + (estaOcupada() ? "OCUPADA" : "LIBRE");
    }
}

class Seccion {

    private char letra;
    private Silla[] sillas;

    public Seccion(char letra) {
        this.letra = letra;
        this.sillas = new Silla[25];
        for (int i = 0; i < 25; i++) {
            String codigo = String.format("%c-%02d", letra, i + 1);
            String tipo = (i < 5) ? "VIP" : "GENERAL";
            sillas[i] = new Silla(codigo, tipo);
        }
    }

    public char getLetra() {
        return letra;
    }

    public Silla[] getSillas() {
        return sillas;
    }

    public Silla getSilla(int numero) {
        if (numero < 1 || numero > 25) return null;
        return sillas[numero - 1];
    }

    public int contarOcupadas() {
        int total = 0;
        for (int i = 0; i < sillas.length; i++) {
            if (sillas[i].estaOcupada()) total++;
        }
        return total;
    }

    public int contarLibres() {
        return 25 - contarOcupadas();
    }
}