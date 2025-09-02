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
        <!-- 로고 -->
        <div class="flex-shrink-0">
            <img src="/images/logo.png" alt="Agreeya 로고" class="h-24" />
        </div>

        <!-- 메뉴 -->
        <nav class="flex items-center space-x-8 text-xl font-semibold text-gray-800 pr-4">
            <a href="/" class="hover:text-blue-600">홈</a>
            <a href="/chatbot/aiSimulationMain" class="hover:text-blue-600">AI 모의 협상</a>
            <a href="/contract/upload" class="hover:text-blue-600">계약서 분석</a>
            <a href="/chatbot/qnaChatbot" class="hover:text-blue-600">Q&A 챗봇</a>

            <!-- 로그인 버튼(데모) -->
            <a id="loginButton" href="#" onclick="simulateLogin()"
               class="bg-blue-500 text-white px-6 py-3 rounded-full hover:bg-blue-600 transition text-lg font-bold">로그인</a>

            <!-- 로그인 후 드롭다운 -->
            <div id="profileDropdownWrapper" class="relative hidden">
                <button onclick="toggleDropdown()" class="flex items-center space-x-2 text-xl font-bold text-gray-800 focus:outline-none">
                    <i class="fa-solid fa-user-circle text-2xl"></i>
                    <span>Hong</span>
                </button>
                <div id="profileDropdown" class="absolute right-0 mt-2 w-40 bg-white border border-gray-300 rounded-md shadow-lg hidden z-50">
                    <a href="/mypage" class="block px-4 py-3 text-center text-gray-800 hover:bg-gray-100">내 정보</a>
                    <a href="#" onclick="logout()" class="block px-4 py-3 text-center text-red-600 hover:bg-red-100 border-t border-gray-300">로그아웃</a>
                </div>
            </div>
        </nav>
    </div>
</header>

<!-- 본문: 중앙정렬 -->
<main class="flex-1 flex items-center justify-center px-4">
    <div class="bg-white/95 p-10 rounded-xl shadow-md w-full max-w-md text-center">
        <h2 class="text-2xl font-bold mb-6">본인 인증</h2>

        <form id="verify-form" class="space-y-6 text-left">
            <div class="relative h-12">
                <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <i class="fa-solid fa-key text-gray-400 text-[16px]"></i>
                </div>
                <input id="code" type="text" placeholder="인증번호 (6자리 숫자)"
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

            <a href="/login"
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
