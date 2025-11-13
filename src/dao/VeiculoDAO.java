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
                        rs.getInt("ano"),
                        rs.getString("nomeCliente")
                );
                lista.add(v);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }
}