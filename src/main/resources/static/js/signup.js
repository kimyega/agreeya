// ===== 공통 메시지/스타일 헬퍼 =====
const setError = (input, msgEl, message) => {
    input.classList.remove("valid");
    input.classList.add("invalid");
    msgEl.classList.remove("success");
    msgEl.classList.add("error");
    msgEl.textContent = message;
};

const setSuccess = (input, msgEl, message) => {
    input.classList.remove("invalid");
    input.classList.add("valid");
    msgEl.classList.remove("error");
    msgEl.classList.add("success");
    msgEl.textContent = message;
};

// ===== 페이지 로직 =====
document.addEventListener("DOMContentLoaded", function () {
    // 테스트용 이메일 목록
    const TEST_EMAILS = new Set([
        "test@test.com",
        "test@example.com",
        "user1@demo.com",
        "hello@agreeya.ai",
    ]);

    const signupForm = document.getElementById("signupForm");
    const name = document.getElementById("name");
    const password = document.getElementById("password");
    const passwordCheck = document.getElementById("password-check");
    const nickname = document.getElementById("nickname");
    const phone = document.getElementById("phone");
    const email = document.getElementById("email");
    const emailCode = document.getElementById("email-code");
    const year = document.getElementById("birth-year");
    const month = document.getElementById("birth-month");
    const day = document.getElementById("birth-day");

    // 이름
    name?.addEventListener("input", function () {
        const msg = document.getElementById("name-msg");
        if (!this.value.trim()) setError(this, msg, "이름을 입력해 주세요.");
        else setSuccess(this, msg, "");
    });

    // 비밀번호
    const validatePassword = () => {
        const msg = document.getElementById("password-msg");
        const value = password.value;
        if (!value) return setError(password, msg, "비밀번호를 입력해 주세요.");
        const regex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]).{8,20}$/;
        if (!regex.test(value)) return setError(password, msg, "비밀번호가 조건에 맞지 않습니다.");
        setSuccess(password, msg, "사용 가능한 비밀번호 입니다.");
    };
    password?.addEventListener("input", validatePassword);

    // 비밀번호 확인
    const validatePasswordCheck = () => {
        const msg = document.getElementById("password-check-msg");
        if (!passwordCheck.value) return setError(passwordCheck, msg, "비밀번호를 다시 입력해 주세요.");
        if (passwordCheck.value !== password.value) return setError(passwordCheck, msg, "비밀번호가 일치하지 않습니다.");
        setSuccess(passwordCheck, msg, "비밀번호가 일치합니다.");
    };
    passwordCheck?.addEventListener("input", validatePasswordCheck);

    // 닉네임
    nickname?.addEventListener("input", function () {
        const msg = document.getElementById("nickname-msg");
        const value = nickname.value.trim();
        const regex = /^[가-힣a-zA-Z0-9]{1,15}$/;
        if (!value) return setError(nickname, msg, "닉네임을 입력해 주세요.");
        if (!regex.test(value)) return setError(nickname, msg, "형식이 올바르지 않습니다.");
        setSuccess(nickname, msg, "사용 가능한 닉네임입니다.");
    });

    // 전화번호
    phone?.addEventListener("input", function () {
        const msg = document.getElementById("phone-msg");
        const value = phone.value.trim();
        const regex = /^01[016789]-?\d{3,4}-?\d{4}$/;
        if (!value) return setError(phone, msg, "전화번호를 입력해 주세요.");
        if (!regex.test(value)) return setError(phone, msg, "전화번호 형식이 잘못되었습니다.");
        setSuccess(phone, msg, "사용 가능한 전화번호입니다.");
    });

    // 생년월일
    const validateBirth = () => {
        const msg = document.getElementById("birth-msg");
        [year, month, day].forEach(el => el.classList.remove("invalid"));
        if (!year.value) return setError(year, msg, "년도를 입력해 주세요.");
        if (!/^[0-9]{4}$/.test(year.value)) return setError(year, msg, "년도는 4자리수 입니다.");
        if (!month.value) return setError(month, msg, "월을 선택해 주세요.");
        if (!day.value) return setError(day, msg, "일을 입력해 주세요.");

        const date = new Date(`${year.value}-${month.value}-${day.value}`);
        if (isNaN(date.getTime()) || date.getDate() !== Number(day.value))
            return setError(day, msg, "일수가 맞지 않습니다.");

        [year, month, day].forEach(el => el.classList.add("valid"));
        msg.classList.remove("error");
        msg.classList.add("success");
        msg.textContent = "생년월일이 유효합니다.";
    };
    year?.addEventListener("blur", validateBirth);
    day?.addEventListener("blur", validateBirth);
    month?.addEventListener("change", validateBirth);

    // 이메일 중복확인 + 인증메일 전송 모달
    const checkEmail = (isSubmit = false) => {
        const msg = document.getElementById("email-msg");
        const value = email.value.trim();
        if (!value) return setError(email, msg, "이메일을 입력해 주세요.");
        const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!regex.test(value)) return setError(email, msg, "이메일 주소 형식이 맞지 않습니다.");

        const isDuplicated = TEST_EMAILS.has(value);
        if (isDuplicated) return setError(email, msg, "중복된 이메일 입니다.");

        setSuccess(email, msg, "사용 가능한 이메일입니다.");

        if (!isSubmit) {
            const modal = document.getElementById("emailSentModal");
            if (modal) modal.classList.remove("hidden");
        }
    };
    email?.addEventListener("blur", checkEmail);
    // 외부에서 버튼 onclick으로 쓰니 export
    window.checkEmail = checkEmail;

    // 이메일 인증번호 확인
    const verifyEmailCode = () => {
        const msg = document.getElementById("email-code-msg");
        const expected = "123456";
        const inputCode = emailCode.value.trim();

        if (!inputCode) return setError(emailCode, msg, "인증번호를 입력해 주세요.");
        if (inputCode !== expected) return setError(emailCode, msg, "인증번호가 일치하지 않습니다.");

        setSuccess(emailCode, msg, "인증되었습니다.");
        email.disabled = true;
        email.nextElementSibling.disabled = true; // 인증메일 버튼
        emailCode.disabled = true;
        email.nextElementSibling.style.opacity = 0.5;
        email.nextElementSibling.style.cursor = "not-allowed";
    };
    emailCode?.addEventListener("blur", verifyEmailCode);
    window.verifyEmailCode = verifyEmailCode;

    // 폼 제출
    signupForm?.addEventListener("submit", function (e) {
        e.preventDefault();

        name.dispatchEvent(new Event("input"));
        password.dispatchEvent(new Event("input"));
        passwordCheck.dispatchEvent(new Event("input"));
        nickname.dispatchEvent(new Event("input"));
        phone.dispatchEvent(new Event("input"));
        email.dispatchEvent(new Event("blur"));
        emailCode.dispatchEvent(new Event("blur"));
        year.dispatchEvent(new Event("blur"));
        day.dispatchEvent(new Event("blur"));

        const requiredValidInputs = [name, password, passwordCheck, nickname, phone, email, emailCode];
        const allValid = requiredValidInputs.every((el) => el.classList.contains("valid"));

        const birthValid =
            year.value && /^[0-9]{4}$/.test(year.value) &&
            month.value && day.value &&
            !isNaN(new Date(`${year.value}-${month.value}-${day.value}`).getTime());

        if (!allValid || !birthValid) {
            console.log("유효하지 않음");
            return;
        }

        const modal = document.getElementById("signupCompleteModal");
        if (modal) modal.classList.remove("hidden");
    });
});

// ===== 모달 & 헤더 유틸 =====
function closeEmailSentModal() {
    const modal = document.getElementById("emailSentModal");
    if (modal) modal.classList.add("hidden");
}
window.closeEmailSentModal = closeEmailSentModal;

function goToLogin() {
    window.location.href = "/login";
}
window.goToLogin = goToLogin;

function togglePassword(inputId, iconEl) {
    const input = document.getElementById(inputId);
    if (!input) return;
    const isHidden = input.type === "password";
    input.type = isHidden ? "text" : "password";
    if (iconEl && iconEl.classList) {
        iconEl.classList.toggle("fa-eye-slash", !isHidden);
        iconEl.classList.toggle("fa-eye", isHidden);
    }
}
window.togglePassword = togglePassword;

function simulateLogin() {
    const toast = document.getElementById("loginMessage");
    toast?.classList.remove("hidden");
    setTimeout(() => toast?.classList.add("hidden"), 1200);
    document.getElementById("loginButton")?.classList.add("hidden");
    document.getElementById("profileDropdownWrapper")?.classList.remove("hidden");
}
window.simulateLogin = simulateLogin;

function toggleDropdown() {
    const dd = document.getElementById("profileDropdown");
    dd?.classList.toggle("hidden");
    const closeOnOutside = (e) => {
        if (dd && !dd.contains(e.target)) {
            dd.classList.add("hidden");
            document.removeEventListener("click", closeOnOutside);
        }
    };
    document.addEventListener("click", closeOnOutside);
}
window.toggleDropdown = toggleDropdown;

function logout() {
    document.getElementById("profileDropdown")?.classList.add("hidden");
    document.getElementById("profileDropdownWrapper")?.classList.add("hidden");
    document.getElementById("loginButton")?.classList.remove("hidden");
}
window.logout = logout;
