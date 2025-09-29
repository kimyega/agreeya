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

    // 계약서 리스트 조회
    const resContracts = await fetch(contextPath + '/user/mypage/list', {
      method: 'GET',
      credentials: 'include'
    });

    if (!resContracts.ok) throw new Error('계약서 응답 오류');

    const contracts = await resContracts.json();
    console.log("계약서 리스트 응답:", contracts);  // ✅ 전체 리스트 출력

    const tbody = document.getElementById('contractList');
    tbody.innerHTML = '';

    contracts.forEach((c, index) => {
      console.log(`계약서 #${index + 1}:`, c);

      const tr = document.createElement('tr');
      tr.classList.add('border-b');

      // 분석일
      const dateTd = document.createElement('td');
      dateTd.classList.add('py-3');
      dateTd.textContent = c.createdAt ?? '-';

      // 위험요소 건수
      const riskTd = document.createElement('td');
      riskTd.classList.add('py-3');
      riskTd.textContent = c.riskCount ?? 0;
      riskTd.classList.add(c.riskCount > 0 ? 'text-red-500' : 'text-green-500');

      // 위험도 등급
      const levelTd = document.createElement('td');
      levelTd.classList.add('py-3');
      levelTd.textContent = c.riskLevel ?? '-';

      // 위험도 색상
      if (c.riskLevel === '높음') {
        levelTd.classList.add('text-red-500');
      } else if (c.riskLevel === '보통') {
        levelTd.classList.add('text-yellow-500');
      } else {
        levelTd.classList.add('text-green-500');
      }

      // 리포트 링크
      const linkTd = document.createElement('td');
      linkTd.classList.add('py-3');
      const link = document.createElement('a');
      link.href = `/contract/result?contractId=${c.contractId}`;
      link.textContent = '리포트';
      link.classList.add('text-blue-600', 'hover:underline');
      linkTd.appendChild(link);

      tr.append(dateTd, riskTd, levelTd, linkTd);
      tbody.appendChild(tr);
    });



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
        method: 'POST',
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
