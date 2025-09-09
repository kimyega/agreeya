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

    // 숫자만 입력 허용 + 붙여넣기 차단
    const allowOnlyNumbers = (input) => {
        input.addEventListener("input", () => {
            input.value = input.value.replace(/[^0-9]/g, "");
        });
    };

    // 적용
    allowOnlyNumbers(year);
    allowOnlyNumbers(day);
    allowOnlyNumbers(phone);

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

    const $signupForm = $("#signupForm");

    let userIdCheck = "Y";
    let emailAuthNumber = ""; // 서버에서 받은 인증번호 저장
    let emailVerified = false; // 이메일 인증 완료 여부

// 이메일 검증 함수
    function validateEmailFormat(input, msgEl) {
        const value = input.value.trim();

        if (!value) {
            setError(input, msgEl, "이메일을 입력해 주세요.");
            return false;
        }

        const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!regex.test(value)) {
            setError(input, msgEl, "이메일 주소 형식이 맞지 않습니다.");
            return false;
        }

        setSuccess(input, msgEl, "");
        return true;
    }

    // 이메일 중복 AJAX 요청
    function checkEmail() {
        const input = document.getElementById("email");
        const msg = document.getElementById("email-msg");

        if (!validateEmailFormat(input, msg)) return;

        $.ajax({
            url: "/user/getEmailExists",
            type: "POST",
            dataType: "JSON",
            data: $signupForm.serialize(),
            success: function (json) {
                if (json.existsYn === "Y") {
                    setError(input, msg, "중복된 이메일 입니다.");
                    setTimeout(() => input.focus(), 0);
                } else {
                    setSuccess(input, msg, "인증번호가 발송되었습니다.");
                    alert("인증번호: " + json.authNumber); // 개발용
                    emailAuthNumber = json.authNumber;
                    emailVerified = false; // 새 인증번호 발급 → 다시 인증 필요
                }
            },
            error: function () {
                setError(input, msg, "서버 요청 중 오류가 발생했습니다.");
            }
        });
    }

// 이메일 인증번호 확인
    function verifyEmailCode() {
        const code = document.getElementById("email-code");
        const msg = document.getElementById("email-code-msg");
        const emailInput = document.getElementById("email");
        const emailBtn = emailInput.nextElementSibling;
        const emailCodeBtn = code.nextElementSibling;

        const expected = (emailAuthNumber + "").trim();
        const inputCode = code.value.trim();

        if (!inputCode) {
            return setError(code, msg, "인증번호를 입력해 주세요.");
        }

        if (inputCode !== expected) {
            emailVerified = false;
            return setError(code, msg, "인증번호가 일치하지 않습니다.");
        }

        // ✅ 인증번호 일치
        setSuccess(code, msg, "인증되었습니다.");
        emailVerified = true; // 인증 성공 시 true

        // 입력창/버튼 비활성화
        emailInput.readOnly = true;
        code.readOnly = true;
        emailBtn.disabled = true;
        emailCodeBtn.disabled = true;

        [emailInput, code, emailBtn, emailCodeBtn].forEach(el => {
            el.style.opacity = 0.5;
            el.style.cursor = "not-allowed";
        });
    }

// 이벤트 연결
    $("#email").on("blur", checkEmail);
    $("#email-code").on("blur", verifyEmailCode);

// 이메일 값이 바뀌면 인증 다시 요구
    $("#email").on("input", function () {
        emailVerified = false;
    });

// 폼 제출
    signupForm?.addEventListener("submit", function (e) {
        e.preventDefault();

        name.dispatchEvent(new Event("input"));
        password.dispatchEvent(new Event("input"));
        passwordCheck.dispatchEvent(new Event("input"));
        nickname.dispatchEvent(new Event("input"));
        phone.dispatchEvent(new Event("input"));
        year.dispatchEvent(new Event("blur"));
        day.dispatchEvent(new Event("blur"));

        const requiredValidInputs = [name, password, passwordCheck, nickname, phone];
        const allValid = requiredValidInputs.every((el) => el.classList.contains("valid"));

        const birthValid =
            year.value && /^[0-9]{4}$/.test(year.value) &&
            month.value && day.value &&
            !isNaN(new Date(`${year.value}-${month.value}-${day.value}`).getTime());

        // ✅ 이메일 인증 완료 여부 확인
        if (!emailVerified) {
            setError(email, document.getElementById("email-msg"), "이메일 인증을 완료해 주세요.");
            return;
        }

        if (!allValid || !birthValid) {
            console.log("유효하지 않음");
            return;
        }

        //  회원가입 AJAX 요청

        $.ajax({
            url: "/user/insertUser",
            type: "POST",
            dataType: "JSON",
            data: $signupForm.serialize(),
            success: function (json) {
                if (json.result === 1) {
                    // 성공 시 모달 표시
                    const modal = document.getElementById("signupCompleteModal");
                    if (modal) modal.classList.remove("hidden");

                    // 버튼 클릭 시 로그인 페이지로 이동
                    document.getElementById("modalLoginBtn").onclick = function() {
                        location.href = "/user/login";
                    };
                } else {
                    alert(json.msg);
                }
            }
        });


    });
});

// ===== 모달 & 헤더 유틸 =====
function closeEmailSentModal() {
    const modal = document.getElementById("emailSentModal");
    if (modal) modal.classList.add("hidden");
}
window.closeEmailSentModal = closeEmailSentModal;

function goToLogin() {
    window.location.href = "/user/login";
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
