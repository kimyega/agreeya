const form = document.getElementById("reset-form");
const newPassword = document.getElementById("new-password");
const confirmPassword = document.getElementById("confirm-password");
const newMsg = document.getElementById("new-password-msg");
const confirmMsg = document.getElementById("confirm-password-msg");
const modal = document.getElementById("modal");
const modalMessage = document.getElementById("modal-message");
const modalConfirm = document.getElementById("modal-confirm");

const setError = (input, msgEl, message) => {
    input.classList.remove("border-gray-300", "focus:ring-blue-300");
    input.classList.add("border-red-500", "focus:ring-red-300");
    msgEl.classList.remove("text-green-500");
    msgEl.classList.add("text-red-500");
    msgEl.textContent = message;
};

const setSuccess = (input, msgEl, message) => {
    input.classList.remove("border-red-500", "focus:ring-red-300");
    input.classList.add("border-green-500", "focus:ring-green-300");
    msgEl.classList.remove("text-red-500");
    msgEl.classList.add("text-green-500");
    msgEl.textContent = message;
};

form?.addEventListener("submit", function (e) {
    e.preventDefault();

    const pw1 = newPassword.value.trim();
    const pw2 = confirmPassword.value.trim();
    let valid = true;

    newMsg.textContent = "";
    confirmMsg.textContent = "";

    // 비밀번호 유효성: 영문+숫자 포함 6자 이상
    const regex = /^(?=.*[A-Za-z])(?=.*\d).{6,}$/;

    if (!pw1) {
        setError(newPassword, newMsg, "새 비밀번호를 입력해주세요.");
        valid = false;
    } else if (!regex.test(pw1)) {
        setError(newPassword, newMsg, "영문+숫자 포함 6자 이상이어야 합니다.");
        valid = false;
    } else {
        setSuccess(newPassword, newMsg, "");
    }

    if (!pw2) {
        setError(confirmPassword, confirmMsg, "비밀번호 확인을 입력해주세요.");
        valid = false;
    } else if (pw1 !== pw2) {
        setError(confirmPassword, confirmMsg, "비밀번호가 일치하지 않습니다.");
        valid = false;
    } else {
        setSuccess(confirmPassword, confirmMsg, "");
    }

    if (valid) {
        modal.classList.remove("hidden");
        modalMessage.textContent = "비밀번호가 성공적으로 변경되었습니다.";
    }
});

modalConfirm?.addEventListener("click", () => {
    modal.classList.add("hidden");
    window.location.href = "/login"; // 라우트에 맞게 이동
});
