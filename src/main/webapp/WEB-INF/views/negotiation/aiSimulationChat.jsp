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

<!-- 메인 콘텐츠 -->
<main class="max-w-4xl mx-auto px-6 py-16">

    <div class="bg-white rounded-2xl p-8 shadow-xl space-y-6 relative">

        <!-- 홈 아이콘 버튼 (왼쪽 위) -->
        <a href="index3.1.html"
           class="absolute top-4 left-4 text-blue-600 hover:text-blue-800 text-4xl">
            <i class="fa-solid fa-house"></i>
        </a>

        <h2 class="text-3xl font-bold text-blue-600 mb-4 text-center">🗣️ AI 모의 협상</h2>
        <p class="text-gray-600 text-sm mb-6 text-center">
            아래 협상 시뮬레이션은 실제 근로계약서를 기반으로<br>
            근로자와 고용주 간의 협상 상황을 AI가 대화 형식으로 보여줍니다.
        </p>

        <div class="border rounded-xl p-4 bg-gray-50 space-y-4 h-[50vh] overflow-y-auto" id="chat-box">
            <!-- 대화 기록 예시 -->
            <div class="flex flex-col space-y-2">

                <div class="bg-green-100 text-green-800 w-fit px-4 py-2 rounded-xl self-start">
                    안녕하세요, 계약 조항에 대해 협상해볼까요?<br />
                    원하는 조건이나 우려 사항을 말씀해 주세요.
                </div>
            </div>
        </div>

        <!-- 입력창 폼으로 감싸기 -->
        <form id="chat-form" class="flex gap-4" onsubmit="handleSubmit(event)">
            <input type="text" id="user-input" placeholder="근로자 역할로 질문해보세요..." autocomplete="off"
                   class="flex-grow px-4 py-2 rounded-full border border-gray-300 shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-400" />
            <button type="submit"
                    class="bg-blue-600 text-white px-6 py-2 rounded-full hover:bg-blue-700 transition">
                보내기
            </button>
        </form>
    </div>
</main>

<!-- 푸터 -->
<footer class="mt-20 text-sm text-gray-500 py-6 text-center border-t border-gray-200">
    © 2025 안심계약. All rights reserved.
</footer>

<!-- JS 시뮬레이션 (모의 응답용) -->
<script type="javascript">
    const chatBox = document.getElementById('chat-box');

    function handleSubmit(event) {
        event.preventDefault(); // 폼 제출 기본 동작 막기
        sendMessage();
    }

    function sendMessage() {
        const input = document.getElementById('user-input');
        const message = input.value.trim();
        if (!message) return;

        // 사용자 메시지
        const userDiv = document.createElement('div');
        userDiv.className = 'bg-blue-100 text-blue-800 w-fit px-4 py-2 rounded-xl self-end';
        userDiv.innerText = `근로자: ${message}`;
        const userWrapper = document.createElement('div');
        userWrapper.className = 'flex flex-col space-y-2';
        userWrapper.appendChild(userDiv);
        chatBox.appendChild(userWrapper);

        // AI 고용주 응답 (모의)
        const response = mockReply(message);
        setTimeout(() => {
            const aiDiv = document.createElement('div');
            aiDiv.className = 'bg-green-100 text-green-800 w-fit px-4 py-2 rounded-xl self-start';
            aiDiv.innerText = `고용주: ${response}`;
            userWrapper.appendChild(aiDiv);
            chatBox.scrollTop = chatBox.scrollHeight;
        }, 700);

        input.value = '';
    }

    // 임시 모의 응답 함수
    function mockReply(msg) {
        if (msg.includes("임금")) return "현재 수준은 법정 최저 이상이며, 6개월 후 재협의 가능합니다.";
        if (msg.includes("휴식") || msg.includes("휴가")) return "연 15일 기본 유급휴가 제공되며, 1년 이상 근무 시 추가됩니다.";
        if (msg.includes("근무시간")) return "업무량에 따라 탄력적으로 조정 가능합니다. 평균 52시간 준수 예정입니다.";
        return "해당 부분은 검토 후 조정 가능하겠습니다.";
    }
</script>
</body>
</html>

