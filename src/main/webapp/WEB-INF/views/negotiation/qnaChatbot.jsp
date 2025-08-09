<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
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
            <a href="#" class="hover:text-blue-600">홈</a>
            <a href="#" class="hover:text-blue-600">AI 모의 협상</a>
            <a href="#" class="hover:text-blue-600">계약서 분석</a>
            <a href="#" class="hover:text-blue-600">Q&A 챗봇</a>

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

<!-- 메인 챗봇 영역 -->
<main class="min-h-screen px-4 py-12 flex flex-col items-center">
    <div class="max-w-4xl w-full bg-white rounded-2xl shadow-lg p-8 relative">

        <!-- 홈 아이콘 버튼 (왼쪽 위) -->
        <a href="index3.1.html"
           class="absolute top-4 left-4 text-blue-600 hover:text-blue-800 text-4xl">
            <i class="fa-solid fa-house"></i>
        </a>


        <h2 class="text-3xl font-bold text-blue-600 mb-4 text-center">AI Q&A 챗봇</h2>
        <p class="text-gray-600 text-sm mb-6 text-center">
            노동법, 근로계약서 관련 질문을 입력해 주세요. AI가 실시간으로 답변해드립니다.
        </p>

        <!-- 채팅 영역 -->
        <div id="chat-box" class="bg-gray-50 border rounded-xl p-4 h-[32rem] overflow-y-auto space-y-4 mb-6">
            <!-- 예시 메시지 -->
            <div class="flex justify-start">
                <div class="bg-gray-200 text-gray-800 px-4 py-2 rounded-lg max-w-xs">
                    안녕하세요! 무엇을 도와드릴까요?
                </div>
            </div>
        </div>

        <!-- 입력창 -->
        <form id="chat-form" class="flex gap-2" onsubmit="handleSend(event)">
            <input
                    type="text"
                    id="user-input"
                    placeholder="질문을 입력하세요..."
                    class="flex-1 border rounded-full px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-300"
                    required
            />
            <button
                    type="submit"
                    class="bg-blue-600 text-white px-6 py-2 rounded-full hover:bg-blue-700 transition"
            >
                전송
            </button>
        </form>
    </div>
</main>

<!-- 푸터 -->
<footer class="mt-4 text-sm text-gray-500 py-6 text-center border-t border-gray-200">
    © 2025 안심계약. All rights reserved.
</footer>

<!-- 간단한 자바스크립트 -->
<script type="javascript">
    function handleSend(event) {
        event.preventDefault();
        const input = document.getElementById('user-input');
        const message = input.value.trim();
        if (!message) return;

        const chatBox = document.getElementById('chat-box');

        // 사용자 메시지 추가
        const userMsg = document.createElement('div');
        userMsg.className = 'flex justify-end';
        userMsg.innerHTML = `<div class="bg-blue-600 text-white px-4 py-2 rounded-lg max-w-xs">${message}</div>`;
        chatBox.appendChild(userMsg);

        // 자동 스크롤
        chatBox.scrollTop = chatBox.scrollHeight;

        // 입력 초기화
        input.value = '';

        // AI 응답 (예시, 추후 API 연동 가능)
        setTimeout(() => {
            const botMsg = document.createElement('div');
            botMsg.className = 'flex justify-start';
            botMsg.innerHTML = `<div class="bg-gray-200 text-gray-800 px-4 py-2 rounded-lg max-w-xs">
          좋은 질문이에요. 일반적으로 수습기간은 필수는 아니지만 고용주와 근로자가 협의하여 명시할 수 있어요.
        </div>`;
            chatBox.appendChild(botMsg);
            chatBox.scrollTop = chatBox.scrollHeight;
        }, 800);
    }
</script>
</body>
</html>
