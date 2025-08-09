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

  <link rel="stylesheet" href="country.css"/>
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

<main class="relative z-10 flex justify-center items-center min-h-screen px-4">
  <div class="bg-white rounded-2xl shadow-xl w-full max-w-6xl px-16 py-16">
    <!-- 진행 단계 표시 -->
    <div class="flex justify-between items-center mb-12 max-w-md mx-auto">
      <div class="flex flex-col items-center text-blue-600">
        <div class="w-3 h-3 rounded-full bg-blue-600 mb-1"></div>
        <span class="text-sm font-bold">이미지 업로드</span>
      </div>
      <div class="flex flex-col items-center text-blue-600">
        <div class="w-3 h-3 rounded-full bg-blue-600 mb-1"></div>
        <span class="text-sm font-bold">국가 선택</span>
      </div>
      <div class="flex flex-col items-center text-gray-400">
        <div class="w-3 h-3 rounded-full bg-gray-300 mb-1"></div>
        <span class="text-sm">AI 분석</span>
      </div>
      <div class="flex flex-col items-center text-gray-400">
        <div class="w-3 h-3 rounded-full bg-gray-300 mb-1"></div>
        <span class="text-sm">결과</span>
      </div>
    </div>

    <h2 class="text-center text-2xl font-bold mb-1">취업 국가를 선택해주세요</h2>
    <p class="text-center text-gray-600 text-base mb-10">근무 국가에 따라서 기준이 다르게 적용됩니다</p>

    <!-- 국가 카드 -->
    <div class="flex justify-center gap-20 mb-6">
      <div class="flex flex-col items-center cursor-pointer country-card" data-country="대한민국">
        <img src="kor.PNG" class="w-28 h-28 object-contain mb-2" />
        <span class="text-xl font-semibold">대한민국</span>
      </div>
      <div class="flex flex-col items-center cursor-pointer country-card" data-country="E U">
        <img src="eu.PNG" class="w-28 h-28 object-contain mb-2" />
        <span class="text-xl font-semibold">E U</span>
      </div>
      <div class="flex flex-col items-center cursor-pointer country-card" data-country="일본">
        <img src="jp.PNG" class="w-28 h-28 object-contain mb-2" />
        <span class="text-xl font-semibold">일본</span>
      </div>
    </div>

    <!-- 체크박스 -->
    <div class="text-center mb-3">
      <label class="inline-flex items-center text-lg">
        <input type="checkbox" class="w-5 h-5 mr-2" />
        유사사례로 저장하시겠습니까?
      </label>
    </div>

    <!-- 선택된 국가 -->
    <p class="text-center text-lg text-blue-700 font-semibold mb-6" id="selectedCountryText">선택한 국가: 없음</p>

    <!-- 버튼 -->
    <div class="flex justify-between mt-8">
      <button id="prevBtn" class="bg-blue-500 text-white px-6 py-3 rounded-full font-bold">이전 단계</button>
      <button id="nextBtn" class="bg-blue-400 text-white px-6 py-3 rounded-full font-bold opacity-50" disabled>다음 단계 →</button>
    </div>
  </div>

  <!-- EU 모달 -->
  <div id="euModal" class="fixed inset-0 bg-black/50 flex justify-center items-center z-50 hidden">
    <div class="bg-white w-full max-w-2xl rounded-lg p-8 overflow-y-auto max-h-[80vh]">
      <h3 class="text-2xl font-bold mb-6 text-center">EU 국가 선택</h3>
      <div class="grid grid-cols-2 gap-4 text-lg text-center">
        <button class="bg-gray-100 py-3 rounded">독일</button>
        <button class="bg-gray-100 py-3 rounded">프랑스</button>
        <button class="bg-gray-100 py-3 rounded">이탈리아</button>
        <button class="bg-gray-100 py-3 rounded">스페인</button>
        <button class="bg-gray-100 py-3 rounded">네덜란드</button>
        <button class="bg-gray-100 py-3 rounded">벨기에</button>
        <button class="bg-gray-100 py-3 rounded">오스트리아</button>
        <button class="bg-gray-100 py-3 rounded">핀란드</button>
        <button class="bg-gray-100 py-3 rounded">스웨덴</button>
        <button class="bg-gray-100 py-3 rounded">덴마크</button>
        <button class="bg-gray-100 py-3 rounded">포르투갈</button>
        <button class="bg-gray-100 py-3 rounded">그리스</button>
        <button class="bg-gray-100 py-3 rounded">폴란드</button>
        <button class="bg-gray-100 py-3 rounded">체코</button>
        <button class="bg-gray-100 py-3 rounded">헝가리</button>
        <button class="bg-gray-100 py-3 rounded">루마니아</button>
        <button class="bg-gray-100 py-3 rounded">불가리아</button>
        <button class="bg-gray-100 py-3 rounded">슬로바키아</button>
        <button class="bg-gray-100 py-3 rounded">슬로베니아</button>
        <button class="bg-gray-100 py-3 rounded">크로아티아</button>
        <button class="bg-gray-100 py-3 rounded">리투아니아</button>
        <button class="bg-gray-100 py-3 rounded">라트비아</button>
        <button class="bg-gray-100 py-3 rounded">에스토니아</button>
        <button class="bg-gray-100 py-3 rounded">아일랜드</button>
        <button class="bg-gray-100 py-3 rounded">룩셈부르크</button>
        <button class="bg-gray-100 py-3 rounded">키프로스</button>
        <button class="bg-gray-100 py-3 rounded">몰타</button>
      </div>
    </div>
  </div>
</main>



<!-- ✅ JS -->
  <script src="country.js"></script>
</body>
</html>
