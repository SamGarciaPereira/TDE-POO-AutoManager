package dao;

import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UsuarioDAO {

    private Connection con;
    private PreparedStatement ps;

    public UsuarioDAO(){
        this.con = ConexaoMySQL.getConexaoMySQL().getConnection();
    }

    public void criarUsuario(Usuario u){
        String query = "INSERT INTO usuario(nome, email) VALUES (?, ?)";
        try{
            ps = this.con.prepareStatement(query);
            ps.setString(1, u.getNome());
            ps.setString(2, u.getEmail());
            ps.execute();
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
    public ArrayList<Usuario> listarUsuarios(){
        String query = "SELECT * FROM usuario";

        ArrayList<Usuario> lista = new ArrayList<Usuario>();

        try{
            ps = this.con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int id = rs.getInt("id_usuario");
                String nome = rs.getString("nome");
                String email = rs.getString("email");

                lista.add(new Usuario(id, nome, email));
            }

        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return lista;
    }
    public void deletarUsuario(Usuario u){
        String query = "DELETE FROM usuario WHERE id_usuario = ?";
        try{
            ps = this.con.prepareStatement(query);
            ps.setInt(1, u.getIdUsuario());
            ps.execute();
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void atualizarUsuario(Usuario u){
        String query = "UPDATE usuario SET nome = ?, email = ? WHERE id_usuario = ?";
        try{
            ps = this.con.prepareStatement(query);
            ps.setString(1, u.getNome());
            ps.setString(2, u.getEmail());
            ps.setInt(3, u.getIdUsuario());
            ps.executeUpdate();
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }
}
