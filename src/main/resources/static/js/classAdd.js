function showToast(message) {
    $('#validationToast .toast-body').text(message);

    const toastElement = $('#validationToast');
    const toast = new bootstrap.Toast(toastElement);

    toast.show();
}

function submitFormData() {
    return new Promise((resolve, reject) => {
        const courseId = $('#courseId').val();
        const classSubject = $('#classSubject').val();
        const classContent = $('#classContent').val();
        const classType = $('input[name=type]:checked').val();

        $.ajax({
            url: 'addClassProcess',
            type: 'POST',
            data: {
                courseId,
                classSubject,
                classContent,
                classType,
            },
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (response) {
                let newClassId = response.data.classId;
                if (newClassId) {
                    resolve({courseId, newClassId, classType}); // 서버로부터 class_id 반환받기
                } else {
                    reject(new Error("class_id를 반환받지 못했습니다."));
                }
            },
            error: function (error) {
                alert('기본 정보 저장 중 오류가 발생했습니다.');
                console.error(error);
                reject(error);
            }
        });
    });
}

function submitFormAndUploadFile() {
    submitFormData().then(({courseId, newClassId, classType}) => {
        const uploadPromises = []; // 프로미스객체 배열
        const file = $('#uploadFile')[0].files[0];

        uploadPromises.push(uploadFile(courseId, newClassId, classType, file));

        return Promise.all(uploadPromises);
    }).then(() => {
        console.log('강의가 성공적으로 등록되었습니다.');
        window.location.href = '/content/class';
    }).catch(err => {
        console.log('파일 업로드 중 오류가 발생했습니다.');
    })
}

function uploadFile(courseId, newClassId, classType, file) {
    return new Promise((resolve, reject) => {
        const formData = new FormData();
        formData.append('courseId', courseId);
        formData.append('classId', newClassId);
        formData.append('classType', classType);
        formData.append('file', file);

        $.ajax({
            url: 'addClassFilesProcess', // 파일 업로드 처리 엔드포인트
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            xhr: function () {
                const xhr = new window.XMLHttpRequest();
                xhr.upload.addEventListener("progress", function (evt) {
                    if (evt.lengthComputable) {
                        const percentComplete = Math.round((evt.loaded / evt.total) * 100);
                        updateProgress(percentComplete);
                    }
                }, false);
                return xhr;
            },
            success: function (response) {
                console.log(`업로드 완료`);
                updateProgress(100);
                resolve();
            },
            error: function (error) {
                updateProgress(0);
                reject(error);
            }
        });
    });
}

function updateProgress(percentage) {
    const progressBar = document.getElementById('progressBar');
    const progressText = document.getElementById('progressText');

    progressBar.style.width = percentage + '%'; // 프로그레스 바 너비 업데이트
    progressText.textContent = percentage + '%'; // 텍스트 업데이트

    if (percentage >= 100) {
        setTimeout(() => {
            document.getElementById('progressContainer').style.display = 'none'; // 완료 후 숨김
        }, 500);
    } else {
        document.getElementById('progressContainer').style.display = 'block'; // 프로그레스 바 표시
    }
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

    $('#classFile').change(function () {
        const file = this.files[0];

        if (file) {
            // const fileType = file.type;
            const inputFile = $(this).val().split('\\');
            $('#fileValue').text(inputFile[inputFile.length - 1]);
        }
    });

    const dropZone = document.getElementById("dropZone");
    const fileInput = document.getElementById("uploadFile");

    dropZone.addEventListener('click', (event) => {
        if (event.target.id !== 'resetUploadFile')
            fileInput.click();
    });

    fileInput.addEventListener('change', (event) => {
        const file = event.target.files[0];

        if ( $('input[name=type]:checked').val() === 'video' ) {
            const fileType = file.type;
            const validVideoTypes = ['video/mp4', 'video/webm', 'video/ogg'];

            if (!validVideoTypes.includes(fileType)) {
                showToast('영상 파일만 업로드 가능합니다. (허용 형식: mp4, webm, ogg)')

                $(this).val('');  // 입력값 초기화
                $('#defaultText').text('파일을 이곳에 드래그 앤 드롭하거나 클릭하여 업로드하세요.');
                $('#resetUploadFile').css('display', 'none');
                return;
            }
        }

        $('#defaultText').text(file.name);

        $('#resetUploadFile').css('display', 'inline-block');
    });

    $('#resetUploadFile').on('click', function (event) {
        event.stopPropagation();

        $('#defaultText').text('파일을 이곳에 드래그 앤 드롭하거나 클릭하여 업로드하세요.');
        $('#uploadFile').val('');
        $('#resetUploadFile').css('display', 'none');
    })

    $('#submitBtn').on('click', function (e) {
        e.preventDefault(); // 기본 폼 제출 방지

        // 과정명
        const $courseId = $('#courseId');
        if (!$courseId.val()) {
            showToast('과정명을 선택해주세요.');
            $courseId.focus();

            return false;
        }

        // 강의명
        const $classSubject = $('#classSubject');
        if ($classSubject.val().trim() === '') {
            showToast('강의명을 입력해주세요.');
            $classSubject.focus();

            return false;
        }

        // 상세 설명
        // quill에서 html태그가 포함된 데이터를 가져오는 법 : quill.root.innerHTML
        // html이 제거된 일반 문자열 데이터만 가져오는 법 : quill.getText()
        let introString = quill.getText();
        let introHtml = quill.root.innerHTML;
        if (introString.trim() === '') {
            showToast('강의를 표현하는 상세설명을 입력해주세요.');

            return false;
        }
        $('#classContent').val(introHtml);

        // 강의 상세 타입
        const classType = $('input[name=type]:checked');
        if (classType.val() === undefined) {
            showToast('강의 상세 타입을 선택해주세요.');

            return false;
        }

        // 강의 동영상 또는 자료 체크
        const $uploadFile = $('#uploadFile');
        if ($uploadFile[0].files.length <= 0) {
            showToast('강의 파일 (영상 또는 강의자료) 중 하나는 필수로 첨부해야합니다.');

            return false;
        }

        submitFormAndUploadFile();
    });
})