document.addEventListener("DOMContentLoaded", () => {
  const formCriarUsuario = document.getElementById("form-criar-usuario");

  if (formCriarUsuario) {
    const msgErro = formCriarUsuario.querySelector(".mensagem.erro");
    const msgSucesso = formCriarUsuario.querySelector(".mensagem.sucesso");

    formCriarUsuario.addEventListener("submit", async (event) => {
      event.preventDefault();

      const nome = formCriarUsuario.nome.value;
      const email = formCriarUsuario.email.value;
      const senha = formCriarUsuario.senha.value.trim();
      const senhaConfirmada = formCriarUsuario.senhaConfirmada.value.trim();

      msgErro.textContent = "";
      msgSucesso.textContent = "";

      if (senha && senhaConfirmada && senha !== senhaConfirmada) {
        msgErro.textContent = "As senhas não conferem!";
        return;
      }

      try {
        const bodyData = { nome, email, senha };

        const response = await fetch("/usuarios", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(bodyData),
        });

        const resultado = await response.json();

        if (response.ok) {
          msgSucesso.textContent = "Usuário criado!";
          formCriarUsuario.reset();
          setTimeout(() => {
            window.location.href = "login.html";
          }, 1000);
        } else {
          msgErro.textContent = resultado.message || "Erro ao salvar usuário.";
        }
      } catch (error) {
        console.error("Erro na requisição:", error);
        msgErro.textContent =
          "Erro de conexão com o servidor. Tente novamente.";
      }
    });
  }
});
