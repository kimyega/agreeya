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
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />

    <!--j쿼리-->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <!-- Table CSS JS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/table.css"/>
    <script src="${pageContext.request.contextPath}/js/table.js"></script>
</head>

<body class="min-h-screen flex flex-col bg-slate-100 text-gray-800 font-sans">

<!-- 헤더 -->
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/include/header.jsp" />

<!-- 본문: 중앙정렬 -->
<main class="flex-1 flex items-center justify-center px-4">
    <div class="bg-white/95 p-10 rounded-xl shadow-md w-full max-w-md text-center">
        <h2 class="text-2xl font-bold mb-8">이메일로 온 인증번호를 입력해주세요</h2>

        <form id="code-form" class="space-y-6 text-left">
            <div class="relative h-12">
                <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <i class="fa-solid fa-key text-gray-400 text-[16px]"></i>
                </div>
                <input id="code" name="code" type="text" placeholder="인증번호를 입력해주세요"
                       class="w-full h-full pl-10 pr-4 border rounded-full focus:outline-none focus:ring-2 focus:ring-blue-300 text-base placeholder-gray-400 text-gray-800" />
                <p id="message" class="text-sm mt-1 ml-2 text-red-500 hidden"></p>
            </div>

            <button type="submit" class="w-full bg-blue-500 text-white py-3 rounded-full font-semibold hover:bg-blue-600">
                인증메일 확인
            </button>
        </form>
    </div>

    <!-- 오류 모달 -->
    <div id="errorModal" class="fixed inset-0 bg-black/40 flex items-center justify-center z-50 hidden">
        <div class="bg-white rounded-xl shadow-xl p-8 w-full max-w-sm text-center">
            <h3 class="text-xl font-semibold text-red-500 mb-4">인증번호가 틀립니다.</h3>
            <button onclick="closeModal()" class="bg-blue-500 hover:bg-blue-600 text-white font-semibold px-6 py-2 rounded-full">닫기</button>
        </div>
    </div>
</main>

<!-- 페이지 전용 JS -->
<script src="/js/emailVerify.js"></script>

</body>
</html>
