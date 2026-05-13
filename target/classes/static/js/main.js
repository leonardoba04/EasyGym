function openModal(id) {
  document.getElementById(id).classList.add('open');
  document.body.style.overflow = 'hidden';
}

function closeModal(id) {
  document.getElementById(id).classList.remove('open');
  document.body.style.overflow = '';
}

document.querySelectorAll('.modal-backdrop').forEach(b => {
  b.addEventListener('click', function(e) {
    if (e.target === this) closeModal(this.id);
  });
});

document.addEventListener('keydown', e => {
  if (e.key === 'Escape')
    document.querySelectorAll('.modal-backdrop.open').forEach(m => closeModal(m.id));
});

// Auto-dismiss alerts
setTimeout(() => {
  document.querySelectorAll('.alert').forEach(el => {
    el.style.transition = 'opacity 0.5s';
    el.style.opacity = '0';
    setTimeout(() => el.remove(), 500);
  });
}, 4500);

// Animate progress bars
window.addEventListener('load', () => {
  document.querySelectorAll('.progress-fill').forEach(bar => {
    const target = bar.style.width;
    bar.style.width = '0';
    requestAnimationFrame(() => {
      bar.style.transition = 'width 0.8s cubic-bezier(0.4,0,0.2,1)';
      bar.style.width = target;
    });
  });
});

// Animate stat numbers
function animateCount(el, target) {
  let current = 0;
  const step = Math.max(1, Math.ceil(target / 25));
  const iv = setInterval(() => {
    current = Math.min(current + step, target);
    el.textContent = current;
    if (current >= target) clearInterval(iv);
  }, 35);
}
window.addEventListener('load', () => {
  document.querySelectorAll('.stat-value').forEach(el => {
    const n = parseInt(el.textContent);
    if (!isNaN(n) && n > 0 && n < 99999) animateCount(el, n);
  });
});
