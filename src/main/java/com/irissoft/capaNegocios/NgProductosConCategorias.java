package com.irissoft.capaNegocios;

import com.irissoft.capaAccesoDatos.QProductosConCategorias;
import com.irissoft.datos.DtCategorias;
import com.irissoft.datos.DtProductos;
import com.irissoft.repositorios.RpProductosConCategorias;
import java.util.List;

public class NgProductosConCategorias {

    private final RpProductosConCategorias rpProductosConCategorias;

    public NgProductosConCategorias() {
        rpProductosConCategorias = new QProductosConCategorias();
    }

    // Método para insertar relación producto-categoría
    public boolean insertarRelacionProductoCategoria(DtProductos dtProducto, DtCategorias dtCategoria) {
        // Primero, verificar que el producto y la categoría existen
        boolean productoExiste = verificarExistenciaProducto(dtProducto);
        boolean categoriaExiste = verificarExistenciaCategoria(dtCategoria);

        if (!productoExiste || !categoriaExiste) {
            // Si alguno no existe, no realizamos la inserción de la relación
            return false;
        }

        // Si ambos existen, entonces insertamos la relación
        return rpProductosConCategorias.insertProductoConCategoria(dtProducto.getId(), dtCategoria.getId()) > 0;
    }

    // Método para verificar la existencia de un producto
    private boolean verificarExistenciaProducto(DtProductos dtProducto) {
        // Aquí implementas la lógica para verificar que el producto exista en la base de datos
        // Puede ser una consulta a la base de datos, usando rpProductos.getAll() o algún método similar.
        // Esto depende de cómo tengas organizada tu capa de acceso a datos.
        return true;  // Por ahora retornamos true solo como ejemplo
    }

    // Método para verificar la existencia de una categoría
    private boolean verificarExistenciaCategoria(DtCategorias dtCategoria) {
        // Similar a verificarExistenciaProducto, aquí se verifica si la categoría existe en la base de datos
        return true;  // Por ahora retornamos true solo como ejemplo
    }

    // Método para obtener las categorías asociadas a un producto
    public List<String> obtenerCategoriasDeProducto(DtProductos dtProducto) {
        return rpProductosConCategorias.getCategoriasDeProducto(dtProducto.getId());
    }

    // Método para eliminar una relación entre un producto y una categoría
    public boolean eliminarRelacionProductoCategoria(DtProductos dtProducto, DtCategorias dtCategoria) {
        return rpProductosConCategorias.deleteProductoConCategoria(dtProducto.getId(), dtCategoria.getId());
    }

    // Método para actualizar una relación (si fuera necesario)
    public boolean actualizarRelacionProductoCategoria(DtProductos dtProducto, DtCategorias dtCategoria) {
        return rpProductosConCategorias.updateProductoConCategoria(dtProducto.getId(), dtCategoria.getId());
    }
}
