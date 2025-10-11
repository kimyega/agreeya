<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>안심계약 - 유사 계약서 추천</title>

    <!-- Tailwind CSS -->
    <script src="https://cdn.tailwindcss.com"></script>

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />

    <!-- 공통 CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/table.css"/>

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
        <h2 class="text-4xl font-extrabold text-gray-900 mb-4">📚 유사 계약서 추천</h2>
        <p class="text-lg text-gray-600">AI가 분석한 계약서 사례를 확인하고, 상세 분석 결과 페이지로 이동할 수 있습니다.</p>
    </section>

    <!-- 유사사례 영역 -->
    <section id="similar-cases" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <p class="text-gray-500 text-center">🔄 유사 계약서 불러오는 중...</p>
    </section>

    <!-- ✅ hidden input -->
    <input type="hidden" id="contractId" value="${param.contractId}"/>

    <!-- ✅ 버튼 영역 -->
    <section class="flex flex-col sm:flex-row gap-4 justify-center mt-12">
        <button id="backBtn"
                class="bg-blue-600 text-white px-6 py-3 rounded-full hover:bg-blue-700 transition w-full sm:w-auto">
            분석 결과로 돌아가기
        </button>

        <button id="draftBtn"
                class="bg-blue-600 text-white px-6 py-3 rounded-full hover:bg-blue-700 transition w-full sm:w-auto">
            계약서 초안 생성
        </button>

        <button id="homeBtn"
                class="bg-gray-200 text-gray-800 px-6 py-3 rounded-full hover:bg-gray-300 transition w-full sm:w-auto">
            홈으로 돌아가기
        </button>
    </section>
</main>

<!-- 푸터 -->
<footer class="mt-20 text-sm text-gray-500 py-6 text-center border-t border-gray-200">
    © 2025 안심계약. All rights reserved.
</footer>

<!-- JS -->
<script src="${pageContext.request.contextPath}/js/similarCase.js"></script>
</body>
</html>
