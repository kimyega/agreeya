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
  let selectedCountry = '';

  

document.addEventListener('DOMContentLoaded', function () {
  const countryCards = document.querySelectorAll('.country-card');
  const selectedText = document.getElementById('selectedCountryText');
  const nextBtn = document.getElementById('nextBtn');
  const prevBtn = document.getElementById('prevBtn');
  const modal = document.getElementById('euModal');

  countryCards.forEach(card => {
    card.addEventListener('click', () => {
      const country = card.dataset.country;
      if (country === 'E U') {
        modal.classList.remove('hidden');
        return;
      }
      selectedText.textContent = `선택한 국가: ${country}`;
      nextBtn.disabled = false;
      nextBtn.classList.remove('opacity-50');
    });
  });

  modal.addEventListener('click', (e) => {
    if (e.target.tagName === 'BUTTON') {
      const selectedEU = e.target.textContent;
      selectedText.textContent = `선택한 국가: ${selectedEU}`;
      modal.classList.add('hidden');
      nextBtn.disabled = false;
      nextBtn.classList.remove('opacity-50');
    } else if (e.target === modal) {
      modal.classList.add('hidden');
    }
  });

  prevBtn.addEventListener('click', () => {
    window.location.href = 'upload.html';
  });

  nextBtn.addEventListener('click', () => {
    if (!nextBtn.disabled) {
      alert("다음 단계로 이동!");
    }
  });
});
