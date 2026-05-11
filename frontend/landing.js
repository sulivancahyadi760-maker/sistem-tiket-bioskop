/**
 * landing.js — Root index.html
 * ABSOLUTE CINEMA
 *
 * Fully public — tidak perlu auth untuk browse film.
 * Auth state menentukan UI nav + behavior tombol "Pilih Tiket".
 */

const API_URL = "http://localhost:8080/api";

/* ─────────────────────────────────────────────────────
   AUTH STATE — Deteksi user dari localStorage
───────────────────────────────────────────────────── */
const user = JSON.parse(localStorage.getItem("user")) || null;
const isLoggedIn    = !!user;
const isCustomer    = isLoggedIn && user.role === "customer";
const isAdmin       = isLoggedIn && user.role === "admin";

/* ─────────────────────────────────────────────────────
   NAV — Render sesuai auth state
───────────────────────────────────────────────────── */
function initNav() {
  if (!isLoggedIn) {
    document.getElementById("navGuest").classList.remove("hidden");
  } else if (isCustomer) {
    document.getElementById("navCustomer").classList.remove("hidden");
    document.getElementById("usernameDisplay").textContent = user.username;
    document.getElementById("saldoDisplay").textContent    = user.saldo.toLocaleString("id-ID");

    document.getElementById("logoutBtn").addEventListener("click", () => {
      localStorage.removeItem("user");
      location.reload();
    });
  } else if (isAdmin) {
    document.getElementById("navAdmin").classList.remove("hidden");
    document.getElementById("adminDisplay").textContent = user.username;

    document.getElementById("adminLogoutBtn").addEventListener("click", () => {
      localStorage.removeItem("user");
      location.reload();
    });
  }

  // Sembunyikan CTA section kalau sudah login
  if (isLoggedIn) {
    const cta = document.getElementById("ctaSection");
    if (cta) cta.classList.add("hidden");
  }

  // Sembunyikan tombol "LOGIN UNTUK PESAN" di hero kalau sudah login
  if (isLoggedIn) {
    const heroLoginBtn = document.getElementById("heroLoginBtn");
    if (heroLoginBtn) heroLoginBtn.style.display = "none";
  }
}

/* ─────────────────────────────────────────────────────
   NAV SCROLL EFFECT
───────────────────────────────────────────────────── */
window.addEventListener("scroll", () => {
  document.getElementById("mainNav")
    .classList.toggle("scrolled", window.scrollY > 60);
}, { passive: true });

/* ─────────────────────────────────────────────────────
   SCROLL TO SHOWCASE
───────────────────────────────────────────────────── */
function scrollToShowcase() {
  document.getElementById("showcaseSection").scrollIntoView({ behavior: "smooth" });
}
window.scrollToShowcase = scrollToShowcase;

/* ─────────────────────────────────────────────────────
   POSTER IMAGE POOL — per genre
───────────────────────────────────────────────────── */
const GENRE_POSTERS = {
  "action"      : "https://images.unsplash.com/photo-1536440136628-849c177e76a1?q=80&w=1025&auto=format&fit=crop",
  "aksi"        : "https://images.unsplash.com/photo-1536440136628-849c177e76a1?q=80&w=1025&auto=format&fit=crop",
  "horror"      : "https://images.unsplash.com/photo-1605979399824-f2b08a5f0d77?q=80&w=1025&auto=format&fit=crop",
  "horor"       : "https://images.unsplash.com/photo-1605979399824-f2b08a5f0d77?q=80&w=1025&auto=format&fit=crop",
  "comedy"      : "https://images.unsplash.com/photo-1478720568477-152d9b164e26?q=80&w=1025&auto=format&fit=crop",
  "komedi"      : "https://images.unsplash.com/photo-1478720568477-152d9b164e26?q=80&w=1025&auto=format&fit=crop",
  "romance"     : "https://images.unsplash.com/photo-1596237563267-84ffd99c80e1?q=80&w=1025&auto=format&fit=crop",
  "romantis"    : "https://images.unsplash.com/photo-1596237563267-84ffd99c80e1?q=80&w=1025&auto=format&fit=crop",
  "drama"       : "https://images.unsplash.com/photo-1485846234645-a62644f84728?q=80&w=1025&auto=format&fit=crop",
  "thriller"    : "https://images.unsplash.com/photo-1509347528160-9a9e33742cdb?q=80&w=1025&auto=format&fit=crop",
  "sci-fi"      : "https://images.unsplash.com/photo-1446776811953-b23d57bd21aa?q=80&w=1025&auto=format&fit=crop",
  "animation"   : "https://images.unsplash.com/photo-1594908900066-3f47337549d8?q=80&w=1025&auto=format&fit=crop",
  "animasi"     : "https://images.unsplash.com/photo-1594908900066-3f47337549d8?q=80&w=1025&auto=format&fit=crop",
  "adventure"   : "https://images.unsplash.com/photo-1469474968028-56623f02e42e?q=80&w=1025&auto=format&fit=crop",
  "petualangan" : "https://images.unsplash.com/photo-1469474968028-56623f02e42e?q=80&w=1025&auto=format&fit=crop",
};

const FALLBACK_POSTERS = [
  "https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?q=80&w=1025&auto=format&fit=crop",
  "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?q=80&w=1025&auto=format&fit=crop",
  "https://images.unsplash.com/photo-1595769816263-9b910be24d5f?q=80&w=1025&auto=format&fit=crop",
  "https://images.unsplash.com/photo-1462275646964-a0e3386b89fa?q=80&w=1025&auto=format&fit=crop",
  "https://images.unsplash.com/photo-1574267432553-4b4628081c31?q=80&w=1025&auto=format&fit=crop",
  "https://images.unsplash.com/photo-1626814026160-2237a95fc5a0?q=80&w=1025&auto=format&fit=crop",
];

function getPoster(genre, idx) {
  return GENRE_POSTERS[(genre || "").toLowerCase()]
    || FALLBACK_POSTERS[idx % FALLBACK_POSTERS.length];
}

/* ─────────────────────────────────────────────────────
   DATA STATE
───────────────────────────────────────────────────── */
let allMovies      = [];
let filteredMovies = [];

/* ─────────────────────────────────────────────────────
   FETCH DATA
───────────────────────────────────────────────────── */
async function loadData() {
  try {
    const res      = await fetch(`${API_URL}/schedules`);
    const result   = await res.json();
    const schedules = result.data || [];

    // Group schedules by film
    const map = {};
    schedules.forEach(sch => {
      const key = sch.movie.namaFilm;
      if (!map[key]) {
        map[key] = {
          namaFilm  : sch.movie.namaFilm,
          genre     : sch.movie.genre,
          durasi    : sch.movie.durasi,
          schedules : [],
        };
      }
      map[key].schedules.push({
        studio : sch.studio.namaStudio,
        time   : sch.jamTayang,
        price  : sch.harga,
      });
    });

    allMovies      = Object.values(map);
    filteredMovies = [...allMovies];

    buildGenreFilters();
    renderHeroCarousel();
    renderPosters();
    updateStats(schedules);

  } catch (err) {
    console.error("Gagal load data:", err);
    document.getElementById("posterGrid").innerHTML =
      `<div class="no-movie-placeholder">SIGNAL LOST<br/><small style="font-size:0.7rem;letter-spacing:2px;">Backend tidak terhubung</small></div>`;
    // Show default hero even without data
    resetHeroDefault();
  }
}

/* ─────────────────────────────────────────────────────
   DEFAULT HERO (kalau API gagal)
───────────────────────────────────────────────────── */
function resetHeroDefault() {
  document.getElementById("heroLine1").textContent = "ABSOLUTE";
  document.getElementById("heroLine2").textContent = "CINEMA";
  document.getElementById("heroGenre").textContent = "— PREMIUM CINEMATIC EXPERIENCE";
  document.getElementById("heroDesc").textContent  =
    "Enter the void. A brutally dark, radically luxurious platform designed for those who treat film as ritual.";
}

/* ─────────────────────────────────────────────────────
   GENRE FILTERS
───────────────────────────────────────────────────── */
function buildGenreFilters() {
  const genres = [...new Set(allMovies.map(m => m.genre).filter(Boolean))];
  const bar    = document.getElementById("filterBar");
  genres.forEach(genre => {
    const btn = document.createElement("button");
    btn.className    = "filter-btn";
    btn.textContent  = genre.toUpperCase();
    btn.dataset.genre = genre.toLowerCase();
    btn.onclick      = () => filterByGenre(genre.toLowerCase(), btn);
    bar.appendChild(btn);
  });
}

function filterByGenre(genre, btnEl) {
  document.querySelectorAll(".filter-btn").forEach(b => b.classList.remove("active"));
  btnEl.classList.add("active");
  filteredMovies = genre === "all"
    ? [...allMovies]
    : allMovies.filter(m => (m.genre || "").toLowerCase() === genre);
  renderPosters();
}
window.filterByGenre = filterByGenre;

/* ─────────────────────────────────────────────────────
   HERO CAROUSEL
───────────────────────────────────────────────────── */
let heroIdx   = 0;
let heroTimer = null;
const MAX_HERO_SLIDES = 6;

function renderHeroCarousel() {
  if (!allMovies.length) return;
  const slides = allMovies.slice(0, MAX_HERO_SLIDES);
  const dotWrap = document.getElementById("heroIndicators");
  dotWrap.innerHTML = "";

  slides.forEach((_, i) => {
    const dot = document.createElement("button");
    dot.className    = "hero-dot" + (i === 0 ? " active" : "");
    dot.setAttribute("aria-label", `Film ${i + 1}`);
    dot.onclick      = () => jumpHero(i);
    dotWrap.appendChild(dot);
  });

  showHeroSlide(0);
  heroTimer = setInterval(() => {
    heroIdx = (heroIdx + 1) % slides.length;
    showHeroSlide(heroIdx);
  }, 5500);
}

function jumpHero(i) {
  clearInterval(heroTimer);
  heroIdx = i;
  showHeroSlide(i);
  heroTimer = setInterval(() => {
    heroIdx = (heroIdx + 1) % Math.min(allMovies.length, MAX_HERO_SLIDES);
    showHeroSlide(heroIdx);
  }, 5500);
}

function showHeroSlide(i) {
  const movie = allMovies[i];
  if (!movie) return;

  // Split title for 2-line effect
  const words = movie.namaFilm.toUpperCase().split(" ");
  const mid   = Math.ceil(words.length / 2);
  const line1 = words.slice(0, mid).join(" ");
  const line2 = words.slice(mid).join(" ") || "—";

  const el1 = document.getElementById("heroLine1");
  const el2 = document.getElementById("heroLine2");

  el1.style.opacity = "0";
  el2.style.opacity = "0";
  setTimeout(() => {
    el1.textContent    = line1;
    el2.textContent    = line2;
    el1.style.opacity  = "1";
    el2.style.opacity  = "1";
  }, 280);

  document.getElementById("heroGenre").textContent =
    movie.genre ? `— ${movie.genre.toUpperCase()} · ${movie.durasi} MIN` : "—";
  document.getElementById("heroDesc").textContent =
    `${movie.schedules.length} jadwal tayang tersedia. Dapatkan kursi terbaikmu sekarang.`;

  // Swap hero background
  const reel = document.getElementById("heroReel");
  reel.style.backgroundImage = `url('${getPoster(movie.genre, i)}')`;

  // Sync dots
  document.querySelectorAll(".hero-dot").forEach((d, idx) =>
    d.classList.toggle("active", idx === i));
}

/* ─────────────────────────────────────────────────────
   POSTER GRID
───────────────────────────────────────────────────── */
function renderPosters() {
  const grid = document.getElementById("posterGrid");
  grid.innerHTML = "";

  if (!filteredMovies.length) {
    grid.innerHTML = `<div class="no-movie-placeholder">TIDAK ADA FILM UNTUK GENRE INI</div>`;
    return;
  }

  filteredMovies.forEach((movie, idx) => {
    const posterUrl = getPoster(movie.genre, idx);
    const minPrice  = Math.min(...movie.schedules.map(s => s.price));

    // Tombol aksi — beda tergantung auth state
    const actionBtn = isCustomer
      ? `<button class="poster-cta" onclick="handleBooking('${encodeURIComponent(movie.namaFilm)}')">PILIH TIKET</button>`
      : `<button class="poster-gate" onclick="promptLogin()">LOGIN UNTUK PESAN</button>`;

    const card = document.createElement("div");
    card.className = "poster-card";
    card.innerHTML = `
      <img class="poster-img" src="${posterUrl}" alt="${movie.namaFilm}" loading="lazy" />
      <div class="poster-top">
        <span class="poster-genre-badge">${movie.genre || "FILM"}</span>
        <span class="poster-duration">${movie.durasi}m</span>
      </div>
      <div class="poster-overlay">
        <div class="poster-info">
          <h3 class="poster-movie-title">${movie.namaFilm}</h3>
          <p class="poster-schedule-count">${movie.schedules.length} JADWAL TAYANG</p>
          <div class="poster-price-row">
            <div class="poster-price">
              <small>MULAI DARI</small>
              Rp ${minPrice.toLocaleString("id-ID")}
            </div>
            ${actionBtn}
          </div>
        </div>
      </div>
    `;

    // Staggered entrance
    card.style.cssText = "opacity:0;transform:translateY(18px);transition:opacity 0.5s ease,transform 0.5s ease;";
    card.style.transitionDelay = `${idx * 0.055}s`;
    grid.appendChild(card);

    requestAnimationFrame(() => requestAnimationFrame(() => {
      card.style.opacity   = "1";
      card.style.transform = "translateY(0)";
    }));
  });
}

/* ─────────────────────────────────────────────────────
   BOOKING FLOW
───────────────────────────────────────────────────── */

/** Customer: simpan pilihan dan arahkan ke halaman tiket */
function handleBooking(encodedName) {
  sessionStorage.setItem("selectedMovie", decodeURIComponent(encodedName));
  window.location.href = "customer/customer.html";
}
window.handleBooking = handleBooking;

/** Non-login: redirect ke login dengan pesan */
function promptLogin() {
  // Simpan intended destination
  sessionStorage.setItem("loginRedirect", "customer/customer.html");
  window.location.href = "login/login.html";
}
window.promptLogin = promptLogin;

/* ─────────────────────────────────────────────────────
   STATS
───────────────────────────────────────────────────── */
function updateStats(schedules) {
  const studios = new Set(schedules.map(s => s.studio?.namaStudio).filter(Boolean));
  animateCounter("statMovies",    allMovies.length);
  animateCounter("statStudios",   studios.size);
  animateCounter("statSchedules", schedules.length);
}

function animateCounter(id, target) {
  const el        = document.getElementById(id);
  const startTime = performance.now();
  const duration  = 1400;

  (function tick(now) {
    const p = Math.min((now - startTime) / duration, 1);
    const v = Math.round((1 - Math.pow(1 - p, 3)) * target);
    el.textContent = v.toLocaleString("id-ID");
    if (p < 1) requestAnimationFrame(tick);
  })(performance.now());
}

/* ─────────────────────────────────────────────────────
   INIT
───────────────────────────────────────────────────── */
document.addEventListener("DOMContentLoaded", () => {
  initNav();
  loadData();
});
