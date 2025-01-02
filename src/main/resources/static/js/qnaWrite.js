$(document).ready(function() {
    const quill = new Quill('#editor2', {
        theme: 'snow'
    });

    let check = 0;

    $('#board_file_name').change(function (){
        console.log($(this).val());

        const inputFile = $(this).val().split('\\');
        $('#fileValue').text(inputFile[inputFile.length - 1]);
    })

    // '등록' 버튼 클릭 시 Quill 에디터 내용을 숨겨진 input에 저장하고 폼 제출
    $('#save-button').on('click', function(event) {
        event.preventDefault(); // 기본 submit 동작을 막음

        // Quill 에디터의 HTML 내용 가져오기
        let content = quill.root.innerHTML;
        console.log("Quill 에디터 내용:", content);  // 확인용 로그

        // 유효성 검사
        let boardTitle = $('#board_title').val().trim();
        let exposureChecked = $('#exposure').is(':checked');
        let hideChecked = $('#hide').is(':checked');

        // 유효성 검사

        if (!boardTitle) {
            alert("제목을 입력해주세요.");
            $('#board_title').focus();
            return false;
        }

        if (content === '' || content === '<p><br></p>') {
            alert('내용을 입력하세요');
            $('#editor2').focus();  // Quill 에디터로 포커스 이동
            return false;
        }

        if (!exposureChecked && !hideChecked) {
            alert("노출 또는 숨김을 선택해주세요.");
            $('#exposure').focus();
            return false;
        }

        // Quill editor의 내용을 hidden input에 저장
        $('#qna_content').val(content);
        console.log("숨겨진 input 값:", $('#qna_content').val());  // 확인용 로그

        // 폼 제출
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