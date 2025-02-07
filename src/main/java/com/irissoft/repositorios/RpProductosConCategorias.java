package com.irissoft.repositorios;

import java.util.List;

public interface RpProductosConCategorias {

    // Método para insertar una relación entre un producto y una o varias categorías
    int insertProductoConCategoria(String idProducto, String idCategoria);

    // Método para obtener todas las relaciones de productos con categorías
    List<String> getCategoriasDeProducto(String idProducto);

    // Método para eliminar una relación entre un producto y una categoría
    boolean deleteProductoConCategoria(String idProducto, String idCategoria);

    // Método para actualizar una relación (si fuera necesario)
    boolean updateProductoConCategoria(String idProducto, String idCategoria);
}
