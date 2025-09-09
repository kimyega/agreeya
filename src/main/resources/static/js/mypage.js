document.addEventListener('DOMContentLoaded', async () => {
  try {
    // 프로필 조회
    const res = await fetch(contextPath + '/user/mypage/profile', {
      method: 'GET',
      credentials: 'include'
    });

    if (!res.ok) throw new Error('응답 오류');

    const d = await res.json();
    if (!d) {
      alert('로그인이 필요합니다.');
      location.href = contextPath + '/user/login';
      return;
    }

    // ✅ DOM 채우기
    document.getElementById('pfName').textContent  = d.name ?? '-';
    document.getElementById('pfEmail').textContent = d.email ?? '-';
    document.getElementById('pfRegDt').textContent = d.createdAt ?? '-';
    document.getElementById('headerNick').textContent = d.name || 'User';

    // ✅ 상단바 토글
    document.getElementById('loginButton')?.classList.add('hidden');
    document.getElementById('profileDropdownWrapper')?.classList.remove('hidden');

  } catch (e) {
    console.error(e);
    alert('프로필 로딩 실패');
    location.href = contextPath + '/user/login';
  }

  // 회원 탈퇴 모달
  const modal = document.getElementById('withdrawModal');
  const doneModal = document.getElementById('withdrawDoneModal');

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
      const res = await fetch(contextPath + '/user/delete', {
        method: 'DELETE',
        credentials: 'include'
      });

      const d = await res.json();

      if (d === 1) {   // ✅ 컨트롤러가 int 반환하므로 그대로 비교
        // 탈퇴 확인 모달 닫기
        modal.classList.add('hidden');
        modal.classList.remove('flex');

        // ✅ 탈퇴 완료 모달 열기
        doneModal.classList.remove('hidden');
        doneModal.classList.add('flex');

        // 2초 후 메인으로 이동
        setTimeout(() => {
          location.href = contextPath + '/';
        }, 2000);

      } else {
        alert('탈퇴 실패');
      }

    } catch (err) {
      console.error(err);
      alert('탈퇴 요청 실패');
    }
  });
});
