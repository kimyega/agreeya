// 상단 메뉴용 (필요 시 공통 js로 이동 가능)
function simulateLogin() {
    const modal = document.getElementById("loginModal");
    if (modal) modal.classList.remove("hidden");
}
function toggleDropdown() {
    const dropdown = document.getElementById("profileDropdown");
    dropdown?.classList.toggle("hidden");
}
function logout() {
    document.getElementById("profileDropdown")?.classList.add("hidden");
    document.getElementById("profileDropdownWrapper")?.classList.add("hidden");
    document.getElementById("loginButton")?.classList.remove("hidden");
}

// ===== 이메일 인증 로직 =====
const form = document.getElementById("code-form");
const codeInput = document.getElementById("code");
const messageBox = document.getElementById("message");
const errorModal = document.getElementById("errorModal");

// 서버 테스트용 하드코딩 값
const correctCode = "123456";

form?.addEventListener("submit", function (e) {
    e.preventDefault();
    const inputCode = codeInput.value.trim();

    messageBox.textContent = "";
    messageBox.classList.remove("text-red-500", "text-green-500");
    messageBox.classList.add("hidden");

    if (inputCode === "") {
        messageBox.textContent = "인증번호를 입력해주세요.";
        messageBox.classList.remove("hidden");
        messageBox.classList.add("text-red-500");
        return;
    }

    if (inputCode !== correctCode) {
        // 틀림 → 모달
        errorModal?.classList.remove("hidden");
        return;
    }

    // 성공
    messageBox.textContent = "인증이 완료되었습니다.";
    messageBox.classList.remove("text-red-500");
    messageBox.classList.add("text-green-500");
    messageBox.classList.remove("hidden");

    // 다음 단계로 이동 (라우트에 맞춰 수정)
    setTimeout(() => {
        window.location.href = "/changePw"; // 예: 비밀번호 변경 페이지
    }, 1500);
});

// 모달 닫기
function closeModal() {
    errorModal?.classList.add("hidden");
}
window.closeModal = closeModal;
