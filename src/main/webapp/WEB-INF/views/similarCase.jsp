<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>유사사례 상세 - 안심계약</title>
    <!-- Tailwind CSS -->
    <script src="https://cdn.tailwindcss.com"></script>

    <!-- Font Awesome (아이콘용) -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />

    <link rel="stylesheet" href="css/table.css"/>
    <script src="js/table.js"></script>
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
            <img src="images/logo.png" alt="Agreeya 로고" class="h-24" />
        </div>

        <!-- 메뉴 -->
        <nav class="flex items-center space-x-8 text-xl font-semibold text-gray-800 pr-4">
            <a href="#" class="hover:text-blue-600">홈</a>
            <a href="#" class="hover:text-blue-600">AI 모의 협상</a>
            <a href="#" class="hover:text-blue-600">계약서 분석</a>
            <a href="#" class="hover:text-blue-600">Q&A 챗봇</a>

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

<!-- 본문 -->
<main class="max-w-6xl mx-auto px-6 py-20 space-y-16 text-center">

    <!-- 제목 -->
    <section>
        <h2 class="text-4xl font-extrabold text-gray-900 mb-4">📚 유사 사례 상세 보기</h2>
        <p class="text-lg text-gray-600">AI와 법률 DB 분석을 기반으로 추천된 근로계약 사례입니다.</p>
    </section>

    <!-- 사례 카드 리스트 -->
    <section class="space-y-12">
        <!-- 각각의 카드 -->
        <div class="bg-white rounded-2xl shadow-lg p-8 text-left space-y-4">
            <h3 class="text-2xl font-semibold text-blue-700">근로시간 초과로 인한 분쟁 사례</h3>
            <p class="text-sm text-gray-500">유형: 연장근로 ｜ 위험도: 70%</p>

            <!-- 조항 1 -->
            <div class="bg-gray-50 border border-gray-200 rounded-xl p-6 space-y-4">
                <h4 class="text-lg font-bold text-blue-600">📄 계약서 발췌 조항 1</h4>
                <pre class="whitespace-pre-line text-sm text-gray-800 leading-relaxed">
제 5조 (근로시간)
근로자는 주 6일 근무, 일일 10시간 근무를 원칙으로 하며, 상황에 따라 연장근로를 할 수 있다.
연장근로에 대한 명확한 한도나 보상 조건은 별도로 기재하지 않는다.
        </pre>
                <div class="bg-red-50 border-l-4 border-red-500 p-4 rounded-md text-sm text-gray-700">
                    <strong class="text-red-600">🤖 AI 분석 코멘트:</strong><br/>
                    해당 조항은 주 52시간 초과 소지가 있으며, 연장근로 한도 및 수당 규정 부재로 근로자 권익 침해 위험이 큽니다.
                    명확한 시간 기준과 연장 시 서면 동의 요건, 수당 지급 기준을 명시할 필요가 있습니다.
                </div>
            </div>

            <!-- 조항 2 -->
            <div class="bg-gray-50 border border-gray-200 rounded-xl p-6 space-y-4">
                <h4 class="text-lg font-bold text-blue-600">📄 계약서 발췌 조항 2</h4>
                <pre class="whitespace-pre-line text-sm text-gray-800 leading-relaxed">
제 8조 (휴게시간)
회사의 업무 상황에 따라 휴게시간은 유동적으로 조정할 수 있으며, 별도 고지는 하지 않는다.
        </pre>
                <div class="bg-orange-50 border-l-4 border-orange-400 p-4 rounded-md text-sm text-gray-700">
                    <strong class="text-orange-600">🤖 AI 분석 코멘트:</strong><br/>
                    휴게시간은 근로기준법상 일정 시간 이상 의무적으로 부여되어야 하며, '유동적 조정'이나 '고지 없음'은 위법 소지가 있습니다.
                    최소 휴게시간과 고지 방식에 대한 명확한 조항 삽입이 필요합니다.
                </div>
            </div>

            <!-- 조항 3 -->
            <div class="bg-gray-50 border border-gray-200 rounded-xl p-6 space-y-4">
                <h4 class="text-lg font-bold text-blue-600">📄 계약서 발췌 조항 3</h4>
                <pre class="whitespace-pre-line text-sm text-gray-800 leading-relaxed">
제 12조 (계약 해지)
회사는 필요 시 근로자의 동의 없이 계약을 해지할 수 있다.
        </pre>
                <div class="bg-red-50 border-l-4 border-red-500 p-4 rounded-md text-sm text-gray-700">
                    <strong class="text-red-600">🤖 AI 분석 코멘트:</strong><br/>
                    근로계약 해지는 정당한 사유 및 절차가 필요합니다.
                    일방적 해지는 부당해고로 간주될 수 있으며, 최소한의 해지 사유와 통보 절차가 명시되어야 법적 분쟁을 예방할 수 있습니다.
                </div>
            </div>
        </div>
    </section>

    <!-- 분석 요약 -->
    <section class="bg-blue-50/50 border-l-4 border-blue-500 p-6 rounded-xl max-w-4xl mx-auto text-left">
        <h4 class="text-lg font-semibold text-blue-700 mb-2">📌 AI 기반 분석 요약</h4>
        <ul class="list-disc list-inside text-gray-700 text-sm space-y-1">
            <li>근로시간 명시 부재로 인한 위험도 높음</li>
            <li>연장근로 한도와 수당 조건 추가 필요</li>
            <li>재발 방지를 위한 명확한 규정 필수</li>
        </ul>
    </section>

    <!-- 유사 계약서 비교 -->
    <section class="bg-white rounded-2xl shadow-lg p-8 text-left space-y-4 max-w-4xl mx-auto">
        <h3 class="text-xl font-bold text-gray-800">🔍 유사 계약서 비교</h3>
        <ul class="list-disc list-inside text-gray-700 text-sm space-y-1">
            <li>사례 계약서와 87% 유사한 해외 파견 근로계약서에서 유사한 문제 발견</li>
            <li>일본 파견 계약서에서는 <strong>최대 근로시간</strong> 명확히 명시</li>
            <li>권장 문구: <span class="bg-yellow-100 px-2 py-0.5 rounded text-yellow-800">"주당 최대 근로시간은 52시간을 초과할 수 없다. 연장근로 시 서면 동의가 필요하다."</span></li>
        </ul>
    </section>

    <!-- 버튼 영역 -->
    <section class="flex flex-col sm:flex-row gap-4 justify-center mt-12">
        <a href="/analysis-result.html" class="bg-gray-300 text-gray-800 px-6 py-3 rounded-full hover:bg-gray-400 transition w-full sm:w-auto text-center">
            분석 결과로 돌아가기
        </a>
        <a href="/generate-draft.html" class="bg-blue-600 text-white px-6 py-3 rounded-full hover:bg-blue-700 transition w-full sm:w-auto text-center">
            GPT로 계약서 초안 생성하기
        </a>
    </section>
</main>


<!-- 푸터 -->
<footer class="mt-20 text-sm text-gray-500 py-6 text-center border-t border-gray-200">
    © 2025 안심계약. All rights reserved.
</footer>
</body>
</html>
