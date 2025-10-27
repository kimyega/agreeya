<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>안심계약 - AI 근로계약 분석</title>

    <!-- Tailwind CSS -->
    <script src="https://cdn.tailwindcss.com"></script>

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />

    <!--j쿼리-->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <!-- Table CSS JS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/table.css"/>
    <script src="${pageContext.request.contextPath}/js/table.js"></script>
</head>

<!-- ✅ 중앙정렬 기본틀 -->
<body class="min-h-screen flex flex-col bg-slate-100 text-gray-800 font-sans">

<!-- 헤더 -->
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/include/header.jsp" />

<!-- ✅ 본문: 정중앙 -->
<main class="flex-1 flex items-center justify-center px-4">
    <div class="bg-white/95 p-10 rounded-xl shadow-md w-full max-w-md text-center">
        <h2 class="text-2xl font-bold mb-6">비밀번호 찾기</h2>

        <form id="find-password-form" class="space-y-6 text-left">
            <div class="relative h-12">
                <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <i class="fa-solid fa-envelope text-gray-400 text-[16px]"></i>
                </div>
                <input id="email" name="email" type="email" placeholder="이메일을 입력하세요"
                       class="w-full h-full pl-10 pr-4 border rounded-full focus:outline-none focus:ring-2 focus:ring-blue-300 text-base placeholder-gray-400 text-gray-800" />
                <p id="email-msg" class="text-sm mt-1 ml-2 text-red-500 hidden"></p>
            </div>

            <br>
            <button type="submit"
                    class="w-full bg-blue-500 text-white py-3 rounded-full font-semibold hover:bg-blue-600 transition">
                인증메일 받기
            </button>
        </form>
    </div>
</main>

<!-- ✅ findPw.jsp 하단 모달 (글씨 강조 버전) -->
<div id="alertModal"
     class="fixed inset-0 flex items-center justify-center bg-black/40 backdrop-blur-sm hidden z-50">
    <div class="bg-white rounded-2xl shadow-xl p-8 w-full max-w-md text-center">

        <!-- ✨ 글씨 두껍게 강조 + 중앙 정렬 유지 -->
        <p id="alertModalMsg"
           class="text-gray-900 mb-8 text-lg font-semibold text-center leading-relaxed break-keep">
        </p>

        <button id="alertModalBtn"
                class="bg-blue-600 hover:bg-blue-700 text-white font-semibold py-3 px-6 rounded-lg transition-all duration-200">
            확인
        </button>
    </div>
</div>



<!-- 페이지 JS (있다면 사용) -->
<script src="/js/findPw.js"></script>

</body>
</html>
