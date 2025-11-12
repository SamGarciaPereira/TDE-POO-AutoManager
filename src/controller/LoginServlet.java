package controller;

import com.google.gson.Gson;
import dao.UsuarioDAO;
import model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private  Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jsonRequest = req.getReader().lines().collect(Collectors.joining());
        Usuario usuarioLogin = gson.fromJson(jsonRequest, Usuario.class);

        Usuario usuarioLogado = usuarioDAO.fazerLogin(usuarioLogin.getEmail(), usuarioLogin.getSenha());

        if(usuarioLogado != null) {
            HttpSession session = req.getSession();
            session.setAttribute("usuarioLogado", usuarioLogado);

            sendJsonResponse(resp, gson.toJson(usuarioLogado), HttpServletResponse.SC_OK);
        } else{
            sendJsonResponse(resp, "{\"message\":\"Email ou senha inválidos\"}", HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
    private void sendJsonResponse(HttpServletResponse resp, String json, int statusCode) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(statusCode);
        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }
}
