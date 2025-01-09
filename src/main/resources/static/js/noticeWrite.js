$(document).ready(function() {
    const quill = new Quill('#editor', {
        theme: 'snow'
    });

    $('#upfile').change(function(){
        console.log($(this).val()) // c:\fakepath\upload.png <- 자체적으로 관리하는 경로, paper path
        const inputfile = $(this).val().split('\\'); // \ 기준으로 분할
        $('#filevalue').text(inputfile[inputfile.length - 1]);
        // 분할된 배열에서 마지막 인덱스 선택하여 <span>에 text 노드 추가
    });

    // '등록' 버튼 클릭 시 Quill 에디터 내용을 숨겨진 input에 저장하고 폼 제출
    $('#save-button').on('click', function(event) {
        event.preventDefault(); // 기본 submit 동작을 막음

        // Quill 에디터의 HTML 내용 가져오기
        let content = quill.root.innerHTML;

        // 유효성 검사
        let courseId = $('#courseId').val().trim();
        let boardTitle = $('#boardSubject').val().trim();
        let fixedChecked = $('#fixed').is(':checked');
        let unfixedChecked = $('#unfixed').is(':checked');
        let exposureChecked = $('#exposure').is(':checked');
        let hideChecked = $('#hide').is(':checked');


        // 유효성 검사
        if (!courseId) {
            alert("과목명을 입력해주세요.");
            $('#course_id').focus();
            return false;
        }

        if (!boardTitle) {
            alert("제목을 입력해주세요.");
            $('#board_title').focus();
            return false;
        }

        // Quill editor에서 가져온 내용이 비어 있거나 기본값인 경우
        if (content === '' || content === '<p><br></p>') {
            alert('내용을 입력하세요');
            $('#editor').focus();  // Quill 에디터로 포커스 이동
            return false;
        }

        if (!exposureChecked && !hideChecked) {
            alert("노출 또는 숨김을 선택해주세요.");
            $('#exposure').focus();
            return false;
        }

        if (!fixedChecked && !unfixedChecked) {
            alert("고정 또는 비고정을 선택해주세요.");
            $('#fixed').focus();
            return false;
        }

        // Quill editor의 내용을 hidden input에 저장
        $('#boardContent').val(content);

        // 폼 제출
        $('#save-form').submit();
    });
});