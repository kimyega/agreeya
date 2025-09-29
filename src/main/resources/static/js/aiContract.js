$(document).ready(function () {
    const contractId = $("#contractId").val();
    const contractPreview = $("#contractPreview");
    let translations = { ko: "", ja: "", en: "" };

    // ✅ 텍스트 포맷 함수 (조항 줄바꿈 + <br> 변환)
    function formatContractText(text) {
        return text
            .replace(/(제\d+조.*?)(?=제\d+조|$)/gs, "$1\n\n") // 조항 단위 줄바꿈
            .replace(/\n/g, "<br>"); // 화면 표시용 줄바꿈
    }

    // ✅ 초안 불러오기
    $.ajax({
        url: "/contract/newDraft",
        type: "POST",
        data: { contractId: contractId },
        success: function (res) {
            if (res.result === 1) {
                const draft = JSON.parse(res.data); // String → Object
                translations.ko = draft.draftKr;
                translations.ja = draft.draftJp;
                translations.en = draft.draftEn;

                // 기본은 한국어 표시
                contractPreview.html(formatContractText(translations.ko));
            } else {
                contractPreview.text("❌ 초안 생성 실패: " + res.msg);
            }
        },
        error: function (xhr, status, error) {
            console.error("초안 불러오기 실패:", error);
            contractPreview.text("❌ 서버 오류 발생");
        }
    });

    // ✅ 번역 버튼
    $("#translateBtn").on("click", function () {
        $("#translateModal").removeClass("hidden");
    });

    // ✅ 모달에서 언어 선택
    $("#translateModal button[data-lang]").on("click", function () {
        const lang = $(this).data("lang");
        contractPreview.html(formatContractText(translations[lang] || "❌ 번역 없음"));
        $("#translateModal").addClass("hidden");
    });

    // ✅ 모달 외부 클릭 시 닫기
    $("#translateModal").on("click", function (e) {
        if (e.target === this) {
            $(this).addClass("hidden");
        }
    });

    // ✅ PDF 다운로드
    $("#pdfBtn").on("click", function () {
        const text = document.getElementById("contractPreview").innerText;

        const { jsPDF } = window.jspdf;
        const pdf = new jsPDF({
            orientation: "portrait",
            unit: "mm",
            format: "a4"
        });

        // ✅ 한국어/일본어 폰트 등록 (base64 → js 파일 필요)
        pdf.addFileToVFS("NotoSansKR-Regular.ttf", window.notoSansKr);
        pdf.addFont("NotoSansKR-Regular.ttf", "NotoSansKR", "normal");

        pdf.addFileToVFS("NotoSansJP-Regular.ttf", window.notoSansJp);
        pdf.addFont("NotoSansJP-Regular.ttf", "NotoSansJP", "normal");

        // ✅ 기본은 한국어 폰트 사용
        pdf.setFont("NotoSansKR");
        pdf.setFontSize(12);

        // ✅ 긴 텍스트 자동 줄바꿈 처리
        const margin = 15;
        const lineHeight = 8;
        const pageWidth = pdf.internal.pageSize.getWidth() - margin * 2;
        const lines = pdf.splitTextToSize(text, pageWidth);

        let y = margin;
        lines.forEach(line => {
            if (y > pdf.internal.pageSize.getHeight() - margin) {
                pdf.addPage();
                y = margin;
                pdf.setFont("NotoSansKR"); // 새 페이지에서도 폰트 유지
            }
            pdf.text(line, margin, y);
            y += lineHeight;
        });

        // 저장
        pdf.save("contract_draft.pdf");
    });
});
