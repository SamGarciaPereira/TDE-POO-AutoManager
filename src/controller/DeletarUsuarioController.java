package controller;

import dao.UsuarioDAO;
import model.Usuario;
import view.DeletarUsuarioView;

public class DeletarUsuarioController {
    private DeletarUsuarioView duv;

    public DeletarUsuarioController(){
        duv = new DeletarUsuarioView();
        Usuario u = duv.deletarUsuario();
        UsuarioDAO uDAO = new UsuarioDAO();
        uDAO.deletarUsuario(u);
    }
}
