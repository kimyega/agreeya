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

  <!-- 공통 CSS/JS -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/table.css"/>
  <script src="${pageContext.request.contextPath}/js/table.js"></script>

  <!-- jQuery -->
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>

<body class="bg-slate-100 text-gray-800 font-sans">

<!-- 헤더 -->
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/include/header.jsp" />

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

    <!-- 제목 -->
    <h2 class="text-center text-2xl font-bold mb-1">취업 국가를 선택해주세요</h2>
    <p class="text-center text-gray-600 text-base mb-10">근무 국가에 따라서 기준이 다르게 적용됩니다</p>

    <!-- 국가 카드 -->
    <form id="countryForm" method="post">
      <div class="flex flex-wrap justify-center gap-6 mb-6">
        <!-- 대한민국 -->
        <button type="button" value="KR"
                class="country-card flex flex-col items-center w-60 p-4 bg-white rounded-xl shadow-md cursor-pointer
                   hover:ring-4 hover:ring-blue-400 focus:outline-none active:scale-95 transition-transform duration-150">
          <img src="${pageContext.request.contextPath}/images/kor.png" class="w-32 h-32 object-contain mb-2" />
          <span class="text-xl font-semibold">대한민국</span>
        </button>

        <!-- 호주 -->
        <button type="button" value="AU"
                class="country-card flex flex-col items-center w-60 p-4 bg-white rounded-xl shadow-md cursor-pointer
                   hover:ring-4 hover:ring-blue-400 focus:outline-none active:scale-95 transition-transform duration-150">
          <img src="${pageContext.request.contextPath}/images/au.png" class="w-32 h-32 object-contain mb-2" />
          <span class="text-xl font-semibold">호주</span>
        </button>

        <!-- 일본 -->
        <button type="button" value="JP"
                class="country-card flex flex-col items-center w-60 p-4 bg-white rounded-xl shadow-md cursor-pointer
                   hover:ring-4 hover:ring-blue-400 focus:outline-none active:scale-95 transition-transform duration-150">
          <img src="${pageContext.request.contextPath}/images/jp.png" class="w-32 h-32 object-contain mb-2" />
          <span class="text-xl font-semibold">일본</span>
        </button>
      </div>

      <!-- 선택된 국가 코드 저장 -->
      <input type="hidden" name="countryCode" id="countryCode" value="">
    </form>

    <!-- 유사사례 체크박스 -->
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
      <button id="prevBtn" class="bg-blue-500 text-white px-6 py-3 rounded-full font-bold hover:bg-blue-600">이전 단계</button>
      <button id="nextBtn" class="bg-blue-400 text-white px-6 py-3 rounded-full font-bold opacity-50 cursor-not-allowed" disabled>다음 단계 →</button>
    </div>
  </div>

  <!-- 홈 이동 확인 모달 -->
  <div id="homeConfirmModal"
       class="hidden fixed inset-0 bg-black/50 flex justify-center items-center z-[1000]">
    <div class="bg-white rounded-lg shadow-xl w-96 p-6 text-center">
      <h3 class="text-xl font-bold mb-4">정말 나가시겠습니까?</h3>
      <p class="text-gray-600 mb-6">선택한 내용은 저장되지 않습니다.</p>
      <div class="flex justify-center space-x-4">
        <button id="confirmHomeBtn"
                class="px-5 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">
          확인
        </button>
        <button id="cancelHomeBtn"
                class="px-5 py-2 bg-gray-300 rounded hover:bg-gray-400">
          취소
        </button>
      </div>
    </div>
  </div>
</main>

<!-- JS -->
<script src="${pageContext.request.contextPath}/js/country.js"></script>
</body>
</html>
