<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html >
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>안심계약 - AI 근로계약 분석</title>

    <!-- Tailwind CSS -->
    <script src="https://cdn.tailwindcss.com"></script>

    <!-- Font Awesome (아이콘용) -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />

    <link rel="stylesheet" href="css/table.css"/>
    <script src="js/table.js"></script>
</head>

<body class="bg-slate-100 text-gray-800 font-sans">

<!-- ✅ 로그인 알림 메시지 -->
<div id="loginMessage"
     class="hidden fixed inset-0 flex items-center justify-center bg-black/30 z-50">
    <div class="bg-white text-green-600 font-bold text-2xl px-6 py-4 rounded-xl shadow-lg">
        로그인되었습니다.
    </div>
</div>

<!-- ✅ 헤더 -->
<header class="bg-white/90 backdrop-blur-md shadow-sm sticky top-0 z-10">
    <div class="w-full flex items-center justify-between py-1 px-6">

        <!-- 로고 -->
        <div class="flex-shrink-0">
            <img src="images/logo.png" alt="Agreeya 로고" class="h-24" />
        </div>

        <!-- 메뉴 -->
        <nav class="flex items-center space-x-8 text-xl font-semibold text-gray-800 pr-4">
            <a href="/" class="hover:text-blue-600">홈</a>
            <a href="/chatbot/aiSimulationMain" class="hover:text-blue-600">AI 모의 협상</a>
            <a href="/contract/upload" class="hover:text-blue-600">계약서 분석</a>
            <a href="/chatbot/qnaChatbot" class="hover:text-blue-600">Q&A 챗봇</a>

            <!-- ✅ 로그인 버튼 -->
            <a id="loginButton" href="#" onclick="simulateLogin()"
               class="bg-blue-500 text-white px-6 py-3 rounded-full hover:bg-blue-600 transition text-lg font-bold">
                로그인
            </a>

            <!-- ✅ 로그인 후 드롭다운 메뉴 -->
            <div id="profileDropdownWrapper" class="relative hidden">
                <button onclick="toggleDropdown()"
                        class="flex items-center space-x-2 text-xl font-bold text-gray-800 focus:outline-none">
                    <i class="fa-solid fa-user-circle text-2xl"></i>
                    <span>Hong</span>
                </button>
                <div id="profileDropdown"
                     class="absolute right-0 mt-2 w-40 bg-white border border-gray-300 rounded-md shadow-lg hidden z-50">
                    <a href="/profile" class="block px-4 py-3 text-center text-gray-800 hover:bg-gray-100">내 정보</a>
                    <a href="#" onclick="logout()" class="block px-4 py-3 text-center text-red-600 hover:bg-red-100 border-t border-gray-300">로그아웃</a>
                </div>
            </div>
        </nav>
    </div>
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
