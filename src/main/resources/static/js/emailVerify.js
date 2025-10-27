// ✅ 공용 모달 함수 (성공/실패 둘 다 처리)
function showModal(message, callback, isError = false) {
    const alertModal = $("#alertModal");
    const errorModal = $("#errorModal");

    // 에러 모달
    if (isError) {
        errorModal.find("h3").text(message);
        errorModal.removeClass("hidden");
        errorModal.find("button").off("click").on("click", function () {
            errorModal.addClass("hidden");
        });
        return;
    }

    // 일반 알림 모달
    $("#alertModalMsg").text(message);
    alertModal.removeClass("hidden");
    $("#alertModalBtn").off("click").on("click", function () {
        alertModal.addClass("hidden");
        if (callback) callback();
    });
}

// ===== 이메일 인증 로직 =====
$("#code-form").on("submit", function (e) {
    e.preventDefault();

    const code = $("#code").val().trim();

    if (!/^\d{6}$/.test(code)) {
        showModal("6자리 숫자로 입력하세요.", null, true);
        return;
    }

    $.ajax({
        url: "/email/verifyResetCode",
        type: "post",
        dataType: "json",
        data: { code: code },
        success: function (json) {
            if (json.result === 1) {
                // ✅ 인증 성공
                showModal(json.msg, () => location.href = "/user/changePw");
            } else {
                // ✅ 인증 실패 (오류 모달로)
                showModal("인증번호가 틀렸습니다.", null, true);
            }
        },
        error: function () {
            showModal("❌ 서버 요청 중 오류 발생", null, true);
        },
    });
});
