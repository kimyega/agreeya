<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>안심계약 - 비밀번호 찾기</title>

    <!-- Tailwind -->
    <script src="https://cdn.tailwindcss.com"></script>

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />

    <!-- 페이지 CSS -->
    <link rel="stylesheet" href="/css/findPw.css" />
</head>

<body class="bg-cover bg-center bg-no-repeat min-h-screen flex flex-col">

<!-- ✅ 상단 헤더 -->
<header class="w-full bg-white/90 backdrop-blur-md shadow-sm sticky top-0 z-10">
    <div class="w-full flex items-center justify-between py-1 px-6">
        <div class="flex-shrink-0">
            <img src="/images/logo.png" alt="Agreeya 로고" class="h-24" />
        </div>
        <nav class="flex items-center space-x-8 text-xl font-semibold text-gray-800 pr-4">
            <a href="#" class="hover:text-blue-600">홈</a>
            <a href="#" class="hover:text-blue-600">AI 모의 협상</a>
            <a href="#" class="hover:text-blue-600">계약서 분석</a>
            <a href="#" class="hover:text-blue-600">Q&A 챗봇</a>
            <a id="loginButton" href="#" onclick="simulateLogin()" class="bg-blue-500 text-white px-6 py-3 rounded-full hover:bg-blue-600 transition text-lg font-bold">로그인</a>
            <div id="profileDropdownWrapper" class="relative hidden">
                <button onclick="toggleDropdown()" class="flex items-center space-x-2 text-xl font-bold text-gray-800 focus:outline-none">
                    <i class="fa-solid fa-user-circle text-2xl"></i>
                    <span>Hong</span>
                </button>
                <div id="profileDropdown" class="absolute right-0 mt-2 w-40 bg-white border border-gray-300 rounded-md shadow-lg hidden z-50">
                    <a href="/profile" class="block px-4 py-3 text-center text-gray-800 hover:bg-gray-100">내 정보</a>
                    <a href="#" onclick="logout()" class="block px-4 py-3 text-center text-red-600 hover:bg-red-100 border-t border-gray-300">로그아웃</a>
                </div>
            </div>
        </nav>
    </div>
</header>

<!-- ✅ 본문 -->
<main class="flex-grow flex items-center justify-center">
    <div class="bg-white/95 p-10 rounded-xl shadow-md w-full max-w-md text-center mt-10">
        <h2 class="text-2xl font-bold mb-6">비밀번호 찾기</h2>

        <form id="find-password-form" class="space-y-6 text-left">
            <div class="relative mb-4 h-12">
                <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <i class="fa-solid fa-envelope text-gray-400 text-[16px]"></i>
                </div>
                <input id="email" type="email" placeholder="이메일을 입력하세요"
                       class="w-full h-full pl-10 pr-4 border rounded-full focus:outline-none focus:ring-2 focus:ring-blue-300 text-base placeholder-gray-400 text-gray-800" />
                <p id="email-msg" class="text-sm mt-1 ml-2 text-red-500 hidden"></p>
            </div>

            <button type="submit"
                    class="w-full bg-blue-500 text-white py-3 rounded-full font-semibold hover:bg-blue-600 transition">
                인증메일 받기
            </button>
        </form>
    </div>
</main>

<!-- ✅ 성공 모달 (원하면 사용 가능, 기본은 즉시 이동) -->
<div id="successModal" class="fixed inset-0 flex items-center justify-center bg-black/40 backdrop-blur-sm z-50 hidden">
    <div class="bg-white rounded-2xl shadow-xl p-10 w-full max-w-md text-center border border-gray-300">
        <h2 class="text-xl font-semibold mb-6 text-green-600">전송되었습니다.</h2>
        <button onclick="closeModal()" class="bg-blue-500 hover:bg-blue-600 text-white font-semibold py-3 px-6 rounded-full">
            확인
        </button>
    </div>
</div>

<!-- 페이지 JS -->
<script src="/js/findPw.js"></script>
</body>
</html>
