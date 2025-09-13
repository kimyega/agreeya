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

<!-- ✅ 중앙정렬 기본틀 -->
<body class="min-h-screen flex flex-col bg-slate-100 text-gray-800 font-sans">

<!-- 헤더 -->
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/include/header.jsp" />

<!-- ✅ 본문: 정중앙 -->
<main class="flex-1 flex items-center justify-center px-4">
    <div class="bg-white/95 p-10 rounded-xl shadow-md w-full max-w-md text-center">
        <h2 class="text-2xl font-bold mb-6">본인 인증</h2>

        <form id="phone-verify-form" class="space-y-6 text-left">
            <!-- 이름 -->
            <div class="relative h-12">
                <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <i class="fa-solid fa-user text-gray-400 text-[16px]"></i>
                </div>
                <input id="name" name="name" type="text" placeholder="이름을 입력하세요"
                       class="w-full h-full pl-10 pr-4 border rounded-full focus:outline-none focus:ring-2 focus:ring-blue-300 text-base placeholder-gray-400 text-gray-800"/>
                <p id="name-msg" class="text-sm mt-1 ml-2 text-red-500 hidden"></p>
            </div>

            <!-- 전화번호 -->
            <div class="relative h-12">
                <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <i class="fa-solid fa-phone text-gray-400 text-[16px]"></i>
                </div>
                <input id="phone" name="tel" type="tel" placeholder="전화번호를 입력하세요"
                       class="w-full h-full pl-10 pr-4 border rounded-full focus:outline-none focus:ring-2 focus:ring-blue-300 text-base placeholder-gray-400 text-gray-800"/>
                <p id="phone-msg" class="text-sm mt-1 ml-2 text-red-500 hidden"></p>
            </div>

            <button type="submit"
                    class="w-full bg-blue-500 text-white py-3 rounded-full font-semibold hover:bg-blue-600 text-base">
                인증번호 받기
            </button>
        </form>
    </div>
</main>

<!-- 페이지 전용 JS -->
<script src="/js/findEmail.js"></script>


</body>
</html>
