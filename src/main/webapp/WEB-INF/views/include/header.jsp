<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!-- 로그인 알림 -->
<div id="loginMessage"
     class="hidden fixed inset-0 flex items-center justify-center bg-black/30 z-50">
    <div class="bg-white text-green-600 font-bold text-2xl px-6 py-4 rounded-xl shadow-lg">
        로그인되었습니다.
    </div>
</div>

<!-- 헤더 -->
<header class="bg-white/90 backdrop-blur-md shadow-sm sticky top-0 z-[999]">
    <div class="w-full flex items-center justify-between py-1 px-6">
        <!-- 로고 -->
        <div class="flex-shrink-0">
            <img src="/images/logo.png" alt="Agreeya 로고" class="h-24"/>
        </div>

        <!-- 메뉴 -->
        <nav class="flex items-center space-x-8 text-xl font-semibold text-gray-800 pr-4">
            <a href="/" class="hover:text-blue-600">홈</a>
            <a href="/chatbot/aiSimulationMain" class="hover:text-blue-600">AI 모의 협상</a>
            <a href="/contract/upload" class="hover:text-blue-600">계약서 분석</a>
            <a href="/chatbot/qnaChatbot" class="hover:text-blue-600">Q&A 챗봇</a>

            <!-- 로그인 여부 확인 -->
            <c:choose>
                <c:when test="${not empty sessionScope.LOGIN_USER_ID}">
                    <!-- 로그인 후 드롭다운 -->
                    <div id="profileDropdownWrapper" class="relative">
                        <button onclick="toggleDropdown()"
                                class="flex items-center space-x-2 text-xl font-bold text-gray-800 focus:outline-none">
                            <i class="fa-solid fa-user-circle text-2xl"></i>
                            <span id="headerNick">${sessionScope.LOGIN_USER_NICKNAME}</span>
                        </button>
                        <div id="profileDropdown"
                             class="absolute right-0 mt-2 w-40 bg-white border border-gray-300 rounded-md shadow-lg hidden z-50">
                            <a href="/user/mypage" class="block px-4 py-3 text-center text-gray-800 hover:bg-gray-100">내 정보</a>
                            <a href="/user/logout"
                               class="block px-4 py-3 text-center text-red-600 hover:bg-red-100 border-t border-gray-300">로그아웃</a>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <!-- 로그인 버튼 -->
                    <a href="/user/login"
                       class="bg-blue-500 text-white px-6 py-3 rounded-full hover:bg-blue-600 transition text-lg font-bold">
                        로그인
                    </a>
                </c:otherwise>
            </c:choose>
        </nav>
    </div>
</header>