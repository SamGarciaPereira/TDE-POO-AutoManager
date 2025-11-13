package controller;

import com.google.gson.Gson;
import dao.VeiculoDAO;
import model.Veiculo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.stream.Collectors;

@WebServlet("/veiculos")
public class VeiculoServlet extends HttpServlet {

    private VeiculoDAO veiculoDAO = new VeiculoDAO();
    private Gson gson = new Gson();

    //retorna lista de veículos
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ArrayList<Veiculo> veiculos = veiculoDAO.listarVeiculos();
        String jsonResponse = gson.toJson(veiculos);
        sendJsonResponse(resp, jsonResponse, HttpServletResponse.SC_OK);
    }

    //criar veículos
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jsonRequest = req.getReader().lines().collect(Collectors.joining());
        Veiculo novoVeiculo = gson.fromJson(jsonRequest, Veiculo.class);
        boolean sucesso = veiculoDAO.criarVeiculo(novoVeiculo);

        if (sucesso) {
            sendJsonResponse(resp, "{\"message\":\"Veículo criado com sucesso\"}", HttpServletResponse.SC_CREATED);
        } else {
            sendJsonResponse(resp, "{\"message\":\"Erro ao criar veículo\"}", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    //atualiza veículos
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jsonRequest = req.getReader().lines().collect(Collectors.joining());
        Veiculo veiculoAtualizar = gson.fromJson(jsonRequest, Veiculo.class);
        boolean sucesso = veiculoDAO.atualizarVeiculo(veiculoAtualizar);

        if (sucesso) {
            sendJsonResponse(resp, "{\"message\":\"Veículo atualizado com sucesso\"}", HttpServletResponse.SC_OK);
        } else {
            sendJsonResponse(resp, "{\"message\":\"Erro ao atualizar veículo\"}", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    //deleta veículos
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        boolean sucesso = veiculoDAO.deletarVeiculo(id);

        if (sucesso) {
            sendJsonResponse(resp, "{\"message\":\"Veículo deletado com sucesso\"}", HttpServletResponse.SC_OK);
        } else {
            sendJsonResponse(resp, "{\"message\":\"Erro ao deletar veículo\"}", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

   //enviar response em json
    private void sendJsonResponse(HttpServletResponse resp, String json, int statusCode) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(statusCode);
        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }
}