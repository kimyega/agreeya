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

<!-- 본인 인증 박스 -->
<main class="flex-grow flex items-center justify-center">
    <div class="bg-white bg-opacity-95 p-10 rounded-xl shadow-md w-full max-w-md text-center mt-10">

        <h2 class="text-2xl font-bold mb-6">본인 인증</h2>

        <form class="space-y-6 text-left">

            <!-- 이름 입력 -->
            <!-- 이름 입력 -->
            <!-- ✅ 이름 입력 -->
            <!-- 이름 입력 -->
            <div class="relative mb-4 h-12">
                <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <i class="fa-solid fa-user text-gray-400 text-[16px]"></i>
                </div>
                <input
                        id="name"
                        type="text"
                        placeholder="이름을 입력하세요"
                        class="w-full h-full pl-10 pr-4 border rounded-full focus:outline-none focus:ring-2 focus:ring-blue-300 text-base placeholder-gray-400 text-gray-800"
                />
                <p id="name-msg" class="text-sm mt-1 ml-2 text-red-500 hidden"></p>
            </div>


            <!-- ✅ 전화번호 입력 -->
            <!-- 전화번호 입력 -->
            <div class="relative mb-4 h-12">
                <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <i class="fa-solid fa-phone text-gray-400 text-[16px]"></i>
                </div>
                <input
                        id="phone"
                        type="tel"
                        placeholder="전화번호를 입력하세요"
                        class="w-full h-full pl-10 pr-4 border rounded-full focus:outline-none focus:ring-2 focus:ring-blue-300 text-base placeholder-gray-400 text-gray-800"
                />
                <p id="phone-msg" class="text-sm mt-1 ml-2 text-red-500 hidden"></p>
            </div>




            <!-- 인증번호 받기 버튼 -->
            <button type="submit"
                    class="w-full bg-blue-500 text-white py-3 rounded-full font-semibold hover:bg-blue-600 text-base">
                인증번호 받기
            </button>
        </form>

        </form>
    </div>
</main>

<!-- ✅ JavaScript -->
<script>
    const form = document.querySelector("form");
    const nameInput = document.getElementById("name");
    const phoneInput = document.getElementById("phone");
    const nameMsg = document.getElementById("name-msg");
    const phoneMsg = document.getElementById("phone-msg");

    // 테스트용 사용자 정보
    const dummyUser = {
        name: "홍길동",
        phone: "01012345678"
    };

    form.addEventListener("submit", function (e) {
        e.preventDefault();

        const name = nameInput.value.trim();
        const phone = phoneInput.value.trim();

        // 초기화
        nameMsg.textContent = "";
        phoneMsg.textContent = "";
        nameMsg.classList.add("hidden");
        phoneMsg.classList.add("hidden");

        if (name === "") {
            nameMsg.textContent = "이름을 입력해주세요.";
            nameMsg.classList.remove("hidden");
        }

        if (phone === "") {
            phoneMsg.textContent = "전화번호를 입력해주세요.";
            phoneMsg.classList.remove("hidden");
        }

        if (name === "" || phone === "") return;

        if (name !== dummyUser.name) {
            nameMsg.textContent = "등록되지 않은 이름입니다.";
            nameMsg.classList.remove("hidden");
            return;
        }

        if (phone !== dummyUser.phone) {
            phoneMsg.textContent = "전화번호가 일치하지 않습니다.";
            phoneMsg.classList.remove("hidden");
            return;
        }


        // 쿼리스트링으로 이름과 전화번호 전달
        location.href = `Pin_001.html?name=${encodeURIComponent(name)}&phone=${encodeURIComponent(phone)}`;
    });
</script>


</body>
</html>
