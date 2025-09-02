package br.com.pizzaria.adminlogin;

import java.sql.Connection;

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
