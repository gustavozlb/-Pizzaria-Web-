package br.com.pizzaria.adminlogin.service;

import java.sql.Connection;

import br.com.pizzaria.adminlogin.config.DatabaseConnection;

public class TestConnection {

    public static void main(String[] args) {
        Connection conn = DatabaseConnection.connect();
        if (conn != null) {
            System.out.println("Conexão bem-sucedida!");
        } else {
            System.out.println("Falha na conexão.");
        }
    }
}
