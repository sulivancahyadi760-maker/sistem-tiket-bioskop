

document.addEventListener("DOMContentLoaded", () => {
  // Expose handy utilities globally
  window.utils = {
    formatRupiah: (number) => {
      return new Intl.NumberFormat("id-ID", {
        style: "currency",
        currency: "IDR",
        minimumFractionDigits: 0,
      }).format(number);
    },
    // Simple check if user is logged in
    checkAuth: () => {
      const user = localStorage.getItem("user");
      return user ? JSON.parse(user) : null;
    },
    logout: () => {
      localStorage.removeItem("user");
      window.location.href = "../index.html"; // Adjust based on relativity, typically handled in specific dashboards
    },
    // Show sweet alert or custom toast theoretically (for now simplistic alert fallback if needed)
    showAlert: (msg, type = "info") => {
      alert(`[${type.toUpperCase()}] ${msg}`);
    },
  };
});
