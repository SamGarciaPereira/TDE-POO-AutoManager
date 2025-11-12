document.getElementById("form-login").addEventListener("submit", async (event) => {
    event.preventDefault(); 

    const email = document.getElementById("email").value;
    const senha = document.getElementById("senha").value;
    const mensagemErro = document.getElementById("mensagem-erro");

    mensagemErro.textContent = "";

    try {
      
      const response = await fetch("../login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email: email, senha: senha }),
      });

      if (response.ok) {
        const usuario = await response.json();
        alert(`Bem-vindo, ${usuario.nome}!`);
        window.location.href = "usuarios.html";
      } else if (response.status === 401) {
        const erro = await response.json();
        mensagemErro.textContent = erro.message;
      } else {
        mensagemErro.textContent =
          "Erro ao contactar o servidor (Servlet não encontrado ou com erro).";
      }
    } catch (error) {
      console.error("Erro na requisição:", error);
      mensagemErro.textContent =
        "Erro de conexão com o servidor (O 'fetch' falhou).";
    }
  });
