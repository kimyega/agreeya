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

    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <!-- Font Awesome (아이콘용) -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />

    <!-- Table CSS JS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/table.css"/>
    <script src="${pageContext.request.contextPath}/js/table.js"></script>
</head>

<body class="bg-gradient-to-br from-blue-50 to-blue-100 min-h-screen flex flex-col">

<!-- 헤더 -->
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/include/header.jsp" />

<!-- 메인 콘텐츠 -->
<main class="w-[50%] max-w-[1600px] mx-auto px-4 py-10 flex flex-col h-[calc(100vh-100px)]">

    <div class="bg-white rounded-2xl p-6 shadow-xl flex flex-col flex-grow relative w-full">

    <!-- 홈 버튼 -->
        <a href="/" class="absolute top-4 left-4 text-blue-600 hover:text-blue-800 text-4xl">
            <i class="fa-solid fa-house"></i>
        </a>

        <!-- 제목 영역 -->
        <div class="text-center mb-8">
            <!-- 메인 타이틀 -->
            <h1 class="text-xl md:text-5xl text-blue-600 flex items-center justify-center gap-3 animate-fadeIn">
                <span>🗣️</span>
                <span>AI 모의 협상 시뮬레이터</span>
            </h1>

            <!-- 부제목 -->
            <p class="text-gray-600 text-base text-center mt-4 leading-relaxed animate-fadeIn">
                실제 근로계약서를 기반으로 근로자와 고용주 간의 협상 과정을<br> AI가 대화 형식으로 시뮬레이션합니다.
            </p>
        </div>

        <!-- 대화창 -->
        <div id="chat-box"
             class="flex-grow border rounded-2xl p-4 bg-gray-50 overflow-y-auto scroll-smooth shadow-inner">

            <!-- 초기 왼쪽 메시지 (AI) -->
            <div class="flex justify-start items-start space-x-3 mt-2 animate-fadeIn">
                <!-- 프로필 아이콘 -->
                <div class="flex flex-col items-center">
                    <!-- 아이콘 -->
                    <div class="w-10 h-10 rounded-full bg-green-400 flex items-center justify-center text-white text-lg">
                        <i class="fa-solid fa-user-tie"></i>
                    </div>
                    <!-- 아이콘 밑 글씨 -->
                    <span class="text-xs text-gray-600 mt-1">고용주</span>
                </div>

                <!-- 메시지 -->
                <div class="bg-green-100 text-green-800 px-4 py-5 rounded-2xl shadow-sm max-w-[75%] whitespace-normal text-left">
                    안녕하세요! 근로계약서에 대해 협상해볼까요?<br>원하는 조건이나 우려 사항을 말씀해주세요.
                </div>
            </div>
        </div>


        <!-- 입력창 -->
        <form id="chat-form" class="flex gap-3 mt-4" onsubmit="handleSubmit(event)">
            <input id="user-input" type="text" placeholder="근로자 역할로 질문해보세요..."
                   autocomplete="off" name="scenarioInput"
                   class="flex-grow px-4 py-3 rounded-full border border-gray-300 shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-400 text-gray-800" />
            <button type="submit"
                    class="bg-blue-600 text-white px-8 py-3 rounded-full hover:bg-blue-700 transition text-lg font-semibold">
                보내기
            </button>
        </form>
    </div>
</main>

<script>
    const chatBox = document.getElementById('chat-box');
    const input = document.getElementById('user-input');

    function handleSubmit(event) {
        event.preventDefault();
        sendMessage();
    }

    function sendMessage() {
        const message = input.value.trim();
        if (!message) return;

        // serialize 전에 value가 살아있을 때 formData 생성
        const formData = $('#chat-form').serialize();

        // 사용자 메시지 화면에 표시
        appendChat('근로자', message, 'right');

        // 입력값 초기화
        input.value = '';

        // 로딩 메시지
        const loadingEl = appendChat('고용주', '💭 생각 중...', 'left', true);

        console.log("serialize:", formData);

        $.ajax({
            url: '/chatbot/simulateChat',
            type: 'POST',
            data: formData, // serialize된 문자열
            success: function(reply) {
                loadingEl.remove();
                appendChat('고용주', reply.data, 'left');
            },
            error: function(xhr, status, error) {
                console.error("AI 응답 실패:", error);
                loadingEl.remove();
                appendChat('시스템', '⚠️ AI 응답 중 오류가 발생했습니다.', 'left');
            }
        });
    }

    // 메시지 추가 함수
    function appendChat(speaker, text, side) {
        var wrapper = document.createElement('div');
        wrapper.className = 'flex ' + (side === 'left' ? 'justify-start' : 'justify-end') + ' items-start space-x-3 mt-2';

        if (side === 'left') {
            // 왼쪽 메시지 (AI/시스템)
            wrapper.innerHTML =
                '<div class="flex flex-col items-center">' +
                '<div class="w-10 h-10 rounded-full bg-green-400 flex items-center justify-center text-white text-lg animate-fadeIn">' +
                '<i class="fa-solid fa-user-tie"></i>' +
                '</div>' +
                '<span class="text-xs text-gray-600 mt-1">고용주</span>' +
                '</div>' +
                '<div class="bg-green-100 text-green-800 px-4 py-5 rounded-2xl shadow-sm max-w-[75%] break-words whitespace-pre-wrap text-left">' +
                text +
                '</div>';
        } else {
            // 오른쪽 메시지 (사용자)
            wrapper.innerHTML =
                '<div class="bg-green-100 text-green-800 px-4 py-5 rounded-2xl shadow-sm max-w-[75%] break-words whitespace-pre-wrap text-left">' +
                text +
                '</div>' +
                '<div class="flex flex-col items-center">' +
                '<div class="w-10 h-10 rounded-full bg-blue-400 flex items-center justify-center text-white text-lg animate-fadeIn">' +
                '<i class="fa-solid fa-user"></i>' +
                '</div>' +
                '<span class="text-xs text-gray-600 mt-1">근로자</span>' +
                '</div>';
        }

        chatBox.appendChild(wrapper);
        wrapper.scrollIntoView({ behavior: 'smooth', block: 'end' });
        return wrapper;
    }

</script>

<style>
    @keyframes fadeIn {
        from { opacity: 0; transform: translateY(10px); }
        to { opacity: 1; transform: translateY(0); }
    }
    .animate-fadeIn { animation: fadeIn 0.3s ease-in-out; }
</style>

</body>
</html>
