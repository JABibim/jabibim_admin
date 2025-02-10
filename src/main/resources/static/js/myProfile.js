$(document).ready(function () {
    const changePasswordButton = $('#changePasswordButton');
    const currentPasswordMessage = $('#currentPasswordMessage');
    const passwordMatchMessage = $('#passwordMatchMessage');
    let isCurrValid = false;
    let isPassMatch = false;

    // 현재 비밀번호 확인
    $('#currentPassword').blur(function () {
        const currentPassword = $(this).val();
        console.log(currentPassword);

        $.ajax({
            url: 'checkPassword',  // 요청 URL 확인
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({password: currentPassword}),  // 비밀번호 데이터 전달
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (response) {

                console.log(response)
                if (response.valid) {
                    currentPasswordMessage.text('현재 비밀번호가 확인되었습니다.').attr('class', 'green');
                    console.log('확인 잘 되었음!!')
                    isCurrValid = true;

                } else {
                    currentPasswordMessage.text('현재 비밀번호가 일치하지 않습니다.').attr('class', 'red');
                    changePasswordButton.prop('disabled', true);  // 비밀번호 불일치 시 버튼 비활성화
                    isCurrValid = false;
                }

                checkFormValidity();  // 버튼 활성화 확인
            },
            error: function () {
                currentPasswordMessage.text('비밀번호 확인 중 오류가 발생했습니다.').css('color', 'red');
            },
        });
    });

    $('#newPassword, #renewPassword').keyup(function () {
        const newPassword = $('#newPassword').val();
        const renewPassword = $('#renewPassword').val();

        console.log(newPassword)
        console.log(renewPassword)

        if (newPassword === renewPassword) {
            $('#passwordMatchMessage').text('Passwords match').css('color', 'green');
            isPassMatch = true;
        } else {
            $('#passwordMatchMessage').text('Passwords do not match').css('color', 'red');
            isPassMatch = false;
        }
        checkFormValidity();
    });

    function checkFormValidity() {
        if (isCurrValid && isPassMatch) {
            changePasswordButton.prop('disabled', false);  // 버튼 활성화
            console.log('버튼 클릭 가능해짐!!!')
        } else {
            changePasswordButton.prop('disabled', true);  // 버튼 비활성화
        }
    }

    const dropZone = document.getElementById("dropZone");
    const fileInput = document.getElementById("teacherProfileImage");
    const previewImage = document.getElementById("previewImage");
    const imgDeleteBtn = document.getElementById("imgDeleteBtn");

    // 클릭 시 파일 선택 창 열기
    dropZone.addEventListener("click", () => {
        fileInput.click();
    });

    // 파일 선택 시 미리보기 처리
    fileInput.addEventListener("change", (event) => {
        const file = event.target.files[0];
        if (file) {
            displayPreview(file);
        }
    });

    // 드래그앤드롭 이벤트 처리
    dropZone.addEventListener("dragover", (event) => {
        event.preventDefault();
        dropZone.classList.add("dragover");
    });

    dropZone.addEventListener("dragleave", () => {
        dropZone.classList.remove("dragover");
    });

    dropZone.addEventListener("drop", (event) => {
        event.preventDefault();
        dropZone.classList.remove("dragover");

        const file = event.dataTransfer.files[0];
        if (file) {
            fileInput.files = event.dataTransfer.files; // 파일 입력에 할당
            displayPreview(file);
        }
    });

    function displayPreview(file) {
        const reader = new FileReader();
        reader.onload = (e) => {
            previewImage.src = e.target.result;
            previewImage.style.display = "block";
            adjustImageSize(previewImage);

            imgDeleteBtn.style.display = 'inline-block';
        };
        reader.readAsDataURL(file);
    }

    // 이미지 크기 조정 (화면 비율에 맞추기)
    function adjustImageSize(img) {
        img.style.maxWidth = "100%"; // 부모 요소 너비에 맞추기
        img.style.height = "auto";  // 비율 유지
        img.style.display = "block";
        img.style.margin = "0 auto"; // 가운데 정렬
    }

    // 이미지 삭제 버튼 클릭 이벤트
    imgDeleteBtn.addEventListener("click", (event) => {
        event.preventDefault();

        // 미리보기 이미지 숨기기
        previewImage.src = "";
        previewImage.style.display = "none";

        // 파일 입력 초기화
        fileInput.value = "";

        // 삭제 버튼 숨기기
        imgDeleteBtn.style.display = "none";
    });
});
