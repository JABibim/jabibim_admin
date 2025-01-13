$(document).ready(function() {
    const quill = new Quill('#editor', {
        theme: 'snow'
    });

    let check = 0;

    $('form[name=noticeForm]').submit(function () {
        const $boardSubject = $('#boardSubject');
        if ($boardSubject.val().trim() == '') {
            alert('제목을 입력하세요.');
            $boardSubject.focus();
            return false;
        }

        let content = quill.root.innerHTML;
        if (content === '' || content === '<p><br></p>') {
            alert('내용을 입력하세요');
            $('#editor').focus();
            return false;
        }

        const $boardPass = $('#boardPassword');
        if ($boardPass.val().trim() == '') {
            alert('비밀번호를 입력하세요.');
            $boardPass.focus();
            return false;
        }

        const $course = $('#courseId')
        if ($course.val().trim() == '') {
            alert('과목명을 선택하세요.');
            $course.focus();
            return false;
        }

        // 파일첨부를 변경하지 않으면 $('#fileValue').text()의 파일명을 파라미터 'check'라는 이름으로 form에 추가하여 전송합니다.
        if (check == 0) {
            const value = $('#fileValue').text();
            const html = `<input type='hidden' value='${value}' name='check'>`;

            console.log(html);
            $(this).append(html);
        }

        $('#boardContent').val(content);
    }) // submit end

    $('#upFile').change(function () {
        check++;
        const maxSizeInBytes = 5 * 1024 * 1024;
        const file = this.files[0]; // 선택된 파일

        if (file.size > maxSizeInBytes) {
            alert('5MB 이하 크기로 업로드 하세요.');
            $(this).val('');
        } else {
            $('#fileValue').text(file.name);
        }

        show();
    })

    function show() {
        // 파일 이름이 있는 경우, remove 이미지를 보이게 하고
        // 파일 이름이 없는 경우, remove 이미지 보이지 않게 합니다.
        $('.remove')
            .css('display', $('#fileValue').text() ? 'inline-block' : 'none')
            .css({'position': 'relative', 'top': '-5px'});
    }

    show();

    $('.remove').click(function () {
        $('#fileValue').text('');
        $(this).css('display', 'none');
    })
});