/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.irissoft.capaAccesoDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author KALI
 */
public class ConexionBD {
    private final String conexionString = "jdbc:mariadb://localhost:3306/tienda";
    private final String userString = "root";
    private final String passString = "root";

    public Connection conexion = null;

    public ConexionBD() {
        init();
    }

    private void init() {
        try {
            conexion = DriverManager.getConnection(conexionString, userString, passString);
            System.out.println("CONECTADO A " + conexionString);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
