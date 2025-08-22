package br.edu.univille.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static ConnectionFactory instance;

    private ConnectionFactory(){}

    public static ConnectionFactory getInstance(){
        if(instance == null) instance = new ConnectionFactory();
        return instance;
    }

    public Connection get() throws SQLException {

        String url = "jdbc:postgresql://localhost:5432/imobiliaria";
        String user = "imobiliaria";
        String password = "123456";
        return DriverManager.getConnection(url,user,password);
    }

}
