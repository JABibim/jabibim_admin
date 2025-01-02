function showToast(message) {
    $('#validationToast .toast-body').text(message);

    const toastElement = $('#validationToast');
    const toast = new bootstrap.Toast(toastElement);

    toast.show();
}

$(function () {
    const quill = new Quill('#editor', {
        theme: 'snow',
        modules: {
            toolbar: [
                [{'header': '1'}, {'header': '2'}, {'font': []}],
                [{'list': 'ordered'}, {'list': 'bullet'}],
                ['bold', 'italic', 'underline'],
                ['link'],
                [{'align': []}],
            ]
        }
    });

    $('form[name=systemNoticeAddForm]').submit(function () {
        // 카테고리
        const $systemNoticeCategory = $('#system_notice_category');
        if ($systemNoticeCategory.val() === null || $systemNoticeCategory.val() === '0') {
            showToast('카테고리를 선택해주세요.');
            $systemNoticeCategory.focus();

            return false;
        }

        // 제목
        const $systemNoticeTitle = $('#system_notice_title');
        if ($systemNoticeTitle.val().trim() === '') {
            showToast('공지사항 제목을 입력해주세요.');
            $systemNoticeTitle.focus();

            return false;
        }

        // 내용
        // quill에서 html태그가 포함된 데이터를 가져오는 법 : quill.root.innerHTML
        // html이 제거된 일반 문자열 데이터만 가져오는 법 : quill.getText()
        let introString = quill.getText();
        let introHtml = quill.root.innerHTML;
        if (introString.trim() === '') {
            showToast('공지사항 내용을 작성해주세요.');

            return false;
        }
        $('#system_notice_content').val(introHtml);

        return true;
    })
})