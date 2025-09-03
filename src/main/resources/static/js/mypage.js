document.addEventListener('DOMContentLoaded', async () => {
  try {
    // 프로필 조회
    const res = await fetch('/user/mypage/profile', {
      method: 'GET',
      credentials: 'include'
    });

    if (!res.ok) throw new Error('응답 오류');

    const d = await res.json();
    if (!d) {
      alert('로그인이 필요합니다.');
      location.href = '/user/login';
      return;
    }

    // ✅ DOM 채우기
    document.getElementById('pfName').textContent  = d.name ?? '-';
    document.getElementById('pfEmail').textContent = d.email ?? '-';
    document.getElementById('pfRegDt').textContent = d.createdAt ?? '-';
    document.getElementById('headerNick').textContent = d.nickname || d.userId || 'User';

    // ✅ 상단바 토글
    document.getElementById('loginButton')?.classList.add('hidden');
    document.getElementById('profileDropdownWrapper')?.classList.remove('hidden');

  } catch (e) {
    console.error(e);
    alert('프로필 로딩 실패');
    location.href = '/user/login';
  }

  // 회원 탈퇴 모달
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
    try {
      const res = await fetch('/user/delete', {
        method: 'DELETE',
        credentials: 'include'
      });

      const d = await res.json();
      if (d.res === 1) {
        alert('탈퇴 완료');
        location.href = '/';
      } else {
        alert(d.msg || '탈퇴 실패');
      }
    } catch (err) {
      console.error(err);
      alert('탈퇴 요청 실패');
    }
  });

});
