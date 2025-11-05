package controller;

import dao.UsuarioDAO;
import model.Usuario;
import view.CriarUsuarioView;

public class CriarUsuarioController {
    private CriarUsuarioView cuv;

    public CriarUsuarioController(){
        cuv = new CriarUsuarioView();
        Usuario u = cuv.criarUsuario();
        UsuarioDAO uDAO = new UsuarioDAO();
        uDAO.criarUsuario(u);
    }
}
