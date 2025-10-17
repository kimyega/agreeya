$(document).ready(function () {

    // ✅ 로딩 애니메이션 스타일 (스피너 + 점등 효과)
    const loadingStyle = `
        <style id="loading-style">
            .loading-container {
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
                text-align: center;
                margin-top: 3rem;
            }

            /* ✅ 회전 동그라미 (크기 키움) */
            .spinner {
                width: 3.5rem;  /* 기존 2.5rem → 3.5rem */
                height: 3.5rem;
                border: 6px solid #93c5fd;  /* 테두리 두께 살짝 증가 */
                border-top-color: #2563eb;
                border-radius: 50%;
                animation: spin 1s linear infinite;
            }

            @keyframes spin {
                to { transform: rotate(360deg); }
            }

            /* 로딩 텍스트 */
            .loading-dots {
                display: inline-block;
                font-size: 2rem;
                font-weight: 700;
                color: #2563eb;
                letter-spacing: 2px;
                animation: fadeText 1.8s infinite ease-in-out;
                margin-top: 1.2rem;
                transform: translateX(6px); /* ✅ 텍스트 살짝 오른쪽 이동 */
            }

            /* "로딩 중" 자체 점등 */
            @keyframes fadeText {
                0%, 100% { opacity: 0.6; color: #60a5fa; }
                50% { opacity: 1; color: #2563eb; }
            }

            /* 점 3개 순차 점등 */
            .loading-dots span {
                opacity: 0.3;
                color: #60a5fa;
                animation: blueBlink 1.5s infinite;
            }

            .loading-dots span:nth-child(1) { animation-delay: 0s; }
            .loading-dots span:nth-child(2) { animation-delay: 0.3s; }
            .loading-dots span:nth-child(3) { animation-delay: 0.6s; }

            @keyframes blueBlink {
                0%, 80%, 100% { opacity: 0.3; color: #93c5fd; }
                40% { opacity: 1; color: #2563eb; }
            }
        </style>
    `;
    $("head").append(loadingStyle);

    const contractId = $("#contractId").val();
    const contractPreview = $("#contractPreview");
    let translations = { ko: null, ja: null, en: null };

    // ✅ 텍스트 포맷 함수 (콤마 뒤 줄바꿈 제거)
    function formatClauseText(text) {
        return text
            .replace(/\s{2,}/g, " ")
            .replace(/\n{2,}/g, "\n")
            .replace(/,(\s*\n)/g, ", ")
            .trim();
    }

    // ✅ 계약서 렌더링
    function renderContract(data, lang) {
        if (!data) return "❌ 해당 언어의 계약서가 없습니다.";

        $("#contractPreview").css({
            "padding": "16px",
            "margin": "0",
            "line-height": "1.9",
            "font-size": "17px",
            "font-weight": "400",
            "white-space": "normal",
            "display": "block",
            "text-align": "left",
        });

        let html = `
            <div style="margin-bottom:18px; display:block; line-height:1.6;">
                <strong style="font-size:18px;">${lang === "ko" ? "사업주(甲)" : lang === "en" ? "Employer (甲)" : "事業主(甲)"}</strong><br>
                ${lang === "ko" ? "상호" : lang === "en" ? "Company" : "会社名"}: ${data.employer.companyName}<br>
                ${lang === "ko" ? "대표자" : lang === "en" ? "Representative" : "代表者"}: ${data.employer.representative}<br>
                ${lang === "ko" ? "업종" : lang === "en" ? "Business Type" : "業種"}: ${data.employer.businessType}
            </div>

            <div style="margin-bottom:20px; display:block; line-height:1.6;">
                <strong style="font-size:18px;">${lang === "ko" ? "근로자(乙)" : lang === "en" ? "Employee (乙)" : "従業員(乙)"}</strong><br>
                ${lang === "ko" ? "성명" : lang === "en" ? "Name" : "氏名"}: ${data.employee.name}
            </div>
        `;

        html += `<div style="line-height:1.9; margin-bottom:10px;">`;
        data.clauses.forEach((clause) => {
            html += `
                <p style="margin: 12px 0; text-indent: 0; display:block;">
                    ${formatClauseText(clause)}
                </p>`;
        });
        html += `</div>`;

        html += `
            <div style="margin-top:16px; font-weight:600; text-align:left; font-size:16px;">
                ${lang === "ko" ? "작성일" : lang === "en" ? "Date" : "作成日"}: ${data.date}
            </div>
        `;

        return html;
    }

    // ✅ 초안 불러오기 (로딩 효과 추가)
    $.ajax({
        url: "/contract/newDraft",
        type: "POST",
        data: { contractId: contractId },
        beforeSend: function () {
            contractPreview.html(`
                <div class="loading-container">
                    <div class="spinner"></div>
                    <div class="loading-dots">
                        로딩 중<span>.</span><span>.</span><span>.</span>
                    </div>
                </div>
            `);
        },
        success: function (res) {
            if (res.result === 1) {
                const draft = JSON.parse(res.data);
                translations.ko = draft.draftKr ? JSON.parse(draft.draftKr) : null;
                translations.en = draft.draftEn ? JSON.parse(draft.draftEn) : null;
                translations.ja = draft.draftJp ? JSON.parse(draft.draftJp) : null;

                setTimeout(() => {
                    contractPreview.html(renderContract(translations.ko, "ko"));
                }, 800);
            } else {
                contractPreview.text("❌ 초안 생성 실패: " + res.msg);
            }
        },
        error: function (xhr, status, error) {
            console.error("초안 불러오기 실패:", error);
            contractPreview.text("❌ 서버 오류 발생");
        }
    });

    // ✅ 번역 모달
    $("#translateBtn").on("click", function () {
        $("#translateModal").removeClass("hidden");
    });

    // ✅ 언어 선택
    $("#translateModal button[data-lang]").on("click", function () {
        const lang = $(this).data("lang");
        const target =
            lang === "ko" ? translations.ko :
                lang === "en" ? translations.en :
                    translations.ja;

        contractPreview.html(renderContract(target, lang));
        $("#translateModal").addClass("hidden");
    });

    // ✅ 모달 외부 클릭 닫기
    $("#translateModal").on("click", function (e) {
        if (e.target === this) $(this).addClass("hidden");
    });

    // ✅ PDF 다운로드
    $("#pdfBtn").on("click", function () {
        const { jsPDF } = window.jspdf;
        const pdf = new jsPDF({ orientation: "portrait", unit: "mm", format: "a4" });

        pdf.addFileToVFS("NotoSansKR-Regular.ttf", window.notoSansKr);
        pdf.addFont("NotoSansKR-Regular.ttf", "NotoSansKR", "normal");
        pdf.addFileToVFS("NotoSansJP-Regular.ttf", window.notoSansJp);
        pdf.addFont("NotoSansJP-Regular.ttf", "NotoSansJP", "normal");

        pdf.setFont("NotoSansKR");
        pdf.setFontSize(13);

        const text = document.getElementById("contractPreview").innerText;
        const margin = 15;
        const lineHeight = 8;
        const pageWidth = pdf.internal.pageSize.getWidth() - margin * 2;
        const lines = pdf.splitTextToSize(text, pageWidth);

        let y = margin;
        lines.forEach((line) => {
            if (y > pdf.internal.pageSize.getHeight() - margin) {
                pdf.addPage();
                y = margin;
            }
            pdf.text(line, margin, y);
            y += lineHeight;
        });

        const currentLang =
            contractPreview.text().includes("사업주") ? "KR" :
                contractPreview.text().includes("Employer") ? "EN" : "JP";
        pdf.save(`contract_draft_${currentLang}.pdf`);
    });
    function clearDraftSession() {
        $.ajax({
            url: "/clearDraftSession",
            type: "POST",
            async: false, // beforeunload에서 안정적으로 작동
            success: function () {
                console.log("🧹 Draft 세션 정리 완료");
            },
            error: function (xhr, status, error) {
                console.warn("⚠️ Draft 세션 정리 실패:", error);
            }
        });
    }

    // ✅ 1️⃣ 페이지 떠날 때 세션 정리 (새로고침, 뒤로가기, 다른 페이지)
    window.addEventListener("beforeunload", function () {
        clearDraftSession();
    });

    // ✅ 2️⃣ 홈 버튼 클릭 시 세션 정리 후 이동
    $("a[href='/']").on("click", function (e) {
        e.preventDefault();
        clearDraftSession();
        window.location.href = "/";
    });

});
