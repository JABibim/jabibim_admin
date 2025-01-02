$(document).ready(function() {
    const quill = new Quill('#editor', {
        theme: 'snow'
    });

    let check = 0; 


    // '등록' 버튼 클릭 시
    $('#save-button').on('click', function(event) {
        event.preventDefault();

        let content = quill.root.innerHTML;

        // 유효성 검사
        let courseId = $('#course_id').val().trim();
        let boardTitle = $('#board_title').val().trim();
        let exposureChecked = $('#exposure').is(':checked');
        let hideChecked = $('#hide').is(':checked');

        if (!courseId || courseId === '과정명을 선택해주세요') {
            alert("과정명을 입력해주세요.");
            $('#course_id').focus();
            return false;
        }

        if (!boardTitle) {
            alert("제목을 입력해주세요.");
            $('#board_title').focus();
            return false;
        }

        if (content === '' || content === '<p><br></p>') {
            alert('내용을 입력하세요');
            $('#editor').focus();
            return false;
        }

        if (!exposureChecked && !hideChecked) {
            alert("노출 또는 숨김을 선택해주세요.");
            $('#exposure').focus();
            return false;
        }

        if (check === 0) {
            const value = $('#fileValue').text();
            const html = `<input type='hidden' value='${value}' name='check'>`;
            console.log("히든 필드 HTML:", html); // 확인용 콘솔 로그
            $('#save-form').append(html); // 여기서 폼에 추가
        }

        $('#board_content').val(content);

        $('#save-form').submit();
    });

    // 파일 선택 시 파일명 변경
    $('#upfile').change(function () {
        check++;
        const maxSizeInBytes = 5 * 1024 * 1024;
        const file = this.files[0]; // 선택된 파일

        if (file.size > maxSizeInBytes) {
            alert('5MB 이하 크기로 업로드 하세요.');
            $(this).val('');
        } else {
            $('#fileValue').text(file.name);  // 여기에 파일 이름 설정
            console.log("파일 선택 후 #fileValue 텍스트:", $('#fileValue').text());  // 확인용 로그
        }

        show();
    });

    function show() {
        $('.remove')
            .css('display', $('#fileValue').text() ? 'inline-block' : 'none')
            .css({'position': 'relative', 'top': '-5px'});
    }

    show();

    $('.remove').click(function () {
        $('#fileValue').text('');
        $(this).css('display', 'none');
    });
});