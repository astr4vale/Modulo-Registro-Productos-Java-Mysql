
package com.irissoft.capaNegocios;

import com.irissoft.capaAccesoDatos.QProductos;
import com.irissoft.datos.DtProductos;
import com.irissoft.repositorios.RpProductos;
import java.math.BigDecimal;
import java.util.UUID;

public class NgProductos {

    private final RpProductos rpProductos;

    public NgProductos() {
        rpProductos = new QProductos();
    }

    public String validarDatosProducto(String nombre, String cantidadStr, String precioUnitarioStr) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre del producto es requerido";
        }

        try {
            int cantidad = Integer.parseInt(cantidadStr);
            if (cantidad <= 0) {
                return "La cantidad debe ser mayor a cero";
            }

            BigDecimal precioUnitario = new BigDecimal(precioUnitarioStr);
            if (precioUnitario.compareTo(BigDecimal.ZERO) <= 0) {
                return "El precio unitario debe ser mayor a cero";
            }

            return "SUCCESS";
        } catch (NumberFormatException e) {
            return "Error en el formato de los valores numéricos";
        }
    }

    public DtProductos crearProducto(String nombre, String cantidadStr, String precioUnitarioStr) {
        DtProductos producto = new DtProductos();
        producto.setId(UUID.randomUUID().toString());
        producto.setNombre(nombre);
        producto.setCantidad(Integer.parseInt(cantidadStr));
        producto.setPrecioUnitario(new BigDecimal(precioUnitarioStr));
        producto.setPrecioTotal(producto.getPrecioUnitario().multiply(new BigDecimal(producto.getCantidad())));
        return producto;
    }

    public int guardarProducto(DtProductos producto) {
        return rpProductos.insert(producto);
    }
}
