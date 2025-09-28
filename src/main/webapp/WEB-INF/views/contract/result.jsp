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

  <!-- Table CSS JS -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/table.css"/>
  <script src="${pageContext.request.contextPath}/js/table.js"></script>

  <!-- jQuery -->
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <script>const contextPath = "${pageContext.request.contextPath}";</script>
  <script src="${pageContext.request.contextPath}/js/chart.umd.min.js"></script>

</head>

<body class="bg-slate-100 text-gray-800 font-sans">

<!-- 헤더 -->
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/include/header.jsp" />

<main class="flex items-center justify-center min-h-screen relative">

  <div class="bg-white rounded-xl shadow-xl p-10 w-[900px] max-w-[95%] overflow-y-auto max-h-[90vh]">

    <!-- 진행 단계 -->
    <div class="flex justify-center mb-8 space-x-8 text-sm font-medium">
      <span class="text-blue-600 flex items-center gap-1"><span class="w-2 h-2 rounded-full bg-blue-600"></span> 이미지 업로드</span>
      <span class="text-blue-600 flex items-center gap-1"><span class="w-2 h-2 rounded-full bg-blue-600"></span> 국가 선택</span>
      <span class="text-blue-600 flex items-center gap-1"><span class="w-2 h-2 rounded-full bg-blue-600"></span> AI 분석</span>
      <span class="text-blue-600 flex items-center gap-1"><span class="w-2 h-2 rounded-full bg-blue-600"></span> 결과</span>
    </div>


    <!-- 위험 그래프 -->
    <h2 class="text-3xl font-bold text-center mb-6">📊 계약서 위험 요소 분석</h2>
    <canvas id="riskChart" class="mb-10"></canvas>

    <!-- 계약 조항 -->
    <div id="clauseContainer" class="space-y-6"></div>

    <!-- 분석 결과 -->
    <h2 class="text-3xl font-bold text-center mb-6 mt-10">📊 계약서 위험 요소 분석 결과</h2>

    <!-- AI 코멘트 -->
    <div id="aiCommentContainer" class="mb-10 space-y-4"></div>

    <!-- ✅ 유사사례 -->
    <div>
      <h3 class="text-3xl font-bold text-center mb-6">📚 유사 사례 추천</h3>
      <section id="similar-cases" class="space-y-6">
        <p class="text-gray-500">🔄 유사사례 불러오는 중...</p>
      </section>
    </div>


    <!-- 버튼 -->
    <div class="flex justify-center mt-10 gap-6">
      <button id="draftBtn" class="bg-blue-600 text-white text-sm px-6 py-2 rounded-full hover:bg-blue-700 transition">
        계약서 초안 자동 생성
      </button>
      <button id="homeBtn" class="bg-gray-200 text-gray-800 text-sm px-6 py-2 rounded-full hover:bg-gray-300 transition">
        홈으로 돌아가기
      </button>
    </div>
  </div>
</main>

<!-- ✅ JS -->
<script src="${pageContext.request.contextPath}/js/result.js"></script>
</body>
</html>
