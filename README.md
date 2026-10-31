``` # README.md — SIGOC (todo en un bloque para copiar y pegar)

```markdown
# SIGOC — Sistema de Gestión y Reporte de Operación Comercial de Conciertos

> **Proyecto Final — Algoritmos y Programación I**
> Autores: Alejandro Mendieta · Julian Andrés Mesa Díaz
> Lenguaje: Java (consola) · Arquitectura: MVP (Modelo–Vista–Presentador)
> Fecha: 31 – 10 – 2026

---

## 1. Descripción general

**SIGOC** es un sistema de gestión y reporte desarrollado en Java que funciona por consola y organiza la operación comercial de un concierto en **cinco fases operativas**. Al finalizar la jornada, genera un **reporte consolidado** con totales por fase y total general del evento.

El problema que resuelve: la gestión comercial de conciertos en Colombia aún se apoya en hojas de cálculo, cuadernos y canales informales, lo que impide una visión integrada y obliga a decidir con datos incompletos (Sommerville, 2011; Deitel y Deitel, 2018).

### Las cinco fases operativas

| # | Fase | Propósito |
|---|------|-----------|
| 1 | Preventa y Taquilla | Entidades base: clientes, conciertos, zonas y tickets |
| 2 | Alimentos y Bebidas | Inventario, stock y reabastecimiento sugerido |
| 3 | Mercancía Oficial | Ventas de merchandising por punto de venta |
| 4 | Flujo de Caja | Movimientos locales (offline) y validación de montos |
| 5 | Operación de Personal | Roles, turnos y asignación por zona |

---

## 2. Objetivos

### Objetivo general
Diseñar y codificar en Java un sistema de gestión y reporte que permita administrar la información operativa y comercial de un concierto, organizada en cinco fases.

### Objetivos específicos
1. Implementar un **menú principal** en consola con `switch` para navegar entre las cinco fases y el reporte final.
2. Gestionar las entidades base (Cliente, Concierto, Zona, Ticket) con **registro, consulta, búsqueda, actualización y eliminación**, usando arreglos y validación de entradas.
3. Calcular indicadores por fase: precio final del ticket, aforo vendido, inventario disponible, ingresos por punto de venta y turnos asignados.
4. Generar un **reporte consolidado** al finalizar la jornada.
5. Organizar el recinto en secciones con **sillas VIP y Generales**, calculando el precio final a partir del precio base + adicional de zona.

---

## 3. Arquitectura MVP (Modelo – Vista – Presentador)

SIGOC adopta el patrón **MVP**, que separa la lógica de negocio, la interfaz de consola y la coordinación entre ambas. Esto facilita las pruebas de escritorio, la reutilización y el mantenimiento.

```
┌──────────────────────────────────────────────────────────────┐
│                          RUNNER (main)                       │
│                     instancia el Presenter                   │
└───────────────────────────────┬──────────────────────────────┘
                                │
        ┌───────────────────────▼───────────────────────┐
        │                  PRESENTADOR                  │
        │  (Present.java / coordenadas de cada fase)    │
        │  - Orquesta menús y submenús (switch)         │
        │  - Llama al Modelo para cálculos y datos      │
        │  - Solicita/entrega datos a la Vista          │
        └───────┬───────────────────────────────┬───────┘
                │                               │
     ┌──────────▼──────────┐         ┌──────────▼──────────┐
     │       MODELO        │         │        VISTA        │
     │  Cliente, Concierto │         │  IoManager          │
     │  Zona, Ticket,      │         │  - requestInfo()    │
     │  Silla, Sección,    │         │  - showInfo()       │
     │  SIGOC (lógica)     │         │  (solo consola)     │
     └─────────────────────┘         └─────────────────────┘
```

### Responsabilidades por capa

| Capa | Clases | Responsabilidad | Regla clave |
|------|--------|-----------------|-------------|
| **Modelo** | `Cliente`, `Concierto`, `Zona`, `Ticket`, `Silla`, `Seccion`, `SIGOCModel` | Datos, reglas de negocio, cálculos (precio final, edad, aforo, inventario, reporte) | No imprime ni lee consola |
| **Vista** | `IoManager` | Entrada/salida por consola (`Scanner`, `System.out`) | No contiene lógica de negocio |
| **Presentador** | `Present` | Orquesta menús y submenús; media entre Modelo y Vista | No calcula; solo coordina |

> **Regla de oro:** el Modelo nunca debe llamar a `IoManager` ni a `Scanner`, y la Vista nunca debe contener reglas de negocio. Toda la "inteligencia" vive en el Modelo; toda la interacción con el usuario pasa por el Presentador.

---

## 4. Estructura del proyecto

```
SIGOC/
├── Runner.java                # Punto de entrada (main)
├── IoManager.java             # VISTA: entrada/salida por consola
├── Present.java               # PRESENTADOR: menús, submenús y coordinación
└── model/                     # MODELO
    ├── Cliente.java
    ├── Concierto.java
    ├── Zona.java
    ├── Ticket.java
    ├── Silla.java
    ├── Seccion.java
    └── SIGOCModel.java
```

> Nota: para la entrega académica se dispone también de una **versión en un único archivo** (`SIGOC.java`) con todas las clases para copiar y pegar, manteniendo la misma separación lógica por capas MVP.

---

## 5. Clases del Modelo

| Clase | Atributos principales | Métodos clave |
|-------|----------------------|---------------|
| `Cliente` | documento, nombre, teléfono, fecha de nacimiento | `calcularEdad()`, `enmascararTelefono()` |
| `Concierto` | código, nombre, fecha, precio base, capacidad, permiteMenores | `getPrecioBase()` |
| `Zona` | nombre, precio adicional | `getPrecioAdicional()` |
| `Ticket` | id, cliente, concierto, silla, precio final, tipo de venta | — |
| `Silla` | código, tipo (`VIP` / `GENERAL`), ticket asociado | `estaOcupada()`, `ocupar()`, `liberar()` |
| `Seccion` | letra, arreglo de 25 sillas | `consultarSeccion()`, `contarOcupadas()` |
| `SIGOCModel` | arreglos de clientes, conciertos, zonas, tickets; matriz del recinto | `calcularPrecioFinal()`, `verificarAforo()`, `calcularInventarioSugerido()`, `generarReporte()` |

### Detalle del recinto
- Secciones identificadas con **letras** (A, B, C, D...).
- Cada sección tiene **25 sillas numeradas**.
- Las **primeras 5** sillas de cada sección son **VIP** (restricción +18).
- Las **20 restantes** son **Generales**.
- La matriz del recinto se modela como `int[][]` (filas = secciones, columnas = sillas) donde cada posición guarda el id del ticket o `null`/`0` si está libre.

---

## 6. Métodos propios del sistema

| Método | Descripción | Estructura usada |
|--------|-------------|------------------|
| `calcularPrecioFinal(precioBase, adicionalZona)` | Suma precio base + adicional de zona | Suma |
| `calcularEdad(fechaNacimiento)` | Edad del cliente con validación para VIP | `if-else`, fechas (`Period`) |
| `enmascararTelefono(telefono)` | Muestra solo los últimos 4 dígitos (`******3456`) | `substring`, `StringBuilder` |
| `consultarSeccion(seccion)` | Recorre sillas, muestra ocupadas, vacías y detalle | `for`, `if-else` |
| `verificarAforo()` | Porcentaje de ocupación + alerta al 90% | `if-else if-else` |
| `calcularInventarioSugerido(aforoVendido)` | Aplica porcentajes para sugerir cantidades | Acumuladores |
| `generarReporte()` | Recorre las 5 fases y consolida totales | Ciclos anidados |

---

## 7. Requerimientos funcionales

| Código | Requerimiento |
|--------|---------------|
| **RF1** | Menú principal con `switch`: módulos operativos, reporte consolidado y salir |
| **RF2** | Registro de conciertos (código, nombre, precio base, capacidad) e indicador de permitir menores |
| **RF3** | CRUD de clientes, validación de edad y teléfono enmascarado (`******3456`) |
| **RF4** | Escenario en secciones; menores solo en sillas Generales (VIP restringido) |
| **RF5** | Venta de boletas validando disponibilidad y calculando precio final |
| **RF6** | Aforo acumulado + alerta automática al superar el **90%** de capacidad |
| **RF7** | Inventario de alimentos/bebidas por proveedor, producto y cantidad; inventario sugerido |
| **RF8** | Ventas de merchandising (tipo, precio unitario, cantidad, punto de venta) |
| **RF9** | Registro de personal y programación de turnos por rol y zona |
| **RF10** | Operación **100% offline** + validación de montos estrictamente positivos |
| **RF11** | Consolidación financiera: ingresos por boletería, alimentos, bebidas y merchandising vs. costos |

---

## 8. Validaciones implementadas

- Rangos de precios y montos **positivos**.
- Números de silla entre **1 y 25**.
- Formato de documento.
- **Edad mínima 18** para zonas VIP.
- **Disponibilidad de silla** antes de vender.
- **Restricción de menores** según el concierto.
- **Conectividad activa** antes de transacciones electrónicas (modo simulado).

Implementadas con ciclos `while` y condicionales `if-else`.

---

## 9. Cómo ejecutar

### Requisitos
- **JDK 8 o superior** (`java -version` para verificar).

### Compilación y ejecución (versión por archivos)

```bash
# Desde la raíz del proyecto
javac Runner.java IoManager.java Present.java model/*.java
java Runner
```

### 10. Compilación y ejecución (versión en un solo archivo)

```bash
javac SIGOC.java
java SIGOC
```

> Si usas un IDE (IntelliJ, Eclipse, NetBeans), basta con abrir el proyecto y ejecutar la clase `Runner` (o `SIGOC` en la versión unificada).

---


## 11. Flujo de trabajo (metodología)

| Fase | Actividades |
|------|-------------|
| **Análisis** | Caracterización de especificaciones; entradas, procesos y salidas por fase |
| **Diseño** | Clases, atributos, métodos y menús de la arquitectura MVP |
| **Codificación** | Implementación en Java por consola |
| **Pruebas** | Pruebas de escritorio con datos simulados; corrección y optimización |
| **Documentación** | Informe final, material visual y video de sustentación |

### 11. Cronograma (resumen)

| Actividad | Responsable | Inicio | Fin |
|-----------|-------------|--------|-----|
| Análisis de la problemática | Justin A. Mendieta / Julian A. Mesa | 05/10/2026 | 08/10/2026 |
| Diseño de la solución | Justin A. Mendieta / Julian A. Mesa | 09/10/2026 | 13/10/2026 |
| Codificación fase de taquilla | Justin A. Mendieta | 14/10/2026 | 18/10/2026 |
| Búsqueda y cálculo de edad | Justin A. Mendieta | 19/10/2026 | 21/10/2026 |
| Alimentos y bebidas | Julian A. Mesa | 22/10/2026 | 26/10/2026 |
| Inventario sugerido | Julian A. Mesa | 27/10/2026 | 28/10/2026 |
| Merchandising | Justin A. Mendieta | 29/10/2026 | 31/10/2026 |
| Flujo de caja | Julian A. Mesa | 01/11/2026 | 03/11/2026 |
| Operación de personal | Justin A. Mendieta | 04/11/2026 | 06/11/2026 |
| Integración y alerta de aforo | Ambos | 07/11/2026 | 09/11/2026 |
| Reporte consolidado | Ambos | 10/11/2026 | 11/11/2026 |
| Pruebas de escritorio y reales | Ambos | 12/11/2026 | 14/11/2026 |
| Documentación, diapositivas y video | Ambos | 15/11/2026 | 18/11/2026 |

---

## 12. Diagrama de flujo

> Versión preliminar disponible en:
> [Google Drive — Diagrama de flujo SIGOC](https://drive.google.com/drive/folders/17ienDbjxj9VzCF-y2rCXhmK1HhjOniph?usp=sharing)

---

## 13. Referencias bibliográficas

- Barquilla Galeano, I. M. (2025). *Java programación: Teoría y ejemplos*. RA-MA Editorial.
- Ceballos Sierra, F. J. (2015). *Java 2: Curso de programación* (4.ª ed.). RA-MA Editorial.
- Deitel, P., y Deitel, H. (2018). *Java: How to program* (11.ª ed.). Pearson.
- García García-Doncel, J., y Gómez Delgado, J. (2026). *Programación en Java: Construye aplicaciones robustas y multiplataforma*. ESIC Editorial.
- Joyanes Aguilar, L. (2013). *Fundamentos de programación: Algoritmos, estructuras de datos y objetos* (5.ª ed.). McGraw-Hill.
- McMullen, K., Matthews, E., y Parsons, J. J. (2023). *Readings from programming with Java*. Cengage.
- Savitch, W. (2012). *Java: An introduction to problem solving & programming* (2.ª ed.). Pearson.
- Sommerville, I. (2011). *Ingeniería de software* (9.ª ed.). Pearson.

---

## 14. Estado del proyecto

| Componente | Estado |
|------------|--------|
| Arquitectura MVP definida | ✅ |
| Vista (`IoManager`) | ✅ |
| Punto de entrada (`Runner`) | ✅ |
| Modelo (`Cliente`, `Concierto`, `Zona`, `Ticket`, `Silla`, `Seccion`, `SIGOCModel`) | pendiente |
| Presentador (`Present`) | pendiente |
| Reporte consolidado | ✅ |
| Pruebas de escritorio | Pendiente |

---

## 16. Licencia y uso académico

Proyecto desarrollado con fines **exclusivamente académicos** para la asignatura *Algoritmos y Programación I*. Los autores autorizan su uso como referencia educativa citando la fuente.

---

> **SIGOC** — *"Detrás del espectáculo, la coordinación también se programa."*
```

---
```