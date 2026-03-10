const API_URL = "http://localhost:8080/api";

let user = JSON.parse(localStorage.getItem("user"));

if (!user || user.role !== "customer") window.location.href = "../login/login.html";

function updateHeader() {
  document.getElementById("usernameDisplay").textContent = user.username;
  document.getElementById("saldoDisplay").textContent = user.saldo.toLocaleString("id-ID");
}
updateHeader();

document.getElementById("logoutBtn").addEventListener("click", () => {
  localStorage.removeItem("user");
  window.location.href = "../login/login.html";
});

// Tabs
function switchTab(tab) {
  document.getElementById("viewBrowse").classList.toggle("hidden", tab !== "browse");
  document.getElementById("viewTickets").classList.toggle("hidden", tab !== "tickets");
  document.getElementById("tabBrowse").classList.toggle("active", tab === "browse");
  document.getElementById("tabTickets").classList.toggle("active", tab === "tickets");

  if (tab === "browse") loadSchedules();
  else loadTickets();
}

// fungsi untuk load seluruh schedules di halaman customer
async function loadSchedules() {
  try {
    const res = await fetch(`${API_URL}/schedules`);
    const result = await res.json();
    const grid = document.getElementById("scheduleGrid");
    grid.innerHTML = "";

    const hargaMap = { Reguler: 45000, Premium: 75000, VIP: 120000 };

    if (!result.data || result.data.length === 0) {
      grid.innerHTML =
        '<p class="text-muted" style="grid-column: 1/-1; text-align: center; padding: 50px; font-size: 1.2rem;">Belum ada jadwal tayang saat ini.</p>';
      return;
    }

    result.data.forEach((sch) => {
      const harga = hargaMap[sch.studio.tipeStudio] || 45000;
      grid.innerHTML += `
                <div class="card">
                    <h4>${sch.movie.namaFilm}</h4>
                    <span class="badge">${sch.movie.genre}</span>
                    <p class="text-muted" style="font-size: 0.9rem;">${sch.movie.durasi} Menit</p>
                    <hr style="border-color: var(--border-color); margin: 15px 0;">
                    <div class="flex-between">
                        <span>${sch.studio.namaStudio} <br><small style="color: var(--cinematic-yellow)">Jam: ${
        sch.jamTayang
      }</small></span>
                        <div class="text-right">
                            <strong class="price">Rp ${harga.toLocaleString("id-ID")}</strong><br>
                            <button class="btn-sm" style="margin-top: 10px;" onclick="openModal('${
                              sch.movie.namaFilm
                            }', '${sch.studio.namaStudio}', '${sch.jamTayang}', ${harga})">BELI</button>
                        </div>
                    </div>
                </div>
            `;
    });
  } catch (error) {
    console.error("Gagal memuat jadwal:", error);
    document.getElementById("scheduleGrid").innerHTML =
      '<p class="text-danger" style="grid-column: 1/-1; text-align: center;">Gagal memuat jadwal tayang. Pastikan backend Java berjalan.</p>';
  }
}

// fungsi untuk load tiket di halaman customer
async function loadTickets() {
  try {
    const res = await fetch(`${API_URL}/users/${user.username}/bookings`);
    const result = await res.json();
    const grid = document.getElementById("ticketGrid");

    if (!result.data || result.data.length === 0) {
      grid.innerHTML =
        '<p class="text-muted" style="grid-column: 1/-1; text-align: center; padding: 50px; font-size: 1.2rem;">Belum ada tiket yang dibeli.</p>';
      return;
    }

    grid.innerHTML = result.data
      .map(
        (t) => `
          <div class="ticket-card">
              <div class="ticket-header">ADMIT ONE</div>
              <div class="ticket-body">
                  <h4>${t.jadwalFilm.movie.namaFilm}</h4>
                  <p style="color: var(--text-muted); margin-top: 5px;">Studio: ${t.jadwalFilm.studio.namaStudio}</p>
                  <p style="color: var(--text-muted);">Waktu: ${t.jadwalFilm.jamTayang}</p>
                  <div class="seat-badge">SEAT: ${t.seat}</div>
              </div>
          </div>
      `
      )
      .join("");
  } catch (error) {
    console.error("Gagal memuat tiket:", error);
    document.getElementById("ticketGrid").innerHTML =
      '<p class="text-danger" style="grid-column: 1/-1; text-align: center;">Gagal memuat tiket Anda.</p>';
  }
}

function openModal(movie, studio, time, price) {
  document.getElementById("bookMovie").value = movie;
  document.getElementById("bookStudio").value = studio;
  document.getElementById("bookTime").value = time;
  document.getElementById("bookPrice").value = price;
  document.getElementById("bookSeat").value = "";

  document.getElementById("bookingInfo").innerHTML = `
        <p><strong>Film:</strong> ${movie}</p>
        <p><strong>Studio:</strong> ${studio}</p>
        <p><strong>Jam:</strong> ${time}</p>
        <p class="price-highlight"><strong>Harga: Rp ${price.toLocaleString("id-ID")}</strong></p>
    `;
  document.getElementById("bookingModal").classList.remove("hidden");
}

function closeModal() {
  document.getElementById("bookingModal").classList.add("hidden");
}

document.getElementById("bookingForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const price = parseInt(document.getElementById("bookPrice").value);

  if (user.saldo < price) {
    alert("Saldo tidak mencukupi!");
    return;
  }

  const payload = {
    username: user.username,
    namaFilm: document.getElementById("bookMovie").value,
    namaStudio: document.getElementById("bookStudio").value,
    jamTayang: document.getElementById("bookTime").value,
    seat: document.getElementById("bookSeat").value.toUpperCase(),
  };

  try {
    const res = await fetch(`${API_URL}/bookings`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });

    if (res.ok) {
      user.saldo -= price;
      localStorage.setItem("user", JSON.stringify(user));
      updateHeader();
      closeModal();
      alert("Tiket berhasil dipesan!");
      switchTab("tickets");
    } else {
      const result = await res.json();
      alert(result.message || "Gagal memesan tiket.");
    }
  } catch (error) {
    console.error("Booking error:", error);
    alert("Terjadi kesalahan sistem saat memesan tiket.");
  }
});

window.onload = () => switchTab("browse");
