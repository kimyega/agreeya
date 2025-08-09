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

<!-- 메인 -->
<main class="min-h-screen flex flex-col items-center justify-center px-6 py-16 text-center">
    <div class="max-w-3xl space-y-6">
        <h2 class="text-4xl font-extrabold text-gray-900 drop-shadow-sm">
            공정하고 안전한 <span class="text-blue-500">AI 근로계약</span>의 시작
        </h2>
        <p class="text-gray-700 text-lg">
            AI가 근로계약서를 분석해 위험 요소를 감지하고,<br />
            다양한 언어로 계약서 초안을 자동 생성해드립니다.
        </p>
    </div>

    <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-8 mt-14 w-full max-w-6xl px-6 items-stretch">
        <!-- 계약서 분석 카드 -->
        <div class="bg-white rounded-2xl p-8 shadow-md hover:shadow-lg transition flex flex-col justify-between h-full">
            <div>
                <div class="flex items-center gap-4 mb-4">
                    <div class="bg-blue-100 text-blue-600 p-3 rounded-xl">
                        <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M9 12l2 2 4-4M21 12v7a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-7" />
                        </svg>
                    </div>
                    <h3 class="text-xl font-semibold text-gray-800">계약서 분석</h3>
                </div>
                <p class="text-gray-600 mb-6 text-sm leading-relaxed">
                    업로드한 계약서에서 위험한 조항을 자동 탐지하고, 사용자에게 알림과 함께 분석 리포트를 제공합니다.
                </p>
            </div>
            <button class="bg-blue-500 text-white px-6 py-2 rounded-full hover:bg-blue-600 transition">
                시작하기
            </button>
        </div>

        <!-- Q&A 챗봇 카드 -->
        <div class="bg-white rounded-2xl p-8 shadow-md hover:shadow-lg transition flex flex-col justify-between h-full">
            <div>
                <div class="flex items-center gap-4 mb-4">
                    <div class="bg-green-100 text-green-600 p-3 rounded-xl">
                        <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M8 10h.01M12 10h.01M16 10h.01M9 16h6" />
                            <path stroke-linecap="round" stroke-linejoin="round" d="M21 12v7a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-7" />
                        </svg>
                    </div>
                    <h3 class="text-xl font-semibold text-gray-800">Q&A 챗봇</h3>
                </div>
                <p class="text-gray-600 mb-6 text-sm leading-relaxed">
                    노동법, 근로계약서 작성 등 궁금한 점을 AI 챗봇에게 질문하면 실시간으로 답변을 제공합니다.
                </p>
            </div>
            <a href="chatbot.html" class="bg-green-600 text-white px-6 py-2 rounded-full hover:bg-green-700 transition">
                시작하기
            </a>
        </div>

        <!-- AI 모의 협상 카드 -->
        <div class="bg-white rounded-2xl p-8 shadow-md hover:shadow-lg transition flex flex-col justify-between h-full">
            <div>
                <div class="flex items-center gap-4 mb-4">
                    <div class="bg-purple-100 text-purple-600 p-3 rounded-xl">
                        <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M9 12l2 2 4-4M21 12v7a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-7" />
                        </svg>
                    </div>
                    <h3 class="text-xl font-semibold text-gray-800">AI 모의 협상</h3>
                </div>
                <p class="text-gray-600 mb-6 text-sm leading-relaxed">
                    노동법, 근로계약서의 작성 방법 등 AI 챗봇에게 질문하면 실시간 답변을 드립니다.
                </p>
            </div>
            <button class="bg-purple-600 text-white px-6 py-2 rounded-full hover:bg-purple-700 transition">
                시작하기
            </button>
        </div>
    </div>
</main>

<!-- 푸터 -->
<footer class="mt-20 text-sm text-gray-500 py-6 text-center border-t border-gray-200">
    © 2025 안심계약. All rights reserved.
</footer>

</body>
</html>
