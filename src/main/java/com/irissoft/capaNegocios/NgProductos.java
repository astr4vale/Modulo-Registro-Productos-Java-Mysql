
package com.irissoft.capaNegocios;

import com.irissoft.capaAccesoDatos.QProductos;
import com.irissoft.datos.DtProductos;
import com.irissoft.repositorios.RpProductos;
import java.util.List;

public class NgProductos {

    private final RpProductos rpProductos;

    public NgProductos() {
        rpProductos = new QProductos();
    }

    // Método para insertar un producto
    public int insertarProducto(DtProductos dtProducto) {
        return rpProductos.insert(dtProducto);
    }

    // Método para obtener todos los productos
    public List<DtProductos> obtenerTodosLosProductos() {
        return rpProductos.getAll();
    }

    // Método para eliminar un producto por su id
    public boolean eliminarProducto(String idProducto) {
        return rpProductos.delete(idProducto);
    }

    // Método para actualizar un producto
    public boolean actualizarProducto(DtProductos dtProducto) {
        return rpProductos.update(dtProducto);
    }
}
