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

<main class="flex flex-col items-center justify-center min-h-screen bg-[#f9fafb]">
  <section class="w-[900px] p-10 bg-white rounded-2xl shadow-md mb-10">
    <h2 class="text-xl font-bold mb-6">내 정보</h2>
    <div class="flex justify-between items-center">
      <div class="space-y-2">
        <p><span class="font-semibold">이름:</span> 홍길동</p>
        <p><span class="font-semibold">이메일:</span> HongGildong@naver.com</p>
        <p><span class="font-semibold">가입일:</span> 2025-06-25</p>
      </div>
      <div class="flex space-x-4">
        <button class="px-4 py-2 text-sm text-gray-700 bg-gray-100 rounded-full">비밀번호 변경</button>
        <button onclick="showWithdrawModal()" class="px-4 py-2 text-sm text-red-600 border border-red-400 rounded-full">회원 탈퇴</button>
      </div>
    </div>
  </section>

  <section class="w-[900px] p-10 bg-white rounded-2xl shadow-md">
    <h2 class="text-xl font-bold mb-6">업로드한 계약서</h2>
    <table class="w-full text-center">
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
          <td><a href="#" class="text-blue-600">리포트</a></td>
        </tr>
        <tr>
          <td class="py-3">2025-07-25</td>
          <td class="text-green-500">0건</td>
          <td><a href="#" class="text-blue-600">리포트</a></td>
        </tr>
      </tbody>
    </table>
  </section>

  <div class="mt-10 mb-20">
    <button onclick="location.href='index'" class="px-6 py-3 bg-blue-600 text-white rounded-full">홈으로 돌아가기</button>
  </div>

  <!-- 회원탈퇴 모달 -->
  <div id="withdrawModal" class="fixed inset-0 bg-black bg-opacity-50 hidden flex items-center justify-center z-50">
    <div class="bg-white p-8 rounded-xl shadow-md w-80 text-center">
      <h2 class="text-lg font-bold mb-4">회원 탈퇴</h2>
      <p class="text-red-500 mb-6">정말로 하시겠습니까?</p>
      <div class="flex justify-center space-x-6">
        <button onclick="confirmWithdraw()" class="px-5 py-2 bg-blue-600 text-white rounded-full">예</button>
        <button onclick="closeWithdrawModal()" class="px-5 py-2 bg-gray-100 text-gray-700 rounded-full">아니요</button>
      </div>
    </div>
  </div>
</main>


  <!-- 회원 탈퇴 모달 -->
  <div id="withdrawModal" class="fixed inset-0 bg-black bg-opacity-40 hidden items-center justify-center z-50">
    <div class="bg-white p-8 rounded-lg shadow-lg w-[300px] text-center">
      <h2 class="text-lg font-semibold mb-2">회원 탈퇴</h2>
      <p class="text-red-500 font-semibold mb-6">정말로 하시겠습니까?</p>
      <div class="flex justify-center gap-4">
        <button onclick="confirmWithdraw()" class="bg-blue-600 text-white px-5 py-2 rounded-full text-sm">예</button>
        <button onclick="hideWithdrawModal()" class="bg-gray-200 px-5 py-2 rounded-full text-sm">아니요</button>
      </div>
    </div>
  </div>
</main>

<!-- ✅ JS -->
  <script src="../js/mypage.js"></script>
</body>
</html>