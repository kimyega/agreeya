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