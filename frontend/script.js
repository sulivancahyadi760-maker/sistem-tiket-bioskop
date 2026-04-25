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
    let container = document.getElementById("toast-container");
    if (!container) {
      container = document.createElement("div");
      container.id = "toast-container";
      document.body.appendChild(container);
    }

    const toast = document.createElement("div");
    toast.className = `toast toast-${type}`;
    
    const text = document.createElement("span");
    text.className = "toast-message";
    text.textContent = msg;

    const closeBtn = document.createElement("button");
    closeBtn.className = "toast-close";
    closeBtn.innerHTML = "&times;";
    closeBtn.onclick = (e) => {
      e.preventDefault();
      toast.classList.remove("show");
      setTimeout(() => toast.remove(), 400);
    };

    toast.appendChild(text);
    toast.appendChild(closeBtn);
    container.appendChild(toast);

    // Trigger reflow safely using requestAnimationFrame
    requestAnimationFrame(() => {
      requestAnimationFrame(() => {
        toast.classList.add("show");
      });
    });

    setTimeout(() => {
      if (toast.parentElement) {
        toast.classList.remove("show");
        setTimeout(() => toast.remove(), 400);
      }
    }, 4000);
  },
};
