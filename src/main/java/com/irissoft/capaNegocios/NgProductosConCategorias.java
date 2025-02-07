package com.irissoft.capaNegocios;

import com.irissoft.capaAccesoDatos.QProductosConCategorias;
import com.irissoft.datos.DtCategorias;
import com.irissoft.datos.DtProductos;
import com.irissoft.repositorios.RpProductosConCategorias;
import java.util.List;
import java.util.UUID;

public class NgProductosConCategorias {

    private final RpProductosConCategorias rpProductosConCategorias;
    private final NgCategorias ngCategorias;
    private final NgProductos ngProductos;

    public NgProductosConCategorias() {
        rpProductosConCategorias = new QProductosConCategorias();
        ngCategorias = new NgCategorias();
        ngProductos = new NgProductos();
    }

    public boolean procesarProductoConCategorias(DtProductos producto, List<String> nombresCategorias) {
        try {
            // Primero insertamos el producto
            int resultadoProducto = ngProductos.insertarProducto(producto);
            if (resultadoProducto <= 0) {
                return false;
            }

            // Procesamos cada categoría
            for (String nombreCategoria : nombresCategorias) {
                DtCategorias categoria = ngCategorias.buscarCategoriaPorNombre(nombreCategoria);
                
                if (categoria == null) {
                    // La categoría no existe, la creamos
                    DtCategorias nuevaCategoria = new DtCategorias();
                    nuevaCategoria.setId(UUID.randomUUID().toString()); // Generar un nuevo ID
                    nuevaCategoria.setNombre(nombreCategoria);
                    
                    int resultadoCategoria = ngCategorias.insertarCategoria(nuevaCategoria);
                    if (resultadoCategoria > 0) {
                        categoria = nuevaCategoria;
                    } else {
                        continue; // Si falla la inserción de la categoría, continuamos con la siguiente
                    }
                }

                // Creamos la relación entre el producto y la categoría
                rpProductosConCategorias.insertProductoConCategoria(producto.getId(), categoria.getId());
            }
            
            return true;
        } catch (Exception e) {
            System.err.println("Error al procesar producto con categorías: " + e.getMessage());
            return false;
        }
    }
}
