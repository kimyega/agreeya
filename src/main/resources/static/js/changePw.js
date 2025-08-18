document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById("reset-form");
    const newPassword = document.getElementById("new-password");
    const confirmPassword = document.getElementById("confirm-password");
    const newMsg = document.getElementById("new-password-msg");
    const confirmMsg = document.getElementById("confirm-password-msg");

    const setError = (input, msgEl, message) => {
        input.classList.remove("border-gray-300", "focus:ring-blue-300");
        input.classList.add("border-red-500", "focus:ring-red-300");
        msgEl.classList.remove("text-green-500");
        msgEl.classList.add("text-red-500");
        msgEl.textContent = message;
    };

    const setSuccess = (input, msgEl, message) => {
        input.classList.remove("border-red-500", "focus:ring-red-300");
        input.classList.add("border-green-500", "focus:ring-green-300");
        msgEl.classList.remove("text-red-500");
        msgEl.classList.add("text-green-500");
        msgEl.textContent = message;
    };

    form?.addEventListener("submit", function (e) {
        e.preventDefault();

        const pw1 = newPassword.value.trim();
        const pw2 = confirmPassword.value.trim();
        let valid = true;

        newMsg.textContent = "";
        confirmMsg.textContent = "";

        // 비밀번호 유효성: 영문+숫자 포함 6자 이상
        const regex = /^(?=.*[A-Za-z])(?=.*\d).{6,}$/;

        if (!pw1) {
            setError(newPassword, newMsg, "새 비밀번호를 입력해주세요.");
            valid = false;
        } else if (!regex.test(pw1)) {
            setError(newPassword, newMsg, "영문+숫자 포함 6자 이상이어야 합니다.");
            valid = false;
        } else {
            setSuccess(newPassword, newMsg, "");
        }

        if (!pw2) {
            setError(confirmPassword, confirmMsg, "비밀번호 확인을 입력해주세요.");
            valid = false;
        } else if (pw1 !== pw2) {
            setError(confirmPassword, confirmMsg, "비밀번호가 일치하지 않습니다.");
            valid = false;
        } else {
            setSuccess(confirmPassword, confirmMsg, "");
        }

        if (valid) {
            // ✅ 실제 서버에 POST 요청
            fetch("/user/changePw", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                },
                body: new URLSearchParams({
                    email: sessionStorage.getItem("email"), // 세션에 저장된 이메일 사용
                    newPassword: pw1,
                }),
            })
                .then(res => {
                    if (!res.ok) throw new Error("변경 실패");
                    return res.text();
                })
                .then(() => {
                    Swal.fire({
                        title: "비밀번호 변경이 완료되었습니다",
                        icon: "success",
                        draggable: true,
                        customClass: {
                            popup: 'rounded-border', // 'rounded-border'라는 클래스 추가
                            confirmButton: 'rounded-button'   // 확인 버튼에 적용할 클래스
                        },
                        confirmButtonText: "로그인 화면으로 이동하겠습니까?",
                        confirmButtonColor: '#3b82f6'
                    }).then(e => {
                        if (e.isConfirmed) {
                            location.href = '/login'
                        }
                    })
                })
                .catch(err => {
                    alert("비밀번호 변경 중 오류 발생");
                    console.error(err);
                });
        }
    });
})
