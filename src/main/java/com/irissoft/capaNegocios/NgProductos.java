
package com.irissoft.capaNegocios;

import com.irissoft.capaAccesoDatos.QProductos;
import com.irissoft.datos.DtProductos;
import com.irissoft.repositorios.RpProductos;
import java.math.BigDecimal;
import java.util.List;

public class NgProductos {
    
    private final RpProductos<DtProductos> rpProductos;

    public NgProductos() {
        rpProductos = new QProductos();
    }
    
    public boolean insert(String nombre, int cantidad, BigDecimal precioUnitario, int idCategoria) {
        DtProductos dtProductos = new DtProductos();
        dtProductos.setNombre(nombre);
        dtProductos.setCantidad(cantidad);
        dtProductos.setPrecioUnitario(precioUnitario);
        dtProductos.setIdCategoria(idCategoria);

        return rpProductos.insert(dtProductos) > 0;
    }
    
    public List<DtProductos> getAll() {
        return rpProductos.getAll();
    }
    
    public boolean deleteProducto(int idProducto) {
        return rpProductos.delete(idProducto);
    }
    
    public boolean updateProducto(int idProducto, String nombre, int cantidad, BigDecimal precioUnitario, int idCategoria) {
        DtProductos dtProductos = new DtProductos();
        dtProductos.setId(idProducto);
        dtProductos.setNombre(nombre);
        dtProductos.setCantidad(cantidad);
        dtProductos.setPrecioUnitario(precioUnitario);
        dtProductos.setIdCategoria(idCategoria);

        return rpProductos.update(dtProductos);
    }

    public boolean existeProductoPorNombre(String nombreProducto) {
        // Obtener todos los productos y verificar si el nombre ya existe
        List<DtProductos> productos = getAll();
        for (DtProductos producto : productos) {
            if (producto.getNombre().equalsIgnoreCase(nombreProducto)) {
                return true; // Producto con el mismo nombre encontrado
            }
        }
        return false; // No existe un producto con ese nombre
    }
}
