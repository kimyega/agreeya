// ✅ 모달 함수 추가 (맨 위나 맨 아래 아무 곳에 추가 가능)
function showModal(message, callback) {
    const modal = $("#alertModal");
    $("#alertModalMsg").text(message);
    modal.removeClass("hidden");
    $("#alertModalBtn").off("click").on("click", function () {
        modal.addClass("hidden");
        if (callback) callback();
    });
}

// ===== 본문 로직 =====
$("#find-password-form").on("submit", function (e) {
    e.preventDefault();

    const emailInput = $("#email");
    const emailMsg = $("#email-msg");

    const email = (emailInput.val() || "").trim();
    emailMsg.text("");
    emailMsg.addClass("hidden");

    if (email === "") {
        emailMsg.text("이메일을 입력해주세요.");
        emailMsg.removeClass("hidden");
        return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        emailMsg.text("이메일 형식이 올바르지 않습니다.");
        emailMsg.removeClass("hidden");
        return;
    }

    $.ajax({
        url: "/email/sendResetCode",
        type: "post",
        dataType: "json",
        data: { email: email },
        success: function (json) {
            if (json.result === 1) {
                showModal(json.msg, () => location.href = "/user/emailVerify");
            } else {
                showModal(json.msg);
            }
        },
        error: function () {
            showModal("❌ 서버 요청 중 오류 발생");
        }
    });
});
