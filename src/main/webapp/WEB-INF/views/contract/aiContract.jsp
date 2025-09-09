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
            <img src="/images/logo.png" alt="Agreeya 로고" class="h-24" />
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
                    <a href="/user/mypage" class="block px-4 py-3 text-center text-gray-800 hover:bg-gray-100">내 정보</a>
                    <a href="#" onclick="logout()" class="block px-4 py-3 text-center text-red-600 hover:bg-red-100 border-t border-gray-300">로그아웃</a>
                </div>
            </div>
        </nav>
    </div>
</header>

<!-- 홈 아이콘 -->
<a href="/" class="fixed bottom-20 right-20 text-blue-600 hover:text-blue-800 text-4xl z-50" title="홈으로 이동">
    <i class="fa-solid fa-house"></i>
</a>

<main class="max-w-3xl mx-auto px-6 py-16">
    <h1 class="text-4xl font-bold text-gray-900 mb-6 text-center">AI 기반 계약서 초안이 준비 되었습니다</h1>
    <p class="text-center text-gray-500 mb-8">
        AI가 분석한 결과를 바탕으로 위험 요소가 제거된 계약서 초안을 생성했습니다.
    </p>

    <section class="bg-white rounded-xl p-6 shadow-md">
        <h2 class="font-semibold text-xl mb-3">계약서 초안 미리보기</h2>

        <pre id="contractPreview"
             class="bg-gray-100 p-4 rounded-md h-96 max-h-96 overflow-y-auto text-gray-700 whitespace-pre-wrap text-base leading-relaxed"
             style="font-family: 'Pretendard', 'Noto Sans KR', sans-serif;">
[근로계약서 예시]
1. 근로자 성명: 홍길동
2. 고용 형태: 정규직
3. 근무 기간: 2025.08.01 ~ 2026.07.31
4. 근무 장소: 서울특별시 마포구
5. 임금: 월 2,500,000원
6. 주당 근로시간: 40시간
    </pre>

        <!-- 버튼 그룹: 오른쪽 정렬 -->
        <div class="mt-6 flex justify-end gap-4 items-center">
            <button id="translateBtn"
                    class="bg-gray-300 hover:bg-gray-400 text-gray-900 px-5 py-2 rounded-md font-semibold">
                번역
            </button>

            <button id="pdfBtn"
                    class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-md font-semibold">
                PDF 다운로드
            </button>
        </div>

    </section>
</main>

<!-- ✅ 번역 모달 -->
<div id="translateModal" class="fixed inset-0 z-50 hidden bg-black/50 flex items-center justify-center">
    <div class="bg-white rounded-xl w-64 p-6 space-y-4 shadow-lg text-center">
        <h3 class="text-lg font-bold text-gray-800">언어 선택</h3>
        <button class="w-full py-3 bg-blue-100 hover:bg-blue-200 rounded-md font-semibold text-lg" data-lang="ko">Korean (KO)</button>
        <button class="w-full py-3 bg-blue-100 hover:bg-blue-200 rounded-md font-semibold text-lg" data-lang="ja">Japanese (JA)</button>
        <button class="w-full py-3 bg-blue-100 hover:bg-blue-200 rounded-md font-semibold text-lg" data-lang="en">English (EN)</button>
    </div>
</div>

<!-- ✅ 스크립트 -->
<script type="javascript">
    const translateBtn = document.getElementById('translateBtn');
    const translateModal = document.getElementById('translateModal');
    const contractPreview = document.getElementById('contractPreview');

    translateBtn.addEventListener('click', () => {
        translateModal.classList.remove('hidden');
    });

    const translations = {
        ko: `[근로계약서 예시]
1. 근로자 성명: 홍길동
2. 고용 형태: 정규직
3. 근무 기간: 2025.08.01 ~ 2026.07.31
4. 근무 장소: 서울특별시 마포구
5. 임금: 월 2,500,000원
6. 주당 근로시간: 40시간`,

        ja: `[労働契約書例]
1. 労働者名: ホン・ギルドン
2. 雇用形態: 正社員
3. 勤務期間: 2025年08月01日 ～ 2026年07月31日
4. 勤務地: ソウル特別市麻浦区
5. 給与: 月額 2,500,000ウォン
6. 週間労働時間: 40時間`,

        en: `[Sample Labor Contract]
1. Employee Name: Hong Gil-dong
2. Employment Type: Full-time
3. Employment Period: 2025.08.01 ~ 2026.07.31
4. Work Location: Mapo-gu, Seoul
5. Salary: 2,500,000 KRW per month
6. Weekly Working Hours: 40 hours`
    };

    // 번역 모달 버튼 처리
    translateModal.querySelectorAll('button[data-lang]').forEach(btn => {
        btn.addEventListener('click', () => {
            const lang = btn.getAttribute('data-lang');
            contractPreview.textContent = translations[lang];
            translateModal.classList.add('hidden');
        });
    });

    // 외부 클릭 시 모달 닫기
    translateModal.addEventListener('click', (e) => {
        if (e.target === translateModal) {
            translateModal.classList.add('hidden');
        }
    });

    // PDF 다운로드
    const pdfBtn = document.getElementById('pdfBtn');
    // PDF 다운로드 버튼 클릭 시
    pdfBtn.addEventListener('click', () => {
        const target = contractPreview;

        html2canvas(target, {
            scale: 3, // 더 선명하게
            useCORS: true
        }).then(canvas => {
            const imgData = canvas.toDataURL('image/png');
            const { jsPDF } = window.jspdf;
            const pdf = new jsPDF({
                orientation: 'portrait',
                unit: 'mm',
                format: 'a4'
            });

            const pdfWidth = 190; // 여백 고려
            const pdfHeight = (canvas.height * pdfWidth) / canvas.width;

            pdf.addImage(imgData, 'PNG', 10, 10, pdfWidth, pdfHeight);
            pdf.save('contract_draft.pdf');
        });

    });
</script>

</body>
</html>

