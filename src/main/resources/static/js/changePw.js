// ===== 비밀번호 재설정 로직 =====
$("#reset-form").on("submit", function (e) {
    e.preventDefault();

    const pw1 = $("#new-password").val().trim();
    const pw2 = $("#confirm-password").val().trim();
    const newMsg = $("#new-password-msg");
    const confirmMsg = $("#confirm-password-msg");

    let valid = true;
    newMsg.text("");
    confirmMsg.text("");

    // 비밀번호 유효성: 영문+숫자 포함 6자 이상
    const regex = /^(?=.*[A-Za-z])(?=.*\d).{6,}$/;

    if (!pw1) {
        newMsg.text("새 비밀번호를 입력해주세요.").removeClass("text-green-500").addClass("text-red-500");
        valid = false;
    } else if (!regex.test(pw1)) {
        newMsg.text("영문+숫자 포함 6자 이상이어야 합니다.").removeClass("text-green-500").addClass("text-red-500");
        valid = false;
    } else {
        newMsg.text("").removeClass("text-red-500").addClass("text-green-500");
    }

    if (!pw2) {
        confirmMsg.text("비밀번호 확인을 입력해주세요.").removeClass("text-green-500").addClass("text-red-500");
        valid = false;
    } else if (pw1 !== pw2) {
        confirmMsg.text("비밀번호가 일치하지 않습니다.").removeClass("text-green-500").addClass("text-red-500");
        valid = false;
    } else {
        confirmMsg.text("").removeClass("text-red-500").addClass("text-green-500");
    }

    if (!valid) return;

    // Ajax 요청
    $.ajax({
        url: "/user/resetPassword",
        type: "post",
        dataType: "json",
        data: { password: pw1 },
        success: function (json) {
            if (json.result === 1) {
                alert(json.msg);
                location.href = "/login"; // 로그인 화면으로 이동
            } else {
                alert(json.msg);
            }
        },
        error: function () {
            alert("❌ 서버 요청 중 오류 발생");
        }
    });
});
