export function checkAdminStatus(callback) {
  fetch("/api/admin/status", { credentials: "include" })
    .then(res => {
      if (!res.ok) throw new Error("Não autorizado");
      return res.json();
    })
    .then(data => {
      if (data.status === "ok") {
        callback(); 
      } else {
        window.location.href = "home.html";
      }
    })
    .catch(() => window.location.href = "home.html");
}

export function setupLogoutButton() {
  const logoutButton = document.getElementById("btnLogout");
  if (logoutButton) {
    logoutButton.addEventListener("click", () => {
      fetch("/api/logout", { method: "POST", credentials: "include" })
        .then(() => window.location.href = "home.html")
        .catch(() => alert("Erro ao fazer logout"));
    });
  }
}


  export function checkLogin() {
      const username = document.getElementById("username").value;
      const password = document.getElementById("password").value;

      fetch("/api/login", {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({ username, password })
      })
      .then(response => response.json())
      .then(response => {
        if (response.status === "ok") {
          localStorage.setItem("adminLogado", "true"); 
          window.location.href = "manager.html"; 
        } else {
          document.getElementById("message").innerText = response.mensagem || "Usuário ou senha incorretos.";
        }
      })
      .catch(err => {
        console.error("Erro na requisição:", err);
        document.getElementById("message").innerText = "Erro de conexão com o servidor.";
      });
    }

    

    export function recoverPassword() {
    const username = document.getElementById('recoveryUsername').value;
    const message = document.getElementById('message');

    if (!username) {
      message.textContent = "Informe seu usuário.";
      return;
    }

    fetch("/recovery-password", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username })
    })
    .then(res => res.text())
    .then(data => {
      message.style.color = "lightgreen";
      message.textContent = data;
    })
    .catch(err => {
      console.error("Erro:", err);
      message.style.color = "lightgreen";
      message.textContent = "Se o usuário existir, enviaremos instruções por e-mail.";
    });
  }

document.addEventListener("DOMContentLoaded", () => {
  setupLogoutButton();

  const btnRecover = document.getElementById("btnRecover");
  if (btnRecover) btnRecover.addEventListener("click", recoverPassword);

  const btnLogin = document.getElementById("btnLogin");
  if (btnLogin) btnLogin.addEventListener("click", checkLogin);
});
