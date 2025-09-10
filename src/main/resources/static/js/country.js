let selectedCountry = '';

 //버튼
 nextBtn.addEventListener("click", () => {
     if (nextBtn.disabled) return;
     window.location.href = "/contract/loading";
 });

 prevBtn.addEventListener("click", () => {
     if (prevBtn.disabled) return;
     window.location.href = "/contract/upload";
 });

  

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
});
