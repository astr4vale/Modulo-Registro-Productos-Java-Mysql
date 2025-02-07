package com.irissoft.capaNegocios;

import com.irissoft.capaAccesoDatos.QCategorias;
import com.irissoft.datos.DtCategorias;
import com.irissoft.repositorios.RpCategorias;
import java.util.List;
import java.util.UUID;

public class NgCategorias {

    private final RpCategorias rpCategorias;

    public NgCategorias() {
        rpCategorias = new QCategorias();
    }

    // Método existente
    public DtCategorias buscarCategoriaPorNombre(String nombre) {
        return ((QCategorias) rpCategorias).findByNombre(nombre);
    }

    // Método existente
    public int insertarCategoria(DtCategorias dtCategoria) {
        return rpCategorias.insert(dtCategoria);
    }

    // Método existente
    public List<DtCategorias> obtenerTodasLasCategorias() {
        return rpCategorias.getAll();
    }

    // Método existente
    public boolean eliminarCategoria(String idCategoria) {
        return rpCategorias.delete(idCategoria);
    }

    // Nuevos métodos que faltaban
    public String validarCategorias(List<String> nombresCategorias) {
        if (nombresCategorias == null || nombresCategorias.isEmpty()) {
            return "Debe especificar al menos una categoría";
        }

        // Validar que ningún nombre de categoría esté vacío
        for (String nombre : nombresCategorias) {
            if (nombre == null || nombre.trim().isEmpty()) {
                return "Los nombres de categorías no pueden estar vacíos";
            }
        }

        return "SUCCESS";
    }

    public boolean existeProductoEnCategoria(String nombreProducto, String nombreCategoria) {
        DtCategorias categoria = buscarCategoriaPorNombre(nombreCategoria);
        if (categoria == null) {
            return false;
        }
        return ((QCategorias) rpCategorias).existeProductoEnCategoria(nombreProducto, categoria.getId());
    }

    public DtCategorias crearCategoria(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío");
        }

        // Verificar si ya existe una categoría con ese nombre
        DtCategorias categoriaExistente = buscarCategoriaPorNombre(nombre);
        if (categoriaExistente != null) {
            return categoriaExistente;
        }

        // Crear nueva categoría
        DtCategorias nuevaCategoria = new DtCategorias();
        nuevaCategoria.setId(UUID.randomUUID().toString());
        nuevaCategoria.setNombre(nombre.trim());
        return nuevaCategoria;
    }
}
