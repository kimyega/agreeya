// 드롭다운 토글
function toggleDropdown() {
  const dropdown = document.getElementById('profileDropdown');
  dropdown.classList.toggle('hidden');
}

// 바깥 클릭 시 드롭다운 닫기
window.addEventListener('click', function (e) {
  const wrapper = document.getElementById('profileDropdownWrapper');
  const dropdown = document.getElementById('profileDropdown');
  if (wrapper && !wrapper.contains(e.target)) {
    dropdown.classList.add('hidden');
  }
});