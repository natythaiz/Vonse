const btn = document.getElementById('menu-btn');
const menu = document.getElementById('menu');

const btn_usuario = document.getElementById('menu-usuario-btn');
const menu_usuario = document.getElementById('menu-usuario');

btn.addEventListener('click', navToggleMenu);

function navToggleMenu() {
  btn.classList.toggle('open');
  menu.classList.toggle('flex');
  menu.classList.toggle('hidden');
  if (btn.classList.contains('open')) {
    btn_usuario.classList.add('hidden');
  } else {
    btn_usuario.classList.remove('hidden');
  }
}

btn_usuario.addEventListener('click', navToggleUsuario)

function navToggleUsuario() {
  menu_usuario.classList.toggle('flex');
  menu_usuario.classList.toggle('hidden');
  if (!menu_usuario.classList.contains('hidden')) {
    btn.classList.add('hidden');
  } else {
    btn.classList.remove('hidden');
  }
}
