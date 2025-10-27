function showModal(message, callback) {
    const modal = $("#alertModal");
    $("#alertModalMsg").text(message);
    modal.removeClass("hidden");
    $("#alertModalBtn").off("click").on("click", function () {
        modal.addClass("hidden");
        if (callback) callback();
    });
}

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

    const regex = /^(?=.*[A-Za-z])(?=.*\d).{6,}$/;

    if (!pw1) {
        newMsg.text("새 비밀번호를 입력해주세요.").removeClass("text-green-500").addClass("text-red-500");
        valid = false;
    } else if (!regex.test(pw1)) {
        newMsg.text("영문+숫자 포함 6자 이상이어야 합니다.").removeClass("text-green-500").addClass("text-red-500");
        valid = false;
    }

    if (!pw2) {
        confirmMsg.text("비밀번호 확인을 입력해주세요.").removeClass("text-green-500").addClass("text-red-500");
        valid = false;
    } else if (pw1 !== pw2) {
        confirmMsg.text("비밀번호가 일치하지 않습니다.").removeClass("text-green-500").addClass("text-red-500");
        valid = false;
    }

    if (!valid) return;

    $.ajax({
        url: "/user/resetPassword",
        type: "post",
        dataType: "json",
        data: { password: pw1 },
        success: function (json) {
            if (json.result === 1) {
                showModal(json.msg, () => location.href = "/user/login");
            } else {
                showModal(json.msg);
            }
        },
        error: function () {
            showModal("❌ 서버 요청 중 오류 발생");
        }
    });
});
