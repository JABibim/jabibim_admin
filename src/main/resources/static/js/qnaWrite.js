$(document).ready(function() {
    // Quill 에디터 초기화
    const quill = new Quill('#editor3', { theme: 'snow' });
    console.log(quill);  // quill 객체가 제대로 생성되었는지 확인

    // 파일 첨부 변경 이벤트
    $('#upfile2').change(function() {
        console.log($(this).val());  // 파일 경로 출력
        const inputfile = $(this).val().split('\\');  // \ 기준으로 분할
        $('#filevalue2').text(inputfile[inputfile.length - 1]);  // <span>에 파일명 출력
    });

    // '등록' 버튼 클릭 시 폼 제출 처리
    $('#save-button').on('click', function(event) {
        event.preventDefault();  // 기본 submit 동작을 막음

        let content = quill.root.innerHTML;
        console.log("editor3 content: " + content);

        // 유효성 검사
        let qnaSubject = $('#newQnaSubject').val().trim();
        let exposureChecked = $('#exposure').is(':checked');
        let hideChecked = $('#hide').is(':checked');


        // 유효성 검사 - 제목
        if (!qnaSubject) {
            alert("제목을 입력해주세요.");
            $('#newQnaSubject').focus();
            return false;
        }

        // Quill editor에서 가져온 내용이 비어 있거나 기본값인 경우
        if (content === '' || content === '<p><br></p>') {
            alert('내용을 입력하세요');
            $('#editor3').focus();  // Quill 에디터로 포커스 이동
            return false;
        }

        // 유효성 검사 - 노출 또는 숨김 선택 여부
        if (!exposureChecked && !hideChecked) {
            alert("노출 또는 숨김을 선택해주세요.");
            $('#exposure').focus();
            return false;
        }

        console.log("content before setting: ", content);
        console.log($('#qnaContent2'));
        $('#qnaContent2').val(content);
        console.log("content after setting: ", $('#qnaContent2').val());

        // 폼 제출
        $('#save-form').submit();
    });
});
