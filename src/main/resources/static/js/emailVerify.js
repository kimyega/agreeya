// ===== 상단 메뉴용 (공통 기능) =====
function simulateLogin() {
    const modal = document.getElementById("loginModal");
    if (modal) modal.classList.remove("hidden");
}
function toggleDropdown() {
    const dropdown = document.getElementById("profileDropdown");
    dropdown?.classList.toggle("hidden");
}
function logout() {
    document.getElementById("profileDropdown")?.classList.add("hidden");
    document.getElementById("profileDropdownWrapper")?.classList.add("hidden");
    document.getElementById("loginButton")?.classList.remove("hidden");
}

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
        url: "/user/verifyResetCode",
        type: "post",
        dataType: "json",
        data: { code: code },
        success: function (json) {
            if (json.result === 1) {
                alert(json.msg);
                location.href = "/changePw"; // 비밀번호 변경 페이지로 이동
            } else {
                alert(json.msg);
            }
        },
        error: function () {
            alert("❌ 서버 요청 중 오류 발생");
        }
    });
});
