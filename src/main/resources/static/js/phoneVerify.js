// ===== 헤더 유틸 =====
function toggleDropdown() {
    const dropdown = document.getElementById('profileDropdown');
    dropdown?.classList.toggle('hidden');
}
function simulateLogin() {
    const btn = document.getElementById('loginButton');
    const wrapper = document.getElementById('profileDropdownWrapper');
    btn?.classList.add('hidden');
    wrapper?.classList.remove('hidden');
}
function logout() {
    document.getElementById('profileDropdown')?.classList.add('hidden');
    document.getElementById('profileDropdownWrapper')?.classList.add('hidden');
    document.getElementById('loginButton')?.classList.remove('hidden');
}

// ===== 본문 로직 =====
document.addEventListener('DOMContentLoaded', () => {
    const form  = document.getElementById('verify-form');
    const code  = document.getElementById('code');
    const msg   = document.getElementById('message');
    const modal = document.getElementById('resultModal');
    const emailEl = document.getElementById('resultEmail');

    const savedTel = (sessionStorage.getItem('fe_tel') || '').trim();

    function showErr(text) {
        msg.textContent = text;
        msg.classList.remove('hidden');
        code.classList.add('border-red-400');
    }
    function clearErr() {
        msg.textContent = '';
        msg.classList.add('hidden');
        code.classList.remove('border-red-400');
    }

    form?.addEventListener('submit', (e) => {
        e.preventDefault();
        clearErr();

        const v = code.value.trim();
        if (!/^\d{6}$/.test(v)) {
            showErr('인증번호는 6자리 숫자여야 합니다.');
            return;
        }
        if (!savedTel) {
            showErr('세션이 만료되었습니다. 처음부터 다시 진행해주세요.');
            return;
        }

        $.ajax({
            url: "/email/userVerifyFindEmail",
            type: "post",
            dataType: "json",
            data: { tel: savedTel, code: v },
            success: function (res) {
                if (res.result === 1) {
                    if (emailEl) emailEl.textContent = res.msg; // 마스킹된 이메일
                    modal?.classList.remove('hidden');
                } else {
                    showErr(res.msg || "인증 실패");
                }
            },
            error: function () {
                showErr("❌ 서버 요청 중 오류 발생");
            }
        });
    });
});
