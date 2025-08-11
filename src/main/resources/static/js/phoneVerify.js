// ===== 헤더 유틸 =====
function toggleDropdown() {
    const dropdown = document.getElementById('profileDropdown');
    dropdown?.classList.toggle('hidden');
}
function simulateLogin() {
    const btn = document.getElementById('loginButton');
    const wrapper = document.getElementById('profileDropdownWrapper');
    btn?.classList.add('hidden');
    wrapper?.classList.remove('hidden');
}
function logout() {
    document.getElementById('profileDropdown')?.classList.add('hidden');
    document.getElementById('profileDropdownWrapper')?.classList.add('hidden');
    document.getElementById('loginButton')?.classList.remove('hidden');
}

// ===== 본인 인증 로직 =====
const form = document.getElementById("verify-form");
const codeInput = document.getElementById("code");
const messageBox = document.getElementById("message");
const resultModal = document.getElementById("resultModal");

// 서버 테스트용 코드
const correctCode = "123456";

form?.addEventListener("submit", function (e) {
    e.preventDefault();
    const code = codeInput.value.trim();
    messageBox.classList.remove("text-red-500", "text-green-500", "hidden");

    if (code === "") {
        messageBox.textContent = "인증번호를 입력해주세요.";
        messageBox.classList.add("text-red-500");
        return;
    }

    if (code !== correctCode) {
        messageBox.textContent = "인증번호가 일치하지 않습니다.";
        messageBox.classList.add("text-red-500");
        return;
    }

    // 인증 성공 → 결과 모달 오픈
    messageBox.textContent = "";
    resultModal?.classList.remove("hidden");
    resultModal?.classList.add("flex");
});
