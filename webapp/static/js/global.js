document.addEventListener("DOMContentLoaded", () => {
  const sidebarContainer = document.getElementById("sidebar-container");

  if (sidebarContainer) {
   
    fetch("sidebar.html") 
      .then((response) => {
        if (response.ok) {
          return response.text();
        }
        throw new Error("Não foi possível carregar a sidebar.");
      })
      .then((html) => {
        sidebarContainer.innerHTML = html;

        const logoutButton = document.getElementById("btn-logout");
        if (logoutButton) {
          logoutButton.addEventListener("click", () => {
            // (Aqui você faria o fetch para o seu LogoutServlet)
            alert("Função de Logout a ser implementada!");
            // Ex: fetch('../logout', { method: 'POST' });
            window.location.href = "login.html"; // Redireciona para o login
          });
        }
      })
      .catch((error) => {
        console.error("Erro ao carregar a sidebar:", error);
        sidebarContainer.innerHTML =
          "<p class='text-red-500'>Erro ao carregar a sidebar.</p>";
      });
  }
});
