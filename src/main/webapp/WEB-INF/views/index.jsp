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

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />

    <!-- 정적 리소스 -->
    <link rel="stylesheet" href="/css/table.css"/>
    <script src="/js/table.js"></script>
</head>

<body class="bg-slate-100 text-gray-800 font-sans">

<!-- 로그인 알림 -->
<div id="loginMessage"
     class="hidden fixed inset-0 flex items-center justify-center bg-black/30 z-50">
    <div class="bg-white text-green-600 font-bold text-2xl px-6 py-4 rounded-xl shadow-lg">
        로그인되었습니다.
    </div>
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

            <!-- 로그인 버튼 -->
            <a id="loginButton" href="/user/login"
               class="bg-blue-500 text-white px-6 py-3 rounded-full hover:bg-blue-600 transition text-lg font-bold">
                로그인
            </a>

            <!-- 로그인 후 드롭다운 -->
            <div id="profileDropdownWrapper" class="relative hidden">
                <button onclick="toggleDropdown()"
                        class="flex items-center space-x-2 text-xl font-bold text-gray-800 focus:outline-none">
                    <i class="fa-solid fa-user-circle text-2xl"></i>
                    <span id="headerNick">User</span>
                </button>
                <div id="profileDropdown"
                     class="absolute right-0 mt-2 w-40 bg-white border border-gray-300 rounded-md shadow-lg hidden z-50">
                    <a href="/user/mypage" class="block px-4 py-3 text-center text-gray-800 hover:bg-gray-100">내 정보</a>
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

    <!-- 카드들 -->
    <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-8 mt-14 w-full max-w-6xl px-6 items-stretch">
        <!-- 계약서 분석 -->
        <div class="bg-white rounded-2xl p-8 shadow-md hover:shadow-lg transition flex flex-col justify-between h-full">
            <div>
                <h3 class="text-xl font-semibold text-gray-800 mb-2">계약서 분석</h3>
                <p class="text-gray-600 mb-6 text-sm leading-relaxed">
                    업로드한 계약서에서 위험한 조항을 자동 탐지하고 분석 리포트를 제공합니다.
                </p>
            </div>
            <a href="/contract/upload" class="bg-blue-500 text-white px-6 py-2 rounded-full hover:bg-blue-600 transition">시작하기</a>
        </div>

        <!-- Q&A 챗봇 -->
        <div class="bg-white rounded-2xl p-8 shadow-md hover:shadow-lg transition flex flex-col justify-between h-full">
            <div>
                <h3 class="text-xl font-semibold text-gray-800 mb-2">Q&A 챗봇</h3>
                <p class="text-gray-600 mb-6 text-sm leading-relaxed">
                    노동법, 근로계약서 작성 등 궁금한 점을 AI 챗봇에게 질문하세요.
                </p>
            </div>
            <a href="/chatbot/qnaChatbot" class="bg-green-600 text-white px-6 py-2 rounded-full hover:bg-green-700 transition">시작하기</a>
        </div>

        <!-- AI 모의 협상 -->
        <div class="bg-white rounded-2xl p-8 shadow-md hover:shadow-lg transition flex flex-col justify-between h-full">
            <div>
                <h3 class="text-xl font-semibold text-gray-800 mb-2">AI 모의 협상</h3>
                <p class="text-gray-600 mb-6 text-sm leading-relaxed">
                    AI가 계약서를 바탕으로 모의 협상을 진행하고, 협상 포인트를 제공합니다.
                </p>
            </div>
            <a href="/chatbot/aiSimulationMain" class="bg-purple-600 text-white px-6 py-2 rounded-full hover:bg-purple-700 transition">시작하기</a>
        </div>
    </div>
</main>

<!-- 푸터 -->
<footer class="mt-20 text-sm text-gray-500 py-6 text-center border-t border-gray-200">
    © 2025 안심계약. All rights reserved.
</footer>

<!-- ✅ 로그인 상태 확인 스크립트 -->
<script>
    document.addEventListener("DOMContentLoaded", async () => {
        try {
            const res = await fetch("/user/mypage/profile", { credentials: "include" });
            if (!res.ok) return;

            const d = await res.json();
            if (!d) return;

            // 로그인 성공 → UI 업데이트
            document.getElementById("loginButton")?.classList.add("hidden");
            document.getElementById("profileDropdownWrapper")?.classList.remove("hidden");
            document.getElementById("headerNick").textContent =
                d.name || d.nickname || d.userId || "User";
        } catch (e) {
            console.error("세션 확인 실패", e);
        }
    });

    async function logout() {
        try {
            const res = await fetch("/user/logout");
            if (res.ok) location.href = "/";
        } catch (e) {
            alert("로그아웃 실패");
        }
    }
</script>

</body>
</html>
