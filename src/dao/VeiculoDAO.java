package dao;

import model.Veiculo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VeiculoDAO {

    private Connection con;
    private PreparedStatement ps;

    public VeiculoDAO() {
        this.con = ConexaoMySQL.getConexaoMySQL().getConnection();
    }

    //criar veículo
    public boolean criarVeiculo(Veiculo v){
        String query = "INSERT INTO veiculo(placa, marca, modelo, ano) VALUES (?, ?, ?, ?)";
        try{
            ps = this.con.prepareStatement(query);
            ps.setString(1, v.getPlaca());
            ps.setString(2, v.getMarca());
            ps.setString(3, v.getModelo());
            ps.setInt(4, v.getAno());
            ps.execute();
            return true;
        }catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }
    }

    //listar veículos
    public ArrayList<Veiculo> listarVeiculos() {
        String query = "SELECT id_veiculo, placa, marca, modelo, ano FROM veiculo";
        ArrayList<Veiculo> lista = new ArrayList<>();
        try {
            ps = this.con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Veiculo v = new Veiculo(
                        rs.getInt("id_veiculo"),
                        rs.getString("placa"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getInt("ano")
                );
                lista.add(v);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    //atualizar veículo
    public boolean atualizarVeiculo(Veiculo v){
        String query = "UPDATE veiculo SET placa = ?, marca = ?, modelo = ?, ano = ? WHERE id_veiculo = ?";
        try{
            ps = this.con.prepareStatement(query);
            ps.setString(1, v.getPlaca());
            ps.setString(2, v.getMarca());
            ps.setString(3, v.getModelo());
            ps.setInt(4, v.getAno());
            ps.setInt(5, v.getIdVeiculo());
            ps.executeUpdate();
            return true;
        }catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }
    }

    //deletar veículo
    public boolean deletarVeiculo(int id){
        String query = "DELETE FROM veiculo WHERE id_veiculo = ?";
        try{
            ps = this.con.prepareStatement(query);
            ps.setInt(1, id);
            ps.execute();
            return true;
        }catch(SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}