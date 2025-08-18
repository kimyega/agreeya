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

form?.addEventListener('submit', (e) => {
    e.preventDefault();


    const v = code.value.trim();

    if (!/^\d{6}$/.test(v)) { // 6자리 숫자 체크
        alert('6자리 숫자로 입력하세요.');
        return;
    }

    // 서버에 인증번호 검증 요청
    fetch("/user/email/verify", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: `email=${encodeURIComponent(sessionStorage.getItem("email"))}&code=${encodeURIComponent(v)}`
    })
        .then((res) => {
            if (res.ok) {
                // 인증 성공 → 비밀번호 변경 페이지 이동
                window.location.href = '/changePw';
            } else {
                res.text().then(msg => {alert(msg)});
            }
        })
        .catch((err) => {
            console.error("서버 요청 오류:", err);
        });
});
