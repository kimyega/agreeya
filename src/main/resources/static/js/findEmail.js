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
const form = document.getElementById("phone-verify-form");
const nameInput = document.getElementById("name");
const phoneInput = document.getElementById("phone");
const nameMsg = document.getElementById("name-msg");
const phoneMsg = document.getElementById("phone-msg");

// 테스트용 사용자
const dummyUser = {
    name: "홍길동",
    phone: "01012345678"
};

form?.addEventListener("submit", function (e) {
    e.preventDefault();

    const name = (nameInput.value || "").trim();
    const phone = (phoneInput.value || "").replaceAll("-", "").trim();

    // 초기화
    nameMsg.textContent = "";
    phoneMsg.textContent = "";
    nameMsg.classList.add("hidden");
    phoneMsg.classList.add("hidden");

    // 기본 체크
    if (!name) {
        nameMsg.textContent = "이름을 입력해주세요.";
        nameMsg.classList.remove("hidden");
    }
    if (!phone) {
        phoneMsg.textContent = "전화번호를 입력해주세요.";
        phoneMsg.classList.remove("hidden");
    }
    if (!name || !phone) return;

    // 형식 체크(국내 휴대폰)
    const phoneRegex = /^01[016789]\d{7,8}$/; // 하이픈 제거 기준
    if (!phoneRegex.test(phone)) {
        phoneMsg.textContent = "전화번호 형식이 올바르지 않습니다.";
        phoneMsg.classList.remove("hidden");
        return;
    }

    // 더미 유저 매칭
    if (name !== dummyUser.name) {
        nameMsg.textContent = "등록되지 않은 이름입니다.";
        nameMsg.classList.remove("hidden");
        return;
    }
    if (phone !== dummyUser.phone) {
        phoneMsg.textContent = "전화번호가 일치하지 않습니다.";
        phoneMsg.classList.remove("hidden");
        return;
    }

    // 성공 → 코드 입력 페이지로 이동 (라우트에 맞게 수정)
    window.location.href = `/emailVerify?name=${encodeURIComponent(name)}&phone=${encodeURIComponent(phone)}`;
});
