function showToast(message) {
    $('#validationToast .toast-body').text(message);

    const toastElement = $('#validationToast');
    const toast = new bootstrap.Toast(toastElement);

    toast.show();
}
$(document).ready(function () {
    const $systemNoticeTable = $('table');

    $systemNoticeTable.on('change', '.toggle-systemNotice-status', function () {
        let systemNoticeId = $(this).data('system-notice-id');
        let isActive = $(this).is(':checked') ? 1 : 0;

        $.ajax({
            url: 'manageSystemNotice/updateActive',
            type: 'post',
            dataType: 'json',
            data: {systemNoticeId, isActive},
            success: (res) => {
                if (res.result) {
                    showToast('공개여부 업데이트가 정상적으로 처리되었습니다.');
                } else {
                    showToast('공개여부 업데이트가 실패하였습니다.');
                }
            },
            error: (err) => {
                console.error('시스템 공지사항의 공개 여부를 업데이트 하는 도중 오류가 발생했습니다.', err);
            }
        })
    });

    $systemNoticeTable.on('click', '.delete-btn', function () {
        let systemNoticeId = $(this).data('system-notice-id');

        if (confirm('해당 공지사항을 삭제하시겠습니까?')) {
            $.ajax({
                url: 'manageSystemNotice/delete',
                type: 'post',
                dataType: 'json',
                data: { systemNoticeId },
                success: (res) => {
                    if (res.result) {
                        showToast('삭제가 정상적으로 처리되었습니다.');
                        location.href = window.location.href;
                    } else {
                        showToast('삭제 요청이 실패하였습니다.');
                    }
                },
                error: (err) => {
                    console.error('삭제 요청 중 오류 발생:', err);
                    showToast('삭제 요청 중 오류가 발생했습니다.');
                }
            });
        }
    });

    $('#addSystemNoticeBtn').click(function () {
        location.href = 'manageSystemNotice/add';
    })
})

