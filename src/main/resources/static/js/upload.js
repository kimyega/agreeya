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
        if (goNextBtn.disabled) return;
        window.location.href = "/contract/country";
    });
});


