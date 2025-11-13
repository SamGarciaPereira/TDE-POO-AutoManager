const btnAbrirModal = document.getElementById("btn-abrir-modal");
const modal = document.getElementById("modal-usuario");
const btnFecharModal = document.getElementById("btn-fechar-modal");
const formUsuario = document.getElementById("form-usuario");
const msgErro = formUsuario.querySelector(".mensagem.erro");
const msgSucesso = formUsuario.querySelector(".mensagem.sucesso");

let editandoUsuarioId = null;

btnAbrirModal.addEventListener("click", () => {
    editandoUsuarioId = null;
    formUsuario.reset();
    msgErro.textContent = "";
    msgSucesso.textContent = "";
    modal.style.display = "block";
});

btnFecharModal.addEventListener("click", () => modal.style.display = "none");
window.addEventListener("click", (e) => { if (e.target === modal) modal.style.display = "none"; });

async function atualizarListaUsuarios() {
    try {
        const response = await fetch("/usuarios");
        const usuarios = await response.json();
        const tbody = document.getElementById("lista-usuarios");
        tbody.innerHTML = "";

        usuarios.forEach(u => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${u.idUsuario}</td>
                <td>${u.nome}</td>
                <td>${u.email}</td>
                <td>
                    <button class="btn btn-editar" data-id="${u.idUsuario}" data-nome="${u.nome}" data-email="${u.email}" data-senha="${u.senha}">Editar</button>
                    <button class="btn btn-excluir" data-id="${u.idUsuario}">Excluir</button>
                </td>
            `;
            tbody.appendChild(tr);
        });

        document.querySelectorAll(".btn-editar").forEach(btn => {
            btn.addEventListener("click", () => abrirModalEdicao(btn));
        });

        document.querySelectorAll(".btn-excluir").forEach(btn => {
            btn.addEventListener("click", () => deletarUsuario(btn.dataset.id));
        });

    } catch (error) {
        console.error("Erro ao carregar usuários:", error);
    }
}

function abrirModalEdicao(btn) {
    editandoUsuarioId = btn.dataset.id;
    formUsuario.nome.value = btn.dataset.nome;
    formUsuario.email.value = btn.dataset.email;
    formUsuario.senha.value = "";
    formUsuario.senhaConfirmada.value = "";

    msgErro.textContent = "";
    msgSucesso.textContent = "";
    modal.style.display = "block";
}

formUsuario.addEventListener("submit", async (event) => {
    event.preventDefault();

    const nome = formUsuario.nome.value;
    const email = formUsuario.email.value;
    const senha = formUsuario.senha.value.trim();
    let senhaConfirmada = "";
    if (formUsuario.senhaConfirmada) {
    senhaConfirmada = formUsuario.senhaConfirmada.value.trim();
    }

    msgErro.textContent = "";
    msgSucesso.textContent = "";

    if (senha && senhaConfirmada && senha !== senhaConfirmada) {
        msgErro.textContent = "As senhas não conferem!";
        return;
    }

    try {
        let method = "POST";

        if (editandoUsuarioId) {
        method = "PUT";
        }

        let bodyData = { nome, email };

        if (editandoUsuarioId) {
        bodyData.idUsuario = Number(editandoUsuarioId);
        }

        if (senha) bodyData.senha = senha;

        const response = await fetch("/usuarios", {
            method,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(bodyData)
        });

        const resultado = await response.json();

        if (response.ok) {
            if (editandoUsuarioId) {
            msgSucesso.textContent = "Usuário atualizado!";
            } else {
            msgSucesso.textContent = "Usuário criado!";
            }
            formUsuario.reset();
            atualizarListaUsuarios();

            setTimeout(() => {
                modal.style.display = "none";
                msgSucesso.textContent = "";
            }, 1000);
        } else {
            msgErro.textContent = resultado.message || "Erro ao salvar usuário.";
        }
    } catch (error) {
        console.error("Erro na requisição:", error);
        msgErro.textContent = "Erro de conexão com o servidor. Tente novamente.";
    }
});

async function deletarUsuario(id) {
    if (!confirm("Deseja realmente deletar este usuário?")) return;

    try {
        const response = await fetch(`/usuarios?id=${id}`, { method: "DELETE" });
        const resultado = await response.json();

        if (response.ok) {
            atualizarListaUsuarios();
        } else {
            alert(resultado.message || "Erro ao deletar usuário.");
        }
    } catch (error) {
        console.error("Erro ao deletar usuário:", error);
        alert("Erro de conexão com o servidor.");
    }
}

atualizarListaUsuarios();
