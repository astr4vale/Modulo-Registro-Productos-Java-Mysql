
package com.irissoft.repositorios;

import java.util.List;

public interface RpProductos<Dt> {

    int insert(Dt dt);

    List<Dt> getAll();

    boolean delete(String idProducto);

    boolean update(Dt dt);

}
