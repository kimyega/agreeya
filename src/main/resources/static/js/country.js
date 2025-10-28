let selectedCountryCode = '';
let selectedCountryName = '';

document.addEventListener('DOMContentLoaded', function () {
    const countryCards = document.querySelectorAll('.country-card');
    const selectedText = document.getElementById('selectedCountryText');
    const nextBtn = document.getElementById('nextBtn');
    const prevBtn = document.getElementById('prevBtn');
    const countryCodeInput = document.getElementById('countryCode');

    // 상단 메뉴 관련
    const navLinks = document.querySelectorAll('.nav-link');
    const homeModal = document.getElementById('homeConfirmModal');
    const confirmBtn = document.getElementById('confirmHomeBtn');
    const cancelBtn = document.getElementById('cancelHomeBtn');

    let targetHref = null; // ✅ 클릭한 메뉴 URL 저장용

    // ==============================
    // 상단바 메뉴 → 모달 열기
    // ==============================
    navLinks.forEach(link => {
        link.addEventListener("click", function (e) {
            e.preventDefault();
            targetHref = this.getAttribute("href");
            homeModal.classList.remove("hidden");
        });
    });

    // 모달 → 확인 버튼
    if (confirmBtn) {
        confirmBtn.addEventListener("click", function () {
            if (!targetHref) return;

            $.ajax({
                url: "/contract/cancelNation",
                type: "POST",
                success: function (res) {
                    console.log("✅ cancelNation 응답:", res);
                    if (res === "success") {
                        window.location.href = targetHref;
                    } else if (res === "login_required") {
                        alert("로그인 후 이용 가능합니다.");
                        window.location.href = "/user/login";
                    } else {
                        alert("계약서 삭제 실패: " + res);
                    }
                },
                error: function (xhr, status, error) {
                    console.error("❌ cancelNation 오류:", status, error);
                    alert("서버 오류 발생");
                }
            });
        });
    }

    // 모달 → 취소 버튼
    if (cancelBtn) {
        cancelBtn.addEventListener("click", function () {
            homeModal.classList.add("hidden");
            targetHref = null;
        });
    }

    // ==============================
    // 국가 카드 클릭 이벤트
    // ==============================
    countryCards.forEach(card => {
        card.addEventListener('click', () => {
            const code = card.value;
            const name = card.innerText.trim();

            // 기존 선택 해제
            countryCards.forEach(c => c.classList.remove('ring-4', 'ring-blue-500'));

            // 현재 선택
            card.classList.add('ring-4', 'ring-blue-500');

            selectedCountryCode = code;
            selectedCountryName = name;
            countryCodeInput.value = code;

            // 선택 표시 갱신
            selectedText.textContent = `선택한 국가: ${name}`;
            nextBtn.disabled = false;
            nextBtn.classList.remove('opacity-50', 'cursor-not-allowed');
        });
    });

    // ==============================
    // 다음 단계 버튼
    // ==============================
    nextBtn.addEventListener("click", () => {
        if (!selectedCountryCode) {
            alert("국가를 선택해주세요.");
            return;
        }

        console.log("📌 saveCountry 호출 준비:", selectedCountryCode, selectedCountryName);

        $.ajax({
            url: "/contract/saveCountry",
            type: "POST",
            dataType: "json",
            data: { countryCode: selectedCountryCode },
            beforeSend: function () {
                console.log("📩 서버로 전송할 데이터:", { countryCode: selectedCountryCode });
            },
            success: function (res) {
                console.log("✅ saveCountry 응답:", res);
                if (res.result === 1) {
                    window.location.href = "/contract/loading";
                } else if (res.result === 0 && res.msg.includes("로그인")) {
                    alert("로그인 후 이용 가능합니다.");
                    window.location.href = "/user/login";
                } else {
                    alert("국가 선택 저장 실패: " + res.msg);
                }
            },
            error: function (xhr, status, error) {
                console.error("❌ saveCountry 오류:", status, error);
                alert("서버 오류 발생");
            }
        });
    });

    // ==============================
    // 이전 단계 버튼
    // ==============================
    prevBtn.addEventListener("click", () => {
        if (prevBtn.disabled) return;
        window.location.href = "/contract/upload";
    });
});
