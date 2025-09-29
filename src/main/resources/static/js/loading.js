$(document).ready(function () {
    // ==============================
    // 상단 메뉴 관련
    // ==============================
    const navLinks = document.querySelectorAll(".nav-link");
    const homeModal = document.getElementById("homeConfirmModal");
    const confirmBtn = document.getElementById("confirmHomeBtn");
    const cancelBtn = document.getElementById("cancelHomeBtn");

    let targetHref = null;

    navLinks.forEach((link) => {
        link.addEventListener("click", function (e) {
            e.preventDefault();
            targetHref = this.getAttribute("href");
            if (homeModal) {
                homeModal.classList.remove("hidden");
                console.log("✅ 모달 열림:", targetHref);
            }
        });
    });

    if (confirmBtn) {
        confirmBtn.addEventListener("click", function () {
            if (!targetHref) return;
            $.ajax({
                url: "/contract/loadingCancelNation",
                type: "POST",
                success: function (res) {
                    if (res === "success") {
                        window.location.href = targetHref;
                    } else {
                        alert("삭제 실패: " + res);
                    }
                },
                error: function () {
                    alert("서버 오류 발생");
                },
            });
        });
    }

    if (cancelBtn) {
        cancelBtn.addEventListener("click", function () {
            if (homeModal) {
                homeModal.classList.add("hidden");
            }
        });
    }

    // ==============================
    // 로딩 단계 애니메이션 (로딩 페이지에서만 실행)
    // ==============================
    if ($("#analysisSteps").length > 0) {   // ✅ 이 조건 추가
        const steps = $("#analysisSteps .circle");
        const completeMsg = $("#completeMessage");

        $.ajax({
            url: "/contract/analyze",
            type: "POST",
            success: function (result) {
                if (result === "success") {
                    let current = 0;
                    const interval = setInterval(() => {
                        if (current < steps.length) {
                            $(steps[current]).addClass("on bg-blue-500");
                            current++;
                        } else {
                            clearInterval(interval);
                            completeMsg.removeClass("hidden");
                            window.location.href = "/contract/result";
                        }
                    }, 2000);
                } else {
                    alert("❌ 분석 실패");
                }
            },
            error: function () {
                alert("❌ 분석 요청 실패");
            },
        });
    }
});
