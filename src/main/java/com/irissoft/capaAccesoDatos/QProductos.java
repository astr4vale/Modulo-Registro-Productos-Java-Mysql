
package com.irissoft.capaAccesoDatos;

import com.irissoft.datos.DtProductos;
import com.irissoft.repositorios.RpProductos;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QProductos implements RpProductos<DtProductos> {

    private ConexionBD con;
    private String query;

    @Override
    public int insert(DtProductos dt) {
        con = new ConexionBD();
        query = "INSERT INTO productos (id, nombre, cantidad, precioUnitario) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement ps = con.conexion.prepareStatement(query);
            ps.setString(1, dt.getId());  // id es un String
            ps.setString(2, dt.getNombre());
            ps.setInt(3, dt.getCantidad());
            ps.setBigDecimal(4, dt.getPrecioUnitario());

            int rowsAffected = ps.executeUpdate();
            con.conexion.close();
            return rowsAffected;
        } catch (SQLException e) {
            System.err.println("Error insert: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public List<DtProductos> getAll() {
        con = new ConexionBD();
        query = "SELECT * FROM productos ORDER BY creadoEn DESC";
        List<DtProductos> listaProductos = new ArrayList<>();

        try {
            PreparedStatement ps = con.conexion.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                DtProductos dt = new DtProductos();
                dt.setId(rs.getString("id"));  // id es un String
                dt.setNombre(rs.getString("nombre"));
                dt.setCantidad(rs.getInt("cantidad"));
                dt.setPrecioUnitario(rs.getBigDecimal("precioUnitario"));
                dt.setPrecioTotal(rs.getBigDecimal("precioTotal"));
                dt.setCreadoEn(rs.getTimestamp("creadoEn"));
                dt.setActualizadoEn(rs.getTimestamp("actualizadoEn"));
                listaProductos.add(dt);
            }
            con.conexion.close();
            return listaProductos;
        } catch (SQLException e) {
            System.err.println("Error getAll: " + e.getMessage());
            return listaProductos;
        }
    }

    @Override
    public boolean delete(String idProducto) {
        con = new ConexionBD();
        query = "DELETE FROM productos WHERE id = ?";

        try {
            PreparedStatement ps = con.conexion.prepareStatement(query);
            ps.setString(1, idProducto);  // idProducto es ahora un String
            int rowsAffected = ps.executeUpdate();
            con.conexion.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error delete: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(DtProductos dt) {
        con = new ConexionBD();
        query = "UPDATE productos SET nombre = ?, cantidad = ?, precioUnitario = ? WHERE id = ?";

        try {
            PreparedStatement ps = con.conexion.prepareStatement(query);
            ps.setString(1, dt.getNombre());
            ps.setInt(2, dt.getCantidad());
            ps.setBigDecimal(3, dt.getPrecioUnitario());
            ps.setString(4, dt.getId());  // id es un String

            int rowsAffected = ps.executeUpdate();
            con.conexion.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error update: " + e.getMessage());
            return false;
        }
    }

}
