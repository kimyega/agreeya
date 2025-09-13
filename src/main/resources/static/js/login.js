// 로그인 모달 열기/닫기
function simulateLogin() {
    $("#loginModal").removeClass("hidden");
}
function closeLoginModal() {
    $("#loginModal").addClass("hidden");
}

// 성공 모달
function showSuccessModal() {
    const successModal = $("#successModal");
    if (!successModal.length) return;

    successModal.removeClass("hidden");
    setTimeout(() => successModal.addClass("hidden"), 1500);
}

// 로그아웃 (✅ GET 방식)
function logout() {
    $.ajax({
        url: contextPath + "/user/logout", // ✅ contextPath 변수 사용
        type: "GET",
        xhrFields: { withCredentials: true },
        success: function () {
            location.href = contextPath + "/";
        },
        error: function (xhr, status, error) {
            console.error("로그아웃 실패:", error);
            alert("로그아웃 실패");
        }
    });
}

// 로그인 처리
$(document).ready(function () {
    $("#login-form").on("submit", function (e) {
        e.preventDefault();

        const email = $("#email").val().trim();
        const password = $("#password").val().trim();
        const emailMsgEl = $("#email-msg");
        const passwordMsgEl = $("#password-msg");

        emailMsgEl.addClass("hidden");
        passwordMsgEl.addClass("hidden");

        if (!email) {
            emailMsgEl.text("이메일을 입력해주세요.").removeClass("hidden");
            return;
        }
        if (!password) {
            passwordMsgEl.text("비밀번호를 입력해주세요.").removeClass("hidden");
            return;
        }

        $.ajax({
            url: contextPath + "/user/loginProc", // ✅ contextPath 변수 사용
            type: "POST",
            data: { email: email, password: password },
            xhrFields: { withCredentials: true }, // ✅ 세션 유지
            success: function (data) {
                // ✅ UserDTO 반환 시 userId가 존재하면 성공
                if (data && data.userId) {
                    showSuccessModal();

                    // 상단바 갱신
                    $("#loginButton").addClass("hidden");
                    const profileWrapper = $("#profileDropdownWrapper");
                    profileWrapper.removeClass("hidden");
                    profileWrapper.find("span").text(data.name || data.userId || "User");

                    setTimeout(() => (location.href = contextPath + "/"), 1500);
                } else {
                    // 실패 시 null 반환
                    emailMsgEl.text("이메일 또는 비밀번호가 일치하지 않습니다.").removeClass("hidden");
                }
            },
            error: function (xhr, status, error) {
                console.error("로그인 요청 오류:", error);
                alert("로그인 요청 중 문제가 발생했습니다.");
            }
        });
    });
});
