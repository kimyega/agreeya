// ===== 헤더 유틸 =====
function simulateLogin() {
    const btn = document.getElementById("loginButton");
    const wrap = document.getElementById("profileDropdownWrapper");
    btn?.classList.add("hidden");
    wrap?.classList.remove("hidden");
}
function toggleDropdown() {
    const dd = document.getElementById("profileDropdown");
    dd?.classList.toggle("hidden");
}
function logout() {
    document.getElementById("profileDropdown")?.classList.add("hidden");
    document.getElementById("profileDropdownWrapper")?.classList.add("hidden");
    document.getElementById("loginButton")?.classList.remove("hidden");
}

// ===== 본문 로직 =====
$(document).ready(function () {
    $("#phone-verify-form").on("submit", function (e) {
        e.preventDefault();

        const name = $("#name").val().trim();
        const tel = $("#phone").val().replace(/\D/g, ""); // 숫자만 추출

        if (!name) {
            alert("이름을 입력하세요.");
            return;
        }
        if (!/^01[0-9]{8,9}$/.test(tel)) {
            alert("휴대폰 번호 형식이 올바르지 않습니다.");
            return;
        }

        // ✅ Ajax 요청
        $.ajax({
            url: "/user/userFindEmail",
            type: "post",
            dataType: "json",
            data: { name: name, tel: tel },
            success: function (res) {
                if (res.result === 1) {
                    alert(res.msg);
                    sessionStorage.setItem("fe_tel", tel);
                    sessionStorage.setItem("fe_name", name);
                    location.href = "/user/phoneVerify";
                } else {
                    alert(res.msg);
                }
            },
            error: function () {
                alert("❌ 서버 요청 중 오류 발생");
            }
        });
    });
});

