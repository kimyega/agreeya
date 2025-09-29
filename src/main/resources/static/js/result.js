$(document).ready(function() {
    console.log("🔍 result.js 실행됨");

    // 유사사례 조회 AJAX
    $.ajax({
        url: contextPath + "/contract/similar/data",
        type: "POST",
        beforeSend: function () {
            console.log("📡 /contract/similar/data 요청 보냄 (세션 contractId 사용)");
        },
        success: function (cases) {
            console.log("✅ 응답 받음:", cases);

            if (!Array.isArray(cases)) {
                $("#similar-cases").html("<p class='text-red-500'>⚠️ 잘못된 응답 형식입니다.</p>");
                return;
            }

            let html = "";
            if (cases.length > 0) {
                cases.forEach(c => {
                    html += `
                      <div class="bg-white rounded-xl shadow-md p-6 text-left cursor-pointer hover:shadow-lg transition"
                           onclick="window.location.href='${contextPath}/contract/result?contractId=${c.contractId}'">

                        <!-- 제목 -->
                        <h3 class="text-lg font-bold text-blue-700 mb-2">
                          ${c.title || "제목 없음"}
                        </h3>

                        <!-- 요약 -->
                        <p class="text-sm text-gray-700 leading-relaxed">
                          ${c.summary || "요약 없음"}
                        </p>

                        <!-- 유형 태그 -->
                        <span class="inline-block mt-3 px-3 py-1 text-xs rounded-full
                          ${c.category === '근로시간' ? 'bg-blue-100 text-blue-600' :
                        c.category === '계약 해지' ? 'bg-red-100 text-red-600' :
                            c.category === '임금' ? 'bg-green-100 text-green-600' :
                                c.category === '복리후생' ? 'bg-yellow-100 text-yellow-600' :
                                    'bg-gray-100 text-gray-600'}">
                          유형: ${c.category || "미분류"}
                        </span>
                      </div>
                    `;
                });
            } else {
                html = "<p class='text-gray-500'>❌ 추천된 유사사례가 없습니다.</p>";
            }
            $("#similar-cases").html(html);
        },
        error: function (xhr, status, error) {
            console.error("❌ AJAX 오류:", status, error);
            $("#similar-cases").html("<p class='text-red-500'>⚠️ 유사사례를 불러오는 중 오류가 발생했습니다.</p>");
        }
    });

    // 버튼 이벤트
    $("#draftBtn").on("click", function() {
        window.location.href = contextPath + "/contract/draft"; // 세션에서 contractId 사용
    });

    $("#homeBtn").on("click", function() {
        window.location.href = contextPath + "/";
    });

    // 계약서 요약 + 조항 데이터 가져오기
    $.ajax({
        url: contextPath + "/contract/result/data",
        type: "POST",
        dataType: "json",
        success: function(res) {
            if (res.summary) {
                $("#aiCommentContainer").html(`
                  <div class="bg-gray-50 border border-gray-200 rounded-xl p-4 text-sm text-gray-700">
                    ${res.summary.translatedText}
                  </div>
                `);

                let riskData = res.summary.riskChartData;
                if (typeof riskData === "string") {
                    riskData = JSON.parse(riskData);
                }

                const ctx = document.getElementById('riskChart').getContext('2d');
                new Chart(ctx, {
                    type: 'bar',
                    data: {
                        labels: Object.keys(riskData),
                        datasets: [{
                            label: '위험 점수',
                            data: Object.values(riskData),
                            backgroundColor: ['#3b82f6','#ef4444','#facc15','#10b981'],
                        }]
                    },
                    options: {
                        responsive: true,
                        plugins: {
                            legend: { display: false },
                            tooltip: { enabled: true }
                        },
                        scales: {
                            y: { beginAtZero: true }
                        }
                    }
                });
            }

            if (Array.isArray(res.clauses) && res.clauses.length > 0) {
                let html = '';
                res.clauses.forEach((c, idx) => {
                    html += `
                      <div class="bg-white rounded-2xl shadow-lg p-6 space-y-2">
                        <h3 class="text-lg font-semibold text-blue-700">조항 ${idx + 1}</h3>
                        <p class="text-sm text-gray-500">유형: ${c.riskType} ｜ 위험 점수: ${c.riskScore}</p>
                        <div class="bg-gray-50 border border-gray-200 rounded-xl p-4 text-sm text-gray-800">
                          ${c.clauseText.replace(/\n/g,'<br>')}
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
        error: function(xhr,status,error) {
            console.error("❌ AJAX 오류:", status,error);
        }
    });
});
