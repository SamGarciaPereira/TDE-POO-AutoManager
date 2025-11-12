document.getElementById("form-criar-usuario").addEventListener("submit", async (event) => {
    event.preventDefault(); 

    const nome = document.getElementById("nome").value;
    const email = document.getElementById("email").value;
    const senha = document.getElementById("senha").value;
    const senhaConfirmada = document.getElementById("senhaConfirmada").value;

    const msgErro = document.getElementById("mensagem-erro");
    const msgSucesso = document.getElementById("mensagem-sucesso");

    msgErro.textContent = "";
    msgSucesso.textContent = "";

    if (senha !== senhaConfirmada) {
      msgErro.textContent = "As senhas não conferem!";
      return;
    }

    try {
      const response = await fetch("../usuarios", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          nome: nome,
          email: email,
          senha: senha,
        }),
      });

      const resultado = await response.json();

      if (response.ok) {
        msgSucesso.textContent =
          resultado.message + " Redirecionando para o login...";
        document.getElementById("form-criar-usuario").reset();

        setTimeout(() => {
          window.location.href = "login.html"; 
        }, 3000);
      } else {
        msgErro.textContent = resultado.message || "Erro ao criar usuário.";
      }
    } catch (error) {
      console.error("Erro na requisição:", error);
      msgErro.textContent = "Erro de conexão com o servidor. Tente novamente.";
    }
  });
