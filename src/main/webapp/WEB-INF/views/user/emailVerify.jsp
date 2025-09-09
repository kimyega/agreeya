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

    <!-- 정적 리소스: 절대경로 -->
    <link rel="stylesheet" href="/css/table.css"/>
    <script src="/js/table.js"></script>
</head>

<body class="min-h-screen flex flex-col bg-slate-100 text-gray-800 font-sans">
<!-- 로그인 알림 -->
<div id="loginMessage" class="hidden fixed inset-0 flex items-center justify-center bg-black/30 z-50">
    <div class="bg-white text-green-600 font-bold text-2xl px-6 py-4 rounded-xl shadow-lg">로그인되었습니다.</div>
</div>

<!-- 헤더 -->
<header class="bg-white/90 backdrop-blur-md shadow-sm sticky top-0 z-10">
    <div class="w-full flex items-center justify-between py-1 px-6">
        <div class="flex-shrink-0">
            <img src="/images/logo.png" alt="Agreeya 로고" class="h-24" />
        </div>

        <nav class="flex items-center space-x-8 text-xl font-semibold text-gray-800 pr-4">
            <a href="/" class="hover:text-blue-600">홈</a>
            <a href="/chatbot/aiSimulationMain" class="hover:text-blue-600">AI 모의 협상</a>
            <a href="/contract/upload" class="hover:text-blue-600">계약서 분석</a>
            <a href="/chatbot/qnaChatbot" class="hover:text-blue-600">Q&A 챗봇</a>

            <a id="loginButton" href="#" onclick="simulateLogin()"
               class="bg-blue-500 text-white px-6 py-3 rounded-full hover:bg-blue-600 transition text-lg font-bold">로그인</a>

            <div id="profileDropdownWrapper" class="relative hidden">
                <button onclick="toggleDropdown()" class="flex items-center space-x-2 text-xl font-bold text-gray-800 focus:outline-none">
                    <i class="fa-solid fa-user-circle text-2xl"></i>
                    <span>Hong</span>
                </button>
                <div id="profileDropdown" class="absolute right-0 mt-2 w-40 bg-white border border-gray-300 rounded-md shadow-lg hidden z-50">
                    <a href="/user/mypage" class="block px-4 py-3 text-center text-gray-800 hover:bg-gray-100">내 정보</a>
                    <a href="#" onclick="logout()" class="block px-4 py-3 text-center text-red-600 hover:bg-red-100 border-t border-gray-300">로그아웃</a>
                </div>
            </div>
        </nav>
    </div>
</header>

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
