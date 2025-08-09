 function toggleDropdown() {
    const dropdown = document.getElementById('profileDropdown');
    dropdown.classList.toggle('hidden');
  }

  // 외부 클릭 시 드롭다운 닫기
  window.addEventListener('click', function (e) {
    const wrapper = document.getElementById('profileDropdownWrapper');
    const dropdown = document.getElementById('profileDropdown');
    if (!wrapper.contains(e.target)) {
      dropdown.classList.add('hidden');
    }
  });

  // 로그인 시 동작
  function simulateLogin() {
    const message = document.getElementById('loginMessage');
    const loginBtn = document.getElementById('loginButton');
    const profileMenu = document.getElementById('profileDropdownWrapper');

    message.classList.remove('hidden');

    setTimeout(() => {
      message.classList.add('hidden');
      loginBtn.classList.add('hidden');
      profileMenu.classList.remove('hidden');
    }, 1500);
  }

  // 로그아웃 시 원래 상태로 복원
  function logout() {
    const loginBtn = document.getElementById('loginButton');
    const profileMenu = document.getElementById('profileDropdownWrapper');
    const dropdown = document.getElementById('profileDropdown');

    loginBtn.classList.remove('hidden');
    profileMenu.classList.add('hidden');
    dropdown.classList.add('hidden');
  }
  function showWithdrawModal() {
  document.getElementById('withdrawModal').classList.remove('hidden');
}

function hideWithdrawModal() {
  document.getElementById('withdrawModal').classList.add('hidden');
}

function confirmWithdraw() {
  // 로그아웃 처리 및 홈으로 리디렉션
  alert("회원 탈퇴되었습니다.");
  window.location.href = "index.html"; // 홈 경로에 맞게 수정
}

function goToHome() {
  window.location.href = "index.html"; // 홈 경로에 맞게 수정
}
