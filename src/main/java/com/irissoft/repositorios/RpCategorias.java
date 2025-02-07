package com.irissoft.repositorios;

import com.irissoft.datos.DtCategorias;
import java.util.List;

public interface RpCategorias<Dt> {

    int insert(Dt dt);

    List<Dt> getAll();

    boolean delete(String idCategoria);

    DtCategorias findByNombre(String nombre);

    boolean existeProductoEnCategoria(String nombreProducto, String idCategoria);

}
