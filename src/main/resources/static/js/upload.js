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

  // 다음 단계 클릭
  $goNextBtn.on("click", function(e) {
    e.preventDefault();

    const file = $fileInput[0].files[0];
    if (!file) return alert("파일을 선택하세요");

    // 1️⃣ Presigned URL 요청
    $.post("/contract/getPresignedUrl", {
      fileName: file.name,
      contentType: file.type
    }, function(res) {
      if (res.result !== 1) return alert(res.msg);

      // 문자열 → 객체로 변환
      const data = JSON.parse(res.data);
      const uploadUrl = data.uploadUrl;
      const publicUrl = data.publicUrl;

      console.log(publicUrl);
      console.log(uploadUrl);
      console.log(data);

      // 2️⃣ fetch로 업로드 (Content-Type & 필요한 헤더 포함)
      fetch(uploadUrl, {
        method: "PUT",
        body: file,
        headers: {
          "Content-Type": file.type,        // 서버 Presigned URL 생성 시 지정한 타입과 일치
          "x-amz-acl": "public-read"     // 필요하면 활성화
        }
      })
          .then(response => {
            if (!response.ok) throw new Error(`Upload failed: ${response.status}`);
            console.log("업로드 성공:", publicUrl);

            // 3️⃣ 업로드 후 OCR 처리 요청
            $.post("/contract/processOcr", { imageUrl: publicUrl }, function(ocrRes) {
              if (ocrRes.result === 1) {
                window.location.href = "/contract/country";
              } else {
                alert(ocrRes.msg);
              }
            });

          })
          .catch(err => {
            console.error("업로드 실패", err);
            alert("업로드 실패");
          });
    });
  });


  // ==============================
  // 상단바 모든 링크 → 모달 열기
  // ==============================
  const navLinks = document.querySelectorAll('.nav-link');
  const homeModal = document.getElementById('homeConfirmModal');
  const confirmBtn = document.getElementById('confirmHomeBtn');
  const cancelBtn = document.getElementById('cancelHomeBtn');

  let targetHref = null; // 이동할 URL 임시 저장

  navLinks.forEach(link => {
    link.addEventListener("click", function (e) {
      e.preventDefault();
      targetHref = this.getAttribute("href"); // 클릭한 메뉴 URL 저장
      homeModal.classList.remove("hidden"); // 모달 열기
    });
  });

  // 모달 → 확인 버튼
  if (confirmBtn) {
    confirmBtn.addEventListener("click", function () {
      if (targetHref) {
        window.location.href = targetHref; // 선택한 메뉴로 이동
      }
    });
  }

  // 모달 → 취소 버튼
  if (cancelBtn) {
    cancelBtn.addEventListener("click", function () {
      homeModal.classList.add("hidden"); // 모달 닫기
      targetHref = null; // 취소 시 초기화
    });
  }

});


