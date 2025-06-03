document.addEventListener("DOMContentLoaded", () => {
  // CSV Upload
  const csvButton = document.getElementById("csvButton");
  const csvInput = document.getElementById("csvInput");
  const csvForm = document.getElementById("csvForm");

  if (csvButton && csvInput && csvForm) {
    csvButton.addEventListener("click", () => {
      csvInput.click();
    });

    csvInput.addEventListener("change", () => {
      if (csvInput.files.length > 0) {
        csvForm.submit();
      }
    });
  }

  // Paginação
  const rowsPerPage = 5;
  const table = document.querySelector("#document-table tbody");
  const rows = Array.from(table.querySelectorAll("tr"));
  let currentPage = 1;

  const prevBtn = document.getElementById("prev-page");
  const nextBtn = document.getElementById("next-page");
  const pageInfo = document.getElementById("page-info");

  function renderTable() {
    const start = (currentPage - 1) * rowsPerPage;
    const end = start + rowsPerPage;

    rows.forEach(row => row.style.display = "none");

    const pageRows = rows.slice(start, end);
    pageRows.forEach(row => row.style.display = "");

    const missingRows = rowsPerPage - pageRows.length;
    removeEmptyRows();
    for (let i = 0; i < missingRows; i++) {
      const emptyRow = document.createElement("tr");
      emptyRow.innerHTML = `<td colspan="12" style="height: 30px;"></td>`;
      emptyRow.classList.add("empty-row");
      table.appendChild(emptyRow);
    }

    updatePaginationInfo();
  }

  function removeEmptyRows() {
    const emptyRows = document.querySelectorAll("tr.empty-row");
    emptyRows.forEach(row => row.remove());
  }

  function updatePaginationInfo() {
    const totalPages = Math.ceil(rows.length / rowsPerPage);
    pageInfo.textContent = `Página ${currentPage} de ${totalPages}`;
    prevBtn.disabled = currentPage === 1;
    nextBtn.disabled = currentPage === totalPages;
  }

  prevBtn.addEventListener("click", () => {
    if (currentPage > 1) {
      currentPage--;
      renderTable();
    }
  });

  nextBtn.addEventListener("click", () => {
    const totalPages = Math.ceil(rows.length / rowsPerPage);
    if (currentPage < totalPages) {
      currentPage++;
      renderTable();
    }
  });

  renderTable();

  // Pop-up Editar
  document.getElementById("closePopupEdit").addEventListener("click", () => {
    document.getElementById("popupEdit").style.display = "none";
  });

  window.addEventListener("click", (event) => {
    const popupEdit = document.getElementById("popupEdit");
    if (event.target === popupEdit) {
      popupEdit.style.display = "none";
    }
  });

  // (Opcional: também pode proteger contra popup/openPopup se for usar)
  const popup = document.getElementById("popup");
  const openPopup = document.getElementById("openPopup");
  const closePopup = document.getElementById("closePopup");

  if (popup && openPopup && closePopup) {
    openPopup.addEventListener("click", () => {
      popup.style.display = "block";
    });

    closePopup.addEventListener("click", () => {
      popup.style.display = "none";
    });

    window.addEventListener("click", (event) => {
      if (event.target === popup) {
        popup.style.display = "none";
      }
    });
  }
});

// Mantém a função no escopo global
function openEditPopup(element) {
  const id = element.getAttribute("data-id");
  const responsavel = element.getAttribute("data-responsavel");
  const status = element.getAttribute("data-status");
  const data_correcao = element.getAttribute("data-data_correcao");
  const observacao = element.getAttribute("data-observacao");
  const correcao = element.getAttribute("data-correcao");

  document.getElementById("editId").value = id;
  document.getElementById("editResponsavel").value = responsavel;
  document.getElementById("editStatus").value = status;
  document.getElementById("editData_correcao").value = data_correcao;
  document.getElementById("editObservacao").value = observacao;
  document.getElementById("editCorrecao").value = correcao;

  document.getElementById("popupEdit").style.display = "block";
}




document.addEventListener('DOMContentLoaded', function () {
    // Pré-processamento: extrair e agrupar os dados
    const categoriaCount = {};
    const gravidadeCount = {};
    const porData = {};

    documentos.forEach(doc => {
        // Categorias (exemplo: 'Firewall', 'Antivírus', etc. - assumindo que 'ativo' representa categoria)
        const categoria = doc.ativo || 'Desconhecido';
        categoriaCount[categoria] = (categoriaCount[categoria] || 0) + 1;

        // Criticidade
        const crit = doc.criticidade || 'Não Informada';
        gravidadeCount[crit] = (gravidadeCount[crit] || 0) + 1;

        // Data
        const data = doc.data || 'Data Não Informada';
        porData[data] = (porData[data] || 0) + 1;
    });

    // === Gráfico de Barras: Vulnerabilidades por Categoria ===
    new Chart(document.getElementById('vulnerabilidadesPorCategoria'), {
        type: 'bar',
        data: {
            labels: Object.keys(categoriaCount),
            datasets: [{
                label: 'Vulnerabilidades por Categoria',
                data: Object.values(categoriaCount),
                backgroundColor: '#4e73df',
            }]
        },
        options: {
            responsive: true,
            plugins: { legend: { display: true } },
            scales: { y: { beginAtZero: true } }
        }
    });

    // === Gráfico de Pizza: Vulnerabilidades por Criticidade ===
    new Chart(document.getElementById('vulnerabilidadesPorGravidade'), {
        type: 'pie',
        data: {
            labels: Object.keys(gravidadeCount),
            datasets: [{
                label: 'Vulnerabilidades por Gravidade',
                data: Object.values(gravidadeCount),
                backgroundColor: [
                    '#dc3545', // vermelho
                    '#ffc107', // amarelo
                    '#28a745', // verde
                    '#6c757d'  // cinza
                ],
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            plugins: { legend: { position: 'top' } }
        }
    });

    // === Gráfico de Linha: Vulnerabilidades ao Longo do Tempo ===
    const datasOrdenadas = Object.keys(porData).sort((a, b) => new Date(a) - new Date(b));
    new Chart(document.getElementById('vulnerabilidadesAoLongoDoTempo'), {
        type: 'line',
        data: {
            labels: datasOrdenadas,
            datasets: [{
                label: 'Vulnerabilidades ao Longo do Tempo',
                data: datasOrdenadas.map(data => porData[data]),
                borderColor: '#17a2b8',
                fill: false,
                tension: 0.3
            }]
        },
        options: {
            responsive: true,
            scales: {
                x: {
                    title: { display: true, text: 'Data' }
                },
                y: {
                    beginAtZero: true,
                    title: { display: true, text: 'Total de Vulnerabilidades' }
                }
            }
        }
    });
});