// 공통: 드롭다운, AJAX
function toggleDropdown() {
    const dropdown = document.getElementById("profileDropdown");
    dropdown.classList.toggle("hidden");
}

// 외부 클릭 시 드롭다운 닫기
window.addEventListener("click", function (e) {
    const wrapper = document.getElementById("profileDropdownWrapper");
    const dropdown = document.getElementById("profileDropdown");
    if (wrapper && dropdown && !wrapper.contains(e.target)) {
        dropdown.classList.add("hidden");
    }
});

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
    if (successModal.length === 0) return;
    successModal.removeClass("hidden");
    setTimeout(() => successModal.addClass("hidden"), 1500);
}

// 로그아웃
function logout() {
    $.ajax({
        url: ctx + "/user/logout",
        type: "GET",
        success: function () {
            location.href = ctx + "/";
        },
        error: function (xhr, status, error) {
            console.error("로그아웃 실패:", error);
            alert("로그아웃 실패");
        },
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
            url: ctx + "/user/loginProc",
            type: "POST",
            data: { email: email, password: password },
            success: function (data) {
                if (data) {
                    // ✅ 성공 시 DTO 그대로 옴
                    showSuccessModal();

                    $("#loginButton").addClass("hidden");
                    const profileWrapper = $("#profileDropdownWrapper");
                    profileWrapper.removeClass("hidden");

                    // 닉네임 대신 이름 표시
                    profileWrapper.find("span").text(data.name || data.userId || "User");

                    setTimeout(() => (location.href = ctx + "/"), 1500);
                } else {
                    emailMsgEl.text("이메일 또는 비밀번호가 올바르지 않습니다.").removeClass("hidden");
                }
            },
            error: function (xhr, status, error) {
                console.error("로그인 요청 오류:", error);
                alert("로그인 요청 중 문제가 발생했습니다.");
            },
        });
    });
});
