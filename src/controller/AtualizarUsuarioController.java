package controller;

import dao.UsuarioDAO;
import model.Usuario;
import view.AtualizarUsuarioView;

public class AtualizarUsuarioController {
    private AtualizarUsuarioView auv;

    public AtualizarUsuarioController(){
        auv = new AtualizarUsuarioView();
        Usuario u = auv.atualizarUsuario();
        UsuarioDAO uDAO = new UsuarioDAO();
        uDAO.atualizarUsuario(u);
    }
}
