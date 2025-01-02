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

    $('form[name=courseForm]').submit(function () {
        // 과정명
        const $courseName = $('#course_name');
        if ($courseName.val().trim() === '') {
            showToast('과정명을 입력해주세요.');
            $courseName.focus();

            return false;
        }

        // 과목명
        const $courseSubject = $('#course_subject');
        if ($courseSubject.val().trim() === '') {
            showToast('과정이 속한 과목(예) 수학, 과학, ...)을 입력해주세요.');
            $courseSubject.focus();

            return false;
        }

        // 과정 대표이미지
        const $courseImage = $('#course_image');
        if ($courseImage[0].files.length <= 0) {
            showToast('과정 대표 이미지를 첨부해주세요.');

            return false;
        }

        // 상세 설명
        // quill에서 html태그가 포함된 데이터를 가져오는 법 : quill.root.innerHTML
        // html이 제거된 일반 문자열 데이터만 가져오는 법 : quill.getText()
        let introString = quill.getText();
        let introHtml = quill.root.innerHTML;
        if (introString.trim() === '') {
            showToast('과정을 표현하는 상세설명을 입력해주세요.');

            return false;
        }
        $('#course_intro').val(introHtml);

        // 수강 금액
        const $coursePrice = $('#course_price');
        if ($coursePrice.val().trim() === '') {
            showToast('수강금액 입력해주세요.');

            return false;
        }

        return true;
    })

    $('#submitBtn').click(function () {
        if (isAdmin) {
            showToast('강사 계정으로 접속한 뒤 진행해주세요.');
            return false;
        }
    })

    $('#clipImage').click(function () {
        $('#course_image').click();
    })

    $('#course_image').change(function () {
        const file = this.files[0];
        const $profileImage = $('#previewImage');

        if (file) {
            const fileType = file.type;

            if (fileType.startsWith('image/')) {
                const inputFile = $(this).val().split('\\');
                $('#fileValue').text(inputFile[inputFile.length - 1]);

                const reader = new FileReader();
                reader.onload = function (e) {
                    $profileImage.attr('src', e.target.result);
                    $profileImage.show();
                }

                reader.readAsDataURL(file);
            } else {
                showToast('이미지 파일만 업로드 가능합니다.');

                $(this).val('');
                $('#fileValue').text('');
                $profileImage.hide();
            }
        }
    });

    $('#course_price').on('input', function () {
        $(this).val($(this).val().replace(/[^0-9]/g, ''));
    });
})