<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>안심계약 - AI 근로계약 분석</title>

    <!-- Tailwind CSS -->
    <script src="https://cdn.tailwindcss.com"></script>
    <!-- jQuery CDN (head나 body 맨 위쪽에 추가) -->
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />



    <!-- Table CSS JS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/table.css"/>
    <script src="${pageContext.request.contextPath}/js/table.js"></script>
</head>

<body class="min-h-screen flex flex-col bg-slate-100 text-gray-800 font-sans">

<!-- 헤더 -->
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/include/header.jsp" />

<!-- 메인 -->
<main class="flex-1 flex items-center justify-center px-4">
    <div class="bg-white/90 backdrop-blur-md p-10 rounded-xl shadow-md w-full max-w-md text-center">
        <div class="mb-4 flex justify-center">
            <img src="/images/lock.png" alt="로그인 아이콘" class="w-20 h-20" />
        </div>

        <h2 class="text-4xl font-bold mb-6">로그인</h2>

        <form id="login-form" class="space-y-6 text-left">
            <div>
                <input id="email" type="email" placeholder="이메일 주소를 입력해주세요"
                       class="w-full px-4 py-3 rounded-full border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-400" />
                <p id="email-msg" class="text-sm mt-1 ml-2 text-red-500 hidden"></p>
            </div>

            <div>
                <input id="password" type="password" placeholder="비밀번호를 입력하세요"
                       class="w-full px-4 py-3 rounded-full border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-400" />
                <p id="password-msg" class="text-sm mt-1 ml-2 text-red-500 hidden"></p>
            </div>

            <button type="submit"
                    class="w-full bg-blue-500 text-white py-3 rounded-full font-semibold hover:bg-blue-600 transition">
                로그인
            </button>
        </form>

        <div class="flex justify-between text-sm text-gray-500 mt-6">
            <a href="/user/findPw" class="hover:underline">비밀번호 찾기</a>
            <a href="/user/findEmail" class="hover:underline">이메일 찾기</a>
            <a href="/user/signup" class="hover:underline">회원가입</a>
        </div>
    </div>

    <!-- 로그인 성공 모달 -->
    <div id="successModal" class="hidden fixed inset-0 flex items-center justify-center bg-black/40 z-50">
        <div class="bg-white px-8 py-6 rounded-xl shadow-xl text-green-600 text-xl font-bold">
            ✅ 로그인 성공
        </div>
    </div>

    <!-- 로그인 모달 -->
    <div id="loginModal" class="hidden fixed inset-0 z-50 bg-black/50 flex items-center justify-center">
        <div class="bg-white w-full max-w-md rounded-xl p-8 relative">
            <button onclick="closeLoginModal()" class="absolute top-4 right-4 text-gray-400 hover:text-gray-600 text-2xl">&times;</button>

            <div class="text-center mb-6">
                <img src="/images/lock.png" alt="로그인 아이콘" class="w-16 h-16 mx-auto mb-2" />
                <h2 class="text-3xl font-bold">로그인</h2>
            </div>

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

            <div class="flex justify-between text-sm text-gray-500 mt-6">
                <a href="/user/findPw" class="hover:underline">비밀번호 찾기</a>
                <a href="/user/findEmail" class="hover:underline">이메일 찾기</a>
                <a href="/user/signup" class="hover:underline">회원가입</a>
            </div>
        </div>
    </div>
</main>

<!-- 페이지 전용 JS -->
<script>
    const contextPath = "${pageContext.request.contextPath}";
</script>

<!-- ✅ login.js 불러오기 -->
<script src="${pageContext.request.contextPath}/js/login.js"></script>




</body>
</html>
