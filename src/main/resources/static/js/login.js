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
    document.getElementById("loginModal")?.classList.remove("hidden");
}
function closeLoginModal() {
    document.getElementById("loginModal")?.classList.add("hidden");
}

// 성공 모달
const successModal = document.getElementById("successModal");
function showSuccessModal() {
    if (!successModal) return;
    successModal.classList.remove("hidden");
    setTimeout(() => successModal.classList.add("hidden"), 1500);
}

// 로그아웃 (✅ GET 방식으로 복구)
function logout() {
    fetch("/user/logout", { method: "GET", credentials: "include" })
        .then(() => {
            location.href = "/";
        })
        .catch((e) => {
            console.error("로그아웃 실패:", e);
            alert("로그아웃 실패");
        });
}

// 로그인 처리
document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("login-form");
    form?.addEventListener("submit", async (e) => {
        e.preventDefault();

        const email = document.getElementById("email").value.trim();
        const password = document.getElementById("password").value.trim();
        const emailMsgEl = document.getElementById("email-msg");
        const passwordMsgEl = document.getElementById("password-msg");

        emailMsgEl.classList.add("hidden");
        passwordMsgEl.classList.add("hidden");

        if (!email) {
            emailMsgEl.textContent = "이메일을 입력해주세요.";
            emailMsgEl.classList.remove("hidden");
            return;
        }
        if (!password) {
            passwordMsgEl.textContent = "비밀번호를 입력해주세요.";
            passwordMsgEl.classList.remove("hidden");
            return;
        }

        try {
            const res = await fetch("/user/loginProc", {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body: new URLSearchParams({ email, password }),
                credentials: "include"   // ✅ 세션 유지
            });

            const data = await res.json();
            if (data.res === 1) {
                showSuccessModal();

                // 상단바 갱신
                document.getElementById("loginButton")?.classList.add("hidden");
                const profileWrapper = document.getElementById("profileDropdownWrapper");
                profileWrapper?.classList.remove("hidden");
                profileWrapper.querySelector("span").textContent =
                    data.user?.name || data.user?.userId || "User";

                setTimeout(() => (location.href = "/"), 1500);
            } else {
                emailMsgEl.textContent = data.msg || "로그인 실패";
                emailMsgEl.classList.remove("hidden");
            }
        } catch (err) {
            console.error("로그인 요청 오류:", err);
            alert("로그인 요청 중 문제가 발생했습니다.");
        }
    });
});
