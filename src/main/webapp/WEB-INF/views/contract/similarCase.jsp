<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>안심계약 - 유사 사례 상세보기</title>

    <!-- Tailwind CSS -->
    <script src="https://cdn.tailwindcss.com"></script>

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />

    <!-- Table CSS JS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/table.css"/>
    <script src="${pageContext.request.contextPath}/js/table.js"></script>

    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>const contextPath = "${pageContext.request.contextPath}";</script>
</head>

<body class="bg-slate-100 text-gray-800 font-sans">

<!-- 헤더 -->
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/include/header.jsp" />

<main class="max-w-6xl mx-auto px-6 py-20 space-y-16 text-center">

    <!-- 제목 -->
    <section>
        <h2 class="text-4xl font-extrabold text-gray-900 mb-4">📚 유사 사례 상세 보기</h2>
        <p class="text-lg text-gray-600">AI와 법률 DB 분석을 기반으로 추천된 근로계약 사례입니다.</p>
    </section>

    <!-- 유사사례 영역 -->
    <section id="similar-cases" class="space-y-12">
        <p class="text-gray-500">🔄 유사사례 불러오는 중...</p>
    </section>

    <!-- ✅ hidden input (contractId 저장) -->
    <input type="hidden" id="contractId" value="${param.contractId}"/>

    <!-- 버튼 -->
    <section class="flex flex-col sm:flex-row gap-4 justify-center mt-12">
        <a href="${pageContext.request.contextPath}/contract/result?contractId=${param.contractId}"
           class="bg-gray-300 text-gray-800 px-6 py-3 rounded-full hover:bg-gray-400 transition w-full sm:w-auto text-center">
            분석 결과로 돌아가기
        </a>
        <button id="draftBtn"
                class="bg-blue-600 text-white px-6 py-3 rounded-full hover:bg-blue-700 transition w-full sm:w-auto text-center">
            GPT로 계약서 초안 생성하기
        </button>
    </section>
</main>

<!-- 푸터 -->
<footer class="mt-20 text-sm text-gray-500 py-6 text-center border-t border-gray-200">
    © 2025 안심계약. All rights reserved.
</footer>

<!-- ✅ JS (inline script) -->
<script>
    $(document).ready(function() {
        const contractId = $("#contractId").val(); // ✅ JSP hidden input 값 읽기
        console.log("🔍 similar.js 실행됨, contractId=", contractId);

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

                let html = "";
                if (cases.length > 0) {
                    cases.forEach(c => {
                        html += `
                        <div class="bg-white rounded-2xl shadow-lg p-8 text-left space-y-4">
                          <h3 class="text-2xl font-semibold text-blue-700">${c.title}</h3>
                          <p class="text-sm text-gray-500">
                            유형: ${c.riskType || '미분류'} ｜ 조항번호: ${c.articleNumber || '-'}
                          </p>
                          <div class="bg-gray-50 border border-gray-200 rounded-xl p-6 space-y-4">
                            <h4 class="text-lg font-bold text-blue-600">📄 관련 법령 내용</h4>
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
                $("#similar-cases").html("<p class='text-red-500'>⚠️ 유사사례를 불러오는 중 오류가 발생했습니다.</p>");
            }
        });

        // 초안 생성 버튼
        $("#draftBtn").on("click", function() {
            window.location.href = contextPath + "/contract/draft?contractId=" + contractId;
        });
    });
</script>

</body>
</html>
