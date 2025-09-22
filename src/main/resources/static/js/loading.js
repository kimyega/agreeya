
  document.addEventListener("DOMContentLoaded", () => {
  const steps = document.querySelectorAll("#analysisSteps .circle");
  const completeMsg = document.getElementById("completeMessage");

  let current = 0;

  const interval = setInterval(() => {
    if (current < steps.length) {
      steps[current].classList.add("on");
      current++;
    } else {
      $.ajax({
        url: "/contract/analyze",
        type: "post",
        dataType: "json",
        data: { contractId: new URLSearchParams(location.search).get('countryId') },
        success: function (res) {
          clearInterval(interval);
          completeMsg.classList.remove("hidden");

          // 결과 페이지로 5초 뒤 이동
          setTimeout(() => {
            location.href = "/contract/result";
          }, 5000);
        },
        error: function () {
          alert("❌ 서버 요청 중 오류 발생");
        }
      });

    }
  }, 2000);
});
