package dao;

import model.Usuario;
import org.mindrot.jbcrypt.BCrypt;

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

    // ---Método de Login ---
    public Usuario fazerLogin(String email, String senhaDigitada) {
        String query = "SELECT * FROM usuario WHERE email = ?";
        try {
            ps = this.con.prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hashDoBanco = rs.getString("senha");

                // Verifica se a senha digitada bate com o hash salvo
                if (BCrypt.checkpw(senhaDigitada, hashDoBanco)) {
                    // Login OK!
                    int id = rs.getInt("id_usuario");
                    String nome = rs.getString("nome");
                    return new Usuario(id, nome, email);
                }
            }
            return null;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // ---Criar Usuário (retorna boolean) ---
    public boolean criarUsuario(Usuario u){
        String query = "INSERT INTO usuario(nome, email, senha) VALUES (?, ?, ?)";
        try{
            // Gera o HASH da senha
            String hashSenha = BCrypt.hashpw(u.getSenha(), BCrypt.gensalt());

            ps = this.con.prepareStatement(query);
            ps.setString(1, u.getNome());
            ps.setString(2, u.getEmail());
            ps.setString(3, hashSenha);
            ps.execute();
            return true; //
        }
        catch(SQLException ex) {
            ex.printStackTrace();
            return false; // <-- CORRIGE O ERRO
        }
    }

    // ---Listar Usuários---
    public ArrayList<Usuario> listarUsuarios(){
        String query = "SELECT id_usuario, nome, email FROM usuario";
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

    // ---Atualizar Usuário (retorna boolean) ---
    public boolean atualizarUsuario(Usuario u){
        String query = "UPDATE usuario SET nome = ?, email = ? WHERE id_usuario = ?";
        try{
            ps = this.con.prepareStatement(query);
            ps.setString(1, u.getNome());
            ps.setString(2, u.getEmail());
            ps.setInt(3, u.getIdUsuario());
            ps.executeUpdate();
            return true; //
        }
        catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }
    }

    // ---Deletar Usuário (recebe int, retorna boolean) ---
    public boolean deletarUsuario(int id){
        String query = "DELETE FROM usuario WHERE id_usuario = ?";
        try{
            ps = this.con.prepareStatement(query);
            ps.setInt(1, id);
            ps.execute();
            return true;
        }
        catch(SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}