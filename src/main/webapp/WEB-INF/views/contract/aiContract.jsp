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

    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <!--폰트(KR,JP)-->
    <script src="/fonts/NotoSansKR-Regular.js"></script>
    <script src="/fonts/NotoSansJP-Regular.js"></script>

    <!-- jsPDF + html2canvas -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/html2canvas/1.4.1/html2canvas.min.js"></script>

    <!-- 웹폰트 (한글/일본어/영문) -->
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR&family=Noto+Sans+JP&family=Roboto&display=swap" rel="stylesheet">

    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/table.css"/>
</head>

<body class="bg-slate-100 text-gray-800 font-sans">

<!-- 헤더 -->
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/include/header.jsp" />

<!-- 홈 아이콘 -->
<a href="/" class="fixed bottom-20 right-20 text-blue-600 hover:text-blue-800 text-4xl z-50" title="홈으로 이동">
    <i class="fa-solid fa-house"></i>
</a>

<main class="max-w-3xl mx-auto px-6 py-16">
    <h1 class="text-4xl font-bold text-gray-900 mb-6 text-center">AI 기반 계약서 초안 입니다</h1>
    <p class="text-center text-gray-500 mb-8">
        AI가 분석한 결과를 바탕으로 위험 요소가 제거된 계약서 초안을 생성했습니다.
    </p>

    <section class="bg-white rounded-xl p-6 shadow-md">
        <h2 class="font-semibold text-xl mb-3">계약서 초안 미리보기</h2>

        <!-- contractId (URL 파라미터) -->
        <input type="hidden" id="contractId" value="${param.contractId}">

        <!-- 계약서 출력 영역 -->
        <pre id="contractPreview"
             class="bg-gray-100 p-4 rounded-md h-96 max-h-96 overflow-y-auto text-gray-700 whitespace-pre-wrap text-base leading-relaxed"
             style="font-family: 'Noto Sans KR', 'Noto Sans JP', 'Roboto', sans-serif;">
로딩 중...
        </pre>

        <!-- 버튼 그룹 -->
        <div class="mt-6 flex justify-end gap-4 items-center">
            <button id="translateBtn"
                    class="bg-gray-300 hover:bg-gray-400 text-gray-900 px-5 py-2 rounded-md font-semibold">
                번역
            </button>

            <button id="pdfBtn"
                    class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-md font-semibold">
                PDF 다운로드
            </button>
        </div>
    </section>
</main>

<!-- 번역 모달 -->
<div id="translateModal" class="fixed inset-0 z-50 hidden bg-black/50 flex items-center justify-center">
    <div class="bg-white rounded-xl w-64 p-6 space-y-4 shadow-lg text-center">
        <h3 class="text-lg font-bold text-gray-800">언어 선택</h3>
        <button class="w-full py-3 bg-blue-100 hover:bg-blue-200 rounded-md font-semibold text-lg" data-lang="ko">Korean (KO)</button>
        <button class="w-full py-3 bg-blue-100 hover:bg-blue-200 rounded-md font-semibold text-lg" data-lang="ja">Japanese (JA)</button>
        <button class="w-full py-3 bg-blue-100 hover:bg-blue-200 rounded-md font-semibold text-lg" data-lang="en">English (EN)</button>
    </div>
</div>

<!-- Custom JS -->
<script src="${pageContext.request.contextPath}/js/aiContract.js"></script>

</body>
</html>
