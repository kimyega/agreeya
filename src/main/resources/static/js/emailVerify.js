// ===== 이메일 인증 로직 =====
$("#code-form").on("submit", function (e) {
    e.preventDefault();

    const code = $("#code").val().trim();
    console.log("입력한 코드:", code);

    if (!/^\d{6}$/.test(code)) {
        alert("6자리 숫자로 입력하세요.");
        return;
    }

    $.ajax({
        url: "/email/verifyResetCode",
        type: "post",
        dataType: "json",
        data: { code: code },
        success: function (json) {
            if (json.result === 1) {
                alert(json.msg);
                location.href = "/user/changePw"; // 비밀번호 변경 페이지로 이동
            } else {
                alert(json.msg);
            }
        },
        error: function () {
            alert("❌ 서버 요청 중 오류 발생");
        }
    });
});
