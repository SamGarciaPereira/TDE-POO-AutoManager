// Define a URL base da sua API (ajuste a porta se necessário)
const BASE_URL = "http://localhost:8080/"; // Caminho raiz do Tomcat7:run

document
  .getElementById("form-login")
  .addEventListener("submit", async (event) => {
    event.preventDefault(); // Impede o recarregamento da página

    const email = document.getElementById("email").value;
    const senha = document.getElementById("senha").value;
    const mensagemErro = document.getElementById("mensagem-erro");

    mensagemErro.textContent = ""; // Limpa erros anteriores

    try {
      const response = await fetch(`${BASE_URL}login`, {
        // Chama http://localhost:8080/login
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email: email, senha: senha }), // Envia o JSON para o LoginServlet
      });

      if (response.ok) {
        // Status 200-299
        // Login com sucesso!
        const usuario = await response.json();
        alert(`Bem-vindo, ${usuario.nome}!`);

        // Redireciona para a página principal
        window.location.href = "usuarios.html";
      } else if (response.status === 401) {
        // Não autorizado
        // Senha ou email errados
        const erro = await response.json();
        mensagemErro.textContent = erro.message;
      } else {
        mensagemErro.textContent = "Erro ao tentar fazer login.";
      }
    } catch (error) {
      console.error("Erro na requisição:", error);
      mensagemErro.textContent = "Erro de conexão com o servidor.";
    }
  });
