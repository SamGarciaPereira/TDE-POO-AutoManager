package controller;

import com.google.gson.Gson;
import dao.ManutencaoDAO;
import dao.UsuarioDAO;
import model.Manutencao;
import model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.stream.Collectors;

@WebServlet("/manutencao")
public class ManutencaoServlet extends HttpServlet {
    private ManutencaoDAO manutencaoDAO = new ManutencaoDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        ArrayList<Manutencao> manutencoes = manutencaoDAO.listarManutencoes();

    }



}
