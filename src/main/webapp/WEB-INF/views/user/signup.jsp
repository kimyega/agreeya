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

    <script type="text/javascript" src="/js/jquery-3.6.0.min.js"></script>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/table.css"/>
    <script src="${pageContext.request.contextPath}/js/table.js"></script>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/signup.css"/>

</head>

<body class="bg-slate-100 text-gray-800 font-sans">

<!-- ✅ 로그인 알림 메시지 -->
<div id="loginMessage"
     class="hidden fixed inset-0 flex items-center justify-center bg-black/30 z-50">
    <div class="bg-white text-green-600 font-bold text-2xl px-6 py-4 rounded-xl shadow-lg">
        로그인되었습니다.
    </div>
</div>

<!-- ✅ 헤더 -->
<header class="bg-white/90 backdrop-blur-md shadow-sm sticky top-0 z-10">
    <div class="w-full flex items-center justify-between py-1 px-6">

        <!-- 로고 -->
        <div class="flex-shrink-0">
            <img src="${pageContext.request.contextPath}/images/logo.png" alt="Agreeya 로고" class="h-24" />
        </div>

        <!-- 메뉴 -->
        <nav class="flex items-center space-x-8 text-xl font-semibold text-gray-800 pr-4">
            <a href="/" class="hover:text-blue-600">홈</a>
            <a href="/chatbot/aiSimulationMain" class="hover:text-blue-600">AI 모의 협상</a>
            <a href="/contract/upload" class="hover:text-blue-600">계약서 분석</a>
            <a href="/chatbot/qnaChatbot" class="hover:text-blue-600">Q&A 챗봇</a>

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
            <div class="relative flex items-center">
                <i class="fa-solid fa-user absolute left-4 text-gray-400"></i>
                <input id="name" name="name" type="text" placeholder="이름을 입력하세요"
                       class="w-full pl-10 p-3 border rounded-full focus:outline-none focus:ring-0" />
            </div>
            <p id="name-msg" class="text-xs ml-8 h-4 overflow-hidden m-0"></p>

            <!-- 비밀번호 -->
            <div class="relative flex items-center">
                <i class="fa-solid fa-lock absolute left-4 text-gray-400 text-base"></i>
                <input id="password" name="password" type="password" placeholder="비밀번호[특수문자 포함 8자내]"
                       class="w-full pl-10 pr-10 py-[0.875rem] border rounded-full focus:outline-none focus:ring-0 text-base" />
                <i class="fa-solid fa-eye-slash absolute right-4 text-gray-400 cursor-pointer"
                   onclick="togglePassword('password', this)"></i>
            </div>
            <p id="password-msg" class="text-xs ml-8 h-4 overflow-hidden m-0"></p>

            <!-- 비밀번호 확인 -->
            <div class="relative flex items-center">
                <i class="fa-solid fa-lock absolute left-4 top-0 bottom-0 my-auto flex items-center text-gray-400"></i>
                <input id="password-check" type="password" placeholder="비밀번호를 다시 입력하세요"
                       class="w-full pl-10 pr-10 py-[0.875rem] border rounded-full focus:outline-none focus:ring-0 text-base" />
                <i class="fa-solid fa-eye-slash absolute right-4 text-gray-400 cursor-pointer"
                   onclick="togglePassword('password-check', this)"></i>
            </div>
            <p id="password-check-msg" class="text-xs ml-8 h-4 overflow-hidden m-0"></p>

            <!-- 닉네임 -->
            <div class="relative flex items-center">
                <i class="fa-solid fa-user-pen absolute left-4 top-0 bottom-0 my-auto flex items-center text-gray-400"></i>
                <input id="nickname" name="nickname" type="text" placeholder="닉네임[한글·영문·숫자 조합 15자 이내]"
                       class="w-full pl-10 py-[0.875rem] border rounded-full focus:outline-none focus:ring-0 text-base" />
            </div>
            <p id="nickname-msg" class="text-xs ml-8 h-4 overflow-hidden m-0"></p>

            <!-- 전화번호 -->
            <div class="relative flex items-center">
                <i class="fa-solid fa-phone absolute left-4 top-0 bottom-0 my-auto flex items-center text-gray-400"></i>
                <input id="phone" name="tel" type="text" placeholder="전화번호를 입력하세요"
                       class="w-full pl-10 py-[0.875rem] border rounded-full focus:outline-none focus:ring-0 text-base"
                       maxlength="11" />
            </div>
            <p id="phone-msg" class="text-xs ml-8 h-4 overflow-hidden m-0"></p>

            <!-- 생년월일 -->
            <div class="flex gap-2">
                <input id="birth-year" name="birthYear" type="text" placeholder="년[4자]"
                       class="w-1/2 p-3 border rounded-lg focus:outline-none focus:ring-0"
                       maxlength="4" />
                <select id="birth-month" name="birthMonth"
                        class="w-1/4 p-3 border rounded-lg focus:outline-none focus:ring-0">
                    <option value="">월</option>
                    <option value="1">1월</option><option value="2">2월</option><option value="3">3월</option>
                    <option value="4">4월</option><option value="5">5월</option><option value="6">6월</option>
                    <option value="7">7월</option><option value="8">8월</option><option value="9">9월</option>
                    <option value="10">10월</option><option value="11">11월</option><option value="12">12월</option>
                </select>
                <input id="birth-day" name="birthDay" type="text" placeholder="일"
                       class="w-1/4 p-3 border rounded-lg focus:outline-none focus:ring-0"
                       maxlength="2" />
            </div>
            <p id="birth-msg" class="text-xs ml-2 h-4 overflow-hidden m-0"></p>

            <!-- 이메일 + 인증메일 -->
            <div class="flex gap-2">
                <input id="email" name="email" type="text" placeholder="이메일 주소를 입력해주세요"
                       class="flex-1 p-3 border rounded-lg focus:outline-none focus:ring-0" />
                <button type="button" onclick="checkEmail()" class="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 text-sm">
                    인증메일 받기
                </button>
            </div>
            <p id="email-msg" class="text-xs ml-2 h-4 overflow-hidden m-0"></p>

            <!-- 인증번호 확인 -->
            <div class="flex gap-2">
                <input id="email-code" type="text" placeholder="인증번호를 입력해주세요"
                       class="flex-1 p-3 border rounded-lg focus:outline-none focus:ring-0" />
                <button type="button" onclick="verifyEmailCode()" class="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 text-sm">
                    인증확인
                </button>
            </div>
            <p id="email-code-msg" class="text-xs ml-2 h-4 overflow-hidden m-0"></p>

            <!-- 국적 선택 -->
            <div class="flex justify-center gap-10 my-6">
                <label class="flex items-center gap-2">
                    <input type="radio" name="isForeigner" value="0" checked class="accent-blue-500" />
                    <span>내국인</span>
                </label>
                <label class="flex items-center gap-2">
                    <input type="radio" name="isForeigner" value="1" class="accent-blue-500" />
                    <span>외국인</span>
                </label>
            </div>

            <!-- 버튼 -->
            <button type="submit" class="w-full bg-blue-500 text-white py-3 rounded-lg hover:bg-blue-600 transition mt-2">회원가입</button>
            <button type="button" class="w-full border py-3 rounded-lg mt-2" onclick="history.back()">취소</button>
        </form>
    </div>
</main>

<!-- ✅ 페이지 전용 JS -->
<script src="${pageContext.request.contextPath}/js/signup.js"></script>
</body>
</html>
