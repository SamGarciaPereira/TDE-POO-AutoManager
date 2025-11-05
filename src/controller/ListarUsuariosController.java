package controller;

import dao.UsuarioDAO;
import view.ListarUsuariosView;

public class ListarUsuariosController {
    private ListarUsuariosView luv;

    public ListarUsuariosController(){
        luv = new ListarUsuariosView();
        UsuarioDAO uDAO = new UsuarioDAO();
        luv.listarUsuarios(uDAO.listarUsuarios());
    }
}
