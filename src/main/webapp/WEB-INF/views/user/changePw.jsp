<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>AGREEYA | 비밀번호 변경</title>

    <!-- Tailwind -->
    <script src="https://cdn.tailwindcss.com"></script>

    <!-- 페이지/공통 CSS -->
    <link rel="stylesheet" href="/css/changePw.css" />
</head>

<body class="bg-cover bg-center bg-no-repeat min-h-screen flex flex-col">

<!-- 상단바 -->
<header class="w-full bg-white shadow-sm flex justify-between items-center px-10 py-4">
    <div class="flex items-center">
        <img src="/images/logo.png" alt="AGREEYA 로고" class="h-10 mr-2" />
        <span class="text-lg font-bold text-[#00BCD4] tracking-widest">AGREEYA</span>
    </div>
    <nav class="flex items-center space-x-10">
        <a href="#" class="text-lg text-black font-medium">홈</a>
        <a href="#" class="text-lg text-black font-medium">AI모의 협상</a>
        <a href="#" class="text-lg text-black font-medium">계약서 분석</a>
        <a href="#" class="text-lg font-bold text-black">Q&amp;A 챗봇</a>
        <button class="bg-blue-500 text-white px-6 py-2 rounded-full text-sm font-semibold hover:bg-blue-600">로그인</button>
    </nav>
</header>

<!-- 비밀번호 변경 박스 -->
<main class="flex-grow flex items-center justify-center">
    <div class="bg-white bg-opacity-95 p-10 rounded-xl shadow-md w-[500px] text-center mt-10">
        <h2 class="text-xl font-semibold mb-6">비밀번호 변경</h2>

        <form id="reset-form" class="space-y-5">
            <!-- 새 비밀번호 -->
            <div class="text-left">
                <label for="new-password" class="block mb-1 text-sm font-medium">새 비밀번호</label>
                <input type="password" id="new-password" placeholder="비밀번호를 입력하세요"
                       class="w-full px-4 py-2 rounded-full border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-300" />
                <p id="new-password-msg" class="text-sm mt-1"></p>
            </div>

            <!-- 비밀번호 확인 -->
            <div class="text-left">
                <label for="confirm-password" class="block mb-1 text-sm font-medium">비밀번호 확인</label>
                <input type="password" id="confirm-password" placeholder="비밀번호를 다시 입력하세요"
                       class="w-full px-4 py-2 rounded-full border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-300" />
                <p id="confirm-password-msg" class="text-sm mt-1"></p>
            </div>

            <!-- 버튼 -->
            <button type="submit"
                    class="w-full bg-blue-500 text-white py-3 rounded-full font-semibold hover:bg-blue-600">
                비밀번호 변경
            </button>
        </form>
    </div>
</main>

<!-- ✅ 모달 -->
<div id="modal" class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center hidden z-50">
    <div class="bg-white p-6 rounded-lg shadow-xl text-center w-full max-w-sm">
        <p id="modal-message" class="text-green-600 font-semibold text-base mb-4">비밀번호가 성공적으로 변경되었습니다.</p>
        <button id="modal-confirm" class="bg-blue-500 text-white px-6 py-2 rounded-full font-medium hover:bg-blue-600">
            로그인 화면으로 돌아가기
        </button>
    </div>
</div>

<!-- 페이지 JS -->
<script src="/js/changePw.js"></script>
</body>
</html>
