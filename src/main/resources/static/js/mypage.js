// 드롭다운 토글
function toggleDropdown() {
  const dropdown = document.getElementById('profileDropdown');
  dropdown?.classList.toggle('hidden');
}

// 외부 클릭 시 드롭다운 닫기
window.addEventListener('click', function (e) {
  const wrapper = document.getElementById('profileDropdownWrapper');
  const dropdown = document.getElementById('profileDropdown');
  if (wrapper && dropdown && !wrapper.contains(e.target)) {
    dropdown.classList.add('hidden');
  }
});

// 로그인 시 동작 (데모)
function simulateLogin() {
  const message = document.getElementById('loginMessage');
  const loginBtn = document.getElementById('loginButton');
  const profileMenu = document.getElementById('profileDropdownWrapper');

  message?.classList.remove('hidden');
  setTimeout(() => {
    message?.classList.add('hidden');
    loginBtn?.classList.add('hidden');
    profileMenu?.classList.remove('hidden');
  }, 1500);
}

// 로그아웃
function logout() {
  const loginBtn = document.getElementById('loginButton');
  const profileMenu = document.getElementById('profileDropdownWrapper');
  const dropdown = document.getElementById('profileDropdown');
  loginBtn?.classList.remove('hidden');
  profileMenu?.classList.add('hidden');
  dropdown?.classList.add('hidden');
}

// 탈퇴 모달
// 이미 있는 모달 show/hide
function showWithdrawModal() {
  const m = document.getElementById('withdrawModal');
  m?.classList.remove('hidden'); m?.classList.add('flex');
}
function hideWithdrawModal() {
  const m = document.getElementById('withdrawModal');
  m?.classList.add('hidden'); m?.classList.remove('flex');
}

// ✅ 완료 모달 show/hide + 홈 이동
function showWithdrawDoneModal() {
  const m = document.getElementById('withdrawDoneModal');
  m?.classList.remove('hidden'); m?.classList.add('flex');
}
function hideWithdrawDoneModal() {
  const m = document.getElementById('withdrawDoneModal');
  m?.classList.add('hidden'); m?.classList.remove('flex');
}
function goHomeNow() {
  window.location.href = "/";  // 또는 "/index"
}

// ✅ alert 대신 모달로 완료 처리
let _withdrawTimer = null;
function confirmWithdraw() {
  // (백엔드 연동이 있다면 여기서 fetch/axios 호출 후 성공 시 아래 실행)
  hideWithdrawModal();           // 확인 모달 닫고
  showWithdrawDoneModal();       // 완료 모달 표시
  // 자동 리다이렉트(원하면 시간 조절)
  clearTimeout(_withdrawTimer);
  _withdrawTimer = setTimeout(goHomeNow, 1200);
}
