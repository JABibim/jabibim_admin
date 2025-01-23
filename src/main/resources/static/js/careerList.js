function showToast(message) {
    $('#validationToast .toast-body').text(message);

    const toastElement = $('#validationToast');
    const toast = new bootstrap.Toast(toastElement);

    toast.show();
}

function updateCareerActive(asisCareerId, tobeCareerId) {
    $.ajax({
        url: '/teacher/updateCareerActive',
        type: 'post',
        data: {asisCareerId, tobeCareerId},
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: (res) => {
            console.log('약력의 활성화 여부 업데이트가 정상적으로 처리되었습니다.');
            showToast('약력 상태가 성공적으로 업데이트 되었습니다.');
        },
        error: (err) => {
            console.error('약력의 활성화 여부를 업데이트 하는 도중 오류가 발생했습니다.', err);
            showToast('약력 상태 업데이트 중 오류가 발생했습니다. 다시 시도해주세요.');
        }
    });
}

$(function () {
    let asisCareerId = $('.toggle-display-status:checked').data('career-id');
    console.log('asisCareerId:', asisCareerId);
    $(document).on('click', '.toggle-display-status', function () {
        let tobeCareerId = $(this).data('career-id');

        if (asisCareerId === tobeCareerId) {
            showToast("선택한 약력이 현재 활성화 된 약력과 동일합니다. 다른 약력을 선택해주세요.");
            $(this).prop('checked', false);
            return;
        }

        $('.toggle-display-status').not(this).prop('checked', false);

        updateCareerActive(asisCareerId, tobeCareerId);

        // 토글 변경한 뒤에, asisCareerId를 갱신
        asisCareerId = tobeCareerId;
    });
});
