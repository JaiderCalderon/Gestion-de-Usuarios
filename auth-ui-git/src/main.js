/* =========================================================================
 * JAS — Auth UI shared logic
 * Talks to the Spring Boot backend (auth-backend-api) over JWT.
 * ========================================================================= */

// Base URL of the backend API. Override by setting window.API_BASE before this script.
const API_BASE = window.API_BASE || 'http://localhost:9090/api';

const STORAGE = {
  token: 'auth_token',
  username: 'auth_username',
  email: 'auth_email',
  role: 'auth_role',
  flag: 'auth_logged_in',
};

/* --------------------------------------------------------------- Session --- */

function saveSession(auth) {
  localStorage.setItem(STORAGE.token, auth.token || '');
  localStorage.setItem(STORAGE.username, auth.username || '');
  localStorage.setItem(STORAGE.email, auth.email || '');
  localStorage.setItem(STORAGE.role, auth.role || '');
  localStorage.setItem(STORAGE.flag, 'true');
}

function clearSession() {
  Object.values(STORAGE).forEach((k) => localStorage.removeItem(k));
}

function getToken()    { return localStorage.getItem(STORAGE.token); }
function getUsername() { return localStorage.getItem(STORAGE.username) || 'Usuario'; }
function getEmail()    { return localStorage.getItem(STORAGE.email); }
function getRole()     { return localStorage.getItem(STORAGE.role); }
function isLoggedIn()  { return localStorage.getItem(STORAGE.flag) === 'true' && !!getToken(); }

function logout() {
  clearSession();
  window.location.href = 'index.html';
}

/** Redirect to the page that matches the user's role. */
function redirectByRole() {
  window.location.href = getRole() === 'ADMIN' ? 'admin.html' : 'user.html';
}

/** Guard: send unauthenticated users back to login. */
function requireAuth() {
  if (!isLoggedIn()) {
    window.location.href = 'index.html';
    return false;
  }
  return true;
}

/** Guard: admin-only pages. */
function requireAdmin() {
  if (!requireAuth()) return false;
  if (getRole() !== 'ADMIN') {
    window.location.href = 'user.html';
    return false;
  }
  return true;
}

/* ------------------------------------------------------------------- API --- */

/**
 * Authenticated fetch wrapper. Adds the Bearer token and JSON headers,
 * parses the response and throws an Error with the backend message on failure.
 */
async function apiFetch(path, options = {}) {
  const headers = { 'Content-Type': 'application/json', ...(options.headers || {}) };
  const token = getToken();
  if (token) headers['Authorization'] = `Bearer ${token}`;

  const res = await fetch(`${API_BASE}${path}`, { ...options, headers });

  if (res.status === 401) {
    clearSession();
    if (!window.location.pathname.endsWith('index.html')) {
      window.location.href = 'index.html';
    }
    throw new Error('Sesión expirada. Inicia sesión de nuevo.');
  }

  let body = null;
  const text = await res.text();
  if (text) {
    try { body = JSON.parse(text); } catch { body = text; }
  }

  if (!res.ok) {
    const msg = (body && (body.error || body.message)) || `Error ${res.status}`;
    throw new Error(msg);
  }
  return body;
}

/* ---------------------------------------------------------------- UI utils --- */
/* ===== Alerta personalizada (reemplazo de alert) ===== */
function mostrarAlerta(mensaje, tipo = 'info') {
  let overlay = document.getElementById('alertaOverlay');
  if (!overlay) {
    overlay = document.createElement('div');
    overlay.id = 'alertaOverlay';
    overlay.className = 'alerta-overlay';
    overlay.innerHTML = `
      <div class="alerta-box">
        <div class="alerta-icon" id="alertaIcon"></div>
        <p class="alerta-msg" id="alertaMsg"></p>
        <button type="button" class="btn btn-primary-custom" id="alertaBtn">Aceptar</button>
      </div>`;
    document.body.appendChild(overlay);

    const cerrar = () => overlay.classList.remove('show');
    overlay.querySelector('#alertaBtn').addEventListener('click', cerrar);
    overlay.addEventListener('click', (e) => { if (e.target === overlay) cerrar(); });
    document.addEventListener('keydown', (e) => {
      if (e.key === 'Escape' && overlay.classList.contains('show')) cerrar();
    });
  }

  const icons = {
    info:    '<i class="bi bi-info-circle-fill"></i>',
    success: '<i class="bi bi-check-circle-fill"></i>',
    error:   '<i class="bi bi-exclamation-triangle-fill"></i>',
  };
  const icon = overlay.querySelector('#alertaIcon');
  icon.className = 'alerta-icon ' + (icons[tipo] ? tipo : 'info');
  icon.innerHTML = icons[tipo] || icons.info;
  overlay.querySelector('#alertaMsg').textContent = mensaje;
  overlay.classList.add('show');
}

function togglePassword(inputId, btnId) {
  const input = document.getElementById(inputId);
  const icon = document.querySelector(`#${btnId} i`);
  const isPwd = input.type === 'password';
  input.type = isPwd ? 'text' : 'password';
  if (icon) {
    icon.classList.toggle('bi-eye');
    icon.classList.toggle('bi-eye-slash');
  }
}

function initials(name) {
  return (name || '?')
    .trim()
    .split(/\s+/)
    .map((w) => w[0])
    .slice(0, 2)
    .join('')
    .toUpperCase();
}

/* -------------------------------------------------------------- Auth flows --- */

async function handleLogin(event) {
  event.preventDefault();
  const email = document.getElementById('loginEmail').value.trim();
  const password = document.getElementById('loginPassword').value;
  const btn = document.getElementById('loginBtn');

  try {
    if (btn) btn.disabled = true;
    const auth = await apiFetch('/auth/login', {
      method: 'POST',
      body: JSON.stringify({ email, password }),
    });
    saveSession(auth);
    redirectByRole();
  } catch (err) {
    mostrarAlerta(err.message || 'No se pudo iniciar sesión.', 'error');
  } finally {
    if (btn) btn.disabled = false;
  }
}

async function handleRegister(event) {
  event.preventDefault();
  const cedula = document.getElementById('registerCedula').value.trim();
  const name = document.getElementById('registerName').value.trim();
  const username = document.getElementById('registerUsername').value.trim();
  const email = document.getElementById('registerEmail').value.trim();
  const password = document.getElementById('registerPassword').value;
  const confirm = document.getElementById('registerPasswordConfirm').value;
  const btn = document.getElementById('registerBtn');

  if (password !== confirm) {
    mostrarAlerta('Las contraseñas no coinciden.', 'error');
    return;
  }

  try {
    if (btn) btn.disabled = true;
    await apiFetch('/auth/register', {
      method: 'POST',
      body: JSON.stringify({ cedula: Number(cedula), name, username, email, password }),
    });
    mostrarAlerta('Cuenta creada. Redirigiendo a iniciar sesión…', 'success');
    setTimeout(() => (window.location.href = 'index.html'), 1200);
  } catch (err) {
    mostrarAlerta(err.message || 'No se pudo crear la cuenta.', 'error');
  } finally {
    if (btn) btn.disabled = false;
  }
}
