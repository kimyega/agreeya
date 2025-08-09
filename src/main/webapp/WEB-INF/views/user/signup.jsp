<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>회원가입 | AGREEYA</title>

    <!-- Tailwind CSS -->
    <script src="https://cdn.tailwindcss.com"></script>

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />

    <!-- 공통 스타일/스크립트 -->
    <link rel="stylesheet" href="/css/table.css" />
    <script src="/js/table.js" defer></script>

    <style>
        .valid { border-color:#4ade80 !important; }
        .invalid { border:2px solid #f87171 !important; }
        .invalid::placeholder { color:#f87171; }
        .success { color:#22c55e; }
        .error { color:#ef4444; }
    </style>
</head>

<body class="bg-cover bg-center bg-no-repeat text-gray-800 font-sans" style="background-image:url('background.png');">

<!-- ✅ 로그인 알림 메시지 -->
<div id="loginMessage" class="hidden fixed inset-0 flex items-center justify-center bg-black/30 z-50">
    <div class="bg-white text-green-600 font-bold text-2xl px-6 py-4 rounded-xl shadow-lg">
        로그인되었습니다.
    </div>
</div>

<!-- ✅ 헤더 -->
<header class="bg-white/90 backdrop-blur-md shadow-sm sticky top-0 z-10">
    <div class="w-full flex items-center justify-between py-1 px-6">
        <!-- 로고 -->
        <div class="flex-shrink-0">
            <img src="logo.png" alt="Agreeya 로고" class="h-24" />
        </div>

        <!-- 메뉴 -->
        <nav class="flex items-center space-x-8 text-xl font-semibold text-gray-800 pr-4">
            <a href="#" class="hover:text-blue-600">홈</a>
            <a href="#" class="hover:text-blue-600">AI 모의 협상</a>
            <a href="#" class="hover:text-blue-600">계약서 분석</a>
            <a href="#" class="hover:text-blue-600">Q&amp;A 챗봇</a>

            <!-- ✅ 로그인 버튼 -->
            <a id="loginButton" href="#" onclick="simulateLogin()"
               class="bg-blue-500 text-white px-6 py-3 rounded-full hover:bg-blue-600 transition text-lg font-bold">
                로그인
            </a>

            <!-- ✅ 로그인 후 드롭다운 메뉴 -->
            <div id="profileDropdownWrapper" class="relative hidden">
                <button onclick="toggleDropdown()"
                        class="flex items-center space-x-2 text-xl font-bold text-gray-800 focus:outline-none">
                    <i class="fa-solid fa-user-circle text-2xl"></i>
                    <span>Hong</span>
                </button>
                <div id="profileDropdown"
                     class="absolute right-0 mt-2 w-40 bg-white border border-gray-300 rounded-md shadow-lg hidden z-50">
                    <a href="/profile" class="block px-4 py-3 text-center text-gray-800 hover:bg-gray-100">내 정보</a>
                    <a href="#" onclick="logout()" class="block px-4 py-3 text-center text-red-600 hover:bg-red-100 border-t border-gray-300">로그아웃</a>
                </div>
            </div>
        </nav>
    </div>
</header>

<!-- ✅ 이메일 인증 모달 -->
<div id="emailSentModal" class="fixed inset-0 bg-black/30 flex items-center justify-center z-50 hidden">
    <div class="bg-white rounded-xl shadow-xl p-8 text-center w-80">
        <p class="text-lg font-semibold text-green-600 mb-6">전송되었습니다</p>
        <button onclick="closeEmailSentModal()"
                class="bg-blue-500 text-white font-bold px-6 py-2 rounded-full hover:bg-blue-600 transition">
            확인
        </button>
    </div>
</div>

<!-- ✅ 회원가입 완료 모달 -->
<div id="signupCompleteModal" class="fixed inset-0 bg-black/30 flex items-center justify-center z-50 hidden">
    <div class="bg-white rounded-xl shadow-xl p-8 text-center w-80">
        <p class="text-xl font-semibold text-gray-800 mb-6">회원가입 완료</p>
        <button onclick="goToLogin()"
                class="bg-blue-500 text-white font-bold px-6 py-3 rounded-full hover:bg-blue-600 transition">
            로그인 화면으로
        </button>
    </div>
</div>

<!-- ✅ 회원가입 폼 -->
<main class="flex items-center justify-center min-h-screen px-4 py-10">
    <div class="bg-white w-full max-w-md p-8 rounded-2xl shadow-xl">
        <h2 class="text-center text-2xl font-bold mb-6">회원가입</h2>

        <form id="signupForm" class="space-y-3">
            <!-- 이름 -->
            <div class="relative">
                <i class="fa-solid fa-user absolute left-4 top-1/2 -translate-y-1/2 text-gray-400"></i>
                <input id="name" type="text" placeholder="이름을 입력하세요"
                       class="w-full pl-10 p-3 border rounded-full focus:outline-none focus:ring-0" />
                <p id="name-msg" class="text-sm mt-1"></p>
            </div>

            <!-- 비밀번호 -->
            <div class="relative flex items-center">
                <i class="fa-solid fa-lock absolute left-4 text-gray-400 text-base"></i>
                <input id="password" type="password" placeholder="비밀번호[특수문자 포함 8자내]"
                       class="w-full pl-10 pr-10 py-[0.875rem] border rounded-full focus:outline-none focus:ring-0 text-base" />
                <i class="fa-solid fa-eye-slash absolute right-4 text-gray-400 cursor-pointer"
                   onclick="togglePassword('password', this)"></i>
            </div>
            <p id="password-msg" class="text-sm mt-1"></p>

            <!-- 비밀번호 확인 -->
            <div class="relative flex items-center">
                <i class="fa-solid fa-lock absolute left-4 top-0 bottom-0 my-auto flex items-center text-gray-400"></i>
                <input id="password-check" type="password" placeholder="비밀번호를 다시 입력하세요"
                       class="w-full pl-10 pr-10 py-[0.875rem] border rounded-full focus:outline-none focus:ring-0 text-base" />
                <i class="fa-solid fa-eye-slash absolute right-4 text-gray-400 cursor-pointer"
                   onclick="togglePassword('password-check', this)"></i>
            </div>
            <p id="password-check-msg" class="text-sm mt-1"></p>

            <!-- 닉네임 -->
            <div class="relative flex items-center">
                <i class="fa-solid fa-user-pen absolute left-4 top-0 bottom-0 my-auto flex items-center text-gray-400"></i>
                <input id="nickname" type="text" placeholder="닉네임[한글·영문·숫자 조합 15자 이내]"
                       class="w-full pl-10 py-[0.875rem] border rounded-full focus:outline-none focus:ring-0 text-base" />
            </div>
            <p id="nickname-msg" class="text-sm mt-1"></p>

            <!-- 전화번호 -->
            <div class="relative flex items-center">
                <i class="fa-solid fa-phone absolute left-4 top-0 bottom-0 my-auto flex items-center text-gray-400"></i>
                <input id="phone" type="text" placeholder="전화번호를 입력하세요"
                       class="w-full pl-10 py-[0.875rem] border rounded-full focus:outline-none focus:ring-0 text-base" />
            </div>
            <p id="phone-msg" class="text-sm mt-1"></p>

            <!-- 생년월일 -->
            <div class="flex gap-2">
                <input id="birth-year" type="text" placeholder="년[4자]"
                       class="w-1/2 p-3 border rounded-lg focus:outline-none focus:ring-0" />
                <select id="birth-month"
                        class="w-1/4 p-3 border rounded-lg focus:outline-none focus:ring-0">
                    <option value="">월</option>
                    <option value="1">1월</option><option value="2">2월</option><option value="3">3월</option>
                    <option value="4">4월</option><option value="5">5월</option><option value="6">6월</option>
                    <option value="7">7월</option><option value="8">8월</option><option value="9">9월</option>
                    <option value="10">10월</option><option value="11">11월</option><option value="12">12월</option>
                </select>
                <input id="birth-day" type="text" placeholder="일"
                       class="w-1/4 p-3 border rounded-lg focus:outline-none focus:ring-0" />
            </div>
            <p id="birth-msg" class="text-sm mt-1"></p>

            <!-- 이메일 + 인증메일 -->
            <div class="flex gap-2">
                <input id="email" type="text" placeholder="이메일 주소를 입력해주세요"
                       class="flex-1 p-3 border rounded-lg focus:outline-none focus:ring-0" />
                <button type="button" onclick="checkEmail()" class="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 text-sm">
                    인증메일 받기
                </button>
            </div>
            <p id="email-msg" class="text-sm mt-1"></p>

            <!-- 인증번호 확인 -->
            <div class="flex gap-2">
                <input id="email-code" type="text" placeholder="이메일로 받은 인증번호를 입력해주세요"
                       class="flex-1 p-3 border rounded-lg focus:outline-none focus:ring-0" />
                <button type="button" onclick="verifyEmailCode()" class="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 text-sm">
                    인증확인
                </button>
            </div>
            <p id="email-code-msg" class="text-sm mt-1"></p>

            <!-- 국적 선택 -->
            <div class="flex justify-center gap-10 my-4">
                <label class="flex items-center gap-2">
                    <input type="radio" name="nation" checked class="accent-blue-500" />
                    <span>내국인</span>
                </label>
                <label class="flex items-center gap-2">
                    <input type="radio" name="nation" class="accent-blue-500" />
                    <span>외국인</span>
                </label>
            </div>

            <!-- 버튼 -->
            <button type="submit" class="w-full bg-blue-500 text-white py-3 rounded-lg hover:bg-blue-600 transition">회원가입</button>
            <button type="button" class="w-full border py-3 rounded-lg mt-2" onclick="history.back()">취소</button>
        </form>
    </div>
</main>

<!-- ====================== -->
<!--  내장 스크립트 (기존 JS) -->
<!-- ====================== -->
<script>
    document.addEventListener("DOMContentLoaded", function () {
        // ✅ 테스트용 이메일 목록 (중복확인용)
        const TEST_EMAILS = new Set([
            "test@test.com",
            "test@example.com",
            "user1@demo.com",
            "hello@agreeya.ai",
        ]);

        // ✅ DOM 요소 참조
        const signupForm = document.getElementById("signupForm");
        const name = document.getElementById("name");
        const password = document.getElementById("password");
        const passwordCheck = document.getElementById("password-check");
        const nickname = document.getElementById("nickname");
        const phone = document.getElementById("phone");
        const email = document.getElementById("email");
        const emailCode = document.getElementById("email-code");
        const year = document.getElementById("birth-year");
        const month = document.getElementById("birth-month");
        const day = document.getElementById("birth-day");

        // ✅ 공통 메시지/스타일 헬퍼
        const setError = (input, msgEl, message) => {
            input.classList.remove("valid");
            input.classList.add("invalid");
            msgEl.classList.remove("success");
            msgEl.classList.add("error");
            msgEl.textContent = message;
        };

        const setSuccess = (input, msgEl, message) => {
            input.classList.remove("invalid");
            input.classList.add("valid");
            msgEl.classList.remove("error");
            msgEl.classList.add("success");
            msgEl.textContent = message;
        };

        // ✅ 이름
        name?.addEventListener("input", function () {
            const msg = document.getElementById("name-msg");
            if (!this.value.trim()) setError(this, msg, "이름을 입력해 주세요.");
            else setSuccess(this, msg, "");
        });

        // ✅ 비밀번호
        const validatePassword = () => {
            const msg = document.getElementById("password-msg");
            const value = password.value;
            if (!value) return setError(password, msg, "비밀번호를 입력해 주세요.");
            const regex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]).{8,20}$/;
            if (!regex.test(value)) return setError(password, msg, "비밀번호가 조건에 맞지 않습니다.");
            setSuccess(password, msg, "사용 가능한 비밀번호 입니다.");
        };
        password?.addEventListener("input", validatePassword);

        // ✅ 비밀번호 확인
        const validatePasswordCheck = () => {
            const msg = document.getElementById("password-check-msg");
            if (!passwordCheck.value) return setError(passwordCheck, msg, "비밀번호를 다시 입력해 주세요.");
            if (passwordCheck.value !== password.value) return setError(passwordCheck, msg, "비밀번호가 일치하지 않습니다.");
            setSuccess(passwordCheck, msg, "비밀번호가 일치합니다.");
        };
        passwordCheck?.addEventListener("input", validatePasswordCheck);

        // ✅ 닉네임
        nickname?.addEventListener("input", function () {
            const msg = document.getElementById("nickname-msg");
            const value = nickname.value.trim();
            const regex = /^[가-힣a-zA-Z0-9]{1,15}$/;
            if (!value) return setError(nickname, msg, "닉네임을 입력해 주세요.");
            if (!regex.test(value)) return setError(nickname, msg, "형식이 올바르지 않습니다.");
            setSuccess(nickname, msg, "사용 가능한 닉네임입니다.");
        });

        // ✅ 전화번호
        phone?.addEventListener("input", function () {
            const msg = document.getElementById("phone-msg");
            const value = phone.value.trim();
            const regex = /^01[016789]-?\d{3,4}-?\d{4}$/;
            if (!value) return setError(phone, msg, "전화번호를 입력해 주세요.");
            if (!regex.test(value)) return setError(phone, msg, "전화번호 형식이 잘못되었습니다.");
            setSuccess(phone, msg, "사용 가능한 전화번호입니다.");
        });

        // ✅ 생년월일
        const validateBirth = () => {
            const msg = document.getElementById("birth-msg");
            [year, month, day].forEach(el => el.classList.remove("invalid"));
            if (!year.value) return setError(year, msg, "년도를 입력해 주세요.");
            if (!/^[0-9]{4}$/.test(year.value)) return setError(year, msg, "년도는 4자리수 입니다.");
            if (!month.value) return setError(month, msg, "월을 선택해 주세요.");
            if (!day.value) return setError(day, msg, "일을 입력해 주세요.");

            const date = new Date(`${year.value}-${month.value}-${day.value}`);
            if (isNaN(date.getTime()) || date.getDate() != Number(day.value))
                return setError(day, msg, "일수가 맞지 않습니다.");

            [year, month, day].forEach(el => el.classList.add("valid"));
            msg.classList.remove("error");
            msg.classList.add("success");
            msg.textContent = "생년월일이 유효합니다.";
        };
        year?.addEventListener("blur", validateBirth);
        day?.addEventListener("blur", validateBirth);
        month?.addEventListener("change", validateBirth);

        // ✅ 이메일 중복확인 + 인증메일 전송 모달
        const checkEmail = (isSubmit = false) => {
            const msg = document.getElementById("email-msg");
            const value = email.value.trim();
            if (!value) return setError(email, msg, "이메일을 입력해 주세요.");
            const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!regex.test(value)) return setError(email, msg, "이메일 주소 형식이 맞지 않습니다.");

            const isDuplicated = TEST_EMAILS.has(value);
            if (isDuplicated) return setError(email, msg, "중복된 이메일 입니다.");

            setSuccess(email, msg, "사용 가능한 이메일입니다.");

            if (!isSubmit) {
                const modal = document.getElementById("emailSentModal");
                if (modal) modal.classList.remove("hidden");
            }
        };
        email?.addEventListener("blur", checkEmail);
        window.checkEmail = checkEmail;

        // ✅ 이메일 인증번호 확인
        const verifyEmailCode = () => {
            const msg = document.getElementById("email-code-msg");
            const expected = "123456";
            const inputCode = emailCode.value.trim();

            if (!inputCode) return setError(emailCode, msg, "인증번호를 입력해 주세요.");
            if (inputCode !== expected) return setError(emailCode, msg, "인증번호가 일치하지 않습니다.");

            setSuccess(emailCode, msg, "인증되었습니다.");
            email.disabled = true;
            email.nextElementSibling.disabled = true; // 인증메일 버튼
            emailCode.disabled = true;
            email.nextElementSibling.style.opacity = 0.5;
            email.nextElementSibling.style.cursor = "not-allowed";
        };
        emailCode?.addEventListener("blur", verifyEmailCode);
        window.verifyEmailCode = verifyEmailCode;

        // ✅ 폼 제출
        signupForm?.addEventListener("submit", function (e) {
            e.preventDefault();

            // 모든 입력값 검사 이벤트 트리거
            name.dispatchEvent(new Event("input"));
            password.dispatchEvent(new Event("input"));
            passwordCheck.dispatchEvent(new Event("input"));
            nickname.dispatchEvent(new Event("input"));
            phone.dispatchEvent(new Event("input"));
            email.dispatchEvent(new Event("blur"));
            emailCode.dispatchEvent(new Event("blur"));
            year.dispatchEvent(new Event("blur"));
            day.dispatchEvent(new Event("blur"));

            const requiredValidInputs = [name, password, passwordCheck, nickname, phone, email, emailCode];
            const allValid = requiredValidInputs.every((el) => el.classList.contains("valid"));

            const birthValid =
                year.value && /^[0-9]{4}$/.test(year.value) &&
                month.value && day.value &&
                !isNaN(new Date(`${year.value}-${month.value}-${day.value}`).getTime());

            if (!allValid || !birthValid) {
                console.log("유효하지 않음");
                return;
            }

            const modal = document.getElementById("signupCompleteModal");
            if (modal) modal.classList.remove("hidden");
        });
    });

    // ✅ 이메일 전송 모달 닫기
    function closeEmailSentModal() {
        const modal = document.getElementById("emailSentModal");
        if (modal) modal.classList.add("hidden");
    }
    window.closeEmailSentModal = closeEmailSentModal;

    // ✅ 로그인 이동
    function goToLogin() {
        // 필요 시 경로 수정
        window.location.href = "/html/login (1).html";
    }
    window.goToLogin = goToLogin;

    // ✅ 비밀번호 보기/숨기기
    function togglePassword(inputId, iconEl) {
        const input = document.getElementById(inputId);
        if (!input) return;
        const isHidden = input.type === "password";
        input.type = isHidden ? "text" : "password";
        if (iconEl && iconEl.classList) {
            iconEl.classList.toggle("fa-eye-slash", !isHidden);
            iconEl.classList.toggle("fa-eye", isHidden);
        }
    }
    window.togglePassword = togglePassword;

    // =====================
    // 헤더용 유틸 (누락 보완)
    // =====================
    function simulateLogin() {
        // 로그인 메시지 토스트
        const toast = document.getElementById("loginMessage");
        toast.classList.remove("hidden");
        setTimeout(() => toast.classList.add("hidden"), 1200);

        // 버튼 → 드롭다운 전환
        document.getElementById("loginButton").classList.add("hidden");
        document.getElementById("profileDropdownWrapper").classList.remove("hidden");
    }

    function toggleDropdown() {
        const dd = document.getElementById("profileDropdown");
        dd.classList.toggle("hidden");
        // 외부 클릭 닫기
        const closeOnOutside = (e) => {
            if (!dd.contains(e.target)) {
                dd.classList.add("hidden");
                document.removeEventListener("click", closeOnOutside);
            }
        };
        document.addEventListener("click", closeOnOutside);
    }

    function logout() {
        // 드롭다운/버튼 상태 복원
        document.getElementById("profileDropdown").classList.add("hidden");
        document.getElementById("profileDropdownWrapper").classList.add("hidden");
        document.getElementById("loginButton").classList.remove("hidden");
    }
</script>
</body>
</html>
