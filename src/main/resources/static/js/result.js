$(document).ready(function() {
    const contractId = $("#contractId").val();
    console.log("🔍 result.js 실행됨, contractId=", contractId);

    if (!contractId) {
        console.error("❌ contractId가 비어 있습니다.");
        $("#similar-cases").html("<p class='text-red-500'>⚠️ 계약 ID가 없어 유사사례를 조회할 수 없습니다.</p>");
        return;
    }

    // 유사사례 조회 AJAX
    $.ajax({
        url: contextPath + "/contract/similar/data",
        type: "POST",
        data: { contractId: contractId },
        beforeSend: function() {
            console.log("📡 /contract/similar/data 요청 보냄 → contractId=", contractId);
        },
        success: function(cases) {
            console.log("✅ 응답 받음:", cases);

            if (!Array.isArray(cases)) {
                $("#similar-cases").html("<p class='text-red-500'>⚠️ 잘못된 응답 형식입니다.</p>");
                return;
            }

            let html = "";
            if (cases.length > 0) {
                cases.forEach(c => {
                    html += `
                      <div class="bg-white rounded-2xl shadow-lg p-6 text-left space-y-4">
                        <h3 class="text-lg font-semibold text-blue-700">${c.title}</h3>
                        <p class="text-sm text-gray-500">
                          유형: ${c.riskType || '미분류'} ｜ 조항번호: ${c.articleNumber || '-'}
                        </p>
                        <div class="bg-gray-50 border border-gray-200 rounded-xl p-4 space-y-4">
                          <pre class="whitespace-pre-line text-sm text-gray-800 leading-relaxed">${c.content}</pre>
                        </div>
                      </div>
                    `;
                });
            } else {
                html = "<p class='text-gray-500'>❌ 추천된 유사사례가 없습니다.</p>";
            }
            $("#similar-cases").html(html);
        },
        error: function(xhr, status, error) {
            console.error("❌ AJAX 오류:", status, error);
            console.error("📩 응답 내용:", xhr.responseText);
            $("#similar-cases").html("<p class='text-red-500'>⚠️ 유사사례를 불러오는 중 오류가 발생했습니다.</p>");
        }
    });

    // 버튼 이벤트
    $("#draftBtn").on("click", function() {
        if (!contractId) {
            alert("계약 ID가 없습니다. 다시 시도해주세요.");
            return;
        }
        window.location.href = contextPath + "/contract/draft?contractId=" + contractId;
    });

    $("#homeBtn").on("click", function() {
        window.location.href = contextPath + "/";
    });
});
