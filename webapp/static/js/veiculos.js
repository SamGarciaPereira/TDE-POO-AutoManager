const btnAbrirModal = document.getElementById("btn-abrir-modal");
const modal = document.getElementById("modal-veiculo");
const btnFecharModal = document.getElementById("btn-fechar-modal");
const formVeiculo = document.getElementById("form-veiculo");
const msgErro = formVeiculo.querySelector(".mensagem.erro");
const msgSucesso = formVeiculo.querySelector(".mensagem.sucesso");

let editandoVeiculoId = null;

btnAbrirModal.addEventListener("click", () => {
    editandoVeiculoId = null;
    formVeiculo.reset();
    msgErro.textContent = "";
    msgSucesso.textContent = "";
    modal.style.display = "block";
});

btnFecharModal.addEventListener("click", () => modal.style.display = "none");
window.addEventListener("click", (e) => {
    if (e.target === modal) modal.style.display = "none";
});

async function atualizarListaVeiculos() {
    try {
        const response = await fetch("/veiculos");
        const veiculos = await response.json();
        const tbody = document.getElementById("lista-veiculos");
        tbody.innerHTML = "";

        veiculos.forEach(v => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${v.idVeiculo}</td>
                <td>${v.placa}</td>
                <td>${v.marca}</td>
                <td>${v.modelo}</td>
                <td>${v.ano}</td>
                <td>
                    <button class="btn btn-editar" 
                        data-id="${v.idVeiculo}" 
                        data-placa="${v.placa}" 
                        data-marca="${v.marca}" 
                        data-modelo="${v.modelo}" 
                        data-ano="${v.ano}">
                        Editar
                    </button>
                    <button class="btn btn-excluir" data-id="${v.idVeiculo}">Excluir</button>
                </td>
            `;
            tbody.appendChild(tr);
        });

        document.querySelectorAll(".btn-editar").forEach(btn => {
            btn.addEventListener("click", () => abrirModalEdicao(btn));
        });

        document.querySelectorAll(".btn-excluir").forEach(btn => {
            btn.addEventListener("click", () => deletarVeiculo(btn.dataset.id));
        });

    } catch (error) {
        console.error("Erro ao carregar veículos:", error);
    }
}

function abrirModalEdicao(btn) {
    editandoVeiculoId = btn.dataset.id;
    formVeiculo.placa.value = btn.dataset.placa;
    formVeiculo.marca.value = btn.dataset.marca;
    formVeiculo.modelo.value = btn.dataset.modelo;
    formVeiculo.ano.value = btn.dataset.ano;

    msgErro.textContent = "";
    msgSucesso.textContent = "";
    modal.style.display = "block";
}

formVeiculo.addEventListener("submit", async (event) => {
    event.preventDefault();

    const placa = formVeiculo.placa.value.trim();
    const marca = formVeiculo.marca.value.trim();
    const modelo = formVeiculo.modelo.value.trim();
    const ano = parseInt(formVeiculo.ano.value.trim());

    msgErro.textContent = "";
    msgSucesso.textContent = "";

    let method = "POST";
    if (editandoVeiculoId) {
        method = "PUT";
    }

    let bodyData = {
        placa: placa,
        marca: marca,
        modelo: modelo,
        ano: ano
    };

    if (editandoVeiculoId) {
        bodyData.idVeiculo = Number(editandoVeiculoId);
    }

    try {
        const response = await fetch("/veiculos", {
            method: method,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(bodyData)
        });

        const resultado = await response.json();

        if (response.ok) {
            if (editandoVeiculoId) {
                msgSucesso.textContent = "Veículo atualizado!";
            } else {
                msgSucesso.textContent = "Veículo cadastrado!";
            }
            formVeiculo.reset();
            atualizarListaVeiculos();

            setTimeout(() => {
                modal.style.display = "none";
                msgSucesso.textContent = "";
            }, 1000);
        } else {
            msgErro.textContent = resultado.message || "Erro ao salvar veículo.";
        }
    } catch (error) {
        console.error("Erro na requisição:", error);
        msgErro.textContent = "Erro de conexão com o servidor.";
    }
});

async function deletarVeiculo(id) {
    if (!confirm("Deseja realmente deletar este veículo?")) return;

    try {
        const response = await fetch(`/veiculos?id=${id}`, { method: "DELETE" });
        const resultado = await response.json();

        if (response.ok) {
            atualizarListaVeiculos();
        } else {
            alert(resultado.message || "Erro ao deletar veículo.");
        }
    } catch (error) {
        console.error("Erro ao deletar veículo:", error);
        alert("Erro de conexão com o servidor.");
    }
}

atualizarListaVeiculos();
