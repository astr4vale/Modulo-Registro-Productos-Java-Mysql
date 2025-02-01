package com.irissoft.capaNegocios;

import com.irissoft.capaAccesoDatos.QCategorias;
import com.irissoft.datos.DtCategorias;
import com.irissoft.repositorios.RpCategorias;
import java.util.List;

public class NgCategorias {

    private final RpCategorias<DtCategorias> rpCategorias;

    public NgCategorias() {
        rpCategorias = new QCategorias();
    }

    

    public boolean insert(String nombre) {
        DtCategorias dtCategorias = new DtCategorias();
        dtCategorias.setNombre(nombre);

        return rpCategorias.insert(dtCategorias) > 0;
    }

    public List<DtCategorias> getAll() {
        return rpCategorias.getAll();
    }

    public boolean deleteCategoria(int idCategoria) {
        return rpCategorias.delete(idCategoria);
    }
    public boolean existeCategoriaPorNombre(String nombre) {
    // Aquí debes tener la lógica para verificar en la base de datos o en la lista si ya existe la categoría
    List<DtCategorias> categorias = getAll();
    for (DtCategorias categoria : categorias) {
        if (categoria.getNombre().equalsIgnoreCase(nombre)) {
            return true; // Ya existe una categoría con ese nombre
        }
    }
    return false; // No existe ninguna categoría con ese nombre
}

}
