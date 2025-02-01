
package com.irissoft.repositorios;

import java.util.List;

public interface RpCategorias<Dt> {

    int insert(Dt dt);

    List<Dt> getAll();

    boolean delete(int idProducto);

}
