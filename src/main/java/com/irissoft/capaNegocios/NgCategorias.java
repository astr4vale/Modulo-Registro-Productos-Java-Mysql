package com.irissoft.capaNegocios;

import com.irissoft.capaAccesoDatos.QCategorias;
import com.irissoft.datos.DtCategorias;
import com.irissoft.repositorios.RpCategorias;
import java.util.List;

public class NgCategorias {

    private final RpCategorias rpCategorias;

    public NgCategorias() {
        rpCategorias = new QCategorias();
    }

    public DtCategorias buscarCategoriaPorNombre(String nombre) {
        return ((QCategorias) rpCategorias).findByNombre(nombre);
    }

    // Método para insertar una categoría
    public int insertarCategoria(DtCategorias dtCategoria) {
        return rpCategorias.insert(dtCategoria);
    }

    // Método para obtener todas las categorías
    public List<DtCategorias> obtenerTodasLasCategorias() {
        return rpCategorias.getAll();
    }

    // Método para eliminar una categoría por su id
    public boolean eliminarCategoria(String idCategoria) {
        return rpCategorias.delete(idCategoria);
    }
}
