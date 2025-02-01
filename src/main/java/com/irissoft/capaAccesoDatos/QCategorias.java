package com.irissoft.capaAccesoDatos;

import com.irissoft.datos.DtCategorias;
import com.irissoft.repositorios.RpCategorias;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QCategorias implements RpCategorias<DtCategorias> {

    private ConexionBD con;
    private String query;

    @Override
    public int insert(DtCategorias dt) {
        con = new ConexionBD();
        query = "INSERT INTO categorias (nombre) VALUES (?)";

        try {
            PreparedStatement ps = con.conexion.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, dt.getNombre());

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
        query = "SELECT id, nombre FROM categorias";
        List<DtCategorias> categorias = new ArrayList<>();

        try {
            PreparedStatement ps = con.conexion.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                DtCategorias categoria = new DtCategorias();
                categoria.setId(rs.getInt("id"));
                categoria.setNombre(rs.getString("nombre"));
                categorias.add(categoria);
            }
            con.conexion.close();
            return categorias;
        } catch (SQLException e) {
            System.err.println("Error getAll: " + e.getMessage());
            return categorias;
        }
    }

    @Override
    public boolean delete(int idCategoria) {
        con = new ConexionBD();
        query = "DELETE FROM categorias WHERE id = ?";

        try {
            PreparedStatement ps = con.conexion.prepareStatement(query);
            ps.setInt(1, idCategoria);
            int rowsAffected = ps.executeUpdate();
            con.conexion.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error delete: " + e.getMessage());
            return false;
        }
    }
//
//    @Override
//    public boolean update(DtCategorias dt) {
//        con = new ConexionBD();
//        query = "UPDATE categorias SET nombre = ? WHERE id = ?";
//
//        try {
//            PreparedStatement ps = con.conexion.prepareStatement(query);
//            ps.setString(1, dt.getNombre());
//            ps.setInt(2, dt.getId());
//
//            int rowsAffected = ps.executeUpdate();
//            con.conexion.close();
//            return rowsAffected > 0;
//        } catch (SQLException e) {
//            System.err.println("Error update: " + e.getMessage());
//            return false;
//        }
//    }
}
