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

  <!-- Table CSS JS -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/table.css"/>
  <script src="${pageContext.request.contextPath}/js/table.js"></script>
</head>

<body class="bg-slate-100 text-gray-800 font-sans">

<!-- 헤더 -->
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/include/header.jsp" />


<main class="relative z-10 flex justify-center items-start pt-16 pb-24 px-4">
  <div class="bg-white rounded-2xl shadow-xl w-full max-w-5xl px-14 py-14">

    <!-- 단계 표시 -->
    <div class="flex justify-between items-center mb-12 max-w-md mx-auto">
      <div class="flex flex-col items-center text-blue-600">
        <div class="w-3 h-3 rounded-full bg-blue-600 mb-1"></div>
        <span class="text-sm font-bold">이미지 업로드</span>
      </div>
      <div class="flex flex-col items-center text-gray-400">
        <div class="w-3 h-3 rounded-full bg-gray-300 mb-1"></div>
        <span class="text-sm">국가 선택</span>
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

    <!-- 업로드 박스 -->
    <div id="dropZone"
         class="border-2 border-dashed border-gray-400 rounded-xl p-12 bg-gray-50 text-center hover:border-blue-400 transition">
      <i class="fa-solid fa-cloud-arrow-up text-6xl text-gray-400 mb-6"></i>
      <p class="text-xl text-gray-600 mb-4">이미지를 드래그 하세요</p>
      <label for="fileInput"
             class="bg-blue-500 hover:bg-blue-600 text-white px-6 py-2 rounded-full text-sm font-semibold cursor-pointer inline-block">
        파일 선택
      </label>
      <input type="file" id="fileInput" class="hidden" accept="image/*" />
      <p class="text-sm text-gray-500 mt-2" id="fileNameText">선택된 파일 없음</p>
    </div>

    <!-- 가이드 문구 -->
    <ul class="mt-10 px-2 space-y-2 text-base text-gray-700 leading-relaxed pl-5 list-disc marker:text-gray-800 marker:text-lg">
      <li>계약서 전체가 잘 보이도록 촬영하세요</li>
      <li>빛 반사나 그림자가 없도록 촬영해주세요</li>
      <li>흐릿하지 않게, 초점이 맞게 촬영해주세요</li>
      <li>개인정보는 가린 뒤 업로드해도 됩니다</li>
    </ul>
    

    <!-- 다음 단계 버튼 -->
    <div class="flex justify-end mt-10">
      <button id="goNextBtn"
              class="bg-blue-400 text-white px-6 py-2 rounded-full font-bold disabled:opacity-40 transition"
              disabled>
        다음 단계 →
      </button>
    </div>
  </div>
</main>


  <!-- ✅ JS -->
  <script src="../js/upload.js"></script>
</body>
</html>
