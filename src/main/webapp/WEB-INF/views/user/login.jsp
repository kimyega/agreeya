<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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

    <link rel="stylesheet" href="table.css" />
    <script src="table.js"></script>
</head>

<body class="bg-cover bg-center bg-no-repeat min-h-screen flex flex-col" style="background-image: url('main.jpg');">

<!-- ✅ 상단 헤더 -->
<header class="w-full bg-white/90 backdrop-blur-md shadow-sm sticky top-0 z-10">
    <div class="w-full flex items-center justify-between py-1 px-6">
        <div class="flex-shrink-0">
            <img src="logo.png" alt="Agreeya 로고" class="h-24" />
        </div>

        <nav class="flex items-center space-x-8 text-xl font-semibold text-gray-800 pr-4">
            <a href="#" class="hover:text-blue-600">홈</a>
            <a href="#" class="hover:text-blue-600">AI 모의 협상</a>
            <a href="#" class="hover:text-blue-600">계약서 분석</a>
            <a href="#" class="hover:text-blue-600">Q&A 챗봇</a>
            <a id="loginButton" href="#" onclick="simulateLogin()" class="bg-blue-500 text-white px-6 py-3 rounded-full hover:bg-blue-600 transition text-lg font-bold">로그인</a>
            <div id="profileDropdownWrapper" class="relative hidden">
                <button onclick="toggleDropdown()" class="flex items-center space-x-2 text-xl font-bold text-gray-800 focus:outline-none">
                    <i class="fa-solid fa-user-circle text-2xl"></i>
                    <span>Hong</span>
                </button>
                <div id="profileDropdown" class="absolute right-0 mt-2 w-40 bg-white border border-gray-300 rounded-md shadow-lg hidden z-50">
                    <a href="/profile" class="block px-4 py-3 text-center text-gray-800 hover:bg-gray-100">내 정보</a>
                    <a href="#" onclick="logout()" class="block px-4 py-3 text-center text-red-600 hover:bg-red-100 border-t border-gray-300">로그아웃</a>
                </div>
            </div>
        </nav>
    </div>
</header>

<!-- ✅ 로그인 박스 -->
<main class="flex-grow flex items-center justify-center px-4">
    <div class="bg-white/90 backdrop-blur-md p-10 rounded-xl shadow-md w-full max-w-md text-center">

        <!-- 🔒 아이콘 -->
        <div class="mb-4 flex justify-center">
            <img src="좌물쇠.png" alt="로그인 아이콘" class="w-20 h-20" />
        </div>

        <h2 class="text-4xl font-bold mb-6">로그인</h2>


        <form id="login-form" class="space-y-6 text-left">
            <!-- 이메일 -->
            <div>
                <input id="email" type="email" placeholder="이메일 주소를 입력해주세요"
                       class="w-full px-4 py-3 rounded-full border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-400" />
                <p id="email-msg" class="text-sm mt-1 ml-2 text-red-500 hidden"></p>
            </div>

            <!-- 비밀번호 -->
            <div>
                <input id="password" type="password" placeholder="비밀번호를 입력하세요"
                       class="w-full px-4 py-3 rounded-full border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-400" />
                <p id="password-msg" class="text-sm mt-1 ml-2 text-red-500 hidden"></p>
            </div>

            <!-- 버튼 -->
            <button type="submit"
                    class="w-full bg-blue-500 text-white py-3 rounded-full font-semibold hover:bg-blue-600 transition">
                로그인
            </button>
        </form>

        <!-- 링크 -->
        <div class="flex justify-between text-sm text-gray-500 mt-6">
            <a href="FindPw.html" class="hover:underline">비밀번호 찾기</a>
            <a href="emailFd.html" class="hover:underline">이메일 찾기</a>
            <a href="signup.html" class="hover:underline">회원가입</a>
        </div>
    </div>
    <!-- ✅ 로그인 성공 모달 -->
    <div id="successModal"
         class="hidden fixed inset-0 flex items-center justify-center bg-black/40 z-50">
        <div class="bg-white px-8 py-6 rounded-xl shadow-xl text-green-600 text-xl font-bold">
            ✅ 로그인 성공
        </div>
    </div>
    <!-- ✅ 로그인 모달 -->
    <div id="loginModal" class="hidden fixed inset-0 z-50 bg-black/50 flex items-center justify-center">
        <div class="bg-white w-full max-w-md rounded-xl p-8 relative">
            <!-- 닫기 버튼 -->
            <button onclick="closeLoginModal()" class="absolute top-4 right-4 text-gray-400 hover:text-gray-600 text-2xl">&times;</button>

            <!-- 로그인 제목 -->
            <div class="text-center mb-6">
                <img src="좌물쇠.png" alt="로그인 아이콘" class="w-16 h-16 mx-auto mb-2" />
                <h2 class="text-3xl font-bold">로그인</h2>
            </div>

            <!-- 로그인 폼 -->
            <form id="modal-login-form" class="space-y-4">
                <input id="modal-email" type="email" placeholder="이메일 입력"
                       class="w-full px-4 py-3 border border-gray-300 rounded-full focus:outline-none focus:ring-2 focus:ring-blue-400" />
                <p id="modal-email-msg" class="text-sm text-red-500 hidden"></p>

                <input id="modal-password" type="password" placeholder="비밀번호 입력"
                       class="w-full px-4 py-3 border border-gray-300 rounded-full focus:outline-none focus:ring-2 focus:ring-blue-400" />
                <p id="modal-password-msg" class="text-sm text-red-500 hidden"></p>

                <button type="submit"
                        class="w-full bg-blue-500 text-white py-3 rounded-full font-semibold hover:bg-blue-600 transition">
                    로그인
                </button>
            </form>

            <!-- 링크 -->
            <div class="flex justify-between text-sm text-gray-500 mt-6">
                <a href="FindPw.html" class="hover:underline">비밀번호 찾기</a>
                <a href="emailFd.html" class="hover:underline">이메일 찾기</a>
                <a href="signup.html" class="hover:underline">회원가입</a>
            </div>
        </div>
    </div>


</main>

<!-- ✅ 로그인 스크립트 -->
<script>
    const form = document.getElementById("login-form");
    const emailInput = document.getElementById("email");
    const passwordInput = document.getElementById("password");
    const emailMsg = document.getElementById("email-msg");
    const passwordMsg = document.getElementById("password-msg");

    const dummyUser = {
        email: "test@example.com",
        password: "12345678"
    };

    form.addEventListener("submit", function (e) {
        e.preventDefault();

        const email = emailInput.value.trim();
        const password = passwordInput.value.trim();

        // 초기화
        emailMsg.textContent = "";
        passwordMsg.textContent = "";
        emailMsg.classList.add("hidden");
        passwordMsg.classList.add("hidden");

        // 유효성 검사
        if (!email) {
            emailMsg.textContent = "이메일을 입력해주세요.";
            emailMsg.classList.remove("hidden");
            return;
        }

        if (!password) {
            passwordMsg.textContent = "비밀번호를 입력해주세요.";
            passwordMsg.classList.remove("hidden");
            return;
        }

        if (email !== dummyUser.email) {
            emailMsg.textContent = "등록되지 않은 이메일입니다.";
            emailMsg.classList.remove("hidden");
            return;
        }

        if (password !== dummyUser.password) {
            passwordMsg.textContent = "비밀번호가 일치하지 않습니다.";
            passwordMsg.classList.remove("hidden");
            return;
        }

// 로그인 성공 처리
        showSuccessModal();


        alert("로그인에 성공했습니다.");
        // window.location.href = "dashboard.html";
    });
    const successModal = document.getElementById("successModal");

    function showSuccessModal() {
        successModal.classList.remove("hidden");
        setTimeout(() => {
            successModal.classList.add("hidden");
            // 로그인 후 이동 예시
            // window.location.href = "dashboard.html";
        }, 1500); // 1.5초 후 사라짐
    }
    // 로그인 모달 열기
    function simulateLogin() {
        document.getElementById("loginModal").classList.remove("hidden");
    }

    // 로그인 모달 닫기
    function closeLoginModal() {
        document.getElementById("loginModal").classList.add("hidden");
    }

    // 로그인 모달 form 유효성 검사 (기존과 동일)
    const modalForm = document.getElementById("modal-login-form");
    const modalEmail = document.getElementById("modal-email");
    const modalPassword = document.getElementById("modal-password");
    const modalEmailMsg = document.getElementById("modal-email-msg");
    const modalPasswordMsg = document.getElementById("modal-password-msg");

    modalForm.addEventListener("submit", function (e) {
        e.preventDefault();
        const email = modalEmail.value.trim();
        const password = modalPassword.value.trim();

        modalEmailMsg.textContent = "";
        modalPasswordMsg.textContent = "";
        modalEmailMsg.classList.add("hidden");
        modalPasswordMsg.classList.add("hidden");

        if (!email) {
            modalEmailMsg.textContent = "이메일을 입력해주세요.";
            modalEmailMsg.classList.remove("hidden");
            return;
        }

        if (!password) {
            modalPasswordMsg.textContent = "비밀번호를 입력해주세요.";
            modalPasswordMsg.classList.remove("hidden");
            return;
        }

        if (email !== dummyUser.email) {
            modalEmailMsg.textContent = "등록되지 않은 이메일입니다.";
            modalEmailMsg.classList.remove("hidden");
            return;
        }

        if (password !== dummyUser.password) {
            modalPasswordMsg.textContent = "비밀번호가 일치하지 않습니다.";
            modalPasswordMsg.classList.remove("hidden");
            return;
        }

        // 성공 모달 띄우기
        showSuccessModal();
        closeLoginModal();
    });

</script>

</body>
</html>
