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
            data: JSON.stringify({ password: currentPassword }),  // 비밀번호 데이터 전달
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

                checkFormValidity();  // 버튼 활성화 확
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
});