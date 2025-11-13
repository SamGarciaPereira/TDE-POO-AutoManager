package controller;

import com.google.gson.Gson;
import dao.ManutencaoDAO;
import model.Manutencao;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.stream.Collectors;

@WebServlet("/manutencoes")
public class ManutencaoServlet extends HttpServlet {
    private ManutencaoDAO manutencaoDAO = new ManutencaoDAO();
    private Gson gson = new Gson();


    //retorna lista de manutenções
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ArrayList<Manutencao> manutencoes = manutencaoDAO.listarManutencoes();
        String jsonResponse = gson.toJson(manutencoes);
        sendJsonResponse(resp, jsonResponse, HttpServletResponse.SC_OK);
    }

    //cria nova manutenção
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jsonRequest = req.getReader().lines().collect(Collectors.joining());
        Manutencao novaManutencao = gson.fromJson(jsonRequest, Manutencao.class);
        boolean sucesso = manutencaoDAO.criarManutencao(novaManutencao);

        if (sucesso) {
            sendJsonResponse(resp, "{\"message\":\"Manutenção criada com sucesso\"}", HttpServletResponse.SC_CREATED);
        } else {
            sendJsonResponse(resp, "{\"message\":\"Erro ao criar manutenção\"}", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    //atualiza manutenção
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jsonRequest = req.getReader().lines().collect(Collectors.joining());
        Manutencao manutencaoAtualizar = gson.fromJson(jsonRequest, Manutencao.class);

        // Chama o DAO para atualizar no banco
        boolean sucesso = manutencaoDAO.atualizarManutencao(manutencaoAtualizar);

        if (sucesso) {
            sendJsonResponse(resp, "{\"message\":\"Manutenção atualizada com sucesso\"}", HttpServletResponse.SC_OK);
        } else {
            sendJsonResponse(resp, "{\"message\":\"Erro ao atualizar manutenção\"}", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    //deleta manutenção
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        boolean sucesso = manutencaoDAO.deletarManutencao(id);

        if (sucesso) {
            sendJsonResponse(resp, "{\"message\":\"Manutenção deletada com sucesso\"}", HttpServletResponse.SC_OK);
        } else {
            sendJsonResponse(resp, "{\"message\":\"Erro ao deletar manutenção\"}", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    //enviar json
    private void sendJsonResponse(HttpServletResponse resp, String json, int statusCode) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(statusCode);
        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }



}
