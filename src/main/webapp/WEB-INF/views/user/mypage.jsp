<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1"/>
  <title>안심계약 - 마이페이지</title>

  <script src="https://cdn.tailwindcss.com"></script>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>

  <!-- Table CSS JS -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/table.css"/>
  <script src="${pageContext.request.contextPath}/js/table.js"></script>
</head>

<body class="min-h-screen flex flex-col bg-slate-100 text-gray-800 font-sans">

<!-- 헤더 -->
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/include/header.jsp" />

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
<!-- contextPath 전역 변수 선언 -->
<script>
  const contextPath = "${pageContext.request.contextPath}";
</script>

<!-- mypage.js 불러오기 -->
<script src="${pageContext.request.contextPath}/js/mypage.js"></script>


</body>
</html>
