let isLoginMode = true;

document.getElementById("toggleAuth").addEventListener("click", (e) => {
  e.preventDefault();
  isLoginMode = !isLoginMode;

  document.getElementById("authTitle").textContent = isLoginMode ? "Login to your account" : "Create an account";
  document.getElementById("authSubtitle").textContent = isLoginMode
    ? "Enter your email below to login to your account"
    : "Enter your details below to create your account";
  document.getElementById("submitBtn").textContent = isLoginMode ? "Login" : "Sign up";
  document.getElementById("toggleText").textContent = isLoginMode
    ? "Don't have an account?"
    : "Already have an account?";
  document.getElementById("toggleAuth").textContent = isLoginMode ? "Sign up" : "Login";

  if (isLoginMode) {
    document.getElementById("registerFields").classList.add("hidden");
  } else {
    document.getElementById("registerFields").classList.remove("hidden");
  }
});

document.getElementById("authForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const username = document.getElementById("username").value;
  const password = document.getElementById("password").value;
  const errorMsg = document.getElementById("errorMsg");

  try {
    if (isLoginMode) {
      const res = await fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password }),
      });
      const result = await res.json();

      if (res.ok) {
        const userData = result.data;
        localStorage.setItem("user", JSON.stringify(userData));
        window.location.href = userData.role === "admin" ? "../admin/dashboard.html" : "../customer/customer.html";
      } else {
        errorMsg.textContent = result.message || "Login gagal";
        errorMsg.className = "error text-danger";
      }
    } else {
      const saldo = 0; 
      const role = document.getElementById("role").value;
      const res = await fetch("http://localhost:8080/api/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password, saldo: saldo, role }),
      });
      const result = await res.json();

      if (res.ok) {
        errorMsg.textContent = "Registrasi sukses! Silakan login.";
        errorMsg.className = "error text-success";
        document.getElementById("toggleAuth").click();
      } else {
        errorMsg.textContent = result.message || "Registrasi gagal";
        errorMsg.className = "error text-danger";
      }
    }
  } catch (err) {
    errorMsg.textContent = "Gagal terhubung ke server";
  }
});
