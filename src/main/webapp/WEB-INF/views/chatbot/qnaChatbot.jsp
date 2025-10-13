<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>안심계약 - AI 근로계약 분석</title>

    <!-- Tailwind CSS -->
    <script src="https://cdn.tailwindcss.com"></script>
    <script>const contextPath = "${pageContext.request.contextPath}";</script>


    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>

    <!-- 공통 CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/table.css"/>

    <!-- 챗봇 전용 CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/chatbot.css"/>

    <!-- 공통 JS -->
    <script src="${pageContext.request.contextPath}/js/table.js"></script>
</head>

<body class="bg-slate-100 text-gray-800 font-sans">

<!-- 헤더 -->
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/include/header.jsp"/>

<!-- 메인 -->
<main class="min-h-screen px-4 py-12 flex flex-col items-center">
    <div class="max-w-4xl w-full bg-white rounded-2xl shadow-lg p-8 relative">

        <!-- 홈 버튼 -->
        <a href="${pageContext.request.contextPath}/"
           class="absolute top-4 left-4 text-blue-600 hover:text-blue-800 text-4xl">
            <i class="fa-solid fa-house"></i>
        </a>

        <!-- 타이틀 -->
        <div class="flex justify-between items-center mb-6">
            <h2 class="text-3xl font-bold text-blue-600 text-center flex-1">AI Q&A 챗봇</h2>
            <button id="clearChat"
                    class="ml-4 bg-gray-100 text-gray-700 text-sm px-3 py-1 rounded-lg hover:bg-gray-200 transition">
                새 채팅
            </button>
        </div>

        <p class="text-gray-600 text-sm mb-6 text-center">
            노동법, 근로계약서 관련 질문을 입력해 주세요. AI가 실시간으로 답변해드립니다.
        </p>

        <!-- 채팅창 -->
        <div id="chat-box" class="chat-container"></div>

        <!-- 입력 -->
        <form id="chat-form" class="flex gap-2">
            <input type="text" id="user-input" placeholder="질문을 입력하세요..." class="chat-input" required/>
            <button type="submit" class="chat-btn">전송</button>
        </form>
    </div>
</main>

<!-- 푸터 -->
<footer class="mt-4 text-sm text-gray-500 py-6 text-center border-t border-gray-200">
    © 2025 안심계약. All rights reserved.
</footer>

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<!-- 챗봇 JS -->
<script src="${pageContext.request.contextPath}/js/chatbot.js"></script>

</body>
</html>
