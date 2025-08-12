// 공통: 드롭다운
function toggleDropdown() {
    const dropdown = document.getElementById('profileDropdown');
    dropdown.classList.toggle('hidden');
}

// 외부 클릭 시 드롭다운 닫기
window.addEventListener('click', function (e) {
    const wrapper = document.getElementById('profileDropdownWrapper');
    const dropdown = document.getElementById('profileDropdown');
    if (wrapper && dropdown && !wrapper.contains(e.target)) {
        dropdown.classList.add('hidden');
    }
});

// 로그인 모달 열기/닫기
function simulateLogin() {
    const modal = document.getElementById("loginModal");
    if (modal) modal.classList.remove("hidden");
}
function closeLoginModal() {
    const modal = document.getElementById("loginModal");
    if (modal) modal.classList.add("hidden");
}

// 성공 모달
const successModal = document.getElementById("successModal");
function showSuccessModal() {
    if (!successModal) return;
    successModal.classList.remove("hidden");
    setTimeout(() => successModal.classList.add("hidden"), 1500);
}

// 로그아웃
function logout() {
    const loginBtn = document.getElementById('loginButton');
    const profileMenu = document.getElementById('profileDropdownWrapper');
    const dropdown = document.getElementById('profileDropdown');
    loginBtn?.classList.remove('hidden');
    profileMenu?.classList.add('hidden');
    dropdown?.classList.add('hidden');
}

// 로그인 폼
(function initLoginForms(){
    const form = document.getElementById("login-form");
    const emailInput = document.getElementById("email");
    const passwordInput = document.getElementById("password");
    const emailMsg = document.getElementById("email-msg");
    const passwordMsg = document.getElementById("password-msg");

    const dummyUser = { email: "test@example.com", password: "12345678" };

    form?.addEventListener("submit", function (e) {
        e.preventDefault();
        const email = emailInput.value.trim();
        const password = passwordInput.value.trim();

        emailMsg.textContent = "";
        passwordMsg.textContent = "";
        emailMsg.classList.add("hidden");
        passwordMsg.classList.add("hidden");

        if (!email) { emailMsg.textContent = "이메일을 입력해주세요."; emailMsg.classList.remove("hidden"); return; }
        if (!password) { passwordMsg.textContent = "비밀번호를 입력해주세요."; passwordMsg.classList.remove("hidden"); return; }
        if (email !== dummyUser.email) { emailMsg.textContent = "등록되지 않은 이메일입니다."; emailMsg.classList.remove("hidden"); return; }
        if (password !== dummyUser.password) { passwordMsg.textContent = "비밀번호가 일치하지 않습니다."; passwordMsg.classList.remove("hidden"); return; }

        showSuccessModal();
        // TODO: 실제 로그인 성공 시 이동 경로 지정
        // window.location.href = "/";
    });

    // 모달 폼
    const modalForm = document.getElementById("modal-login-form");
    const modalEmail = document.getElementById("modal-email");
    const modalPassword = document.getElementById("modal-password");
    const modalEmailMsg = document.getElementById("modal-email-msg");
    const modalPasswordMsg = document.getElementById("modal-password-msg");

    modalForm?.addEventListener("submit", function (e) {
        e.preventDefault();
        const email = modalEmail.value.trim();
        const password = modalPassword.value.trim();

        modalEmailMsg.textContent = "";
        modalPasswordMsg.textContent = "";
        modalEmailMsg.classList.add("hidden");
        modalPasswordMsg.classList.add("hidden");

        if (!email) { modalEmailMsg.textContent = "이메일을 입력해주세요."; modalEmailMsg.classList.remove("hidden"); return; }
        if (!password) { modalPasswordMsg.textContent = "비밀번호를 입력해주세요."; modalPasswordMsg.classList.remove("hidden"); return; }
        if (email !== dummyUser.email) { modalEmailMsg.textContent = "등록되지 않은 이메일입니다."; modalEmailMsg.classList.remove("hidden"); return; }
        if (password !== dummyUser.password) { modalPasswordMsg.textContent = "비밀번호가 일치하지 않습니다."; modalPasswordMsg.classList.remove("hidden"); return; }

        showSuccessModal();
        closeLoginModal();
    });
})();
