
package com.irissoft.capaAccesoDatos;

import com.irissoft.datos.DtCategorias;
import com.irissoft.repositorios.RpCategorias;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class QCategorias implements RpCategorias<DtCategorias>{
    
    private ConexionBD con;
    private String query;
    
    @Override
    public int insert(DtCategorias dt) {
        con = new ConexionBD();
        query = "INSERT INTO categorias (id, nombre) VALUES (?, ?)";

        try {
            PreparedStatement ps = con.conexion.prepareStatement(query);
            ps.setString(1, dt.getId());
            ps.setString(2, dt.getNombre());

            int rowsAffected = ps.executeUpdate();
            con.conexion.close();

            return rowsAffected;
        } catch (SQLException e) {
            System.err.println("Error insert: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public List<DtCategorias> getAll() {
        con = new ConexionBD();
        query = "SELECT * FROM categorias ORDER BY creadoEn DESC";
        List<DtCategorias> listaCategorias = new ArrayList<>();

        try {
            PreparedStatement ps = con.conexion.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                DtCategorias dt = new DtCategorias();
                dt.setId(rs.getString("id"));
                dt.setNombre(rs.getString("nombre"));
                dt.setCreadoEn(rs.getTimestamp("creadoEn"));
                dt.setActualizadoEn(rs.getTimestamp("actualizadoEn"));
                listaCategorias.add(dt);
            }
            con.conexion.close();
            return listaCategorias;
        } catch (SQLException e) {
            System.err.println("Error getAll: " + e.getMessage());
            return listaCategorias;
        }
    }

    @Override
    public boolean delete(String idCategoria) {
        con = new ConexionBD();
        query = "DELETE FROM categorias WHERE id = ?";

        try {
            PreparedStatement ps = con.conexion.prepareStatement(query);
            ps.setString(1, idCategoria);
            int rowsAffected = ps.executeUpdate();
            con.conexion.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error delete: " + e.getMessage());
            return false;
        }
    }

    @Override
    public DtCategorias findByNombre(String nombre) {
        con = new ConexionBD();
        query = "SELECT * FROM categorias WHERE nombre = ?";

        try {
            PreparedStatement ps = con.conexion.prepareStatement(query);
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                DtCategorias dt = new DtCategorias();
                dt.setId(rs.getString("id"));
                dt.setNombre(rs.getString("nombre"));
                dt.setCreadoEn(rs.getTimestamp("creadoEn"));
                dt.setActualizadoEn(rs.getTimestamp("actualizadoEn"));
                con.conexion.close();
                return dt;
            }
            con.conexion.close();
            return null;
        } catch (SQLException e) {
            System.err.println("Error findByNombre: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean existeProductoEnCategoria(String nombreProducto, String idCategoria) {
        con = new ConexionBD();
        query = """
            SELECT COUNT(*) FROM productos p 
            JOIN producto_categoria pc ON p.id = pc.idProducto 
            WHERE p.nombre = ? AND pc.idCategoria = ?
        """;

        try {
            PreparedStatement ps = con.conexion.prepareStatement(query);
            ps.setString(1, nombreProducto);
            ps.setString(2, idCategoria);

            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error al verificar producto en categoría: " + e.getMessage());
            return false;
        } finally {
            if (con.conexion != null) try {
                con.conexion.close();
            } catch (SQLException e) {
            }
        }
    }
}
