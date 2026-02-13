// Underwater Background Animation
        const underwaterBg = document.getElementById('underwater-bg');
        
        // Simple bubble creation - Reduced number
        function createBubbles() {
            for (let i = 0; i < 6; i++) {
                const bubble = document.createElement('div');
                bubble.className = 'bubble';
                bubble.style.width = Math.random() * 10 + 5 + 'px';
                bubble.style.height = bubble.style.width;
                bubble.style.left = Math.random() * 100 + '%';
                bubble.style.animationDelay = Math.random() * 10 + 's';
                bubble.style.animationDuration = Math.random() * 2 + 6 + 's';
                underwaterBg.appendChild(bubble);
            }
        }

        // Simple ocean particles
        function createOceanParticles() {
            for (let i = 0; i < 20; i++) {
                const particle = document.createElement('div');
                particle.className = 'ocean-particle';
                particle.style.width = Math.random() * 4 + 2 + 'px';
                particle.style.height = particle.style.width;
                particle.style.top = Math.random() * 100 + '%';
                particle.style.animationDelay = Math.random() * 15 + 's';
                particle.style.animationDuration = Math.random() * 5 + 12 + 's';
                underwaterBg.appendChild(particle);
            }}
            // Research Tabs Functionality - Fixed
        const researchTabs = document.querySelectorAll('.research-tab');
        const researchContents = document.querySelectorAll('.research-content');

        if (researchTabs.length > 0 && researchContents.length > 0) {
            researchTabs.forEach(tab => {
                tab.addEventListener('click', () => {
                    // Remove active class from all tabs and contents
                    researchTabs.forEach(t => t.classList.remove('active'));
                    researchContents.forEach(c => c.classList.remove('active'));

                    // Add active class to clicked tab
                    tab.classList.add('active');

                    // Show corresponding content
                    const tabId = tab.getAttribute('data-tab');
                    const targetContent = document.getElementById(tabId);
                    if (targetContent) {
                        targetContent.classList.add('active');
                    }
                });
            });
        }

        // Simple initialization
        createBubbles();
        createOceanParticles();

        // Simple regeneration
        setInterval(createBubbles, 20000); // Every 20 seconds
        setInterval(createOceanParticles, 30000); // Every 30 seconds

        // ==== POPUP LOGIN ====
document.addEventListener("DOMContentLoaded", function () {
  const popup = document.getElementById("loginPopup");
  const btnLogin = document.getElementById("btnLogin"); // INICIAR SESION
  const closeBtn = document.querySelector(".popup .close");

  const formCliente = document.getElementById("formCliente");
  const formAsociado = document.getElementById("formAsociado");
  const switchAsociado = document.getElementById("switchAsociado");
  const switchCliente = document.getElementById("switchCliente");

  // Abrir popup
  btnLogin.addEventListener("click", function (e) {
    e.preventDefault();
    popup.classList.add("active");  // usamos clase en vez de display:block
  });

  // Cerrar popup
  closeBtn.addEventListener("click", function () {
    popup.classList.remove("active");
  });

  // Cerrar si clickea afuera
  window.addEventListener("click", function (e) {
    if (e.target === popup) {
      popup.classList.remove("active");
    }
  });

  // Cambiar entre Cliente y Asociado
  switchAsociado.addEventListener("click", function (e) {
    e.preventDefault();
    formCliente.style.display = "none";
    formAsociado.style.display = "block";
  });

  switchCliente.addEventListener("click", function (e) {
    e.preventDefault();
    formCliente.style.display = "block";
    formAsociado.style.display = "none";
  });
});
