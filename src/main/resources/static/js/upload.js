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
  });

  const $fileInput = $("#fileInput");
  const $goNextBtn = $("#goNextBtn");
  const $fileNameText = $("#fileNameText");
  const $uploadForm = $("#uploadForm");

  // 파일 선택 시 UI 업데이트
  $fileInput.on("change", function() {
    if(this.files.length > 0){
      $fileNameText.text(this.files[0].name);
      $goNextBtn.prop("disabled", false);
    } else {
      $fileNameText.text("선택된 파일 없음");
      $goNextBtn.prop("disabled", true);
    }
  });

  // 드래그 앤 드롭 지원
  $("#dropZone").on("dragover", function(e){
    e.preventDefault();
    $(this).addClass("border-blue-400");
  }).on("dragleave", function(e){
    $(this).removeClass("border-blue-400");
  }).on("drop", function(e){
    e.preventDefault();
    $(this).removeClass("border-blue-400");
    if(e.originalEvent.dataTransfer.files.length > 0){
      $fileInput[0].files = e.originalEvent.dataTransfer.files;
      $fileNameText.text(e.originalEvent.dataTransfer.files[0].name);
      $goNextBtn.prop("disabled", false);
    }
  });

  // 다음 단계 클릭 → AJAX 업로드
  $goNextBtn.on("click", function(e) {
    e.preventDefault();
    if($fileInput[0].files.length === 0) return alert("파일을 선택하세요");

    const formData = new FormData($uploadForm[0]);

    $.ajax({
      url: "/contract/uploadFile",
      type: "POST",
      data: formData,
      processData: false, // 필수: formData 전송시 false
      contentType: false, // 필수: formData 전송시 false
      dataType: "json",
      success: function(data) {
        console.log(data);
        if(data.result === 1){
          alert("업로드 성공, OCR 완료");
          window.location.href = "/contract/country"; // 다음 단계 이동
        } else {
          alert(data.msg);
        }
      },
      error: function(xhr, status, error) {
        console.error(error);
        alert("업로드 실패");
      }
    });
  });

});


