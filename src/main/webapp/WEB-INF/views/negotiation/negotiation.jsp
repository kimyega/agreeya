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

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />


    <link rel="stylesheet" href="css/table.css"/>
    <script src="js/table.js"></script>

    <!-- jsPDF -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>

    <!-- html2canvas CDN 추가 -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/html2canvas/1.4.1/html2canvas.min.js"></script>

</head>

<body class="bg-slate-100 text-gray-800 font-sans">

<!-- 로그인 메시지 -->
<div id="loginMessage" class="hidden fixed inset-0 flex items-center justify-center bg-black/30 z-50">
    <div class="bg-white text-green-600 font-bold text-2xl px-6 py-4 rounded-xl shadow-lg">
        로그인되었습니다.
    </div>
</div>

<!-- 헤더 -->
<header class="bg-white/90 backdrop-blur-md shadow-sm sticky top-0 z-10">
    <div class="w-full flex items-center justify-between py-1 px-6">
        <div class="flex-shrink-0">
            <img src="images/logo.png" alt="Agreeya 로고" class="h-24" />
        </div>

        <nav class="flex items-center space-x-8 text-xl font-semibold text-gray-800 pr-4">
            <a href="#" class="hover:text-blue-600">홈</a>
            <a href="#" class="hover:text-blue-600">AI 모의 협상</a>
            <a href="#" class="hover:text-blue-600">계약서 분석</a>
            <a href="#" class="hover:text-blue-600">Q&A 챗봇</a>

            <a id="loginButton" href="#" onclick="simulateLogin()" class="bg-blue-500 text-white px-6 py-3 rounded-full hover:bg-blue-600 transition text-lg font-bold">
                로그인
            </a>

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

<main class="max-w-4xl mx-auto px-6 py-16">
    <h2 class="text-3xl font-bold mb-8 text-center text-gray-800">
        협상 연습을 시작해보세요
    </h2>

    <!-- 선택 탭 -->
    <div class="flex justify-center mb-8">
        <button id="ai-tab" class="px-6 py-2 font-semibold rounded-l-full bg-blue-600 text-white">AI 분석 기반</button>
        <button id="manual-tab" class="px-6 py-2 font-semibold rounded-r-full bg-gray-200 text-gray-700">직접 입력</button>
    </div>

    <!-- AI 분석 기반 영역 -->
    <div id="ai-section" class="bg-white p-6 rounded-xl shadow space-y-4 relative">
        <h3 class="text-xl font-bold text-blue-600">① 계약서 자동 분석 결과 기반 협상 시뮬레이션</h3>
        <p class="text-gray-600 text-sm">업로드한 계약서를 AI가 분석하여 주요 협상 항목을 제시합니다.</p>
        <button id="openModalBtn" class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
            AI가 추천한 협상 시작
        </button>
    </div>

    <!-- 사용자 직접 입력 영역 -->
    <div id="manual-section" class="bg-white p-6 rounded-xl shadow space-y-4 hidden">
        <h3 class="text-xl font-bold text-green-600">② 상황 직접 입력 후 협상 시뮬레이션</h3>
        <form id="negotiation-form" class="space-y-4">
            <textarea name="situation" rows="3" placeholder="상황 설명 (예: 퇴직금이 없는 조항이 있습니다)" class="w-full p-3 border rounded"></textarea>
            <input type="text" name="country" placeholder="국가/지역 (예: 사우디아라비아)" class="w-full p-3 border rounded">
            <input type="text" name="position" placeholder="직종 (예: 건설 현장 노동자)" class="w-full p-3 border rounded">
            <input type="text" name="goal" placeholder="협상 목표 (예: 퇴직금 조항 추가)" class="w-full p-3 border rounded">
            <button type="submit" class="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700">
                협상 시뮬레이션 시작
            </button>
        </form>
    </div>
</main>

<!-- 모달 배경 -->
<div id="modal" class="fixed inset-0 bg-black bg-opacity-40 hidden flex items-center justify-center z-50">
    <!-- 모달 박스 -->
    <div class="bg-white rounded-lg shadow-lg w-96 p-6 relative flex flex-col items-center">
        <h2 class="text-xl font-bold mb-4 text-center w-full">업로드한 계약서</h2>

        <!-- 분석일 제목 및 구분선 -->
        <div class="mb-4 w-full text-center">
            <p class="font-semibold text-gray-700 mb-2">분석일</p>
            <hr class="border-gray-300" />
        </div>

        <form class="w-full flex flex-col items-center">
            <div class="flex items-center space-x-2 mb-4 justify-center w-full">
                <input type="radio" id="date1" name="analysisDate" class="w-5 h-5" checked />
                <label for="date1" class="cursor-pointer text-center flex-1">
                    2025-06-25 14시 38분 22초
                </label>
            </div>

            <hr class="border-gray-300 mb-4 w-full" />

            <div class="flex items-center space-x-2 justify-center w-full">
                <input type="radio" id="date2" name="analysisDate" class="w-5 h-5" />
                <label for="date2" class="cursor-pointer text-center flex-1">
                    2025-07-25 18시 38분 22초
                </label>
            </div>

            <!-- 선택 버튼 -->
            <button type="button"
                    onclick="location.href='/negotiation/simulate'"
                    class="mt-6 bg-[#207CEB] text-white font-semibold rounded px-6 py-2 hover:bg-blue-700 transition w-full">
                선택
            </button>
        </form>

        <button id="closeModalBtn" class="absolute top-3 right-3 text-gray-500 hover:text-gray-700 text-2xl font-bold">&times;</button>
    </div>
</div>

<!-- 빈칸 알림용 모달 -->
<div id="emptyModal" class="fixed inset-0 bg-black bg-opacity-40 hidden flex items-center justify-center z-50">
    <div class="bg-white rounded-lg shadow-lg p-6 w-80 text-center relative">
        <p class="text-gray-800 font-semibold mb-6 text-center">
            협상을 시작하려면 </br>
            먼저 내용을 입력해주세요.
        </p>
        <button id="emptyModalClose" class="bg-[#207CEB] text-white rounded px-6 py-2 font-semibold hover:bg-blue-700 transition">
            확인
        </button>
    </div>
</div>

<footer class="mt-20 text-sm text-gray-500 py-6 text-center border-t border-gray-200">
    © 2025 안심계약. All rights reserved.
</footer>

<script>
    const aiTab = document.getElementById('ai-tab');
    const manualTab = document.getElementById('manual-tab');
    const aiSection = document.getElementById('ai-section');
    const manualSection = document.getElementById('manual-section');

    aiTab.addEventListener('click', () => {
        aiSection.classList.remove('hidden');
        manualSection.classList.add('hidden');
        aiTab.classList.add('bg-blue-600', 'text-white');
        aiTab.classList.remove('bg-gray-200', 'text-gray-700');
        manualTab.classList.add('bg-gray-200', 'text-gray-700');
        manualTab.classList.remove('bg-green-600', 'text-white');
    });

    manualTab.addEventListener('click', () => {
        aiSection.classList.add('hidden');
        manualSection.classList.remove('hidden');
        manualTab.classList.add('bg-green-600', 'text-white');
        manualTab.classList.remove('bg-gray-200', 'text-gray-700');
        aiTab.classList.add('bg-gray-200', 'text-gray-700');
        aiTab.classList.remove('bg-blue-600', 'text-white');
    });

    // 모달 관련
    const modal = document.getElementById('modal');
    const openModalBtn = document.getElementById('openModalBtn');
    const closeModalBtn = document.getElementById('closeModalBtn');

    openModalBtn.addEventListener('click', () => {
        modal.classList.remove('hidden');
        modal.classList.add('flex');
    });

    closeModalBtn.addEventListener('click', () => {
        modal.classList.add('hidden');
        modal.classList.remove('flex');
    });

    // 모달 바깥 클릭 시 닫기
    modal.addEventListener('click', (e) => {
        if(e.target === modal) {
            modal.classList.add('hidden');
            modal.classList.remove('flex');
        }
    });

    const form = document.getElementById('negotiation-form');
    const emptyModal = document.getElementById('emptyModal');
    const emptyModalClose = document.getElementById('emptyModalClose');

    form.addEventListener('submit', (e) => {
        e.preventDefault();

        // 입력값 체크
        const situation = form.elements['situation'].value.trim();
        const country = form.elements['country'].value.trim();
        const position = form.elements['position'].value.trim();
        const goal = form.elements['goal'].value.trim();

        if (!situation || !country || !position || !goal) {
            // 빈칸 있으면 모달 보여주기
            emptyModal.classList.remove('hidden');
            return;
        }

        // 모두 입력했을 경우 simulation.html로 이동
        window.location.href = '/negotiation/simulate';
    });


    // 모달 닫기 버튼 이벤트
    emptyModalClose.addEventListener('click', () => {
        emptyModal.classList.add('hidden');
    });

</script>
</body>
</html>
