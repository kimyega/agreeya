<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>안심계약 - AI 근로계약 분석</title>

    <!-- Tailwind -->
    <script src="https://cdn.tailwindcss.com"></script>

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>

    <!-- 정적 리소스: 절대경로로 고정 -->
    <link rel="stylesheet" href="/css/changePw.css"/>
    <!--j쿼리-->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <!-- sweetAlert2 -->
    <link rel="stylesheet" href="/sweetAlert/css/all.min.css"/>
    <script src="/sweetAlert/js/all.min.js"></script>

    <!-- Table CSS JS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/table.css"/>
    <script src="${pageContext.request.contextPath}/js/table.js"></script>
</head>

<!-- ✅ 바디를 flex-col + min-h-screen 로 변경 (중앙정렬 기본틀) -->
<body class="min-h-screen flex flex-col bg-slate-100 text-gray-800 font-sans">

<!-- 헤더 -->
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/include/header.jsp" />

<!-- ✅ 메인: 남는 공간을 차지 + 수평/수직 중앙정렬 -->
<main class="flex-1 flex items-center justify-center px-4">
    <!-- 카드 -->
    <div class="bg-white/95 p-10 rounded-xl shadow-md w-full max-w-md text-center">
        <h2 class="text-2xl font-semibold mb-6">비밀번호 변경</h2>

        <form id="reset-form" class="space-y-5 text-left">
            <!-- 새 비밀번호 -->
            <div>
                <label for="new-password" class="block mb-1 text-sm font-medium">새 비밀번호</label>
                <input type="password" id="new-password" placeholder="비밀번호를 입력하세요"
                       class="w-full px-4 py-2 rounded-full border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-300"/>
                <p id="new-password-msg" class="text-sm mt-1"></p>
            </div>

            <!-- 비밀번호 확인 -->
            <div>
                <label for="confirm-password" class="block mb-1 text-sm font-medium">비밀번호 확인</label>
                <input type="password" id="confirm-password" placeholder="비밀번호를 다시 입력하세요"
                       class="w-full px-4 py-2 rounded-full border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-300"/>
                <p id="confirm-password-msg" class="text-sm mt-1"></p>
            </div>

            <!-- 버튼 -->
            <button type="submit" class="w-full bg-blue-500 text-white py-3 rounded-full font-semibold hover:bg-blue-600">
                비밀번호 변경
            </button>
        </form>
    </div>
</main>

<!-- 페이지 JS -->
<script src="/js/changePw.js"></script>


</body>
</html>
