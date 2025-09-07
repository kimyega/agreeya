$(document).ready(function () {
  // ✅ 프로필 조회
  $.ajax({
    url: "/user/mypage/profile",
    type: "GET",
    xhrFields: { withCredentials: true }, // 세션 유지
    success: function (d) {
      if (!d) {
        alert("로그인이 필요합니다.");
        location.href = "/user/login";
        return;
      }

      // DOM 채우기
      $("#pfName").text(d.name ?? "-");
      $("#pfEmail").text(d.email ?? "-");
      $("#pfRegDt").text(d.createdAt ?? "-");
      $("#headerNick").text(d.name || d.userId || "User");

      // 상단바 토글
      $("#loginButton").addClass("hidden");
      $("#profileDropdownWrapper").removeClass("hidden");
    },
    error: function (xhr, status, error) {
      console.error("프로필 로딩 실패:", error);
      alert("프로필 로딩 실패");
      location.href = "/user/login";
    }
  });

  // ✅ 회원 탈퇴 모달 열기
  $("#btnWithdraw").on("click", () => {
    $("#withdrawModal").removeClass("hidden").addClass("flex");
  });

  // 회원 탈퇴 모달 닫기
  $("#cancelWithdraw").on("click", () => {
    $("#withdrawModal").addClass("hidden").removeClass("flex");
  });

  // ✅ 회원 탈퇴 요청
  $("#confirmWithdraw").on("click", function () {
    $.ajax({
      url: "/user/delete",
      type: "DELETE",
      xhrFields: { withCredentials: true }, // 세션 유지
      success: function (d) {
        if (d.res === 1) {
          $("#withdrawModal").addClass("hidden");
          $("#withdrawDoneModal").removeClass("hidden").addClass("flex");

          setTimeout(() => {
            location.href = "/";
          }, 2000);
        } else {
          alert(d.msg || "탈퇴 실패");
        }
      },
      error: function (xhr, status, error) {
        console.error("탈퇴 요청 실패:", error);
        alert("탈퇴 요청 실패");
      }
    });
  });
});
