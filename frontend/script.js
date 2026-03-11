document.addEventListener("DOMContentLoaded", () => {
  window.utils = {
    /**
     * mengembalikan format rupiah Indonesia
     * @param {number} number - angka yang ingin di format
     * @returns {string} - angka yang sudah di format Rupiah
     * @example formatRupiah(1000) // "Rp1.000"
     */
    formatRupiah: (number) => {
      return new Intl.NumberFormat("id-ID", {
        style: "currency",
        currency: "IDR",
        minimumFractionDigits: 0,
      }).format(number);
    },
    // fungsi untuk cek auth
    checkAuth: () => {
      const user = localStorage.getItem("user");
      return user ? JSON.parse(user) : null;
    },
    // fungsi untuk logout
    logout: () => {
      localStorage.removeItem("user");
      window.location.href = "../index.html";
    },
    // fungsi untuk menampilkan alert
    showAlert: (msg, type = "info") => {
      alert(`[${type.toUpperCase()}] ${msg}`);
    },
  };
});
