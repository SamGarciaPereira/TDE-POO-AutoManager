package controller;

import com.google.gson.Gson;
import dao.UsuarioDAO;
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

@WebServlet("/usuarios")
public class UsuarioServlet extends HttpServlet {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private Gson gson = new Gson();

    // Método para LISTAR usuários (GET
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ArrayList<Usuario> usuarios = usuarioDAO.listarUsuarios();
        String jsonResponse = gson.toJson(usuarios);
        sendJsonResponse(resp, jsonResponse, HttpServletResponse.SC_OK);
    }
    // Método para CRIAR usuário (POST)
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jsonRequest = req.getReader().lines().collect(Collectors.joining());
        Usuario novoUsuario = gson.fromJson(jsonRequest, Usuario.class);

        boolean sucesso = usuarioDAO.criarUsuario(novoUsuario);

        if (sucesso) {
            sendJsonResponse(resp, "{\"message\":\"Usuário criado com sucesso\"}", HttpServletResponse.SC_CREATED);
        } else {
            sendJsonResponse(resp, "{\"message\":\"Erro ao criar usuário\"}", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    // Método para ATUALIZAR usuário (PUT)
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jsonRequest = req.getReader().lines().collect(Collectors.joining());
        Usuario usuarioAtualizar = gson.fromJson(jsonRequest, Usuario.class);

        boolean sucesso = usuarioDAO.atualizarUsuario(usuarioAtualizar);

        if (sucesso) {
            sendJsonResponse(resp, "{\"message\":\"Usuário atualizado com sucesso\"}", HttpServletResponse.SC_OK);
        } else {
            sendJsonResponse(resp, "{\"message\":\"Erro ao atualizar usuário\"}", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    // Método para DELETAR usuário (DELETE)
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));

        boolean sucesso = usuarioDAO.deletarUsuario(id);

        if (sucesso) {
            sendJsonResponse(resp, "{\"message\":\"Usuário deletado com sucesso\"}", HttpServletResponse.SC_OK);
        } else {
            sendJsonResponse(resp, "{\"message\":\"Erro ao deletar usuário\"}", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    // Método utilitário (copie o mesmo do LoginServlet)
    private void sendJsonResponse(HttpServletResponse resp, String json, int statusCode) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(statusCode);
        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }
}


