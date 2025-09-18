let selectedCountry = '';

document.addEventListener('DOMContentLoaded', function () {
    const countryCards = document.querySelectorAll('.country-card');
    const selectedText = document.getElementById('selectedCountryText');
    const nextBtn = document.getElementById('nextBtn');
    const prevBtn = document.getElementById('prevBtn');
    const modal = document.getElementById('euModal');
    const homeLink = document.getElementById('homeLink'); // ✅ 홈 링크
    const homeModal = document.getElementById('homeConfirmModal'); // ✅ 홈 확인 모달
    const confirmBtn = document.getElementById('confirmHomeBtn'); // ✅ 모달 확인 버튼
    const cancelBtn = document.getElementById('cancelHomeBtn');   // ✅ 모달 취소 버튼

    // ==============================
    // 상단바 홈 버튼 → 모달 열기
    // ==============================
    if (homeLink) {
        homeLink.addEventListener("click", function (e) {
            e.preventDefault(); // 기본 이동 막기
            homeModal.classList.remove("hidden"); // 모달 열기
        });
    }


    confirmBtn.addEventListener("click", function () {
        window.location.href = "/"; // 바로 메인 페이지 이동
    });

    // 모달 → 취소 버튼
    cancelBtn.addEventListener("click", function () {
        homeModal.classList.add("hidden"); // 모달 닫기
    });

    // ==============================
    // 국가 카드 클릭 이벤트
    // ==============================
    countryCards.forEach(card => {
        card.addEventListener('click', () => {
            const country = card.dataset.country;
            selectedCountry = country;

            if (country === 'E U') {
                modal.classList.remove('hidden');
                return;
            }

            selectedText.textContent = `선택한 국가: ${country}`;
            nextBtn.disabled = false;
            nextBtn.classList.remove('opacity-50');
        });
    });

    // EU 선택 모달
    modal.addEventListener('click', (e) => {
        if (e.target.tagName === 'BUTTON') {
            const selectedEU = e.target.textContent;
            selectedCountry = selectedEU;

            selectedText.textContent = `선택한 국가: ${selectedEU}`;
            modal.classList.add('hidden');
            nextBtn.disabled = false;
            nextBtn.classList.remove('opacity-50');
        } else if (e.target === modal) {
            modal.classList.add('hidden');
        }
    });

    // 다음 버튼
    nextBtn.addEventListener("click", () => {
        if (!selectedCountry) {
            alert("국가를 선택해주세요.");
            return;
        }

        $.ajax({
            url: "/contract/selectNation",
            type: "POST",
            data: { countryId: selectedCountry },
            success: function (res) {
                if (res === "success") {
                    window.location.href = "/contract/loading";
                } else if (res === "login_required") {
                    alert("로그인 후 이용 가능합니다.");
                    window.location.href = "/user/login";
                } else {
                    alert("국가 선택 저장 실패");
                }
            },
            error: function () {
                alert("서버 오류 발생");
            }
        });
    });

    // 이전 버튼
    prevBtn.addEventListener("click", () => {
        if (prevBtn.disabled) return;
        window.location.href = "/contract/upload";
    });
});
