package com.irissoft.capaNegocios;

import com.irissoft.capaAccesoDatos.QProductosConCategorias;
import com.irissoft.datos.DtCategorias;
import com.irissoft.datos.DtProductos;
import com.irissoft.repositorios.RpProductosConCategorias;
import java.util.List;

public class NgProductosConCategorias {

    private final RpProductosConCategorias rpProductosConCategorias;
    private final NgCategorias ngCategorias;
    private final NgProductos ngProductos;

    public NgProductosConCategorias() {
        rpProductosConCategorias = new QProductosConCategorias();
        ngCategorias = new NgCategorias();
        ngProductos = new NgProductos();
    }

    public String registrarNuevoProducto(String nombreProducto, String cantidadStr,
            String precioUnitarioStr, List<String> nombresCategorias) {
        // 1. Validar producto usando NgProductos
        String validacionProducto = ngProductos.validarDatosProducto(nombreProducto, cantidadStr, precioUnitarioStr);
        if (!validacionProducto.equals("SUCCESS")) {
            return validacionProducto;
        }

        // 2. Validar categorías usando NgCategorias
        String validacionCategorias = ngCategorias.validarCategorias(nombresCategorias);
        if (!validacionCategorias.equals("SUCCESS")) {
            return validacionCategorias;
        }

        // 3. Verificar si el producto ya existe en alguna categoría
        for (String nombreCategoria : nombresCategorias) {
            if (ngCategorias.existeProductoEnCategoria(nombreProducto, nombreCategoria)) {
                return "El producto ya existe en la categoría: " + nombreCategoria;
            }
        }

        try {
            // 4. Crear y guardar el producto
            DtProductos producto = ngProductos.crearProducto(nombreProducto, cantidadStr, precioUnitarioStr);
            int resultadoProducto = ngProductos.guardarProducto(producto);

            if (resultadoProducto <= 0) {
                return "Error al guardar el producto";
            }

            // 5. Procesar las categorías y sus relaciones
            for (String nombreCategoria : nombresCategorias) {
                DtCategorias categoria = ngCategorias.buscarCategoriaPorNombre(nombreCategoria);
                if (categoria == null) {
                    // Crear nueva categoría si no existe
                    categoria = ngCategorias.crearCategoria(nombreCategoria);
                    ngCategorias.insertarCategoria(categoria);
                }

                // Crear relación producto-categoría
                rpProductosConCategorias.insertProductoConCategoria(producto.getId(), categoria.getId());
            }

            return "SUCCESS";
        } catch (Exception e) {
            return "Error en el proceso: " + e.getMessage();
        }
    }
}
