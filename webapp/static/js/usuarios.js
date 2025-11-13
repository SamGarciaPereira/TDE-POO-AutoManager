const btnAbrirModal = document.getElementById("btn-abrir-modal");
const modal = document.getElementById("modal-usuario");
const btnFecharModal = document.getElementById("btn-fechar-modal");

btnAbrirModal.addEventListener("click", () => {
    modal.style.display = "block";
});

btnFecharModal.addEventListener("click", () => {
    modal.style.display = "none";
});

window.addEventListener("click", (e) => {
    if (e.target === modal) {
        modal.style.display = "none";
    }
});

const formUsuario = document.getElementById("form-usuario");
const msgErro = formUsuario.querySelector(".mensagem.erro");
const msgSucesso = formUsuario.querySelector(".mensagem.sucesso");

formUsuario.addEventListener("submit", async (event) => {
    event.preventDefault();

    const nome = formUsuario.nome.value;
    const email = formUsuario.email.value;
    const senha = formUsuario.senha.value;
    const senhaConfirmada = formUsuario.senhaConfirmada?.value;
    msgErro.textContent = "";
    msgSucesso.textContent = "";

    if (senhaConfirmada && senha !== senhaConfirmada) {
        msgErro.textContent = "As senhas não conferem!";
        return;
    }

    try {
        const response = await fetch("/usuarios", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ nome, email, senha }),
        });

        const resultado = await response.json();

        if (response.ok) {
            msgSucesso.textContent = "Usuário criado com sucesso!";
            formUsuario.reset();

            setTimeout(() => {
                modal.style.display = "none";
                msgSucesso.textContent = "";
            }, 1000);

        } else {
            msgErro.textContent = resultado.message || "Erro ao criar usuário.";
        }

    } catch (error) {
        console.error("Erro na requisição:", error);
        msgErro.textContent = "Erro de conexão com o servidor. Tente novamente.";
    }
});
