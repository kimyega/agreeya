// ===== 헤더 유틸 =====
function simulateLogin() {
    const btn = document.getElementById("loginButton");
    const wrap = document.getElementById("profileDropdownWrapper");
    btn?.classList.add("hidden");
    wrap?.classList.remove("hidden");
}
function toggleDropdown() {
    const dd = document.getElementById("profileDropdown");
    dd?.classList.toggle("hidden");
}
function logout() {
    document.getElementById("profileDropdown")?.classList.add("hidden");
    document.getElementById("profileDropdownWrapper")?.classList.add("hidden");
    document.getElementById("loginButton")?.classList.remove("hidden");
}

// ===== 본문 로직 =====
document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('phone-verify-form');
    const name = document.getElementById('name');
    const phone = document.getElementById('phone');
    const nMsg = document.getElementById('name-msg');
    const pMsg = document.getElementById('phone-msg');

    const setErr = (el, msgEl, msg) => {
        msgEl.textContent = msg;
        msgEl.classList.remove('hidden');
        el.classList.add('border-red-400');
    };
    const clrErr = (el, msgEl) => {
        msgEl.textContent = '';
        msgEl.classList.add('hidden');
        el.classList.remove('border-red-400');
    };

    // 휴대폰 번호 입력 시 숫자만 추출 후 하이픈 넣어주기
    phone.addEventListener('input', () => {
        const digits = phone.value.replace(/\D/g, '');
        let v = digits;
        if (v.length > 3 && v.length <= 7) v = v.replace(/(\d{3})(\d+)/, '$1-$2');
        else if (v.length > 7) v = v.replace(/(\d{3})(\d{4})(\d+)/, '$1-$2-$3');
        phone.value = v;
    });

    // 폼 제출 시 실행
    form?.addEventListener('submit', (e) => {
        e.preventDefault();
        let ok = true;

        if (!name.value.trim()) {
            setErr(name, nMsg, '이름을 입력하세요.');
            ok = false;
        } else {
            clrErr(name, nMsg);
        }

        if (!/^01[0-9]-?\d{3,4}-?\d{4}$/.test(phone.value.trim())) {
            setErr(phone, pMsg, '휴대폰 번호 형식이 올바르지 않습니다.');
            ok = false;
        } else {
            clrErr(phone, pMsg);
        }

        if (!ok) return;

        // ✅ 서버로 인증번호 발송 요청
        const rawPhone = phone.value.trim();
        const digits = rawPhone.replace(/\D/g, '');

        fetch('/user/find-email/request', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({ phone: digits, name: name.value.trim() })
        })
            .then(r => r.json())
            .then(res => {
                if (res.ok) {
                    sessionStorage.setItem('fe_phone', digits); // 다음 화면에서 사용
                    window.location.href = '/phoneVerify';
                } else {
                    alert(res.message || '인증번호 발송 실패');
                }
            })
            .catch(() => alert('네트워크 오류가 발생했습니다.'));
    });
});
