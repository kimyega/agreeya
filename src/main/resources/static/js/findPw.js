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

    // 이메일 전송  (value = 값)
    fetch("/user/emailCheck", { //fatch = 받아오다
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ userEmail: emailInput.value }) //{} 제아슨
    })
        .then(res => {
            if (!res.ok) throw new Error("존재하지 않음");
            return res.text();
        })
        .then(data => {
            sessionStorage.setItem("email",data);
            alert("✅ " + "인증 코드 전송 완료");
            window.location.href = "/emailVerify"; // 다음 단계로 이동
        })
        .catch(err => {
            alert("❌ " + err.message);
        });

});

