const API_BASE_URL = "http://localhost:8080/api";

document.addEventListener("DOMContentLoaded", () => {
  const menuItems = document.querySelectorAll("#sidebar-menu li[data-target]");
  const views = document.querySelectorAll(".view-section");
  const navbarTitle = document.getElementById("navbar-title");

  // Authentication Check
  if (window.utils && window.utils.checkAuth) {
    const user = window.utils.checkAuth();
    if (!user || user.role !== "admin") {
      // Redirect if not admin
      window.location.href = "../login/login.html";
      return;
    }
  }

  // Handle Logout
  const logoutBtn = document.getElementById("logout-btn");
  if (logoutBtn) {
    logoutBtn.addEventListener("click", (e) => {
      e.preventDefault();
      if (window.utils && window.utils.logout) {
        window.utils.logout();
      }
    });
  }

  // Sidebar navigation logic
  menuItems.forEach((item) => {
    item.addEventListener("click", () => {
      menuItems.forEach((m) => m.classList.remove("active"));
      item.classList.add("active");

      views.forEach((view) => view.classList.add("hidden"));
      const targetId = item.getAttribute("data-target");
      const targetView = document.getElementById(targetId);
      if (targetView) targetView.classList.remove("hidden");

      navbarTitle.textContent = item.textContent.trim();

      // Fetch data based on view
      if (targetId === "view-manajemen-film" || targetId === "view-dashboard") {
        fetchMovies();
      } else if (targetId === "view-jadwal") {
        fetchSchedules();
      } else if (targetId === "view-tiket") {
        fetchTickets();
      } else if (targetId === "view-pengguna") {
        fetchUsers();
      }
    });
  });

  fetchMovies();
  fetchSchedules();
  fetchTickets();
  fetchUsers();

  // Chart Rendering
  const canvasElement = document.getElementById("salesChart");
  if (canvasElement && typeof Chart !== "undefined") {
    const ctx = canvasElement.getContext("2d");
    new Chart(ctx, {
      type: "bar",
      data: {
        labels: ["Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min"],
        datasets: [
          {
            label: "Tiket Terjual",
            data: [60, 80, 50, 90, 70, 40, 85],
            backgroundColor: "#e5a909",
            borderRadius: 5,
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { display: false } },
        scales: {
          y: {
            beginAtZero: true,
            grid: { display: true, color: "#333" },
            ticks: { color: "#a6a6a6" },
          },
          x: {
            grid: { display: false },
            ticks: { color: "#a6a6a6" },
          },
        },
      },
    });
  }
});

// fetch data from API
const fetchMovies = async () => {
  try {
    const res = await fetch(`${API_BASE_URL}/movies`);
    const json = await res.json();
    if (json.data) {
      renderMovies(json.data);
      updateDashboardStat("stat-total-movies", json.data.length);
    }
  } catch (error) {
    console.error("Failed to fetch movies", error);
  }
};

const fetchSchedules = async () => {
  try {
    const res = await fetch(`${API_BASE_URL}/schedules`);
    const json = await res.json();
    if (json.data) {
      renderSchedules(json.data);
      updateDashboardStat("stat-active-schedules", json.data.length);
    }
  } catch (error) {
    console.error("Failed to fetch schedules", error);
  }
};

const fetchTickets = async () => {
  try {
    const res = await fetch(`${API_BASE_URL}/tickets`);
    const json = await res.json();
    if (json.data) {
      renderTickets(json.data);
      updateDashboardStat("stat-total-tickets", json.data.length);
    }
  } catch (error) {
    console.error("Failed to fetch tickets", error);
  }
};

const fetchUsers = async () => {
  try {
    const res = await fetch(`${API_BASE_URL}/users`);
    const json = await res.json();
    if (json.data) {
      renderUsers(json.data);
      updateDashboardStat("stat-total-users", json.data.length);
    }
  } catch (error) {
    console.error("Failed to fetch users", error);
  }
};

// render data ke DOM
const renderMovies = (movies) => {
  const tbodyManage = document.getElementById("tbody-manage-movies");
  const tbodyDash = document.getElementById("tbody-dash-movies");

  if (!tbodyManage && !tbodyDash) return;

  let htmlManage = "";
  let htmlDash = "";

  movies.forEach((m, idx) => {
    htmlManage += `
      <tr>
        <td>${idx + 1}</td>
        <td>-</td> <!-- Poster mock -->
        <td>${m.namaFilm}</td>
        <td>${m.genre}</td>
        <td>${m.durasi} min</td>
        <td>Aktif</td>
        <td>
          <button class="btn-delete" onclick="deleteMovie('${m.namaFilm}')">Hapus</button>
        </td>
      </tr>
    `;

    htmlDash += `
      <tr>
        <td>${idx + 1}</td>
        <td>${m.namaFilm}</td>
        <td>${m.genre}</td>
        <td>${m.durasi} min</td>
        <td>Aktif</td>
        <td><button class="btn-delete" onclick="deleteMovie('${m.namaFilm}')">Hapus</button></td>
      </tr>
    `;
  });

  if (tbodyManage) tbodyManage.innerHTML = htmlManage;
  if (tbodyDash) tbodyDash.innerHTML = htmlDash;
};

const renderSchedules = (schedules) => {
  const tbodySched = document.getElementById("tbody-schedules");
  if (!tbodySched) return;

  let html = "";
  schedules.forEach((s, idx) => {
    html += `
      <tr>
        <td>${idx + 1}</td>
        <td class="fw-600 text-yellow">${s.movie ? s.movie.namaFilm : "-"}</td>
        <td>${s.studio ? s.studio.namaStudio : "-"}</td>
        <td>-</td> <!-- Asumsi tanggal tidak ada di response model untuk sekarang -->
        <td class="text-blue fw-500">${s.jamTayang}</td>
        <td class="text-green fw-600">Rp ${s.harga ? s.harga.toLocaleString('id-ID') : 0}</td>
        <td><button class="btn-delete" onclick="deleteSchedule('${s.movie ? s.movie.namaFilm : ""}', '${s.studio ? s.studio.namaStudio : ""}', '${s.jamTayang}')">Hapus</button></td>
      </tr>
    `;
  });

  tbodySched.innerHTML = html;
};

const renderTickets = (tickets) => {
  const tbodyTickets = document.getElementById("tbody-tickets");
  if (!tbodyTickets) return;

  let html = "";
  tickets.forEach((t, idx) => {
    html += `
      <tr>
        <td>${idx + 1}</td>
        <td class="fw-500 text-yellow">${t.username}</td>
        <td class="fw-600">${t.namaFilm} <br> <small class="text-muted">${t.namaStudio} - ${t.seat}</small></td>
        <td class="text-green fw-600">Rp ${t.harga.toLocaleString('id-ID')}</td>
        <td>-</td>
      </tr>
    `;
  });

  tbodyTickets.innerHTML = html;
};

const renderUsers = (users) => {
  const tbodyUsers = document.getElementById("tbody-users");
  if (!tbodyUsers) return;

  let html = "";
  users.forEach((u, idx) => {
    html += `
      <tr>
        <td>${idx + 1}</td>
        <td>${u.username} <span class="badge" style="font-size:0.75rem; margin-left:5px; background:var(--cinematic-yellow); color:#000;">${u.role}</span></td>
        <td class="text-green">Rp ${u.saldo ? u.saldo.toLocaleString('id-ID') : 0}</td>
        <td>Aktif</td>
        <td style="display: flex; gap: 10px;">
          <button class="btn" style="padding: 5px 15px; font-size: 0.8rem; background: var(--bg-card); border-color: var(--accent-neon); color: var(--accent-neon);" onclick="openTopupModal('${u.username}')">Top-Up</button>
          <button class="btn-delete" onclick="deleteUser('${u.username}')">Suspend</button>
        </td>
      </tr>
    `;
  });

  tbodyUsers.innerHTML = html;
};



const deleteMovie = async (namaFilm) => {
  if (confirm(`Hapus film ${namaFilm}?`)) {
    try {
      const res = await fetch(`${API_BASE_URL}/movies/${namaFilm}`, { method: "DELETE" });
      const json = await res.json();
      if (res.ok) {
        alert("Film dihapus!");
        fetchMovies(); 
      } else {
        alert("Gagal menghapus: " + json.message);
      }
    } catch (err) {
      console.error(err);
    }
  }
}

const deleteSchedule = async (namaFilm, namaStudio, jamTayang) => {
  if (confirm(`Hapus jadwal ${namaFilm} di ${namaStudio} jam ${jamTayang}?`)) {
    try {
      const res = await fetch(`${API_BASE_URL}/schedules`, { 
        method: "DELETE",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ namaFilm, namaStudio, jamTayang })
      });
      const json = await res.json();
      if (res.ok) {
        alert("Jadwal dihapus!");
        fetchSchedules(); 
      } else {
        alert("Gagal menghapus: " + json.message);
      }
    } catch (err) { console.error(err); }
  }
}

const deleteUser = async (username) => {
  if (confirm(`Suspend / Hapus user ${username}?`)) {
    try {
      const res = await fetch(`${API_BASE_URL}/users/${username}`, { method: "DELETE" });
      const json = await res.json();
      if (res.ok) {
        alert("User dihapus!");
        fetchUsers(); 
      } else {
        alert("Gagal menghapus: " + json.message);
      }
    } catch (err) { console.error(err); }
  }
}

function updateDashboardStat(id, value) {
  const el = document.getElementById(id);
  if (el) el.textContent = value;
}

// --- MODALS ---
function openMovieModal() {
  document.getElementById("movieModal").classList.remove("hidden");
}
function closeMovieModal() {
  document.getElementById("movieModal").classList.add("hidden");
  const frm = document.getElementById("formAddMovie");
  if(frm) frm.reset();
}

async function openScheduleModal() {
  try {
    const resM = await fetch(`${API_BASE_URL}/movies`);
    const jsonM = await resM.json();
    const selM = document.getElementById("scheduleMovie");
    selM.innerHTML = "";
    if (jsonM.data) {
      jsonM.data.forEach(m => {
        selM.innerHTML += `<option value="${m.namaFilm}">${m.namaFilm}</option>`;
      });
    }

    const resS = await fetch(`${API_BASE_URL}/studios`);
    const jsonS = await resS.json();
    const selS = document.getElementById("scheduleStudio");
    selS.innerHTML = "";
    if (jsonS.data) {
      jsonS.data.forEach(s => {
        selS.innerHTML += `<option value="${s.namaStudio}">${s.namaStudio} (${s.tipeStudio})</option>`;
      });
    }

    document.getElementById("scheduleModal").classList.remove("hidden");
  } catch(e) { console.error(e); }
}

function closeScheduleModal() {
  document.getElementById("scheduleModal").classList.add("hidden");
  const frm = document.getElementById("formAddSchedule");
  if(frm) frm.reset();
}

// Form logic hooks
const formAddMovie = document.getElementById("formAddMovie");
if(formAddMovie) {
  formAddMovie.addEventListener("submit", async(e) => {
    e.preventDefault();
    const payload = {
      namaFilm: document.getElementById("movieTitle").value,
      durasi: parseInt(document.getElementById("movieDuration").value),
      genre: document.getElementById("movieGenre").value
    };
    try {
      const res = await fetch(`${API_BASE_URL}/movies`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });
      const json = await res.json();
      if(res.ok) {
        alert("Film berhasil ditambahkan!");
        closeMovieModal();
        fetchMovies();
      } else {
        alert(json.message);
      }
    } catch(e) { console.error(e); }
  });
}

const formAddSchedule = document.getElementById("formAddSchedule");
if(formAddSchedule) {
  formAddSchedule.addEventListener("submit", async(e) => {
    e.preventDefault();
    const payload = {
      namaFilm: document.getElementById("scheduleMovie").value,
      namaStudio: document.getElementById("scheduleStudio").value,
      jamTayang: document.getElementById("scheduleTime").value
    };
    try {
      const res = await fetch(`${API_BASE_URL}/schedules`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });
      const json = await res.json();
      if(res.ok) {
        if(window.utils) window.utils.showAlert("JADWAL ALLOCATED.", "success");
        else alert("Jadwal berhasil ditambahkan!");
        closeScheduleModal();
        fetchSchedules();
      } else {
        if(window.utils) window.utils.showAlert(json.message || "ALLOCATION FAILED.", "error");
        else alert(json.message);
      }
    } catch(e) { console.error(e); }
  });
}

// Top Up Modal hooks
function openTopupModal(username) {
  document.getElementById("topupUsername").value = username;
  document.getElementById("topupTarget").value = username;
  document.getElementById("topupAmount").value = "";
  document.getElementById("topupModal").classList.remove("hidden");
}

function closeTopupModal() {
  document.getElementById("topupModal").classList.add("hidden");
  const frm = document.getElementById("formTopUp");
  if(frm) frm.reset();
}

const formTopUp = document.getElementById("formTopUp");
if (formTopUp) {
  formTopUp.addEventListener("submit", async(e) => {
    e.preventDefault();
    const payload = {
      username: document.getElementById("topupUsername").value,
      jumlah: parseInt(document.getElementById("topupAmount").value)
    };
    try {
      const res = await fetch(`${API_BASE_URL}/users/topup`, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(payload)
      });
      const json = await res.json();
      if(res.ok) {
        if(window.utils) window.utils.showAlert(`FUNDS TRANSFERRED TO ${payload.username}`, "success");
        else alert("Topup sukses!");
        closeTopupModal();
        fetchUsers();
      } else {
        if(window.utils) window.utils.showAlert(json.message || "TRANSFER REJECTED.", "error");
        else alert("Gagal Topup");
      }
    } catch(err) {
       if(window.utils) window.utils.showAlert("NETWORK ERROR.", "error");
       console.error(err);
    }
  });
}
