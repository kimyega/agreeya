$(document).ready(function () {

    const contextPath = window.contextPath || "";

    // ✅ 자주 묻는 질문
    const faqList = [
        "근로계약서에는 어떤 항목이 꼭 들어가야 하나요?",
        "퇴직금은 언제 지급받을 수 있나요?",
        "연장근무 수당은 법적으로 어떻게 계산되나요?"
    ];

    // ✅ 초기 웰컴 메시지
    const welcomeMsg = `
    <div class="chat-row">
        <img src="${contextPath}/images/logo.png"
             alt="Agreeya 로고"
             class="bot-icon h-10"/>
        <div class="bot-msg flex flex-col items-start space-y-3">
            <div>
                안녕하세요 👋<br>
                <b>안심계약 AI 챗봇</b>입니다.<br><br>
                💼 노동법이나 근로계약서에 대해 궁금하신가요?<br>
                아래 자주 묻는 질문 중 하나를 클릭해보세요 👇
            </div>
            <div class="flex flex-wrap gap-2 mt-2">
                ${faqList.map(q => `
                    <button class="faq-btn bg-blue-50 border border-blue-300 text-blue-700 text-sm px-3 py-1 rounded-full hover:bg-blue-100 transition">
                        ${q}
                    </button>
                `).join("")}
            </div>
        </div>
    </div>
`;
    $("#chat-box").html(welcomeMsg);

    // ✅ FAQ 버튼 클릭 시
    $(document).on("click", ".faq-btn", function () {
        const selectedQuestion = $(this).text();
        sendMessage(selectedQuestion);
    });

    // ✅ 직접 입력 시 (비어 있으면 경고 말풍선 출력)
    $("#chat-form").on("submit", function (e) {
        e.preventDefault();
        const message = $("#user-input").val().trim();

        if (!message) {
            const errorMsg = `
                <div class="chat-row">
                    <img src="${contextPath}/images/logo.png" 
                         alt="Agreeya 로고" 
                         class="bot-icon h-10"/>
                    <div class="error-msg">⚠️ 입력란이 비어 있습니다. 질문을 입력해주세요.</div>
                </div>
            `;
            $("#chat-box").append(errorMsg);
            return; // 전송 중단
        }

        sendMessage(message);
    });

    // ✅ 메시지 전송 함수
    function sendMessage(message) {

        // 사용자 메시지 추가
        const userMsg = `
            <div class="flex justify-end my-2">
                <div class="user-msg">${message}</div>
            </div>
        `;
        $("#chat-box").append(userMsg);
        $("#user-input").val("");

        // ✅ 로딩 (...) 표시
        const loadingDiv = $(`
            <div class="chat-row" id="loading-dots">
                <img src="${contextPath}/images/logo.png" 
                     alt="Agreeya 로고" 
                     class="bot-icon h-10"/>
                <div class="bot-msg typing-dots">
                    <span class="dot"></span><span class="dot"></span><span class="dot"></span>
                </div>
            </div>
        `);
        $("#chat-box").append(loadingDiv);

        // ✅ 서버 요청
        $.ajax({
            url: `${contextPath}/chatbot/ask`,
            type: "POST",
            data: { question: message },
            dataType: "text",
            success: function (res) {
                // 로딩 제거
                $("#loading-dots").remove();

                const safeRes = escapeHtml(res).replace(/\r?\n/g, "<br/>");

                // AI 응답 표시
                const botContainer = $(`
                    <div class="chat-row">
                        <img src="${contextPath}/images/logo.png" 
                             alt="Agreeya 로고" 
                             class="bot-icon h-10"/>
                        <div class="bot-msg"></div>
                    </div>
                `);
                $("#chat-box").append(botContainer);
                const botBubble = botContainer.find(".bot-msg");

                typeMessage(botBubble, safeRes);
            },
            error: function () {
                $("#loading-dots").remove();
                const errorMsg = `
                    <div class="flex justify-start my-2">
                        <div class="error-msg">⚠️ 서버 오류가 발생했습니다.</div>
                    </div>
                `;
                $("#chat-box").append(errorMsg);
            }
        });
    }

    // ✅ 새 채팅 (초기화)
    $("#clearChat").on("click", function () {
        $("#chat-box").html(welcomeMsg);
    });

    // ✅ 자동 스크롤 (함수만 남겨둠, 호출 X)
    function scrollToBottom() {
        const chatBox = $("#chat-box");
        chatBox.stop().animate({ scrollTop: chatBox[0].scrollHeight }, 400);
    }

    // ✅ 타이핑 효과
    function typeMessage(element, text, speed = 30) {
        let i = 0;
        const interval = setInterval(() => {
            if (i < text.length) {
                element.html(text.substring(0, i + 1));
                i++;
            } else {
                clearInterval(interval);
            }
        }, speed);
    }

    // ✅ HTML escape (XSS 방지)
    function escapeHtml(text) {
        return text
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }
});
