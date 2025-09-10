
  document.addEventListener("DOMContentLoaded", () => {
  const steps = document.querySelectorAll("#analysisSteps .circle");
  const completeMsg = document.getElementById("completeMessage");

  let current = 0;

  const interval = setInterval(() => {
    if (current < steps.length) {
      steps[current].classList.add("on");
      current++;
    } else {
      clearInterval(interval);
      completeMsg.classList.remove("hidden");

      // 결과 페이지로 5초 뒤 이동
      setTimeout(() => {
        window.location.href = "/contract/result";
      }, 5000);
    }
  }, 2000);
});
