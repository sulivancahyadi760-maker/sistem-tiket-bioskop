const API_URL = "http://localhost:8080/api";
let user = JSON.parse(localStorage.getItem("user"));

// cek kalo user udah gaada maka lempar ke logout
if (!user || user.role !== "customer") window.location.href = "../login/login.html";

function updateHeader() {
  document.getElementById("usernameDisplay").textContent = user.username;
  document.getElementById("saldoDisplay").textContent = user.saldo.toLocaleString("id-ID");
}
updateHeader();

// Logout
document.getElementById("logoutBtn").addEventListener("click", () => {
  localStorage.removeItem("user");
  window.location.href = "../login/login.html";
});

// Tab Switcher
function switchTab(tab) {
  document.getElementById("viewBrowse").classList.toggle("hidden", tab !== "browse");
  document.getElementById("viewTickets").classList.toggle("hidden", tab !== "tickets");
  document.getElementById("tabBrowse").classList.toggle("active", tab === "browse");
  document.getElementById("tabTickets").classList.toggle("active", tab === "tickets");

  if (tab === "browse") loadSchedules();
  else loadTickets();
}

// load jadwal film fetch dari backend
async function loadSchedules() {
  try {
    const res = await fetch(`${API_URL}/schedules`);
    const result = await res.json();
    const grid = document.getElementById("scheduleGrid");
    grid.innerHTML = "";

    if (!result.data || result.data.length === 0) {
      grid.innerHTML =
        '<p class="text-muted" style="grid-column: 1/-1; text-align: center; padding: 50px;">Belum ada jadwal tayang.</p>';
      return;
    }

    result.data.forEach((sch) => {
      const harga = sch.harga;
      grid.innerHTML += `
                <div class="movie-card">
                    <div class="card-visual">
                        <span class="badge-genre">${sch.movie.genre}</span>
                        <span class="duration-tag">${sch.movie.durasi}m</span>
                    </div>
                    <div class="card-details">
                        <h4 class="movie-title">${sch.movie.namaFilm}</h4>
                        <div class="info-row">
                            <div class="info-group">
                                <label>STUDIO</label>
                                <span>${sch.studio.namaStudio}</span>
                            </div>
                            <div class="info-group text-right">
                                <label>WAKTU</label>
                                <span class="time-highlight">${sch.jamTayang}</span>
                            </div>
                        </div>
                        <div class="card-action">
                            <div class="price-container">
                                <label style="font-size: 9px; color: #666; display: block;">HARGA TIKET</label>
                                <strong class="price-text">Rp ${harga.toLocaleString("id-ID")}</strong>
                            </div>
                            <button class="btn-buy" onclick="openModal('${sch.movie.namaFilm}', '${
        sch.studio.namaStudio
      }', '${sch.jamTayang}', ${harga})">
                                BELI TIKET
                            </button>
                        </div>
                    </div>
                </div>`;
    });
  } catch (error) {
    console.error("Gagal load jadwal:", error);
  }
}

// load tiket yang udah dipesen sama user
const loadTickets = async () => {
  try {
    const res = await fetch(`${API_URL}/users/${user.username}/bookings`);
    const result = await res.json();
    const grid = document.getElementById("ticketGrid");
    grid.innerHTML = "";

    if (!result.data || result.data.length === 0) {
      grid.innerHTML =
        '<p class="text-muted" style="grid-column: 1/-1; text-align: center; padding: 50px;">Belum ada tiket.</p>';
      return;
    }

    grid.innerHTML = result.data
      .map(
        (t) => `
            <div class="ticket-card">
                <div class="ticket-header"><span>ADMIT ONE</span></div>
                <div class="ticket-body">
                    <h4 style="font-size: 1.4rem; text-transform: uppercase; margin-bottom: 10px;">${t.namaFilm}</h4>
                    <div style="font-size: 12px; color: #aaa;">
                        <p><strong>STUDIO:</strong> ${t.namaStudio}</p>
                        <p><strong>TIME:</strong> ${t.jamTayang}</p>
                    </div>
                    <div class="perforation"></div>
                    <div style="display: flex; justify-content: space-between; align-items: center;">
                        <div>
                            <small style="color: #666; font-weight: bold; display: block; font-size: 10px;">SEAT</small>
                            <div class="seat-number">${t.seat}</div>
                        </div>
                        <div class="barcode"></div>
                    </div>
                </div>
            </div>`
      )
      .join("");
  } catch (error) {
    console.error("Gagal load tiket:", error);
  }
};

async function openModal(movie, studio, time, price) {
  document.getElementById("bookMovie").value = movie;
  document.getElementById("bookStudio").value = studio;
  document.getElementById("bookTime").value = time;
  document.getElementById("bookPrice").value = price;
  document.getElementById("bookSeat").value = "";
  document.getElementById("selectedSeatLabel").textContent = "NONE";
  
  // Perbaiki warna kuning, sebelumnya --cinematic-yellow yang tidak defined
  document.getElementById("bookingInfo").innerHTML = `
        <p><strong>Film:</strong> ${movie}</p>
        <p><strong>Studio:</strong> ${studio} | <strong>Jam:</strong> ${time}</p>
        <p style="color: var(--accent-neon); font-size: 1.2rem; margin-top: 10px;"><strong>Harga: Rp ${price.toLocaleString(
          "id-ID"
        )}</strong></p>`;
  
  document.getElementById("bookingModal").classList.remove("hidden");
  document.getElementById("seatGrid").innerHTML = '<p style="grid-column: 1/-1; text-align: center; color: var(--accent-neon); font-family: var(--font-display);">LOADING SEATING PLAN...</p>';

  try {
    const res = await fetch(`${API_URL}/tickets`);
    const result = await res.json();
    const bookedSeats = (result.data || [])
      .filter(t => t.namaFilm === movie && t.namaStudio === studio && t.jamTayang === time)
      .map(t => t.seat.toUpperCase());

    generateSeatGrid(bookedSeats);
  } catch (error) {
    document.getElementById("seatGrid").innerHTML = '<p style="grid-column: 1/-1; text-align: center; color: #ff3333;">ERROR LOADING SEATS.</p>';
  }
}

function generateSeatGrid(bookedSeats) {
  const rows = ['A', 'B', 'C', 'D', 'E'];
  const cols = 8;
  const grid = document.getElementById("seatGrid");
  grid.innerHTML = "";

  rows.forEach(r => {
    for (let c = 1; c <= cols; c++) {
      const seatId = `${r}-${c}`;
      const isBooked = bookedSeats.includes(seatId);
      
      const seatEl = document.createElement("div");
      seatEl.className = `seat ${isBooked ? 'seat-booked' : 'seat-available'}`;
      seatEl.textContent = seatId;
      
      if (!isBooked) {
        seatEl.onclick = () => selectSeat(seatId, seatEl);
      }
      grid.appendChild(seatEl);
    }
  });
}

function selectSeat(seatId, seatEl) {
  document.querySelectorAll(".seat-selected").forEach(el => {
    if(el !== seatEl) el.classList.remove("seat-selected");
  });
  
  seatEl.classList.toggle("seat-selected");
  
  const input = document.getElementById("bookSeat");
  const label = document.getElementById("selectedSeatLabel");
  
  if (seatEl.classList.contains("seat-selected")) {
    input.value = seatId;
    label.textContent = seatId;
  } else {
    input.value = "";
    label.textContent = "NONE";
  }
}

function closeModal() {
  document.getElementById("bookingModal").classList.add("hidden");
}

// form submit pesan tiket
document.getElementById("bookingForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  
  const seatInput = document.getElementById("bookSeat").value;
  if (!seatInput) {
    if(window.utils) window.utils.showAlert("TARGET SEAT REQUIRED.", "error");
    else alert("Silakan pilih tempat duduk terlebih dahulu.");
    return;
  }
  
  const price = parseInt(document.getElementById("bookPrice").value);
  if (user.saldo < price) {
    if(window.utils) window.utils.showAlert("INSUFFICIENT FUNDS.", "error");
    else alert("Saldo tidak mencukupi!");
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
      if(window.utils) window.utils.showAlert("TRANSACTION COMPLETED.", "success");
      else alert("Tiket berhasil dipesan!");
      switchTab("tickets");
    } else {
      const errJson = await res.json();
      if(window.utils) window.utils.showAlert(errJson.message || "FAILED TO ACQUIRE TICKET.", "error");
      else alert(errJson.message || "Gagal memesan.");
    }
  } catch (error) {
    if(window.utils) window.utils.showAlert("CRITICAL SYSTEM FAILURE.", "error");
    else alert("Terjadi kesalahan sistem.");
  }
});

window.onload = () => switchTab("browse");
