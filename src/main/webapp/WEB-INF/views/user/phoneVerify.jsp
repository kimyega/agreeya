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

<body class="min-h-screen flex flex-col bg-slate-100 text-gray-800 font-sans">

<!-- 헤더 -->
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/include/header.jsp" />

<!-- 본문: 중앙정렬 -->
<main class="flex-1 flex items-center justify-center px-4">
    <div class="bg-white/95 p-10 rounded-xl shadow-md w-full max-w-md text-center">
        <h2 class="text-2xl font-bold mb-6">본인 인증</h2>

        <form id="verify-form" class="space-y-6 text-left">
            <div class="relative h-12">
                <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <i class="fa-solid fa-key text-gray-400 text-[16px]"></i>
                </div>
                <input id="code" name="code" type="text" placeholder="인증번호 (6자리 숫자)"
                       class="w-full h-full pl-10 pr-4 border rounded-full focus:outline-none focus:ring-2 focus:ring-blue-300 text-base placeholder-gray-400 text-gray-800" />
                <p id="message" class="text-sm mt-1 ml-2 text-red-500 hidden"></p>
            </div>

            <button type="submit"
                    class="w-full bg-blue-500 text-white py-3 rounded-full font-semibold hover:bg-blue-600">
                본인 인증 확인
            </button>
        </form>
    </div>

    <!-- 이메일 찾기 결과 모달 -->
    <div id="resultModal" class="hidden fixed inset-0 flex items-center justify-center bg-black/40 backdrop-blur-sm z-50">
        <div class="bg-white rounded-2xl shadow-xl p-10 w-full max-w-md text-center border border-gray-300">
            <h2 class="text-xl font-semibold mb-4">
                <span id="resultName"></span>님의 이메일을 찾았습니다!
            </h2>
            <p class="mb-6 text-lg">
                이메일: <span id="resultEmail" class="font-bold"></span>
            </p>

            <a href="/user/login"
               class="inline-block bg-blue-500 hover:bg-blue-600 text-white font-semibold py-3 px-6 rounded-full transition">
                로그인 화면으로 돌아가기
            </a>
        </div>
    </div>
</main>

<!-- 페이지 전용 JS (있으면 사용) -->
<script src="/js/phoneVerify.js"></script>

<!-- 🔧 최소 동작 스크립트(외부 JS 없어도 작동) -->

</body>
</html>
