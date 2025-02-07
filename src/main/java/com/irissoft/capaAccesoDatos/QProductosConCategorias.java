
package com.irissoft.capaAccesoDatos;

import com.irissoft.repositorios.RpProductosConCategorias;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class QProductosConCategorias implements RpProductosConCategorias{
    
    private ConexionBD con;
    private String query;
    
    @Override
    public int insertProductoConCategoria(String idProducto, String idCategoria) {
        con = new ConexionBD();
        query = "INSERT INTO producto_categoria (idProducto, idCategoria) VALUES (?, ?)";

        try {
            PreparedStatement ps = con.conexion.prepareStatement(query);
            ps.setString(1, idProducto);
            ps.setString(2, idCategoria);

            int rowsAffected = ps.executeUpdate();
            con.conexion.close();
            return rowsAffected;  // Retorna el número de filas afectadas
        } catch (SQLException e) {
            System.err.println("Error insertProductoConCategoria: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public List<String> getCategoriasDeProducto(String idProducto) {
        con = new ConexionBD();
        query = "SELECT idCategoria FROM producto_categoria WHERE idProducto = ?";
        List<String> categorias = new ArrayList<>();

        try {
            PreparedStatement ps = con.conexion.prepareStatement(query);
            ps.setString(1, idProducto);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                categorias.add(rs.getString("idCategoria"));
            }
            con.conexion.close();
            return categorias;
        } catch (SQLException e) {
            System.err.println("Error getCategoriasDeProducto: " + e.getMessage());
            return categorias;
        }
    }

    @Override
    public boolean deleteProductoConCategoria(String idProducto, String idCategoria) {
        con = new ConexionBD();
        query = "DELETE FROM producto_categoria WHERE idProducto = ? AND idCategoria = ?";

        try {
            PreparedStatement ps = con.conexion.prepareStatement(query);
            ps.setString(1, idProducto);
            ps.setString(2, idCategoria);

            int rowsAffected = ps.executeUpdate();
            con.conexion.close();
            return rowsAffected > 0;  // Devuelve true si se eliminó correctamente
        } catch (SQLException e) {
            System.err.println("Error deleteProductoConCategoria: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateProductoConCategoria(String idProducto, String idCategoria) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
