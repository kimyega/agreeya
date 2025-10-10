$(document).ready(function () {
    console.log("✅ similarCase.js 실행됨");

    const contractId = $("#contractId").val() || new URLSearchParams(window.location.search).get("contractId");
    console.log("📄 현재 contractId =", contractId);

    // ✅ 로딩 애니메이션 스타일 (섹션 내부 중앙 배치)
    const loadingStyle = `
    <style id="loading-style">
        .loading-container {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 50vh;
            width: 100%;
            text-align: center;
        }

        .spinner {
            width: 3.5rem;
            height: 3.5rem;
            border: 6px solid #93c5fd;
            border-top-color: #2563eb;
            border-radius: 50%;
            animation: spin 1s linear infinite;
        }

        @keyframes spin { to { transform: rotate(360deg); } }

        .loading-dots {
            display: inline-block;
            font-size: 1.5rem;
            font-weight: 700;
            color: #2563eb;
            letter-spacing: 2px;
            animation: fadeText 1.8s infinite ease-in-out;
            margin-top: 1.2rem;
        }

        @keyframes fadeText {
            0%, 100% { opacity: 0.6; color: #60a5fa; }
            50% { opacity: 1; color: #2563eb; }
        }

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
    </style>`;
    if (!document.getElementById("loading-style")) {
        $("head").append(loadingStyle);
    }

    function getCategoryClass(category) {
        switch (category) {
            case "근로시간": return "bg-blue-100 text-blue-600";
            case "계약 해지": return "bg-red-100 text-red-600";
            case "임금": return "bg-green-100 text-green-600";
            case "복리후생": return "bg-yellow-100 text-yellow-600";
            default: return "bg-gray-100 text-gray-600";
        }
    }

    // ✅ AJAX 요청 저장용 변수
    let ajaxRequest = null;

    // ✅ 유사 계약서 불러오기
    ajaxRequest = $.ajax({
        url: contextPath + "/contract/similar/data",
        type: "POST",
        data: { contractId: contractId },
        beforeSend: function () {
            console.log("📡 유사사례 데이터 요청 중...");
            $("#similar-cases")
                .data("oldClass", $("#similar-cases").attr("class"))
                .attr("class", "flex items-center justify-center")
                .html(`
                    <div class="loading-container">
                        <div class="spinner"></div>
                        <div class="loading-dots">
                            유사 계약서 불러오는 중<span>.</span><span>.</span><span>.</span>
                        </div>
                    </div>
                `);
        },
        success: function (cases) {
            console.log("✅ 유사사례 데이터:", cases);
            const oldClass = $("#similar-cases").data("oldClass");
            $("#similar-cases").attr("class", oldClass);

            if (!Array.isArray(cases)) {
                $("#similar-cases").html("<p class='text-red-500 text-center mt-10'>⚠️ 잘못된 데이터 형식입니다.</p>");
                return;
            }

            let html = "";
            if (cases.length > 0) {
                cases.forEach(c => {
                    const categoryClass = getCategoryClass(c.category);
                    html += `
                        <div class="bg-white rounded-xl shadow-md p-6 cursor-pointer hover:shadow-lg hover:scale-[1.02] transition"
                             onclick="window.location.href='${contextPath}/contract/result?contractId=${c.contractId}'">
                            <h3 class="text-lg font-bold text-blue-600 mb-2">${c.title || "제목 없음"}</h3>
                            <p class="text-sm text-gray-700 mb-3">
                                ${(c.summary && c.summary.length > 120)
                        ? c.summary.substring(0, 120) + "..."
                        : c.summary || "요약 없음"}
                            </p>
                            <span class="inline-block px-3 py-1 text-xs font-medium rounded-full ${categoryClass}">
                                유형: ${c.category || '미분류'}
                            </span>
                        </div>
                    `;
                });
            } else {
                html = "<p class='text-gray-500 text-center mt-10'>❌ 추천된 유사 계약서가 없습니다.</p>";
            }
            $("#similar-cases").html(html);
        },
        error: function (xhr, status, error) {
            if (status === "abort") {
                console.log("🛑 유사사례 AJAX 요청이 취소됨 (페이지 이동)");
                return;
            }
            console.error("❌ 유사사례 AJAX 오류:", status, error);
            $("#similar-cases").html("<p class='text-red-500 text-center mt-10'>⚠️ 데이터를 불러오는 중 오류가 발생했습니다.</p>");
        }
    });

    // ✅ 페이지를 떠날 때 AJAX 요청 중단
    $(window).on("beforeunload pagehide", function () {
        if (ajaxRequest && ajaxRequest.readyState !== 4) {
            ajaxRequest.abort();
        }
    });

    // ✅ 버튼 이벤트
    $("#draftBtn").on("click", function () {
        if (ajaxRequest && ajaxRequest.readyState !== 4) ajaxRequest.abort();
        window.location.href = contextPath + "/contract/draft?contractId=" + contractId;
    });

    $("#backBtn").on("click", function () {
        if (ajaxRequest && ajaxRequest.readyState !== 4) ajaxRequest.abort();
        window.location.href = contextPath + "/contract/result?contractId=" + contractId;
    });

    $("#homeBtn").on("click", function () {
        if (ajaxRequest && ajaxRequest.readyState !== 4) ajaxRequest.abort();

        console.log("🏠 홈으로 돌아가기 버튼 클릭됨 — countryId 세션 제거 중...");
        $.ajax({
            url: contextPath + "/contract/clearSimilarSession",
            type: "POST",
            success: function () {
                console.log("✅ 세션의 countryId 제거 완료");
                window.location.href = contextPath + "/";
            },
            error: function (xhr, status, error) {
                console.error("❌ 세션 초기화 오류:", error);
                window.location.href = contextPath + "/";
            }
        });
    });
});
