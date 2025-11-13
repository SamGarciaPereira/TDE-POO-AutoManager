package dao;

import model.Manutencao;
import model.Veiculo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManutencaoDAO {
    private Connection con;
    private PreparedStatement ps;

    public ManutencaoDAO(){
        this.con = ConexaoMySQL.getConexaoMySQL().getConnection();
    }

    //listar manutenções
    public ArrayList<Manutencao> listarManutencoes(){
        String query = "SELECT m.*, v.placa, v.marca, v.modelo, v.ano, v.nome_cliente " +
                "FROM manutencao m " +
                "JOIN veiculo v ON m.id_veiculo_fk = v.id_veiculo " +
                "ORDER BY m.data_entrada DESC";
        ArrayList<Manutencao> lista = new ArrayList<>();
        try{
            ps = this.con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Veiculo v = new Veiculo(
                        rs.getInt("id_veiculo_fk"),
                        rs.getString("placa"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getInt("ano"),
                        rs.getString("nome_cliente")
                );
                Manutencao m = new Manutencao(
                        rs.getInt("id_manutencao"),
                        rs.getString("descricao_servico"),
                        rs.getString("data_entrada"),
                        rs.getString("data_saida"),
                        rs.getString("status_servico"),
                        rs.getInt("id_veiculo_fk"),
                        v
                );
                lista.add(m);
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return lista;
    }

    //criar nova manutenção
    public boolean criarManutencao(Manutencao m) {
        String query = "INSERT INTO manutencao (descricao_servico, status_servico, id_veiculo_fk) " +
                "VALUES (?, ?, ?)";
        try {
            ps = this.con.prepareStatement(query);
            ps.setString(1, m.getDescricao());
            ps.setString(2, m.getStatusServico());
            ps.setInt(3, m.getIdVeiculoFk());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    //atualizar manutenção
    public boolean atualizarManutencao(Manutencao m) {
        String query = "UPDATE manutencao SET " +
                "descricao_servico = ?, " +
                "status_servico = ?, " +
                "data_saida = CASE " +
                "    WHEN ? = 'Concluído' AND data_saida IS NULL THEN NOW() " +
                "    WHEN ? != 'Concluído' THEN NULL " +
                "    ELSE data_saida " +
                "END " +
                "WHERE id_manutencao = ?";
        try {
            ps = this.con.prepareStatement(query);
            ps.setString(1, m.getDescricao());
            ps.setString(2, m.getStatusServico());
            ps.setString(3, m.getStatusServico());
            ps.setString(4, m.getStatusServico());
            ps.setInt(5, m.getIdManutencao());
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    //deletar manutenção
    public boolean deletarManutencao(int id) {
        String query = "DELETE FROM manutencao WHERE id_manutencao = ?";
        try {
            ps = this.con.prepareStatement(query);
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }


}
