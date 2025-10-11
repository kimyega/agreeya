$(document).ready(function () {
    console.log("✅ result.js 실행됨");

    const contractId = new URLSearchParams(window.location.search).get("contractId") || "";

    // 버튼 이벤트
    $("#similarCaseBtn").on("click", function () {
        window.location.href = contextPath + "/contract/similar?contractId=" + contractId;
    });

    $("#draftBtn").on("click", function () {
        window.location.href = contextPath + "/contract/draft?contractId=" + contractId;
    });

    $("#homeBtn").on("click", function () {
        window.location.href = contextPath + "/";
    });

    // ✅ 분석 결과 데이터 불러오기
    $.ajax({
        url: contextPath + "/contract/result/data",
        type: "POST",
        dataType: "json",
        data: { contractId: contractId },
        success: function (res) {
            console.log("📡 result/data 응답:", res);

            // AI 코멘트 요약
            if (res.summary) {
                $("#aiCommentContainer").html(`
          <div class="bg-gray-50 border border-gray-200 rounded-xl p-4 text-sm text-gray-700">
            ${res.summary.translatedText}
          </div>
        `);

                let riskData = res.summary.riskChartData;
                if (typeof riskData === "string") riskData = JSON.parse(riskData);

                const ctx = document.getElementById("riskChart").getContext("2d");
                new Chart(ctx, {
                    type: "bar",
                    data: {
                        labels: Object.keys(riskData),
                        datasets: [
                            {
                                label: "위험 점수",
                                data: Object.values(riskData),
                                backgroundColor: ["#3b82f6", "#ef4444", "#facc15", "#10b981"],
                            },
                        ],
                    },
                    options: {
                        responsive: true,
                        plugins: { legend: { display: false } },
                        scales: { y: { beginAtZero: true } },
                    },
                });
            }

            // 조항별 결과
            if (Array.isArray(res.clauses) && res.clauses.length > 0) {
                let html = "";
                res.clauses.forEach((c, idx) => {
                    html += `
            <div class="bg-white rounded-2xl shadow-lg p-6 space-y-2">
              <h3 class="text-lg font-semibold text-blue-700">조항 ${idx + 1}</h3>
              <p class="text-sm text-gray-500">유형: ${c.riskType} ｜ 위험 점수: ${c.riskScore}</p>
              <div class="bg-gray-50 border border-gray-200 rounded-xl p-4 text-sm text-gray-800">
                ${c.clauseText.replace(/\n/g, "<br>")}
              </div>
              <p class="text-sm text-red-600">💡 AI 코멘트: ${c.aiComment}</p>
            </div>
          `;
                });
                $("#clauseContainer").html(html);
            } else {
                $("#clauseContainer").html("<p class='text-gray-500'>조항 데이터가 없습니다.</p>");
            }
        },
        error: function (xhr, status, error) {
            console.error("❌ 분석 결과 AJAX 오류:", status, error);
        },
    });
});
