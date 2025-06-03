const popup = document.getElementById("popup");
const openPopup = document.getElementById("openPopup");
const closePopup = document.getElementById("closePopup");

// Abre o pop-up quando o botão é clicado
openPopup.addEventListener("click", () => {
  popup.style.display = "block";
});

// Fecha o pop-up ao clicar no "X"
closePopup.addEventListener("click", () => {
  popup.style.display = "none";
});

// Fecha o pop-up ao clicar fora dele
window.addEventListener("click", (event) => {
  if (event.target == popup) {
    popup.style.display = "none";
  }
});

function openEditPopup(element) {
  // Recupera os dados a partir dos atributos data- do elemento
  const cnpj = element.getAttribute("data-cnpj");
  const nome = element.getAttribute("data-nome");
  const razaosocial = element.getAttribute("data-razaosocial");

  // Define os valores nos campos do formulário
  document.getElementById("editCnpj").value = cnpj;
  document.getElementById("editNome").value = nome;
  document.getElementById("editRazaoSocial").value = razaosocial;

  // Abre o pop-up de edição
  document.getElementById("popupEdit").style.display = "block";
}
// Função para fechar o pop-up de edição
document.getElementById("closePopupEdit").onclick = function () {
  document.getElementById("popupEdit").style.display = "none";
};
// Fechar o pop-up de edição se clicar fora dele
window.onclick = function (event) {
  const popupEdit = document.getElementById("popupEdit");
  if (event.target == popupEdit) {
    popupEdit.style.display = "none";
  }
};


document.addEventListener("DOMContentLoaded", function () {
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
});
