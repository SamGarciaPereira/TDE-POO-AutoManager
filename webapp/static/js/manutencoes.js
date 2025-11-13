document.addEventListener("DOMContentLoaded", () => {
  const modalManutencao = document.getElementById("modal-manutencao");
  const btnAbrirModal = document.getElementById("btn-abrir-modal");
  const btnFecharModal = document.getElementById("btn-fechar-modal");
  const formManutencao = document.getElementById("form-manutencao");
  const msgErro = document.getElementById("mensagem-erro");
  const msgSucesso = document.getElementById("mensagem-sucesso");
  const modalTitulo = document.getElementById("modal-titulo");
  const inputIdManutencao = document.getElementById("id_manutencao");
  const selectVeiculo = document.getElementById("select-veiculo");
  const inputDescricao = document.getElementById("descricao");
  const selectStatus = document.getElementById("status");

  let editandoManutencaoId = null;

  btnAbrirModal.addEventListener("click", () => {
    editandoManutencaoId = null;
    modalTitulo.textContent = "Cadastrar Manutenção";
    formManutencao.reset();
    msgErro.textContent = "";
    msgSucesso.textContent = "";
    selectVeiculo.disabled = false;
    carregarVeiculosDropdown();
    modalManutencao.style.display = "block";
    console.log("LOG: Modal de manutenção aberto.");
  });

  btnFecharModal.addEventListener("click", () => {
    modalManutencao.style.display = "none";
  });

  window.addEventListener("click", (e) => {
    if (e.target === modalManutencao) modalManutencao.style.display = "none";
  });

  async function atualizarListaManutencoes() {
    try {
      const response = await fetch("../manutencoes");
      const manutencoes = await response.json();
      const tbody = document.getElementById("lista-manutencoes");
      tbody.innerHTML = "";
      manutencoes.forEach((m) => {
        const tr = document.createElement("tr");
        const dataEntrada = new Date(m.dataEntrada).toLocaleDateString(
          "pt-BR",
          { timeZone: "UTC" }
        );
        const dataSaida = m.dataSaida
          ? new Date(m.dataSaida).toLocaleDateString("pt-BR", {
              timeZone: "UTC",
            })
          : "---";
        tr.innerHTML = `
          <td>${m.idManutencao}</td>
          <td>${m.statusServico}</td>
          <td>${m.descricao}</td>
          <td>${m.veiculo.placa}</td>
          <td>${m.veiculo.nomeCliente}</td>
          <td>${dataEntrada}</td>
          <td>${dataSaida}</td>
          <td>
            <button class="btn btn-editar"
              data-id="${m.idManutencao}"
              data-descricao="${m.descricao}"
              data-status="${m.statusServico}"
              data-idveiculo="${m.veiculo.idVeiculo}"
              data-placa="${m.veiculo.placa}"
              data-cliente="${m.veiculo.nomeCliente}">
              Editar
            </button>
            <button class="btn btn-excluir" data-id="${m.idManutencao}">Excluir</button>
          </td>
        `;
        tbody.appendChild(tr);
      });
      document.querySelectorAll(".btn-editar").forEach((btn) => {
        btn.addEventListener("click", () => abrirModalEdicao(btn));
      });
      document.querySelectorAll(".btn-excluir").forEach((btn) => {
        btn.addEventListener("click", () => deletarManutencao(btn.dataset.id));
      });
    } catch (error) {
      console.error("Erro ao carregar manutenções:", error);
    }
  }

  function abrirModalEdicao(btn) {
    editandoManutencaoId = btn.dataset.id;
    modalTitulo.textContent = "Editar Manutenção";
    msgErro.textContent = "";
    msgSucesso.textContent = "";
    inputIdManutencao.value = btn.dataset.id;
    inputDescricao.value = btn.dataset.descricao;
    selectStatus.value = btn.dataset.status;
    selectVeiculo.disabled = true;
    selectVeiculo.innerHTML = `<option value="${btn.dataset.idveiculo}">${btn.dataset.placa} (${btn.dataset.cliente})</option>`;
    modalManutencao.style.display = "block";
  }

  formManutencao.addEventListener("submit", async (event) => {
    event.preventDefault();
    msgErro.textContent = "";
    msgSucesso.textContent = "";
    const descricao = inputDescricao.value;
    const statusServico = selectStatus.value;
    const idVeiculoFk = selectVeiculo.value;
    try {
      let method = "POST";
      let bodyData = {
        descricao,
        statusServico,
        idVeiculoFk: Number(idVeiculoFk),
      };
      if (editandoManutencaoId) {
        method = "PUT";
        bodyData.idManutencao = Number(editandoManutencaoId);
        delete bodyData.idVeiculoFk;
      } else if (!idVeiculoFk) {
        msgErro.textContent = "Por favor, selecione um veículo.";
        return;
      }
      const response = await fetch("../manutencoes", {
        method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(bodyData),
      });
      const resultado = await response.json();
      if (response.ok) {
        msgSucesso.textContent = editandoManutencaoId
          ? "Manutenção atualizada!"
          : "Manutenção criada!";
        formManutencao.reset();
        atualizarListaManutencoes();
        setTimeout(() => {
          modalManutencao.style.display = "none";
          msgSucesso.textContent = "";
        }, 1000);
      } else {
        msgErro.textContent = resultado.message || "Erro ao salvar manutenção.";
      }
    } catch (error) {
      console.error("Erro na requisição:", error);
      msgErro.textContent = "Erro de conexão com o servidor. Tente novamente.";
    }
  });

  async function deletarManutencao(id) {
    if (!confirm("Deseja realmente deletar esta manutenção?")) return;
    try {
      const response = await fetch(`../manutencoes?id=${id}`, {
        method: "DELETE",
      });
      const resultado = await response.json();
      if (response.ok) {
        atualizarListaManutencoes();
      } else {
        alert(resultado.message || "Erro ao deletar manutenção.");
      }
    } catch (error) {
      console.error("Erro ao deletar manutenção:", error);
      alert("Erro de conexão com o servidor.");
    }
  }

  async function carregarVeiculosDropdown() {
    try {
      const response = await fetch("../veiculos");
      const veiculos = await response.json();
      selectVeiculo.innerHTML =
        '<option value="">-- Selecione um Veículo --</option>';
      veiculos.forEach((v) => {
        selectVeiculo.innerHTML += `
          <option value="${v.idVeiculo}">
            ${v.placa} - ${v.marca} ${v.modelo} (Cliente: ${v.nomeCliente})
          </option>
        `;
      });
    } catch (error) {
      console.error("Erro ao carregar veículos:", error);
      selectVeiculo.innerHTML =
        '<option value="">Erro ao carregar veículos.</option>';
    }
  }

  atualizarListaManutencoes();
  carregarVeiculosDropdown();
});
