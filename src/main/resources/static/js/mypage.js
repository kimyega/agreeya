// /static/js/mypage.js
document.addEventListener('DOMContentLoaded', async () => {
  // 프로필 로드
  try {
    const res = await fetch('/api/mypage/profile');
    if (!res.ok) {
      alert('로그인이 필요합니다.');
      location.href = '/user/login';
      return;
    }
    const d = await res.json();
    // 화면 바인딩 (백엔드 alias와 맞추세요)
    document.getElementById('pfName').textContent   = d.name ?? '-';
    document.getElementById('pfEmail').textContent  = d.email ?? '-';
    document.getElementById('pfRegDt').textContent  = d.regDt ?? '-';
    document.getElementById('headerNick').textContent = d.nickname || d.userId || 'User';
    // 헤더 상태 가짜로그인 UI 토글
    document.getElementById('loginButton')?.classList.add('hidden');
    document.getElementById('profileDropdownWrapper')?.classList.remove('hidden');
  } catch (e) {
    console.error(e);
    alert('프로필 로딩 실패');
  }

  // 모달
  const modal = document.getElementById('withdrawModal');
  document.getElementById('btnWithdraw')?.addEventListener('click', () => {
    modal.classList.remove('hidden');
    modal.classList.add('flex');
  });
  document.getElementById('cancelWithdraw')?.addEventListener('click', () => {
    modal.classList.add('hidden');
    modal.classList.remove('flex');
  });
  document.getElementById('confirmWithdraw')?.addEventListener('click', async () => {
    // TODO: 실제 탈퇴 API 호출 (/api/mypage/delete)
    alert('탈퇴 처리 API 연동 예정');
    modal.classList.add('hidden');
    modal.classList.remove('flex');
  });
});
