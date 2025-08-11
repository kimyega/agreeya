// ===== 헤더 유틸 =====
function simulateLogin() {
    const btn = document.getElementById("loginButton");
    const wrap = document.getElementById("profileDropdownWrapper");
    btn?.classList.add("hidden");
    wrap?.classList.remove("hidden");
}
function toggleDropdown() {
    const dd = document.getElementById("profileDropdown");
    dd?.classList.toggle("hidden");
}
function logout() {
    document.getElementById("profileDropdown")?.classList.add("hidden");
    document.getElementById("profileDropdownWrapper")?.classList.add("hidden");
    document.getElementById("loginButton")?.classList.remove("hidden");
}

// ===== 본문 로직 =====
const form = document.getElementById("find-password-form");
const emailInput = document.getElementById("email");
const emailMsg = document.getElementById("email-msg");
const modal = document.getElementById("successModal");

// 더미 등록 이메일(테스트용)
const registeredEmails = ["gildong@email.com", "user@example.com"];

form?.addEventListener("submit", function (e) {
    e.preventDefault();

    const email = (emailInput.value || "").trim();
    emailMsg.textContent = "";
    emailMsg.classList.add("hidden");

    if (email === "") {
        emailMsg.textContent = "이메일을 입력해주세요.";
        emailMsg.classList.remove("hidden");
        return;
    }

    // 기본 이메일 형식 간단 체크
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        emailMsg.textContent = "이메일 형식이 올바르지 않습니다.";
        emailMsg.classList.remove("hidden");
        return;
    }

    if (!registeredEmails.includes(email)) {
        emailMsg.textContent = "등록되지 않은 이메일입니다.";
        emailMsg.classList.remove("hidden");
        return;
    }

    // 성공 시: 이메일 인증(코드 입력) 페이지로 이동
    window.location.href = `/emailVerify?email=${encodeURIComponent(email)}`;
    // 모달을 쓰고 싶으면 위 라인 주석 처리하고 아래 두 줄 사용:
    // modal?.classList.remove("hidden");
    // setTimeout(() => window.location.href = "/email/verify", 1200);
});

// 모달 닫기
function closeModal() {
    modal?.classList.add("hidden");
}
window.closeModal = closeModal;
