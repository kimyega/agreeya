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

    // 기본 이메일 형식 간단 체크
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        emailMsg.text("이메일 형식이 올바르지 않습니다.");
        emailMsg.removeClass("hidden");
        return;
    }

    // Ajax 요청
    $.ajax({
        url: "/email/sendResetCode",
        type: "post",
        dataType: "json",
        data: { email: email }, // serialize 대신 email만 명확히 전송
        success: function (json) {
            if (json.result === 1) {
                alert(json.msg);
                location.href = "/user/emailVerify"; // 인증코드 입력 화면으로 이동
            } else {
                alert(json.msg);
            }
        },
        error: function () {
            alert("❌ 서버 요청 중 오류 발생");
        }
    });
});

