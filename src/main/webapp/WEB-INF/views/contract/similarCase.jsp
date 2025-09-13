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

    <!-- Table CSS JS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/table.css"/>
    <script src="${pageContext.request.contextPath}/js/table.js"></script>
</head>

<body class="bg-slate-100 text-gray-800 font-sans">

<!-- 헤더 -->
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/include/header.jsp" />

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
        <a href="/contract/result" class="bg-gray-300 text-gray-800 px-6 py-3 rounded-full hover:bg-gray-400 transition w-full sm:w-auto text-center">
            분석 결과로 돌아가기
        </a>
        <a href="/contract/draft" class="bg-blue-600 text-white px-6 py-3 rounded-full hover:bg-blue-700 transition w-full sm:w-auto text-center">
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
