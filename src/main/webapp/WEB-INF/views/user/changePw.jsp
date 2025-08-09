<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>AGREEYA | 비밀번호 변경</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="relative min-h-screen flex flex-col items-center justify-start bg-cover bg-center" style="background-image: url('main.jpg');">

<!-- 상단바 -->
<header class="w-full bg-white shadow-sm flex justify-between items-center px-10 py-4">
    <div class="flex items-center">
        <img src="logo.png" alt="AGREEYA 로고" class="h-10 mr-2" />
        <span class="text-lg font-bold text-[#00BCD4] tracking-widest">AGREEYA</span>
    </div>
    <nav class="flex items-center space-x-10">
        <a href="#" class="text-lg text-black font-medium">홈</a>
        <a href="#" class="text-lg text-black font-medium">AI모의 협상</a>
        <a href="#" class="text-lg text-black font-medium">계약서 분석</a>
        <a href="#" class="text-lg font-bold text-black">Q&amp;A 챗봇</a>
        <button class="bg-blue-500 text-white px-6 py-2 rounded-full text-sm font-semibold hover:bg-blue-600">로그인</button>
    </nav>
</header>

<!-- 비밀번호 변경 박스 -->
<main class="flex-grow flex items-center justify-center">
    <div class="bg-white bg-opacity-95 p-10 rounded-xl shadow-md w-[500px] text-center mt-10">
        <h2 class="text-xl font-semibold mb-6">비밀번호 변경</h2>

        <form id="reset-form" class="space-y-5">
            <!-- 새 비밀번호 -->
            <div class="text-left">
                <label for="new-password" class="block mb-1 text-sm font-medium">새 비밀번호</label>
                <input type="password" id="new-password" placeholder="비밀번호를 입력하세요"
                       class="w-full px-4 py-2 rounded-full border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-300" />
                <p id="new-password-msg" class="text-sm mt-1"></p>
            </div>

            <!-- 비밀번호 확인 -->
            <div class="text-left">
                <label for="confirm-password" class="block mb-1 text-sm font-medium">비밀번호 확인</label>
                <input type="password" id="confirm-password" placeholder="비밀번호를 다시 입력하세요"
                       class="w-full px-4 py-2 rounded-full border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-300" />
                <p id="confirm-password-msg" class="text-sm mt-1"></p>
            </div>

            <!-- 버튼 -->
            <button type="submit"
                    class="w-full bg-blue-500 text-white py-3 rounded-full font-semibold hover:bg-blue-600">
                비밀번호 변경
            </button>
        </form>
    </div>
</main>

<!-- ✅ 모달 -->
<div id="modal" class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center hidden z-50">
    <div class="bg-white p-6 rounded-lg shadow-xl text-center w-full max-w-sm">
        <p id="modal-message" class="text-green-600 font-semibold text-base mb-4">비밀번호가 성공적으로 변경되었습니다.</p>
        <button id="modal-confirm" class="bg-blue-500 text-white px-6 py-2 rounded-full font-medium hover:bg-blue-600">
            로그인 화면으로 돌아가기
        </button>
    </div>
</div>

<!-- ✅ JS 로직 -->
<script>
    const form = document.getElementById("reset-form");
    const newPassword = document.getElementById("new-password");
    const confirmPassword = document.getElementById("confirm-password");
    const newMsg = document.getElementById("new-password-msg");
    const confirmMsg = document.getElementById("confirm-password-msg");
    const modal = document.getElementById("modal");
    const modalMessage = document.getElementById("modal-message");
    const modalConfirm = document.getElementById("modal-confirm");

    const setError = (input, msgEl, message) => {
        input.classList.remove("border-gray-300", "focus:ring-blue-300");
        input.classList.add("border-red-500", "focus:ring-red-300");
        msgEl.classList.remove("text-green-500");
        msgEl.classList.add("text-red-500");
        msgEl.textContent = message;
    };

    const setSuccess = (input, msgEl, message) => {
        input.classList.remove("border-red-500", "focus:ring-red-300");
        input.classList.add("border-green-500", "focus:ring-green-300");
        msgEl.classList.remove("text-red-500");
        msgEl.classList.add("text-green-500");
        msgEl.textContent = message;
    };

    form.addEventListener("submit", function (e) {
        e.preventDefault();

        const pw1 = newPassword.value.trim();
        const pw2 = confirmPassword.value.trim();
        let valid = true;

        newMsg.textContent = "";
        confirmMsg.textContent = "";

        // 비밀번호 유효성
        const regex = /^(?=.*[A-Za-z])(?=.*\d).{6,}$/;

        if (!pw1) {
            setError(newPassword, newMsg, "새 비밀번호를 입력해주세요.");
            valid = false;
        } else if (!regex.test(pw1)) {
            setError(newPassword, newMsg, "영문+숫자 포함 6자 이상이어야 합니다.");
            valid = false;
        } else {
            setSuccess(newPassword, newMsg, "");
        }

        if (!pw2) {
            setError(confirmPassword, confirmMsg, "비밀번호 확인을 입력해주세요.");
            valid = false;
        } else if (pw1 !== pw2) {
            setError(confirmPassword, confirmMsg, "비밀번호가 일치하지 않습니다.");
            valid = false;
        } else {
            setSuccess(confirmPassword, confirmMsg, "");
        }

        // 성공 시 모달 표시
        if (valid) {
            modal.classList.remove("hidden");
            modalMessage.textContent = "비밀번호가 성공적으로 변경되었습니다.";
        }
    });

    modalConfirm.addEventListener("click", () => {
        modal.classList.add("hidden");
        window.location.href = "login (1).html"; // 로그인 페이지로 이동 (필요 시 경로 수정)
    });
</script>

</body>
</html>
