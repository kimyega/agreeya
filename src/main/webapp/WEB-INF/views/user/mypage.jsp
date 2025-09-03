<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1"/>
  <title>안심계약 - 마이페이지</title>

  <script src="https://cdn.tailwindcss.com"></script>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
  <!-- 정적 리소스 -->
  <link rel="stylesheet" href="/css/table.css"/>
  <script src="/js/table.js"></script>
</head>

<body class="min-h-screen flex flex-col bg-slate-100 text-gray-800 font-sans">

<!-- ✅ 로그인 알림 -->
<div id="loginMessage" class="hidden fixed inset-0 flex items-center justify-center bg-black/30 z-50">
  <div class="bg-white text-green-600 font-bold text-2xl px-6 py-4 rounded-xl shadow-lg">
    로그인되었습니다.
  </div>
</div>

<!-- ✅ 헤더 -->
<header class="bg-white/90 backdrop-blur-md shadow-sm sticky top-0 z-10">
  <div class="w-full flex items-center justify-between py-1 px-6">
    <div class="flex-shrink-0">
      <img src="/images/logo.png" alt="Agreeya 로고" class="h-24" />
    </div>

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
        <button onclick="toggleDropdown()" class="flex items-center space-x-2 text-xl font-bold text-gray-800 focus:outline-none">
          <i class="fa-solid fa-user-circle text-2xl"></i>
          <span id="headerNick">User</span>
        </button>
        <div id="profileDropdown" class="absolute right-0 mt-2 w-40 bg-white border border-gray-300 rounded-md shadow-lg hidden z-50">
          <a href="/user/mypage" class="block px-4 py-3 text-center text-gray-800 hover:bg-gray-100">내 정보</a>
          <a href="/user/logout"
             class="block px-4 py-3 text-center text-red-600 hover:bg-red-100 border-t border-gray-300">
            로그아웃
          </a>
        </div>
      </div>
    </nav>
  </div>
</header>

<main class="flex flex-col items-center justify-center flex-1 bg-[#f9fafb]">

  <!-- ✅ 내 정보 -->
  <section class="w-[900px] p-10 bg-white rounded-2xl shadow-md mb-10">
    <h2 class="text-xl font-bold mb-6">내 정보</h2>
    <div class="flex justify-between items-center">
      <div class="space-y-2">
        <p><span class="font-semibold">이름:</span> <span id="pfName">-</span></p>
        <p><span class="font-semibold">이메일:</span> <span id="pfEmail">-</span></p>
        <p><span class="font-semibold">가입일:</span> <span id="pfRegDt">-</span></p>
      </div>
      <div class="flex space-x-4">
        <a href="/user/changePw"
           class="px-4 py-2 text-sm text-gray-700 bg-gray-100 rounded-full hover:bg-gray-200">
          비밀번호 변경
        </a>
        <button id="btnWithdraw"
                class="px-4 py-2 text-sm text-red-600 border border-red-400 rounded-full hover:bg-red-50">
          회원 탈퇴
        </button>
      </div>
    </div>
  </section>

  <!-- ✅ 업로드한 계약서 (예시) -->
  <section class="w-[900px] p-10 bg-white rounded-2xl shadow-md">
    <h2 class="text-xl font-bold mb-6">업로드한 계약서</h2>
    <table class="w-full text-center" id="analysisTable">
      <thead>
      <tr class="border-b">
        <th class="py-2">분석일</th>
        <th class="py-2">위험요소</th>
        <th class="py-2">보기</th>
      </tr>
      </thead>
      <tbody>
      <tr class="border-b">
        <td class="py-3">2025-06-25</td>
        <td class="text-red-500">2건</td>
        <td><a href="/contract/result" class="text-blue-600 hover:underline">리포트</a></td>
      </tr>
      <tr>
        <td class="py-3">2025-07-25</td>
        <td class="text-green-500">0건</td>
        <td><a href="/contract/result" class="text-blue-600 hover:underline">리포트</a></td>
      </tr>
      </tbody>
    </table>
  </section>

  <div class="mt-10 mb-20">
    <button onclick="location.href='/'" class="px-6 py-3 bg-blue-600 text-white rounded-full">홈으로 돌아가기</button>
  </div>
</main>

<!-- ✅ 회원탈퇴 모달 -->
<div id="withdrawModal" class="fixed inset-0 bg-black/40 hidden items-center justify-center z-50">
  <div class="bg-white p-8 rounded-lg shadow-lg w-[300px] text-center">
    <h2 class="text-lg font-semibold mb-2">회원 탈퇴</h2>
    <p class="text-red-500 font-semibold mb-6">정말로 하시겠습니까?</p>
    <div class="flex justify-center gap-4">
      <button id="confirmWithdraw" class="bg-blue-600 text-white px-5 py-2 rounded-full text-sm">예</button>
      <button id="cancelWithdraw" class="bg-gray-200 px-5 py-2 rounded-full text-sm">아니요</button>
    </div>
  </div>
</div>

<!-- ✅ 탈퇴 완료 모달 -->
<div id="withdrawDoneModal" class="fixed inset-0 bg-black/50 hidden items-center justify-center z-50">
  <div class="bg-white p-8 rounded-xl shadow-md w-80 text-center">
    <h2 class="text-lg font-bold mb-2">탈퇴 완료</h2>
    <p class="text-gray-600 mb-6">메인으로 이동합니다...</p>
    <button onclick="location.href='/'" class="px-5 py-2 bg-blue-600 text-white rounded-full">확인</button>
  </div>
</div>

<!-- ✅ 우하단 홈(FAB) 버튼 -->
<a href="/"
   aria-label="홈으로 이동"
   class="fixed bottom-6 right-6 md:bottom-8 md:right-8 z-40
          w-14 h-14 rounded-full bg-blue-600 text-white
          shadow-lg hover:bg-blue-700 active:scale-95
          focus:outline-none focus:ring-4 focus:ring-blue-300
          flex items-center justify-center">
  <i class="fa-solid fa-house text-2xl"></i>
  <span class="sr-only">홈으로 이동</span>
</a>

<!-- ✅ 페이지 전용 JS -->
<script src="/js/mypage.js"></script>
</body>
</html>
