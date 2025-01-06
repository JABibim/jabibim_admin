$(document).ready(function () {
    console.log('fffffffff')

    $('#newPassword, #renewPassword').keyup(function () {
        const newPassword = $('#newPassword').val();
        const renewPassword = $('#renewPassword').val();

        console.log(newPassword)
        console.log(renewPassword)

        if (newPassword === renewPassword) {
            $('#passwordMatchMessage').text('Passwords match').css('color', 'green');
        } else {
            $('#passwordMatchMessage').text('Passwords do not match').css('color', 'red');
        }
    });
});