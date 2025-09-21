let selectedCountryCode = '';
let selectedCountryName = '';

document.addEventListener('DOMContentLoaded', function () {
    const countryCards = document.querySelectorAll('.country-card');
    const selectedText = document.getElementById('selectedCountryText');
    const nextBtn = document.getElementById('nextBtn');
    const prevBtn = document.getElementById('prevBtn');
    const modal = document.getElementById('euModal');
    const homeLink = document.getElementById('homeLink');
    const homeModal = document.getElementById('homeConfirmModal');
    const confirmBtn = document.getElementById('confirmHomeBtn');
    const cancelBtn = document.getElementById('cancelHomeBtn');
    const countryCodeInput = document.getElementById('countryCode'); // hidden input

    // ==============================
    // 상단바 홈 버튼 → 모달 열기
    // ==============================
    if (homeLink) {
        homeLink.addEventListener("click", function (e) {
            e.preventDefault();
            homeModal.classList.remove("hidden");
        });
    }

    // ==============================
    // 모달 → 확인 버튼 (세션 삭제 후 홈 이동)
    // ==============================
    if (confirmBtn) {
        confirmBtn.addEventListener("click", function () {
            console.log("📌 cancelNation 호출 준비: 선택된 국가코드 =", selectedCountryCode);

            $.ajax({
                url: "/contract/cancelNation",
                type: "POST",
                data: { countryCode: selectedCountryCode }, // ✅ 코드 전달
                beforeSend: function () {
                    console.log("📩 서버로 전송할 데이터(cancelNation):", { countryCode: selectedCountryCode });
                },
                success: function (res) {
                    console.log("✅ cancelNation 응답:", res);
                    if (res === "success") {
                        window.location.href = "/";
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
        });
    }

    // ==============================
    // 국가 카드 클릭 이벤트
    // ==============================
    countryCards.forEach(card => {
        card.addEventListener('click', () => {
            const code = card.value;               // DB의 country_code (예: KR, EU, JP)
            const name = card.innerText.trim();   // 화면에 표시될 이름

            selectedCountryCode = code;
            selectedCountryName = name;
            countryCodeInput.value = code;        // hidden input에 저장

            if (code === 'EU') {
                modal.classList.remove('hidden');
                return;
            }

            selectedText.textContent = `선택한 국가: ${name}`;
            nextBtn.disabled = false;
            nextBtn.classList.remove('opacity-50');
        });
    });

    // EU 선택 모달
    modal.addEventListener('click', (e) => {
        if (e.target.tagName === 'BUTTON') {
            const name = e.target.textContent.trim();
            const code = 'EU'; // ✅ EU 선택 시 코드 고정 (상세 국가는 name만 표시)

            selectedCountryCode = code;
            selectedCountryName = name;
            countryCodeInput.value = code;

            selectedText.textContent = `선택한 국가: ${name}`;
            modal.classList.add('hidden');
            nextBtn.disabled = false;
            nextBtn.classList.remove('opacity-50');
        } else if (e.target === modal) {
            modal.classList.add('hidden');
        }
    });

    // ==============================
    // 다음 버튼 → 국가 코드 서버 전송
    // ==============================
    nextBtn.addEventListener("click", () => {
        if (!selectedCountryCode) {
            alert("국가를 선택해주세요.");
            return;
        }

        console.log("📌 selectNation 호출 준비: 선택된 국가코드 =", selectedCountryCode, "국가명 =", selectedCountryName);

        $.ajax({
            url: "/contract/selectNation",
            type: "POST",
            data: { countryCode: selectedCountryCode }, // ✅ 코드 전송
            beforeSend: function () {
                console.log("📩 서버로 전송할 데이터(selectNation):", { countryCode: selectedCountryCode });
            },
            success: function (res) {
                console.log("✅ selectNation 응답:", res);
                if (res === "success") {
                    window.location.href = "/contract/loading";
                } else if (res === "login_required") {
                    alert("로그인 후 이용 가능합니다.");
                    window.location.href = "/user/login";
                } else {
                    alert("국가 선택 저장 실패");
                }
            },
            error: function (xhr, status, error) {
                console.error("❌ selectNation 오류:", status, error);
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
