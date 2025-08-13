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

    <!-- 정적 리소스: 절대경로 -->
    <link rel="stylesheet" href="/css/table.css"/>
    <script src="/js/table.js"></script>
</head>

<!-- ✅ 중앙정렬 기본틀 -->
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

<!-- ✅ 본문: 정중앙 -->
<main class="flex-1 flex items-center justify-center px-4">
    <div class="bg-white/95 p-10 rounded-xl shadow-md w-full max-w-md text-center">
        <h2 class="text-2xl font-bold mb-6">본인 인증</h2>

        <form id="phone-verify-form" class="space-y-6 text-left">
            <!-- 이름 -->
            <div class="relative h-12">
                <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <i class="fa-solid fa-user text-gray-400 text-[16px]"></i>
                </div>
                <input id="name" type="text" placeholder="이름을 입력하세요"
                       class="w-full h-full pl-10 pr-4 border rounded-full focus:outline-none focus:ring-2 focus:ring-blue-300 text-base placeholder-gray-400 text-gray-800"/>
                <p id="name-msg" class="text-sm mt-1 ml-2 text-red-500 hidden"></p>
            </div>

            <!-- 전화번호 -->
            <div class="relative h-12">
                <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <i class="fa-solid fa-phone text-gray-400 text-[16px]"></i>
                </div>
                <input id="phone" type="tel" placeholder="전화번호를 입력하세요"
                       class="w-full h-full pl-10 pr-4 border rounded-full focus:outline-none focus:ring-2 focus:ring-blue-300 text-base placeholder-gray-400 text-gray-800"/>
                <p id="phone-msg" class="text-sm mt-1 ml-2 text-red-500 hidden"></p>
            </div>

            <button type="submit"
                    class="w-full bg-blue-500 text-white py-3 rounded-full font-semibold hover:bg-blue-600 text-base">
                인증번호 받기
            </button>
        </form>
    </div>
</main>

<!-- 페이지 전용 JS -->
<script src="/js/findEmail.js"></script>

<!-- 🔧 최소 동작 스크립트(외부 JS 없어도 동작) -->
<script>
    document.addEventListener('DOMContentLoaded', () => {
        const form  = document.getElementById('phone-verify-form');
        const name  = document.getElementById('name');
        const phone = document.getElementById('phone');
        const nMsg  = document.getElementById('name-msg');
        const pMsg  = document.getElementById('phone-msg');

        const setErr = (el, msgEl, msg) => { msgEl.textContent = msg; msgEl.classList.remove('hidden'); el.classList.add('border-red-400'); };
        const clrErr = (el, msgEl) => { msgEl.textContent = ''; msgEl.classList.add('hidden'); el.classList.remove('border-red-400'); };

        // 간단 포맷터: 숫자만 입력 + 010-1234-5678 형태로 자동 하이픈(선택)
        phone.addEventListener('input', () => {
            const digits = phone.value.replace(/\D/g, '');
            let v = digits;
            if (v.length > 3 && v.length <= 7) v = v.replace(/(\d{3})(\d+)/, '$1-$2');
            else if (v.length > 7) v = v.replace(/(\d{3})(\d{4})(\d+)/, '$1-$2-$3');
            phone.value = v;
        });

        form?.addEventListener('submit', (e) => {
            e.preventDefault();
            let ok = true;

            if (!name.value.trim()) { setErr(name, nMsg, '이름을 입력하세요.'); ok = false; } else { clrErr(name, nMsg); }
            if (!/^01[0-9]-?\d{3,4}-?\d{4}$/.test(phone.value.trim())) {
                setErr(phone, pMsg, '휴대폰 번호 형식이 올바르지 않습니다.'); ok = false;
            } else { clrErr(phone, pMsg); }

            if (!ok) return;

            // TODO: 실제로 인증번호 발송 API 호출 (fetch/axios)
            // 성공 시 다음 단계(휴대폰 인증번호 입력 화면)로 이동
            window.location.href = '/phoneVerify';
        });
    });
</script>
</body>
</html>
