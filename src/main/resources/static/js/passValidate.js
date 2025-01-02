$(document).ready(function () {
    $('#newPassword, #renewPassword').keyup(function () {
        const newPassword = $('#newPassword').val();
        const renewPassword = $('#renewPassword').val();

        if (newPassword === renewPassword) {
            $('#passwordMatchMessage').text('Passwords match').css('color', 'green');
        } else {
            $('#passwordMatchMessage').text('Passwords do not match').css('color', 'red');
        }
    });
});