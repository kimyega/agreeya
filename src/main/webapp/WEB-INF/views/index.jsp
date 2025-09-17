<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html >
<html lang="ko">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>안심계약 - AI 근로계약 분석</title>

    <!-- Tailwind CSS -->
    <script src="https://cdn.tailwindcss.com"></script>

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
</head>

<body class="bg-slate-100 text-gray-800 font-sans">

<!-- 헤더 -->
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/include/header.jsp" />


<!-- 메인 -->
<main class="min-h-screen flex flex-col items-center justify-center px-6 py-16 text-center">
    <div class="max-w-3xl space-y-6">
        <h2 class="text-4xl font-extrabold text-gray-900 drop-shadow-sm">
            공정하고 안전한 <span class="text-blue-500">AI 근로계약</span>의 시작
        </h2>
        <p class="text-gray-700 text-lg">
            AI가 근로계약서를 분석해 위험 요소를 감지하고,<br/>
            다양한 언어로 계약서 초안을 자동 생성해드립니다.
        </p>
    </div>

    <!-- 카드들 -->
    <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-8 mt-14 w-full max-w-6xl px-6 items-stretch">
        <!-- 계약서 분석 -->
        <div class="bg-white rounded-2xl p-8 shadow-md hover:shadow-lg transition flex flex-col justify-between h-full">
            <div>
                <h3 class="text-xl font-semibold text-gray-800 mb-2">계약서 분석</h3>
                <p class="text-gray-600 mb-6 text-sm leading-relaxed">
                    업로드한 계약서에서 위험한 조항을 자동 탐지하고 분석 리포트를 제공합니다.
                </p>
            </div>
            <a href="/contract/upload"
               class="bg-blue-500 text-white px-6 py-2 rounded-full hover:bg-blue-600 transition">시작하기</a>
        </div>

        <!-- Q&A 챗봇 -->
        <div class="bg-white rounded-2xl p-8 shadow-md hover:shadow-lg transition flex flex-col justify-between h-full">
            <div>
                <h3 class="text-xl font-semibold text-gray-800 mb-2">Q&A 챗봇</h3>
                <p class="text-gray-600 mb-6 text-sm leading-relaxed">
                    노동법, 근로계약서 작성 등 궁금한 점을 AI 챗봇에게 질문하세요.
                </p>
            </div>
            <a href="/chatbot/qnaChatbot"
               class="bg-green-600 text-white px-6 py-2 rounded-full hover:bg-green-700 transition">시작하기</a>
        </div>

        <!-- AI 모의 협상 -->
        <div class="bg-white rounded-2xl p-8 shadow-md hover:shadow-lg transition flex flex-col justify-between h-full">
            <div>
                <h3 class="text-xl font-semibold text-gray-800 mb-2">AI 모의 협상</h3>
                <p class="text-gray-600 mb-6 text-sm leading-relaxed">
                    AI가 계약서를 바탕으로 모의 협상을 진행하고, 협상 포인트를 제공합니다.
                </p>
            </div>
            <a href="/chatbot/aiSimulationMain"
               class="bg-purple-600 text-white px-6 py-2 rounded-full hover:bg-purple-700 transition">시작하기</a>
        </div>
    </div>
</main>

<!-- 푸터 -->
<footer class="mt-20 text-sm text-gray-500 py-6 text-center border-t border-gray-200">
    © 2025 안심계약. All rights reserved.
</footer>


</body>
</html>
