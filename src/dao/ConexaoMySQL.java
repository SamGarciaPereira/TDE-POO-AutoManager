package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoMySQL {
    private static ConexaoMySQL con;
    private Connection connection;

    private ConexaoMySQL(){
        String usuario = "root";
        String senha = "1234";
        String strCon = "jdbc:mysql://127.0.0.1:3306/auto_manager?useSSL=false";
        try{
            this.connection = DriverManager.getConnection(strCon, usuario, senha);
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }

    }
    public static ConexaoMySQL getConexaoMySQL(){
        if(con == null){
            con = new ConexaoMySQL();
        }
        return con;
    }
    public Connection getConnection(){
        return this.connection;
    }
}


