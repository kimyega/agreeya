<%@ page contentType="text/html; charset=UTF-8" %>
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

  <link rel="stylesheet" href="loding.css"/>
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
      <img src="logo.png" alt="Agreeya 로고" class="h-24" />
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
<main class="relative z-10 flex justify-center items-start pt-24 pb-20 px-4">
  <div class="bg-white rounded-2xl shadow-xl w-full max-w-3xl px-10 py-12 text-center">

    <!-- 단계 표시 -->
    <div class="flex justify-between items-center mb-10 max-w-md mx-auto">
      <div class="flex flex-col items-center text-blue-600">
        <div class="w-3 h-3 rounded-full bg-blue-600 mb-1"></div>
        <span class="text-sm font-bold">이미지 업로드</span>
      </div>
      <div class="flex flex-col items-center text-blue-600">
        <div class="w-3 h-3 rounded-full bg-blue-600 mb-1"></div>
        <span class="text-sm font-bold">국가 선택</span>
      </div>
      <div class="flex flex-col items-center text-blue-600">
        <div class="w-3 h-3 rounded-full animate-pulse bg-blue-600 mb-1"></div>
        <span class="text-sm font-bold">AI 분석</span>
      </div>
      <div class="flex flex-col items-center text-gray-400">
        <div class="w-3 h-3 rounded-full bg-gray-300 mb-1"></div>
        <span class="text-sm">결과</span>
      </div>
    </div>

    <!-- 로딩 아이콘 -->
    <div class="flex justify-center mb-8">
      <div class="w-10 h-10 border-4 border-blue-500 border-t-transparent rounded-full animate-spin"></div>
    </div>

    <!-- 텍스트 -->
    <h2 class="text-xl font-bold text-blue-700 mb-2">AI가 계약서를 분석 중입니다...</h2>
    <p class="text-gray-600 text-sm mb-6">법률 DB 연동 및 GPT 분석이 진행 중입니다.<br>일반적으로 <strong>5~10초</strong> 정도 소요됩니다.</p>

    <!-- 분석 단계 리스트 -->
    <ul class="space-y-3 text-left text-gray-700 text-base mx-auto w-fit" id="analysisSteps">
      <li class="flex items-center gap-3">
        <div class="circle w-3 h-3 rounded-full bg-gray-300"></div>
        계약서 업로드 확인
      </li>
      <li class="flex items-center gap-3">
        <div class="circle w-3 h-3 rounded-full bg-gray-300"></div>
        법률 DB와 조항 비교
      </li>
      <li class="flex items-center gap-3">
        <div class="circle w-3 h-3 rounded-full bg-gray-300"></div>
        유사 사례 분석 및 위험도 평가
      </li>
    </ul>

    <!-- 완료 문구 -->
    <p id="completeMessage" class="mt-6 text-green-600 font-semibold hidden">
      <i class="fa-solid fa-check mr-2"></i> 분석이 완료되었습니다!
    </p>
  </div>
</main>

<!-- ✅ JS -->
  <script src="loding.js"></script>
</body>
</html>