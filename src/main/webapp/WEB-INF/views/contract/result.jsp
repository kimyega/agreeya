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
      <img src="/images/logo.png" alt="Agreeya 로고" class="h-24" />
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
          <a href="/user/mypage" class="block px-4 py-3 text-center text-gray-800 hover:bg-gray-100">내 정보</a>
          <a href="#" onclick="logout()" class="block px-4 py-3 text-center text-red-600 hover:bg-red-100 border-t border-gray-300">로그아웃</a>
        </div>
      </div>
    </nav>
  </div>
</header>
<main class="flex items-center justify-center min-h-screen relative">

  <!-- 메인 박스 -->
  <div class="bg-white rounded-xl shadow-xl p-10 w-[900px] max-w-[95%] overflow-y-auto max-h-[90vh]">
    
    <!-- 단계 표시 -->
    <div class="flex justify-center mb-8 space-x-8 text-sm font-medium">
      <span class="text-blue-600 flex items-center gap-1"><span class="w-2 h-2 rounded-full bg-blue-600"></span> 이미지 업로드</span>
      <span class="text-blue-600 flex items-center gap-1"><span class="w-2 h-2 rounded-full bg-blue-600"></span> 국가 선택</span>
      <span class="text-blue-600 flex items-center gap-1"><span class="w-2 h-2 rounded-full bg-blue-600"></span> AI 분석</span>
      <span class="text-blue-600 flex items-center gap-1"><span class="w-2 h-2 rounded-full bg-blue-600"></span> 결과</span>
    </div>

    <!-- 분석 결과 제목 -->
    <h2 class="text-xl font-bold text-center mb-6">📊 계약서 위험 요소 분석 결과</h2>

    <!-- 그래프 이미지 -->
    <div class="flex justify-center mb-10">
      <img src="../images/gr.png" alt="분석 결과 그래프" class="max-w-full w-[550px] h-auto">
    </div>

    <!-- AI 코멘트 -->
    <div class="bg-white rounded-lg p-6 mb-10 border border-gray-200 shadow">
      <h3 class="text-lg font-semibold mb-4">💡 AI 코멘트</h3>
      <ul class="space-y-2 text-sm text-gray-700">
        <li>● <b>제8조</b> 계약해지 조건은 모호한 표현이 사용되어 근로자에게 불리할 수 있습니다. 명확한 해지 사유를 기재하는 것이 권장됩니다.</li>
        <li>● <b>제12조</b> 연장근로는 노동법상 기준을 초과하는 것으로 보이며, 고용노동부 가이드를 참고하세요.</li>
        <li>● 기타 조항에 대해 명확한 급여 지급 주기 및 세부조건이 누락되어 있습니다.</li>
      </ul>
    </div>

    <!-- 유사 사례 추천 -->
    <div>
      <h3 class="text-lg font-semibold mb-4">📚 유사 사례 추천</h3>
      <div class="flex flex-col md:flex-row gap-4">
        <!-- 블루 카드 -->
        <div class="bg-blue-50 p-4 rounded-lg border border-blue-200 w-full md:w-1/2">
          <h4 class="text-blue-600 font-semibold text-sm mb-1">근로시간 분쟁</h4>
          <p class="text-xs text-gray-700 mb-2">근로시간 기록이 불명확하여 소송으로 이어진 사례. 연장근로는 반드시 서면 동의가 필요.</p>
          <span class="text-[11px] text-blue-600 font-medium">유형: 연장 근로</span>
        </div>
        <!-- 레드 카드 -->
        <div class="bg-red-50 p-4 rounded-lg border border-red-200 w-full md:w-1/2">
          <h4 class="text-red-600 font-semibold text-sm mb-1">계약 해지 분쟁</h4>
          <p class="text-xs text-gray-700 mb-2">계약 해지 조항이 명확하지 않아 중도 해지 후 분쟁 발생. 명시된 사유와 서면 동의 필수.</p>
          <span class="text-[11px] text-red-600 font-medium">유형: 계약 해지</span>
        </div>
      </div>
    </div>

    <!-- 버튼 -->
    <div class="flex justify-center mt-10 gap-6">
      <button onclick="location.href ='/contract/draft' " class="bg-blue-600 text-white text-sm px-6 py-2 rounded-full hover:bg-blue-700 transition">계약서 초안 자동 생성</button>
      <button onclick="location.href ='/' " class="bg-gray-200 text-gray-800 text-sm px-6 py-2 rounded-full hover:bg-gray-300 transition">홈으로 돌아가기</button>
    </div>

  </div>
</main>

<!-- ✅ JS -->
  <script src="../js/result.js"></script>
</body>
</html>