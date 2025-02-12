$(document).ready(function() {
    // Quill 에디터 초기화
    const quill = new Quill('#editor2', { theme: 'snow' });
    console.log(quill);  // quill 객체가 제대로 생성되었는지 확인

    // let check = 0;
    //
    // // 파일 첨부 변경 이벤트
    // $('#upfile').change(function() {
    //     console.log($(this).val());  // 파일 경로 출력
    //     const inputfile = $(this).val().split('\\');  // \ 기준으로 분할
    //     $('#filevalue').text(inputfile[inputfile.length - 1]);  // <span>에 파일명 출력
    // });

    // '등록' 버튼 클릭 시 폼 제출 처리
    $('#save-button').on('click', function(event) {
        event.preventDefault();  // 기본 submit 동작을 막음

        let content = quill.root.innerHTML;
        console.log("editor2 content: " + content);

        // 유효성 검사
        let qnaSubject = $('#qnaSubject').val().trim();
        let exposureChecked = $('#exposure').is(':checked');
        let hideChecked = $('#hide').is(':checked');


        // 유효성 검사 - 제목
        if (!qnaSubject) {
            alert("제목을 입력해주세요.");
            $('#qnaSubject').focus();
            return false;
        }

        // Quill editor에서 가져온 내용이 비어 있거나 기본값인 경우
        if (content === '' || content === '<p><br></p>') {
            alert('내용을 입력하세요');
            $('#editor2').focus();  // Quill 에디터로 포커스 이동
            return false;
        }

        // 유효성 검사 - 노출 또는 숨김 선택 여부
        if (!exposureChecked && !hideChecked) {
            alert("노출 또는 숨김을 선택해주세요.");
            $('#exposure').focus();
            return false;
        }

        console.log("content before setting: ", content);
        console.log($('#qnaContent'));
        $('#qnaContent').val(content);
        console.log("content after setting: ", $('#qnaContent').val());

        // 폼 제출
        $('#save-form').submit();
    });

    // $('#upFile').change(function () {
    //     check++;
    //     const maxSizeInBytes = 5 * 1024 * 1024;
    //     const file = this.files[0]; // 선택된 파일
    //
    //     if (file.size > maxSizeInBytes) {
    //         alert('5MB 이하 크기로 업로드 하세요.');
    //         $(this).val('');
    //     } else {
    //         $('#fileValue').text(file.name);
    //     }
    //
    //     show();
    // })
    //
    // function show() {
    //     // 파일 이름이 있는 경우, remove 이미지를 보이게 하고
    //     // 파일 이름이 없는 경우, remove 이미지 보이지 않게 합니다.
    //     $('.remove')
    //         .css('display', $('#fileValue').text() ? 'inline-block' : 'none')
    //         .css({'position': 'relative', 'top': '-5px'});
    // }
    //
    // show();
    //
    // $('.remove').click(function () {
    //     $('#fileValue').text('');
    //     $(this).css('display', 'none');
    // })
});
