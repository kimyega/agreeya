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

// upload.js
document.addEventListener("DOMContentLoaded", () => {
  const dropZone = document.getElementById("dropZone");
  const fileInput = document.getElementById("fileInput");
  const goNextBtn = document.getElementById("goNextBtn");
  const fileNameText = document.getElementById("fileNameText");

  dropZone.addEventListener("dragover", (e) => {
    e.preventDefault();
    dropZone.classList.add("border-blue-400");
  });

  dropZone.addEventListener("dragleave", () => {
    dropZone.classList.remove("border-blue-400");
  });

  dropZone.addEventListener("drop", (e) => {
    e.preventDefault();
    dropZone.classList.remove("border-blue-400");
    fileInput.files = e.dataTransfer.files;
    handleFile();
  });

  fileInput.addEventListener("change", handleFile);

  function handleFile() {
    if (fileInput.files.length > 0) {
      goNextBtn.disabled = false;
      fileNameText.textContent = fileInput.files[0].name;
    } else {
      goNextBtn.disabled = true;
      fileNameText.textContent = "선택된 파일 없음";
    }
  }

  goNextBtn.addEventListener("click", () => {
    alert("다음 단계로 이동합니다.");
  });
});


