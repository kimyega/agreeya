$(document).ready(function () {
    const steps = $("#analysisSteps .circle");
    const completeMsg = $("#completeMessage");

    // 1. 서버에 분석 요청
    $.ajax({
        url: "/contract/analyze",
        type: "POST",
        success: function (result) {
            if (result === "success") {
                // 단계 애니메이션 실행
                let current = 0;
                const interval = setInterval(() => {
                    if (current < steps.length) {
                        $(steps[current]).addClass("on");
                        current++;
                    } else {
                        clearInterval(interval);
                        completeMsg.removeClass("hidden");

                        // ✅ DB 저장 성공 → 바로 결과 페이지 이동
                        window.location.href = "/contract/result";
                    }
                }, 2000);
            } else {
                alert("❌ 분석 실패: 서버에서 오류 발생");
            }
        },
        error: function (xhr, status, error) {
            console.error("서버 요청 실패:", error);
            alert("❌ 분석 요청 실패");
        }
    });
});
