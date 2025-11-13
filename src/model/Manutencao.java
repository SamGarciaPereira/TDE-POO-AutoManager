package model;

public class Manutencao {
    private int idManutencao;
    private String descricao;
    private String dataEntrada;
    private String dataSaida;
    private String statusServico;
    private int idVeiculoFk;
    private Veiculo veiculo;

    public Manutencao(String descricao, String statusServico, int idVeiculoFk) {
        this.descricao = descricao;
        this.statusServico = statusServico;
        this.idVeiculoFk = idVeiculoFk;
    }

    public Manutencao(int idManutencao, String descricao, String dataEntrada, String dataSaida, String statusServico, int idVeiculoFk, Veiculo veiculo) {
        this.idManutencao = idManutencao;
        this.descricao = descricao;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.statusServico = statusServico;
        this.idVeiculoFk = idVeiculoFk;
        this.veiculo = veiculo;
    }

    public int getIdManutencao() {
        return idManutencao;
    }

    public void setIdManutencao(int idManutencao) {
        this.idManutencao = idManutencao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(String dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public String getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(String dataSaida) {
        this.dataSaida = dataSaida;
    }

    public String getStatusServico() {
        return statusServico;
    }

    public void setStatusServico(String statusServico) {
        this.statusServico = statusServico;
    }

    public int getIdVeiculoFk() {
        return idVeiculoFk;
    }

    public void setIdVeiculoFk(int idVeiculoFk) {
        this.idVeiculoFk = idVeiculoFk;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }
}
