// ✅ 공용 모달 함수
function showModal(message, callback) {
    const modal = $("#alertModal");
    $("#alertModalMsg").text(message);
    modal.removeClass("hidden");

    $("#alertModalBtn").off("click").on("click", function () {
        modal.addClass("hidden");
        if (callback) callback();
    });
}

// ✅ 전화번호 확인 여부 플래그
let isPhoneChecked = false;

// ✅ 전화번호 확인 버튼
$("#checkPhoneBtn").on("click", function () {
    const tel = $("#phone").val().replace(/\D/g, "");

    if (!tel) {
        showModal("전화번호를 입력해주세요.");
        return;
    }

    if (!/^01[0-9]{8,9}$/.test(tel)) {
        showModal("올바른 휴대폰 번호를 입력해주세요.");
        return;
    }

    $.ajax({
        url: "/user/checkPhoneExist",
        type: "post",
        dataType: "json",
        data: { tel: tel },
        success: function (res) {
            if (res.result === 1) {
                showModal("등록된 번호입니다.", () => {
                    isPhoneChecked = true; // ✅ 확인 완료
                });
            } else {
                showModal("등록되지 않은 번호입니다.");
                isPhoneChecked = false;
            }
        },
        error: function () {
            showModal("❌ 서버 요청 중 오류 발생");
            isPhoneChecked = false;
        },
    });
});

// ✅ 인증번호 받기 (폼 제출 시 기본 동작 방지)
$("#phone-verify-form").on("submit", function (e) {
    e.preventDefault(); // 🔥 기본 form submit 막기 (새로고침 방지)

    const name = $("#name").val().trim();
    const tel = $("#phone").val().replace(/\D/g, "");

    if (!name) {
        showModal("이름을 입력해주세요.");
        return;
    }

    if (!tel) {
        showModal("전화번호를 입력해주세요.");
        return;
    }

    if (!/^01[0-9]{8,9}$/.test(tel)) {
        showModal("올바른 휴대폰 번호를 입력해주세요.");
        return;
    }

    // ✅ 전화번호 확인 버튼 누르지 않은 경우 차단
    if (!isPhoneChecked) {
        showModal("전화번호 확인을 먼저 해주세요.");
        return;
    }

    // ✅ 인증번호 요청 Ajax
    $.ajax({
        url: "/email/userFindEmail",
        type: "POST",
        dataType: "json",
        data: { name: name, tel: tel },
        beforeSend: function () {
            console.log("📩 인증번호 요청 데이터:", { name, tel });
        },
        success: function (res) {
            console.log("✅ sendVerifyCode 응답:", res);
            if (res.result === 1) {
                showModal("인증번호가 발송되었습니다.", () => {
                    // ✅ 다음 페이지로 이동
                    window.location.href = "/user/phoneVerify";
                });
            } else {
                showModal(res.msg || "인증번호 발송 실패");
            }
        },
        error: function (xhr, status, error) {
            console.error("❌ sendVerifyCode 오류:", status, error);
            showModal("서버 요청 중 오류 발생");
        },
    });
});
